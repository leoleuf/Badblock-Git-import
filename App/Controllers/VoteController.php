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
        $toploterie = $this->redis->getJson('vote.toploterie');
        $top = $this->redis->getJson('vote.top');

        $player = "";
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }

        return $this->render($response, 'vote.index', ['toploterie' => $toploterie, 'top' => $top, 'player' => $player]);

    }

    public function voteRedirect(RequestInterface $request, ResponseInterface $response){
        $toploterie = $this->redis->getJson('vote.toploterie');
        $top = $this->redis->getJson('vote.top');

        $player = "";
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }

        return $this->render($response, 'vote.vote-redirect', ['toploterie' => $toploterie, 'top' => $top]);
    }

    public function voteRedirectCustom(RequestInterface $request, ResponseInterface $response){
        $toploterie = $this->redis->getJson('vote.toploterie');
        $top = $this->redis->getJson('vote.top');

        $player = "";
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }

        return $this->render($response, 'vote.vote-select', ['toploterie' => $toploterie, 'top' => $top]);
    }

    public function playerexists(RequestInterface $request, ResponseInterface $response)
    {
        if (!isset($_POST['pseudo']))
        {
            return $response->write("<i class=\"fas fa-exclamation-circle\"></i> User not found !")->withStatus(404);
        }

        $pseudo = htmlspecialchars($_POST['pseudo']);
        $pseudo = strtolower($pseudo);

        if (getenv('APP_DEBUG') == 0){
            $query = "SELECT username FROM xf_user WHERE username = '". $pseudo ."' LIMIT 1";
            $data = $this->container->mysql_forum->fetchRow($query);

            // user exists?
            if ($data == false)
            {
                return $response->write("<i class=\"fas fa-exclamation-circle\"></i> User not found !")->withStatus(404);
            }
        }

        $collection = $this->container->mongo->votes_logs;
        $dbh = $collection->findOne(['name' => $pseudo, 'timestamp' => ['$gte' => (time() - 5400)]]);
        if ($dbh != null && isset($dbh['timestamp']))
        {
            $t = ($dbh['timestamp'] + 5400) - time();
            return $response->write("<i class=\"far fa-clock\"></i> Tu pourras voter dans ".gmdate("H:i:s", $t).".")->withStatus(405);
        }

        return $response->write("Tu peux voter !")->withStatus(405);
    }

    public function badblock(RequestInterface $request, ResponseInterface $response)
    {
        $API_id = 198; // ID du serveur
        $API_da = 'clic'; // vote,clic,commentaire ou note
        $API_url = "https://serveur-prive.net/api/stats/$API_id/$API_da";
        $API_call = @file_get_contents($API_url);

        $API_id2 = 265; // ID du serveur
        $API_da2 = 'clic'; // vote,clic,commentaire ou note
        $API_url2 = "https://serveur-prive.net/api/stats/$API_id2/$API_da2";
        $API_call2 = @file_get_contents($API_url2);

        if ($API_call2 + 27 > $API_call)
        {
            header("Location: https://serveur-prive.net/vote.php?c=198");
            exit;
            return;
        }

        header("Location: https://badblock.fr/jouer");
        exit;
    }

    public function award(RequestInterface $request, ResponseInterface $response)
    {

        if (!isset($_POST['pseudo']) && !isset($_POST['type']))
        {
            return $response->write("<i class=\"fas fa-exclamation-circle\"></i> User not found !")->withStatus(404);
        }

        $dev = getenv('APP_DEBUG') == 1;

        $displayPseudo = $_POST['pseudo'];

        $pseudo = htmlspecialchars($displayPseudo);
        $type = htmlspecialchars($_POST['type']);

        $pseudo = strtolower($pseudo);

        if ($type == 2)
        {
            $type = 6;
        }

        // TODO move
        $types = array(
          1 => 'ptsboutique',
          2 => 'skyb2',
          3 => 'hub',
          4 => 'faction',
          5 => 'box',
          6 => 'skyb2'
        );

       /* if ($type == 6)
        {
            return $response->write("Le Nouveau SkyBlock arrive Bientôt !")->withStatus(200);
        }*/

        // unknown type
        if (!isset($types[$type]))
        {
            return $response->write("<i class=\"fas fa-exclamation-circle\"></i> User not found !")->withStatus(404);
        }

        if (!$dev)
        {
            $query = "SELECT username FROM xf_user WHERE username = '". $pseudo ."' LIMIT 1";
            $data = $this->container->mysql_forum->fetchRow($query);

            // user exists?
            if ($data == false)
            {
                return $response->write("<i class=\"fas fa-exclamation-circle\"></i> User not found !")->withStatus(404);
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

        if (isset($_POST['internal_ip']))
        {
            $API_ip = strtolower($_POST['internal_ip']);
        }

        $API_id = 198; // ID du serveur
        $API_url = "https://serveur-prive.net/api/vote/$API_id/$API_ip";
        $API_call = @file_get_contents($API_url);

        if ($API_call != 1)
        {
            // looking for sm
            $API_id = 93; // ID du serveur
            $API_key = "ePwvH8vBvcVUthJettUe9SW0fKsZ0V"; // Clé API
            $API_url = "https://serveur-minecraft.net/api/$API_id/$API_key/?ip=$API_ip";

            $API_call = @file_get_contents($API_url);
            $API_call = ($API_call == 'true') ? true : false;
        }
        else
        {
            $API_call = true;
        }

        // voted?
        if (!$dev && $API_call != true)
        {

            return $response->write("<i class=\"fas fa-exclamation-circle\"></i> Tu n'as pas voté.")->withStatus(405);
        }

        if ($type == 1)
        {
            $user = $this->container->mongoServer->players->findOne(['name' => strtolower($pseudo)]);
            $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);
            if ($money == null)
            {
                $data = [
                    "uniqueId" => $user['uniqueId'],
                    "points" => 2
                ];
                $this->container->session->set('points', 2);
                $this->container->mongo->fund_list->insertOne($data);
            }
            else
            {
                $money['points'] = $money['points'] + 2;
                $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
                $this->container->session->set('points', $money['points']);
            }
        }

        $queue = $types[$type];


        $collection = $this->container->mongo->votes_logs;
        $dbh = $collection->count(['name' => $pseudo, 'timestamp' => ['$gte' => (time() - 5400)]]);
        // 2 sites de vote, 3 sites à venir?
        if ($dbh == null || $dbh <= 3) {
            $collection = $this->container->mongo->votes_awards;
            $cursor = $collection->find(['type' => intval($type)]);

            $maxRandom = 0;

            $things = array();

            foreach ($cursor as $key => $value) {
                $things[$maxRandom] = $value;
                $maxRandom += $value->probability;
            }

            $rand = rand(1, $maxRandom);

            $winItem = null;

            foreach ($things as $key => $value) {
                if ($rand > $key && $winItem != null) {
                    continue;
                }

                $winItem = $value;
            }

            $collection = $this->container->mongo->votes_logs;
            $total = $collection->count(['timestamp' => ['$gte' => (1536163200 - 86400)]]);
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

            $awardName = $winItem->name;

            $product = array(
                'queue' => $queue,
                'command' => $command,
                'name' => $awardName
            );

            $this->sendRabbitData($pseudo, $product);
            $collection->insertOne($insert);

            $this->top($displayPseudo, 1);
            $this->toploterie($displayPseudo, 1);

        }
        else
        {
            // oust les tricheurs
            $awardName = "Rien";
        }

        $dbh = $collection->count(['name' => $pseudo, 'timestamp' => ['$gte' => (1536163200 - 86400)]]);

        $total = max($total, 1);
        $proba = round(($dbh / $total) * 100, 2);
        $this->broadcast(' &e'.$displayPseudo.' &aa voté. Vote toi aussi en faisant &d/vote');
        $this->broadcast(' &aRécompense gagnée : &d'.$awardName);
        $this->broadcast(' &d&lRésultats loterie à 18H ! &b&nhttps://badblock.fr/vote');

        return $response->write("Ton vote a été pris en compte. Tu as gagné ".$awardName .
            " ainsi qu'une participation à la loterie. Tu es désormais à " . $proba . "% de chance de gagner le lot de la loterie. Tirage ce soir à 18H sur la page de vote.")->withStatus(200);
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

    public function toploterie($player, $vote){
        $player = strtolower($player);

        //Collection
        $mongo = $this->container->mongo->stats_vote;

        $date =  date("Y-m-d");
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
        $date =  date("Y-m-d");
        $data = $mongo->findOne(["date" => $date]);


        $data = (array) $data['players'];
        usort($data, create_function('$a, $b', '
        $a = $a["vote"];
        $b = $b["vote"];

        if ($a == $b) return 0;

        $direction = strtolower(trim("desc"));

        return ($a ' . ("desc" == 'desc' ? '>' : '<') .' $b) ? -1 : 1;
        '));

        $data = array_slice($data, 0, 10);

        //Write in redis
        $this->redis->setJson('vote.toploterie', $data);

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
            $command = str_replace("%pseudo%", $player, $product['command']);

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