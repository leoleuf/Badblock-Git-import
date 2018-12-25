<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/07/2018
 * Time: 11:34
 */

namespace App\Controllers;

use function DI\object;
use function GuzzleHttp\Psr7\str;
use MongoDB\Exception\Exception;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;


class ShopController extends Controller
{


    function index(RequestInterface $request, ResponseInterface $response)
    {

        $data_shop = $this->redis->getJson('shop');
        $data_promo = $this->redis->getJson('shop.promotion');

        $promo = false;

        $this->render($response, 'shop.index',['serverlist' => $data_shop, 'promotion' => $data_promo, 'promo' => $promo]);

    }


    function buy(RequestInterface $request, ResponseInterface $response, $argument)
    {
        $ingame = false;
        $givean = false;
        $playerName = "";

        if (isset($_POST['playerName'])) {
            $ip = isset($_SERVER['HTTP_CF_CONNECTING_IP']) ? $_SERVER['HTTP_CF_CONNECTING_IP'] : $_SERVER['REMOTE_ADDR'];
            $whitelist = array(
                '127.0.0.1',
                '151.80.47.206',
                '54.37.180.38',
                '54.37.180.39',
                '178.33.24.184',
                '178.33.24.185',
                '178.33.24.186',
                '178.33.24.187',
                '178.33.24.188',
                '176.31.106.11',
                '178.33.24.189',
                '178.33.24.190',
                '178.33.24.191',
                '164.132.200.176',
                '164.132.200.183',
                '91.134.165.88',
                '91.134.165.89',
                '91.134.165.90',
                '91.134.165.91',
                '91.134.165.92',
                '91.134.165.93',
                '91.134.165.94',
                '91.134.165.95',
                '91.121.143.108',
                '149.91.82.150',
                '51.38.189.205',
                '51.38.35.126',
                '66.70.251.20',
                '66.70.251.21',
                '149.202.89.72',
                '149.202.88.2',
                '66.70.251.22',
                '91.134.165.88',
                '164.132.200.110',
                '164.132.200.198',
                '149.91.82.150',
                '164.132.200.198'
            );

           /* if (!in_array($ip, $whitelist)) {
                return 'Lolnope.';
            }*/

            $ingame = true;
            $playerName = $_POST['playerName'];
        }

        if (isset($_POST['animation'])) {
            $give = array(
                '127.0.0.1',
                '149.91.82.150',
                '78.124.116.135'
            );
            $ip = isset($_SERVER['HTTP_CF_CONNECTING_IP']) ? $_SERVER['HTTP_CF_CONNECTING_IP'] : $_SERVER['REMOTE_ADDR'];
            if (!in_array($ip, $give)) {
                return 'Lolnope.';
            }else{
                $givean = true;
                $playerName = $_POST['playerName'];
            }
        }


        if (!$ingame)
        {
            //Check if user is connected
            if (!$this->container->session->exist('user'))
            {
                return $response->write("User not connected !")->withStatus(401);
            }

            $playerName = $this->session->getProfile('username')['username'];
        }

        //Search data player
        $player = $this->container->mongoServer->players->findOne(['name' => strtolower($playerName)]);

        if ($player == null)
        {
            return $response->write("Pas inscrit sur le serveur !")->withStatus(403);
        }

        if (!isset($argument['id']))
        {
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

        if (isset($product->buy_one) && $givean == false)
        {
            if ($product->buy_one == true)
            {
                $depend = $this->container->mongo->buy_logs->count(['uniqueId' => $player['uniqueId'], 'offer' => $product->depend_name]);
                if ($depend != 0)
                {
                    return $response->write("Vous ne pouvez re-acheter cette offre !")->withStatus(400);
                }
            }
        }

        //Check depend
        if($product->depend && $givean == false)
        {
            $product_depend = $this->container->mongo->product_list->findOne(['_id' => $product->depend_to]);
            $depend = $this->container->mongo->buy_logs->count(['uniqueId' => $player['uniqueId'], 'offer' => $product_depend->depend_name]);

            if ($depend == 0)
            {
                //Search depend produc pour proposer a la vente
                $product_depend = $product_depend->name;
                return $response->write("Vous devez acheter l'offre $product_depend avant d'acheter celle-ci !")->withStatus(400);
            }
        }

        //Check promotion
        if (isset($product->promotion) && $product->promotion)
        {
            $product->price = $product->promotion_new_price;
        }


        //Check if player have money
        if(!$this->haveMoney(strtolower($playerName), $product->price) && $givean == false)
        {
            return $response->write("Fond insuffisant")->withStatus(405);
        }

        $product->depend_name == null;

        //Log the buy and subtract money
        if (!isset($product->depend_name))
        {
            $product->depend_name = $product->name;
        }

        if ($givean == true){
            $product->price = 0;
        }

        $data = [
            'uniqueId' => $player['uniqueId'],
            'offer' => $product->depend_name,
            'name' => $product->name,
            'price' => $product->price,
            'ingame' => false,
            'ip' => $_SERVER['REMOTE_ADDR'],
            'date' => date('Y-m-d H:i:s')
        ];
        $this->container->mongo->buy_logs->InsertOne($data);

        if ($product->mode == "rabbitmq")
        {
            $this->sendRabbitData($playerName, $product);
        }elseif ($product->mode == "webladder")
        {
            $this->hybrid(strtolower($playerName), $product['command'], $product->price, -1);
        }elseif ($product->mode == "hybrid")
        {
            foreach ((array) $player['permissions']['alternateGroups'] as $k => $row)
            {
                if ($k == "gradeperso")
                {
                    $check = true;
                }
                else
                {
                    $check = false;
                }
            }

            if ($player['permissions']['group'] == "gradeperso")
            {
                $time = $player['permissions']['end'] + ($product['duration'] * 1000);
                $this->hybrid(strtolower($playerName), $product['command'], $product->price, $time);
            }
            else
            {
                $expireC = 0;
                foreach ((array) $player['permissions']['alternateGroups'] as $k => $row)
                {
                    if ($k == "gradeperso")
                    {
                        $expireC = $row;
                    }
                }
                $time = $expireC + ((time() + $product['duration']) * 1000);
                $this->hybrid(strtolower($playerName), $product['command'], $product->price, $time);
            }
        }

        if ($givean == false){
            //Subtract points
            $this->subtract(strtolower($playerName), $product->price);
        }


        return "";
    }


    public function subtract($username, $amount)
    {
        //Search data player
        $player = $this->container->mongoServer->players->findOne(['name' => $username]);
        $player = $this->container->mongo->fund_list->findOne(['uniqueId' => $player->uniqueId]);

        $points = $player->points - $amount;

        $this->container->mongo->fund_list->updateOne(['uniqueId' => $player->uniqueId],['$set' => ['points' => $points]]);

        //Refresh points cache
        $this->container->session->set('points', $points);


    }



    public function haveMoney($username, $amount)
    {
        //Search data player
        $player = $this->container->mongoServer->players->findOne(['name' => $username]);

        if ($player == null)
        {
            return false;
        }

        //Search money of player
        $player = $this->container->mongo->fund_list->findOne(['uniqueId' => $player->uniqueId]);

        if (!isset($player->points))
        {
            return false;
        }

        if($player->points >= $amount)
        {
            return true;
        }

        return false;
    }


   public function sendRabbitData($playerName, $product)
   {
        //Connection to rabbitMQ server
        $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
        $channel = $connection->channel();

        $shopQueue = $product->queue;
        if (is_string($product->command))
        {
            $command = str_replace("%player%", $playerName, $product->command);
        }else
            {
            $product->command = iterator_to_array($product->command);
            $command = "";
            foreach ($product->command as $row)
            {
                $command .= str_replace("%player%", $playerName, $row) . ";";
            }
        }


        $channel->exchange_declare('shopLinker.'.$shopQueue, 'fanout', false, false, false, false);
        $sanction = (object) [
            'dataType' => 'BUY',
            'playerName' => $playerName,
            'displayName' => $product->name,
            'command' => $command,
            'ingame' => false,
            'price' => $product->price
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

   public function hybrid($username, $grade, $price, $duration)
   {
        //Big game server rank
       $server_list = [
           'skyb',
           'fb',
           'faction'
       ];

       if ($duration != -1)
       {
           $duration = (60*60*24*31);
       }
       else
       {
           $duration = -1;
       }

       foreach ($server_list as $row)
       {
           $p = new \stdClass();
           $p->name = "§6" . $grade;
           $p->queue = $row;
           $p->price = $price;
           if ($duration != -1)
           {
               $p->command = "pex user %player% group add ". $grade .' "" '. $duration;
           }
           else
           {
               $p->command = "pex user %player% group add " . $grade;
           }
           $this->sendRabbitData($username, $p);
       }

       if ($grade == "legend")
       {
           $grade = "gradeperso";
       }

       //Ladder rankup
       ////Connection to rabbitMQ server
       $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
       $channel = $connection->channel();

       $channel->exchange_declare('shopLinker.ladder', 'fanout', false, false, false, false);
       $sanction = (object) [
           'dataType' => 'BUY',
           'playerName' => $username,
           'displayName' => $grade,
           'command' => "perms user $username group add $grade",
           'ingame' => false,
           'price' => $price
       ];

       if ($duration != -1)
       {
           $sanction->command = "perms user $username group add $grade 31d";
       }

       $message = (object) [
           'expire' => (time() + 604800) * 1000,
           'message' => json_encode($sanction)
       ];
       $msg = new AMQPMessage(json_encode($message));
       $channel->basic_publish($msg, 'shopLinker.ladder');


       $channel->close();
       $connection->close();
   }



}