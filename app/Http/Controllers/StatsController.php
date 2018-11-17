<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;



class StatsController extends Controller
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

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        if(Auth::user()->is_admin == 1){
            $server = DB::select('select * from server_list');
        }else{
            //Récupération des serveur de l'utilisateur
            $server = DB::select('select * from server_list where user_id = ?', [Auth::user()->id]);
        }


        return view('panel.stats-index',['server' => $server]);
    }

    public function stats($id)
    {
        $id = encname($id);
        if (!Redis::exists('server:' . $id)){
            $data = json_decode(Redis::get('server:' . $id));
            if(!Auth::user()->is_admin == 1){
                if ($data->user_id != Auth::user()->id){
                    return redirect("/dashboard");
                }
            }
        }

        $sr = json_decode(Redis::get('server:' . $id));

        $srv = DB::select('select * from server_list where id = ?', [intval($sr->id)]);

        if ($srv == null || !isset($srv[0]))
        {
            return redirect("/dashboard");
        }

        if (!$srv[0]->verified)
        {
            return redirect("/dashboard/verify/".intval($sr->id));
        }

        if (Redis::exists('stats:' . $id)){
            $data = json_decode(Redis::get('stats:' . $id));
            $datam = json_decode(Redis::get('stats:m:' . $id));
        }else{
            //calcul quotidien
            $data = [];
            $hour = -1;
            while($hour++ < 24)
            {
                $time1 = date('Y-m-d H:i:s',mktime($hour,0,0));
                $time2 = date('Y-m-d H:i:s',mktime($hour + 1,0,0));

                $count = DB::table('vote_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                $count2 = DB::table('click_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                $count3 = DB::table('copy_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                array_push($data, [date('H',mktime($hour,0,0)), $count, $count2, $count3]);
            }

            // Put the vote check
            Redis::set('stats:' . $id, json_encode($data));
            // Set a TTL to the key
            Redis::expire('stats:' . $id, 300);


            //calcul mensuel
            $datam = [];
            $start = strtotime('last month');
            $end = strtotime('now');

            while($end > $start)
            {
                $time1 = date('Y-m-d H:i:s',$start);

                $time2 = date('Y-m-d H:i:s',$start + 86400);

                $count = DB::table('vote_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                $count2 = DB::table('click_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                $count3 = DB::table('copy_logs')
                    ->where('server_id', '=', $sr->id)
                    ->where('date', '>=', $time1)
                    ->where('date', '<=', $time2)
                    ->count();

                array_push($datam, [$time1,$time2, $count, $count2, $count3]);

                $start = $start + 86400;
            }

            // Put the vote check
            Redis::set('stats:m:' . $id, json_encode($datam));
            // Set a TTL to the key
            Redis::expire('stats:m:' . $id, 300);
        }



        return view('panel.stats-server',['data' => $data, 'datam' => $datam]);
    }





}
