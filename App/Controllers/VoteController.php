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
use DateTime;

class VoteController extends Controller
{

    public function getHome(RequestInterface $request, ResponseInterface $response){
        //Read Top from Redis
        $top = $this->redis->getJson('vote.top');
        if ($this->container->session->exist('user')) {
            $player = $this->session->getProfile('username')['username'];
        }else{
            $player = "";
        }

        return $this->render($response, 'vote.index', ['top' => $top, 'player' => $player]);

    }

    public function start(RequestInterface $request, ResponseInterface $response){
        //$query = "SELECT username FROM xf_user WHERE username = '". $_POST['pseudo'] ."' LIMIT 1";
        //$data = $this->container->mysql_forum->fetchRow($query);
        //On vérifie si il est inscrit sur le forum
        if (true){
            $collection = $this->container->mongo->vote;
            $cto = $collection->count(['name' => strtolower($_POST['pseudo'])]);
            //n'a jamais voté -> on créer le fichier de vote
            if ($cto == 0){
                $insert = [
                    "name" => strtolower($_POST['pseudo']),
                    "ip" => $_SERVER['REMOTE_ADDR'],
                    "asf" => ["number" => 0,"time" => 0],
                    "msf" => ["number" => 0,"time" => 0],
                    "vote" => 0,
                    "under_vote" => false
                ];
                $collection->insertOne($insert);
            }

                //vote tout les 3h
                //http://www.rpg-paradize.com/?page=vote&vote=45397
                //https://serveur-prive.net/minecraft/badblock-198
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['asf']['time'] + 10800) <= $date->getTimestamp()){
                    $asf = true;
                }else{
                    $asf = false;
                }
                //vote tout les 1.5h
                $date = new DateTime();
                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()){
                    $msf = true;
                }else{
                    $msf = false;
                }
                $time_asf = $mongo['asf']['time'] + 10800;
                $time_msf = $mongo['msf']['time'] + 5400;
                if ($msf == false && $asf == false){
                    $resp = json_encode(['asf' => $asf, "time_asf" => date("H:i", $time_asf),'msf' => $msf,"time_msf" => date("H:i", $time_msf)]);
                    return $response->write($resp)->withStatus(405);
                }else{
                    $resp = json_encode(['asf' => $asf, "time_asf" => date("H:i", $time_asf),'msf' => $msf,"time_msf" => date("H:i", $time_msf)]);
                    return $response->write($resp)->withStatus(405);
                }
        }else{
            return $response->write("User not found !")->withStatus(404);
        }
    }

    public function check(RequestInterface $request, ResponseInterface $response){
        //$query = "SELECT username FROM xf_user WHERE username = '". $_POST['pseudo'] ."' LIMIT 1";
        //$data = $this->container->mysql_forum->fetchRow($query);
        //On vérifie si il est inscrit sur le forum
        if (true){
            $collection = $this->container->mongo->vote;
            $cto = $collection->count(['name' => strtolower($_POST['pseudo'])]);
            //n'a jamais voté -> on créer le fichier de vote
            if ($cto == 0){
                $insert = [
                    "name" => strtolower($_POST['pseudo']),
                    "ip" => $_SERVER['REMOTE_ADDR'],
                    "asf" => ["number" => 0,"time" => 0],
                    "msf" => ["number" => 0,"time" => 0],
                    "vote" => 0,
                    "under_vote" => false
                ];
                $collection->insertOne($insert);
            }
            if ($_POST['vote'] == "asf"){
                //vote tout les 3h
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['asf']['time'] + 10800) <= $date->getTimestamp()){
                    return $response->write("https://serveur-minecraft.net/info/93")->withStatus(200);
                }else{
                    $time = $mongo['asf']['time'] + 10800;
                    return $response->write(date("H:i", $time))->withStatus(405);
                }
            }elseif ($_POST['vote'] == "msf"){
                //vote tout les 1.5h
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()){
                    return $response->write("https://serveur-prive.net/minecraft/badblock-198/vote")->withStatus(200);
                }else{
                    $time = $mongo['asf']['time'] + 5400;
                    return $response->write(date("H:i", $time))->withStatus(405);
                }
            }
        }else{
            return $response->write("User not found !")->withStatus(404);
        }
    }



    public function end(RequestInterface $request, ResponseInterface $response,$type){
        //New data for back usage
        $date = new DateTime();

        $type = $type["type"];
        if ($type == "asf"){
            $SERVER_ID = 93; // ID du serveur
            $KEY = 'ePwvH8vBvcVUthJettUe9SW0fKsZ0V'; // Api key du serveur
            $IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant
            $SM = "http://serveur-minecraft.net/api/$SERVER_ID/$KEY/?ip=$IP";
            $result = @file_get_contents($SM);

            if ($result == 1 || true){
                $collection = $this->container->mongo->vote;
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);

                if (($mongo['asf']['time'] + 5400) <= $date->getTimestamp()) {
                    $number = $mongo['asf']['number'] + 1;
                    $vote = $mongo['vote'] + 1;
                    $end = $collection->updateOne(["name" => strtolower($_POST['pseudo'])], ['$set' => ["under_vote" => true,"vote" => $vote, "asf.time" => $date->getTimestamp(), "asf.number" => $number]]);

                    $this->top($_POST['pseudo'], 1);

                    return $response->write("1")->withStatus(200);
                }else{
                    return $response->write("")->withStatus(500);
                }
            }else{
                return $response->write("Vote invalid")->withStatus(405);
            }
        }elseif ($type == "msf"){
            $API_id = 198; // ID de votre serveur
            $API_ip = $_SERVER['REMOTE_ADDR']; // Adresse IP de l'utilisateur
            $API_url = "https://serveur-prive.net/api/vote/$API_id/$API_ip";
            $API_call = @file_get_contents($API_url);

            if ($API_call == 1 || true){
                $collection = $this->container->mongo->vote;
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);

                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()) {
                    $number = $mongo['msf']['number'] + 1;
                    $vote = $mongo['vote'] + 1;
                    $end = $collection->updateOne(["name" => strtolower($_POST['pseudo'])], ['$set' => ["under_vote" => true,"vote" => $vote, "msf.time" => $date->getTimestamp(), "msf.number" => $number]]);

                    $this->top($_POST['pseudo'], 1);

                    return $response->write("1")->withStatus(200);
                }else{
                    return $response->write("")->withStatus(500);
                }
            }else{
                return $response->write("Vote invalid")->withStatus(405);
            }
        }
    }


    public function loterie(RequestInterface $request, ResponseInterface $response, $type){
        if (isset($_POST['pseudo'])){

        }else{
            return $response->write("No pseudo")->withStatus(405);
        }
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



}