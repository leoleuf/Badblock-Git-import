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
        $data = DB::select('select * from users where id = ? LIMIT 1', [Auth::user()->id]);
        return view('panel.recharge', ['data' => $data[0]]);
    }

    public function rechargevalidate(Request $request)
    {
        $data = DB::select('select * from users where id = ? LIMIT 1', [Auth::user()->id]);
        $data = $data[0];
        $code = isset($_POST['code']) ? preg_replace('/[^a-zA-Z0-9]+/', '', $_POST['code']) : '';
        if( empty($code) ) {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez saisir un code.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/recharge')->withInput();
        }
        else {
            try {
                $dedipass = @file_get_contents('http://api.dedipass.com/v1/pay/?public_key=c7ff246fcf018c193859f4a52650f73a&private_key=6461584e39e299431b6b99920187e7dcdace9d5f&code=' . $code);
                $dedipass = json_decode($dedipass);
                if ($dedipass->status == 'success') {
                    $virtual_currency = intval($dedipass->virtual_currency);
                    $jour = array("Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi");
                    $mois = array("", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");

                    $stime = time();
                    $shownDate = $jour[date("w", $stime)] . " " . date("d", $stime) . " " . $mois[date("n", $stime)] . " " . date("Y", $stime);
                    DB::table('logs')->insert(
                        [
                            'user_id' => Auth::user()->id,
                            'action' => 'Rechargement le ' . $shownDate . ' de ' . $virtual_currency . ' points',
                            'date' => date("Y-m-d H:i:s"),
                            'ip' => $_SERVER['REMOTE_ADDR'],
                            'success' => true
                        ]
                    );

                    $afterCredit = $data->credit + $virtual_currency;
                    DB::table('users')
                        ->where('id', '=', $data->id)
                        ->update(
                            [
                                'credit' => $afterCredit
                            ]
                        );
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'success',
                            'message' => 'Votre recharge a été validée. Vous avez reçu ' . $virtual_currency . ' crédits.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/mise-en-avant')->withInput();
                } else {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Nous sommes désolé. Le code que vous avez entré est invalide. Réessayez ou contactez-nous en cas de problème.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/recharge')->withInput();
                }
            } catch (Exception $e) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Une erreur est survenue lors de votre rechargement. Veuillez contacter un administrateur.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/recharge')->withInput();
            }
        }
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

        if (count($op) > 5) {
            foreach($op as $k => $v) {
                $srv = DB::select('select * from server_list where id = ?', [$v->server]);
                if (count($srv) < 3)
                {
                    continue;
                }

                if (encname($srv[0]->cat) == encname($server->cat))
                {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Il y a déjà trois mises en avant pour ce jour dans cette catégorie '.seocat($server->cat).'. Veuillez essayer une autre date.',
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
        $op = DB::select('select * from pub where date >= ? ORDER BY date ASC;', [$currentDate]);

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
            0 => 21, // Dimanche
            1 => 9, // Lundi
            2 => 10, // Mardi
            3 => 17, // Mercredi
            4 => 11, // Jeudi
            5 => 16, // Vendredi
            6 => 23 // Samedi
        );

        for ($i = 0; $i < 20; $i++)
        {
            $date = date("Y-m-d", strtotime("+".$i." days"));

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

            $o = [
                'points' => $points
            ];
            $days[$date] = $o;
        }

        $jour = array("Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi");

        $mois = array("","Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");

        return view('panel.mise-en-avant',['currentDate' => $currentDate, 'data' => $data[0],'op' => $op, 'servers' => $servers, 'days' => $days, 'jour' => $jour, 'mois' => $mois]);

    }


    public function index2()
    {
        $currentDate = date("Y-m-d");
        $op = DB::select('select * from pub where date >= ? ORDER BY date ASC;', [$currentDate]);

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
            0 => 21, // Dimanche
            1 => 9, // Lundi
            2 => 10, // Mardi
            3 => 17, // Mercredi
            4 => 11, // Jeudi
            5 => 16, // Vendredi
            6 => 23 // Samedi
        );

        for ($i = 0; $i < 20; $i++)
        {
            $date = date("Y-m-d", strtotime("+".$i." days"));

            $dw = date('w', strtotime($date));
            $points = 0;
            $dt = DB::select('select pts from pbs where date = ?', [$date]);
            $place = 0;
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
                $pvl = DB::select('select id from pub where date = ?', [$date]);
                $points = $dt[0]->pts;
                $place = count($pvl);
            }

            $o = [
                'points' => $points,
                'place' => $place
            ];
            $days[$date] = $o;
        }

        $jour = array("Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi");

        $mois = array("","Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");

        return view('panel.mise-en-avant2',['currentDate' => $currentDate, 'data' => $data[0],'op' => $op, 'servers' => $servers, 'days' => $days, 'jour' => $jour, 'mois' => $mois]);

    }





}
