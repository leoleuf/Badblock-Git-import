<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/01/2018
 * Time: 15:10
 */

namespace App\Http\Controllers\Infra;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;


class DockerController extends Controller
{
    public function index(){

        //Connect to INT & Get data from json
        $All_clusters = Redis::connection('docker')->scan("0");

        $Data_cluster = [];
        foreach($All_clusters[1] as $Cluster){
            $Cluster_Data = Redis::connection('docker')->get($Cluster);
            $Cluster_Data = json_decode($Cluster_Data);
            array_push($Data_cluster, ['name' => $Cluster, 'data' => $Cluster_Data]);
        }

        //dd($Data_cluster);


        return view('Infra.docker')->with('Clusters', $Data_cluster);

    }

    public function send(){

        $connection = new AMQPStreamConnection(getenv('RABBIT_IP'), getenv('RABBIT_PORT'), getenv('RABBIT_USERNAME'), getenv('RABBIT_PASSWORD'), getenv('RABBIT_VIRTUALHOST'));
        $channel = $connection->channel();
        $channel->exchange_declare('docker.instance.stop', 'fanout', false, false, false, false);


        $msg = new AMQPMessage('Hello World!');
        $channel->basic_publish($msg, '', 'docker.instance.stop');

        echo " [x] Sent 'Hello World!'\n";

    }


}