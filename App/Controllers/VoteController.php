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
        $query = "SELECT username FROM xf_user WHERE username = '". $_POST['pseudo'] ."' LIMIT 1";
        $data = $this->container->mysql_forum->fetchRow($query);
        //On vérifie si il est inscrit sur le forum
        if (count($data) > 0){
            $collection = $this->container->mongo->vote;
            $cto = $collection->count(['name' => strtolower($_POST['pseudo'])]);
            //n'a jamais voté -> on créer le fichier de vote
            if ($cto == 0){
                $insert = [
                    "name" => strtolower($_POST['pseudo']),
                    "ip" => $_SERVER['REMOTE_ADDR'],
                    "rpg" => ["number" => 0,"time" => 0],
                    "msf" => ["number" => 0,"time" => 0],
                    "bronze" => 0
                ];
                $collection->insertOne($insert);
            }

                //vote tout les 3h
                //http://www.rpg-paradize.com/?page=vote&vote=45397
                //https://serveur-prive.net/minecraft/badblock-198
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['rpg']['time'] + 10800) <= $date->getTimestamp()){
                    $rpg = true;
                }else{
                    $rpg = false;
                }
                //vote tout les 1.5h
                $date = new DateTime();
                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()){
                    $msf = true;
                }else{
                    $msf = false;
                }
                $time_rpg = $mongo['rpg']['time'] + 10800;
                $time_msf = $mongo['msf']['time'] + 5400;
                if ($msf == false && $rpg == false){
                    $resp = json_encode(['rpg' => $rpg, "time_rpg" => date("H:i", $time_rpg),'msf' => $msf,"time_msf" => date("H:i", $time_msf)]);
                    return $response->write($resp)->withStatus(405);
                }else{
                    $resp = json_encode(['rpg' => $rpg, "time_rpg" => date("H:i", $time_rpg),'msf' => $msf,"time_msf" => date("H:i", $time_msf)]);
                    return $response->write($resp)->withStatus(405);
                }
        }else{
            return $response->write("User not found !")->withStatus(404);
        }
    }

    public function check(RequestInterface $request, ResponseInterface $response){
        $query = "SELECT username FROM xf_user WHERE username = '". $_POST['pseudo'] ."' LIMIT 1";
        $data = $this->container->mysql_forum->fetchRow($query);
        //On vérifie si il est inscrit sur le forum
        if (count($data) > 0){
            $collection = $this->container->mongo->vote;
            $cto = $collection->count(['name' => strtolower($_POST['pseudo'])]);
            //n'a jamais voté -> on créer le fichier de vote
            if ($cto == 0){
                $insert = [
                    "name" => strtolower($_POST['pseudo']),
                    "ip" => $_SERVER['REMOTE_ADDR'],
                    "rpg" => ["number" => 0,"time" => 0],
                    "msf" => ["number" => 0,"time" => 0],
                    "bronze" => 0
                ];
                $collection->insertOne($insert);
            }
            if ($_POST['vote'] == "rpg"){
                //vote tout les 3h
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['rpg']['time'] + 10800) <= $date->getTimestamp()){
                    return $response->write("http://www.rpg-paradize.com/?page=vote&vote=45397")->withStatus(200);
                }else{
                    $time = $mongo['rpg']['time'] + 10800;
                    return $response->write(date("H:i", $time))->withStatus(405);
                }
            }elseif ($_POST['vote'] == "msf"){
                //vote tout les 1.5h
                $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);
                $date = new DateTime();
                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()){
                    return $response->write("https://serveur-prive.net/minecraft/badblock-198/vote")->withStatus(200);
                }else{
                    $time = $mongo['rpg']['time'] + 5400;
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
        if ($type == "rpg"){
            if ($_POST['out'] != null || $_POST['out'] != ""){
                $out = $this->rpgapi->getOut();
                if ($_POST['out'] == $out){
                    $collection = $this->container->mongo->vote;
                    $mongo = $collection->findOne(['name' => strtolower($_POST['pseudo'])]);

                    if (($mongo['rpg']['time'] + 10800) <= $date->getTimestamp()){
                        $number = $mongo['rpg']['number'] + 1;
                        $bronze = $mongo['bronze'] + 2;
                        $end = $collection->updateOne(["name" => strtolower($_POST['pseudo'])],['$set' => ["bronze" => $bronze,"rpg.time" => $date->getTimestamp(),"rpg.number" => $number]]);

                        $this->top($_POST['pseudo'], 1);

                        if ($this->container->session->exist('user')){
                            return $response->write("2")->withStatus(200);
                        }else{
                            return $response->write("2")->withStatus(403);
                        }
                    }else{
                        return $response->write("")->withStatus(500);
                    }
                }else{
                    return $response->write("Out invalide")->withStatus(405);
                }
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
                    $bronze = $mongo['bronze'] + 1;
                    $end = $collection->updateOne(["name" => strtolower($_POST['pseudo'])], ['$set' => ["bronze" => $bronze, "msf.time" => $date->getTimestamp(), "msf.number" => $number]]);

                    $this->top($_POST['pseudo'], 1);

                    if ($this->container->session->exist('user')) {
                        return $response->write("1")->withStatus(200);
                    } else {
                        return $response->write("1")->withStatus(403);
                    }
                }else{
                    return $response->write("")->withStatus(500);
                }
            }else{
                return $response->write("Vote invalid")->withStatus(405);
            }
        }
    }


    public function loterie(RequestInterface $request, ResponseInterface $response, $type){
        if ($type['type'] == "1"){
            if (isset($_POST['pseudo'])){
                //Vérification des bronzes
                $doc = $this->container->mongo->vote->findOne(['name' => strtolower($_POST['pseudo'])]);
                if ($doc['bronze'] >= 1){
                    $bronze = $doc['bronze'] - 1;
                    $doc = $this->container->mongo->vote->updateOne(['name' => strtolower($_POST['pseudo'])], ['$set' => ["bronze" => $bronze]]);


                    //Random 1 / 3
                    $nb1 = mt_rand(1, 3);
                    $nb2 = mt_rand(1, 3);
                    $nb3 = mt_rand(1, 3);
                    $nb4 = mt_rand(1, 3);

                    $nb = ((($nb1 + $nb2 + $nb3 + $nb4) / 4) / 100);

                    //Ajout des points boutique au compte TODO

                    return $response->write($nb . " points boutiques")->withStatus(200);

                }else{
                    return $response->write(1)->withStatus(404);
                }
            }else{
                return $response->write("No pseudo")->withStatus(405);
            }
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