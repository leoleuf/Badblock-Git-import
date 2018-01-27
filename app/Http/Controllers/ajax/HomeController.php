<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 13/01/2018
 * Time: 10:07
 */

namespace App\Http\Controllers\ajax;

use Illuminate\Http\Request;
use Jenssegers\Mongodb;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;


class HomeController
{

    public function mongoStat(){

        try {
            $database = (new \MongoDB\Client(
                'mongodb://'.$_ENV['MONGO_USERNAME'].":".$_ENV['MONGO_PASSWORD']."@".$_ENV['MONGO_HOST'].":".$_ENV['MONGO_PORT']	."/".$_ENV['MONGO_DATABASE']
            ))->admin;

            $cursor = $database->command(['serverStatus' => 1]);
            $data = $cursor->toArray()[0];
            //Uptime
            $timeFormat = gmdate("H:i:s", $data['uptime']);
            //Average response
            $ms = round($data['backgroundFlushing']['average_ms'], 3);

            $json = ["on" => $data['ok'],"ms" => $ms,"uptime" => $timeFormat,"load" => $data["backgroundFlushing"]["last_ms"]];

            return json_encode($json);

        }catch (\MongoConnectionException $e){
            $json = ["on" => 0,"ms" => 0,"uptime" => 0,"load" => 0];

            return json_encode($json);
        }


    }

    public function online(){

        $url = "https://api.serveurs-minecraft.com/api.php?Joueurs_En_Ligne_Ping&ip=play.badblock.fr&port=25565";
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result2 = curl_exec($ch);



        return json_encode(["online" => $result2,"max" => 5000]);



    }

    public function onlineChart(){

        $url = "https://api.serveurs-minecraft.com/api.php?Joueurs_En_Ligne_Ping&ip=play.badblock.fr&port=25565";
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result2 = curl_exec($ch);



        return $result2;


    }


    public function players(){

        if (Redis::exists('stats:players:registred')){

             $reg = Redis::get('stats:players:registred');

        }else{
             try {
                 $database = (new \MongoDB\Client(
                     'mongodb://'.$_ENV['MONGO_USERNAME'].":".$_ENV['MONGO_PASSWORD']."@".$_ENV['MONGO_HOST'].":".$_ENV['MONGO_PORT']	."/".$_ENV['MONGO_DATABASE']
                 ))->admin;

                 $cursor = $database->players;
                 $data = $cursor->count();

                 Redis::set('stats:players:registred',$data);
                 Redis::expire('stats:players:registred', 10);

                 $reg = $data;
             }catch (\MongoConnectionException $e){

                 $reg = 0;
             }
        }

        if (Redis::exists('stats:players:banned')){

            $ban = Redis::get('stats:players:banned');

        }else{
            try {
                $database = (new \MongoDB\Client(
                    'mongodb://'.$_ENV['MONGO_USERNAME'].":".$_ENV['MONGO_PASSWORD']."@".$_ENV['MONGO_HOST'].":".$_ENV['MONGO_PORT']	."/".$_ENV['MONGO_DATABASE']
                ))->admin;

                $cursor = $database->players;
                $data = $cursor->count(['punish.ban' => true]);

                Redis::set('stats:players:banned',$data);
                Redis::expire('stats:players:banned', 30);

                $ban = $data;
            }catch (\MongoConnectionException $e){

                $ban = 0;
            }
        }

        if (Redis::exists('stats:players:muted')){

            $mute = Redis::get('stats:players:muted');

        }else{
            try {
                $database = (new \MongoDB\Client(
                    'mongodb://'.$_ENV['MONGO_USERNAME'].":".$_ENV['MONGO_PASSWORD']."@".$_ENV['MONGO_HOST'].":".$_ENV['MONGO_PORT']	."/".$_ENV['MONGO_DATABASE']
                ))->admin;

                $cursor = $database->players;
                $data = $cursor->count(['punish.mute' => true]);

                Redis::set('stats:players:muted',$data);
                Redis::expire('stats:players:muted', 30);

                $mute = $data;
            }catch (\MongoConnectionException $e){

                $mute = 0;
            }
        }


        return json_encode(["register" => $reg,"banned" => $ban,"muted" => $mute]);



    }




}