<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;



class PubController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function recharge(Request $request)
    {
        return view('panel.recharge');
    }

    public function push(Request $request)
    {
        if (!isset($_POST['server']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez préciser le serveur à mettre en avant.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        if (!isset($_POST['date']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez préciser la date de mise en avant.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        $serverId = intval(htmlspecialchars($_POST['server']));

        $servers = DB::select('select * from server_list where user_id = ? and id = ? LIMIT 1', [Auth::user()->id, $serverId]);

        if (count($servers) == 0)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez préciser un serveur valide qui vous appartient pour la mise en avant.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        $server = $servers[0];

        $date = htmlspecialchars($_POST['date']);
        $date = strtolower($date);
        $dts = DB::select('select * from pbs where date = ?', [$date]);

        if (count($dts) == 0)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Cette date de mise en avant est invalide. Veuillez réitérer votre requête.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        $date = $dts[0]->date;

        $op = DB::select('select * from pub where date = ?', [$date]);

        if (count($op) > 0) {
            foreach($op as $k => $v) {
                $srv = DB::select('select * from server_list where id = ?', [$v->server]);
                if (count($srv) < 1)
                {
                    continue;
                }

                if (encname($srv->cat) == encname($server->cat))
                {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Cette date de mise en avant n\'est pas libre pour la catégorie '.seocat($server->cat).'. Veuillez essayer une autre date.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/mise-en-avant')->withInput();
                }
            }
        }

        $data = DB::select('select * from users where id = ?', [Auth::user()->id]);

        if ($data == null || count($data) < 1)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Impossible de récupérer vos données.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        $data = $data[0];

        $cost = $dts[0]->pts;

        $jour = array("Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi");
        $mois = array("","Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");

        $stime = strtotime($date);
        $shownDate = $jour[date("w", $stime)]." ".date("d", $stime)." ".$mois[date("n", $stime)]." ".date("Y", $stime);

        if ($cost > $data->credit)
        {
            $nenough = $cost - $data->credit;
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Il vous manque '.$nenough.' points pour pouvoir mettre en avant ce serveur à la date du '.$shownDate.'.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/mise-en-avant')->withInput();
        }

        DB::table('logs')->insert(
            [
                'user_id' => Auth::user()->id,
                'action' => 'Mise en avant du serveur n°' . $server->id.' (le '.$shownDate.') pour '.$cost.' points',
                'date' => date("Y-m-d H:i:s"),
                'ip' => $_SERVER['REMOTE_ADDR'],
                'success' => true
            ]
        );

        $afterCredit = $data->credit - $cost;
        DB::table('users')
            ->where('id', '=', $data->id)
            ->update(
                [
                    'credit' => $afterCredit
                ]
            );

        DB::table('pub')->insert(
            [
                'server' => $server->id,
                'date' => $date
            ]
        );

        $request->session()->flash('flash', [
            array(
                'level' => 'success',
                'message' => 'Vous avez bien mis le serveur '.seocat($server->cat).' '.$server->name.' en avant pour la date du '.$shownDate.', contre '.$cost.' points.',
                'important' => true
            )
        ]);
        return redirect('/dashboard/mise-en-avant')->withInput();
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $currentDate = date("Y-m-d");
        $op = DB::select('select * from pub where date >= ?', [$currentDate]);

        $data = DB::select('select * from users where id = ? LIMIT 1', [Auth::user()->id]);
        $servers = DB::select('select * from server_list where user_id = ?', [Auth::user()->id]);

        foreach($op as $k => $v)
        {
            $srv = DB::select('select * from server_list where id = ?', [$v->server]);
            $op[$k]->server = $srv[0];
        }

        $days = array();

        // TODO : COEFFICIENT A GERER AVEC LE TRAFIC
        $coeff = 12;

        $daysCoef = array(
            0 => 15, // Dimanche
            1 => 7, // Lundi
            2 => 8, // Mardi
            3 => 13, // Mercredi
            4 => 6, // Jeudi
            5 => 12, // Vendredi
            6 => 17 // Samedi
        );

        for ($i = 0; $i < 30; $i++)
        {
            $date = date("Y-m-d", strtotime("+".$i." days"));
            $free = true;
            foreach ($op as $k => $v)
            {
                if ($v->date == $date)
                {
                    $free = false;
                    break;
                }
            }

            $points = 0;

            if ($free)
            {
                $dw = date('w', strtotime($date));
                $points = 0;
                $dt = DB::select('select pts from pbs where date = ?', [$date]);
                if (count($dt) == 0)
                {
                    $points = $daysCoef[$dw];
                    $points = floatval($points);
                    $points *= (1 + (rand(70, 80) / 100)) * $coeff;
                    $points = intval($points);

                    DB::table('pbs')->insert(
                        [
                            'date' => $date,
                            'pts' => $points
                        ]
                    );
                }
                else
                {
                    $points = $dt[0]->pts;
                }
            }

            $o = [
                'free' => $free,
                'points' => $points
            ];
            $days[$date] = $o;
        }

        $jour = array("Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi");

        $mois = array("","Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");

        return view('panel.mise-en-avant',['currentDate' => $currentDate, 'data' => $data[0],'op' => $op, 'servers' => $servers, 'days' => $days, 'jour' => $jour, 'mois' => $mois]);

    }





}
