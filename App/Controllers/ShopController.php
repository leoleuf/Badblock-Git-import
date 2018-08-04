<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/07/2018
 * Time: 11:34
 */

namespace App\Controllers;

use MongoDB\Exception\Exception;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;

class ShopController extends Controller
{


    function index(RequestInterface $request, ResponseInterface $response){
        //TODO by TomDev
        $data_shop = $this->redis->getJson('shop');


        $this->render($response, 'shop.index',['serverlist' => $data_shop]);

    }


    function buy(RequestInterface $request, ResponseInterface $response, $argument){
        //Check if user is connected
        if (!$this->container->session->exist('user')){
            return $response->write("User not connected !")->withStatus(401);
        }

        //Search data player
        $player = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        if ($player == null){
            return $response->write("Pas inscrit sur le serveur !")->withStatus(403);
        }

        if (!isset($argument['id'])){
            return $response->write("Invalid ID for product buy !")->withStatus(403);
        }

        //Try make an mongoID is very sensitive
        try {
            $id = new \MongoDB\BSON\ObjectId($argument['id']);
        }catch (\MongoDB\Driver\Exception\InvalidArgumentException $ex){
            return $response->write("Invalid ID for product buy ER : ObjectID not valid !")->withStatus(403);
        }

        //Search data product in mongoDB is very slow
        $product = $this->container->mongo->product_list->findOne(['_id' => $id]);
        if ($product == null){
            return $response->write("Product doesn't exist !")->withStatus(404);
        }

        if ($product->buy_one){
            $depend = $this->container->mongo->buy_logs->count(['uniqueId' => $player['uniqueId'], 'offer' => $product->depend_name]);
            if ($depend != 0){
                return $response->write("Vous ne pouvez re-acheter cette offre !")->withStatus(400);
            }
        }

        //Check depend
        if($product->depend){
            $product_depend = $this->container->mongo->product_list->findOne(['_id' => $product->depend_to]);
            $depend = $this->container->mongo->buy_logs->count(['uniqueId' => $player['uniqueId'], 'offer' => $product_depend->depend_name]);
            if ($depend == 0){
                //Search depend produc pour proposer a la vente
                $product_depend = $product_depend->name;
                return $response->write("Vous devez acheter l'offre $product_depend avant d'acheter celle-ci !")->withStatus(400);
            }
        }

        //Check if player have money
        if(!$this->haveMoney($this->session->getProfile('username')['username'], 100)){
            return $response->write("Fond insuffisant")->withStatus(405);
        }

        $product->depend_name == null;
        //Log the buy and subtract money
        if (!isset($product->depend_name)){
            $product->depend_name = $product->name;
        }
        $data = [
            'uniqueId' => $player['uniqueId'],
            'offer' => $product->depend_name,
            'name' => $product->name,
            'price' => $product->price,
            'ingame' => false
        ];
        $this->container->mongo->buy_logs->InsertOne($data);

        if ($product->mode == "rabbitmq"){
            $this->sendRabbitData($product);
        }

    }



    public function haveMoney($username, $amount){
        //Search data player
        $player = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        if ($player == null){
            return false;
        }

        //Search money of player
        $player = $this->container->mongo->fund_list->findOne(['uniqueId' => $player->uniqueId]);

        if (!isset($player->points)){
            return false;
        }

        if($player->points >= $amount){
            return true;
        }else{
            return false;
        }


    }


   public function sendRabbitData($product){
        //Connection to rabbitMQ server
        $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
        $channel = $connection->channel();

        $player = $this->session->getProfile('username')['username'];
        $shopQueue = $product->queue;
        if (is_string($product->command)){
            $command = str_replace("%player%", $player, $product->command);
        }else{
            $product->command = iterator_to_array($product->command);
            $command = "";
            foreach ($product->command as $row){
                $command .= str_replace("%player%", $player, $row) . ";";
            }
        }


        $channel->exchange_declare('shopLinker.'.$shopQueue, 'fanout', false, false, false, false);
        $sanction = (object) [
            'dataType' => 'BUY',
            'playerName' => $player,
            'displayName' => $product->name,
            'command' => $command,
            'ingame' => false,
            'price' => $product->price,
            'forceCommand' => true
        ];

        $message = (object) [
            'expire' => (time() + 604800) * 1000,
            'message' => json_encode($sanction)
        ];
        $msg = new AMQPMessage(json_encode($message));
        $channel->basic_publish($msg, 'shopLinker.'.$shopQueue);


        $channel->close();
        $connection->close();


    }


    public function getRecharge(RequestInterface $request, ResponseInterface $response){

        $Paiement_data = $this->container->config['paiement'];


        $this->render($response, 'shop.recharge',['paiement' => $Paiement_data]);


    }


}