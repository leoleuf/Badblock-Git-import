<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;




class ApiController extends Controller
{
    public function index($id)
    {
        //Récupération des serveur de l'utilisateur
        $server = DB::select('select * from server_list where user_id = ? and id = ?', [Auth::user()->id, $id]);

        if ($server == null)
        {
            return 'Server not found.';
        }

        return view('panel.api',['server' => $server[0]]);
    }

    public function votes($id)
    {
        if (!Redis::exists('server:' . encname($id)))
        {
            return 'Serveur inconnu';
        }

        $server = json_decode(Redis::get('server:' . encname($id)));

        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$server->id]);

        if (count($server) > 0){
            return $server[0]->votes;
        }else{
            return 'Server not found.';
        }


    }

    /**
     * Show the application api.
     *
     * @return \Illuminate\Http\Response
     */
    public function api($id, $key = 0){
        $ip = $_GET['ip'];
        if (!Redis::exists('server:' . encname($id)))
        {
            return 'Serveur inconnu';
        }

        $server = json_decode(Redis::get('server:' . encname($id)));

        $voteName = $server->id .':user:' . $ip;
        $checkName = $voteName.':check';

        if (!Redis::exists($checkName))
        {
            if ($server->votetype == "JSON")
            {
                $t = Redis::ttl($voteName);
                if ($t == null)
                {
                    $t = -1;
                }
                return json_encode(
                    [
                        'ip' => $ip,
                        'nextvote' => $t,
                        'status' => 'WAIT'
                    ]
                );
            }

            if ($server->votetype == "CALLBACK")
            {
                return "Ce serveur utilise la technologique CALLBACK.";
            }

            if ($server->votetype == "VOTIFIER")
            {
                return "Ce serveur utilise la technologique CALLBACK.";
            }

            return;
        }

        Redis::del($checkName);

        if ($server->votetype == "TRUE")
        {
            return 'true';
        }

        if ($server->votetype == "JSON")
        {
            $t = Redis::ttl($voteName);
            return json_encode(
                [
                    'ip' => $ip,
                    'nextvote' => $t,
                    'status' => 'SUCCESS'
                ]
            );
        }

        if ($server->votetype == "CALLBACK")
        {
            return "Ce serveur utilise la technologique CALLBACK.";
        }

        if ($server->votetype == "VOTIFIER")
        {
            return "Ce serveur utilise la technologique VOTIFIER.";
        }

        return '1';
    }

}
