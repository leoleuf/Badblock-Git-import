<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/02/2018
 * Time: 18:15
 */

namespace App\Controllers;

use function DI\string;
use function DusanKasan\Knapsack\identity;
use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;
use DateTime;

class VoteController extends Controller
{

    public function getHome(RequestInterface $request, ResponseInterface $response){
        //Read Top from Redis
        $top = $this->redis->getJson('vote.top');

        $player = "";
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }

        return $this->render($response, 'vote.index', ['top' => $top, 'player' => $player]);

    }

    public function voteRedirect(RequestInterface $request, ResponseInterface $response){
        $top = $this->redis->getJson('vote.top');

        $player = "";
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }

        return $this->render($response, 'vote.vote-redirect', ['top' => $top]);
    }

    public function playerexists(RequestInterface $request, ResponseInterface $response)
    {
        if (!isset($_POST['pseudo']))
        {
            return $response->write("User not found !")->withStatus(404);
        }

        $pseudo = htmlspecialchars($_POST['pseudo']);

        if (getenv('APP_DEBUG') == 0){
            $query = "SELECT username FROM xf_user WHERE username = '". $pseudo ."' LIMIT 1";
            $data = $this->container->mysql_forum->fetchRow($query);

            // user exists?
            if ($data == false)
            {
                return $response->write("User not found !")->withStatus(404);
            }
        }

        return $response->write("ok")->withStatus(200);
    }

    public function award(RequestInterface $request, ResponseInterface $response)
    {

        if (!isset($_POST['pseudo']) && !isset($_POST['type']))
        {
            return $response->write("User not found !")->withStatus(404);
        }

        $dev = getenv('APP_DEBUG') == 1;

        $displayPseudo = $_POST['pseudo'];
        $pseudo = htmlspecialchars($displayPseudo);
        $type = htmlspecialchars($_POST['type']);

        $pseudo = strtolower($pseudo);

        // TODO move
        $types = array(
          1 => 'ptsboutique',
          2 => 'skyb',
          3 => 'minigames',
          4 => 'faction',
          5 => 'box'
        );

        // unknown type
        if (!isset($types[$type]))
        {
            return $response->write("User not found !")->withStatus(404);
        }

        if (!$dev)
        {
            $query = "SELECT username FROM xf_user WHERE username = '". $pseudo ."' LIMIT 1";
            $data = $this->container->mysql_forum->fetchRow($query);

            // user exists?
            if ($data == false)
            {
                return $response->write("User not found !")->withStatus(404);
            }
        }

       // return $response->write("test")->withStatus(200);
        
        $API_id = 198; // ID de votre serveur
        if ($dev)
        {
            $API_ip = $_SERVER['REMOTE_ADDR'];
        }
        else
        {
            $API_ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
        }

        //$API_url = "https://serveur-prive.net/api/vote/$API_id/$API_ip";
        //$API_call = @file_get_contents($API_url);

        $SERVER_ID = 93; // ID du serveur
        $KEY = 'ePwvH8vBvcVUthJettUe9SW0fKsZ0V'; // Api key du serveur

        $SM = "http://serveur-minecraft.net/api/$SERVER_ID/$KEY/?ip=$API_ip";
        $result = @file_get_contents($SM);

        // voted?
        if (!$dev && $result !== true)
        {
            return $response->write("Vote invalid")->withStatus(405);
        }

        $queue = $types[$type];

        $collection = $this->container->mongo->votes_awards;
        $cursor = $collection->find(['type' => $type]);

        $maxRandom = 0;

        $things = array();

        foreach ($cursor as $key => $value)
        {
            $things[$maxRandom] = $value;
            $maxRandom += $value->probability;
        }

        $rand = rand(1, $maxRandom);

        $winItem = null;

        foreach ($things as $key => $value)
        {
            if ($rand > $key && $winItem != null)
            {
                continue;
            }

            $winItem = $value;
        }

        return $response->write("fdp ".var_dump($winItem))->withStatus(200);

        $collection = $this->container->mongo->votes_logs;
        $command = str_replace("%player%", $pseudo, $winItem->command);

        // award log
        $insert = [
            "name" => $pseudo,
            "ip" => $API_ip,
            "type" => $type,
            "queue" => $queue,
            "date" => date("d/m/Y H:i:s"),
            "timestamp" => time(),
            "user_agent" => htmlspecialchars($API_ip)
        ];


        $collection->insertOne($insert);

        $awardName = $winItem->name;

        $product = array(
            'queue' => $queue,
            'command' => $command,
            'name' => $awardName
        );

        $this->sendRabbitData($pseudo, $product);

        $this->broadcast(' &e'.$displayPseudo.' &aa voté. Vote toi aussi en faisant &d/vote');
        $this->broadcast(' &aRécompense gagnée : &d'.$awardName);

        $this->top($displayPseudo, 1);

        return $response->write("Vous avez gagné ".$type)->withStatus(200);
    }

    public function top($player, $vote){
        $player = strtolower($player);

        //Collection
        $mongo = $this->container->mongo->stats_vote;

        $date =  date("Y-m");
        $count = $mongo->count(["date" => $date]);

        if ($count == 0){
            $data = [
                "date" => $date,
                "give" => false,
                "players" => []
            ];

            $mongo->insertOne($data);
        }

        $data = $mongo->findOne(["date" => $date]);

        foreach($data['players'] as $row) {
            if ($row->name == $player) {
                $datarcp = $row;
                break;
            }
        }

        if(!isset($datarcp)){
            $data = $mongo->updateOne(["_id" => $data['_id']], ['$push' => ["players" => ['name' => $player,'vote' => $vote]]]);
        }else{
            $data = $mongo->updateOne(["_id" => $data['_id'], "players.name" => $player], ['$set' => ["players.$.vote" => $datarcp->vote + $vote]]);
        }

        //Read top from mongoDB
        $mongo = $this->container->mongo->stats_vote;
        $date =  date("Y-m");
        $data = $mongo->findOne(["date" => $date]);


        $data = (array) $data['players'];
        usort($data, create_function('$a, $b', '
        $a = $a["vote"];
        $b = $b["vote"];

        if ($a == $b) return 0;

        $direction = strtolower(trim("desc"));

        return ($a ' . ("desc" == 'desc' ? '>' : '<') .' $b) ? -1 : 1;
        '));

        $data = array_slice($data, 0, 50);

        //Write in redis
        $this->redis->setJson('vote.top', $data);

    }

    public function broadcast($message)
    {
        $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
        $channel = $connection->channel();

        $channel->queue_declare('guardian.broadcast', false, false, false, false);
        $obj = (object)[
            'expire' => (time() + 604800 * 1000),
            'message' => $message
        ];

        $msg = new AMQPMessage(json_encode($obj));
        $channel->basic_publish($msg, '','guardian.broadcast');


        $channel->close();
        $connection->close();
    }


    public function sendRabbitData($player,$product){
        try {
            //Connection to rabbitMQ server
            $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
            $channel = $connection->channel();

            $shopQueue = $product['queue'];
            $command = str_replace("%player%", $player, $product['command']);

            $channel->exchange_declare('shopLinker.' . $shopQueue, 'fanout', false, false, false, false);
            $sanction = (object)[
                'dataType' => 'VOTE',
                'playerName' => $player,
                'displayName' => $product['name'],
                'command' => $command,
                'ingame' => false,
                'price' => 0
            ];

            $message = (object)[
                'expire' => (time() + 604800) * 1000,
                'message' => json_encode($sanction)
            ];
            $msg = new AMQPMessage(json_encode($message));
            $channel->basic_publish($msg, 'shopLinker.' . $shopQueue);


            $channel->close();
            $connection->close();
            return "null";
        }catch (Exception $e)
        {
            return var_dump($e);
        }
    }



}