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
        $url = "https://minecraft-api.com/api/ping/maxplayer.php?ip=play.badblock.fr&port=25565";
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result1 = curl_exec($ch);

        $url = "https://minecraft-api.com/api/ping/playeronline.php?ip=play.badblock.fr&port=25565";
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result2 = curl_exec($ch);



        return json_encode(["online" => $result2,"max" => $result1]);



    }



}