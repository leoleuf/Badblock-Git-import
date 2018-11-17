<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;



class VerifyController extends Controller
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

    public function pleaseverify($id)
    {
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            return view('panel.verify', ['data' => $server[0]]);
        }else{
            return redirect("/dashboard");
        }
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
        $coeff = 9;

        $daysCoef = array(
            0 => 15, // Dimanche
            1 => 7, // Lundi
            2 => 8, // Mardi
            3 => 13, // Mercredi
            4 => 6, // Jeudi
            5 => 12, // Vendredi
            6 => 17 // Samedi
        );

        for ($i = 0; $i < 15; $i++)
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
