<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/02/2018
 * Time: 18:15
 */

namespace App\Controllers;

use function DusanKasan\Knapsack\identity;
use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use DateTime;

class VoteController extends Controller
{

    public function getHome(RequestInterface $request, ResponseInterface $response){

        return $this->render($response, 'vote.index');

    }



    public function check(RequestInterface $request, ResponseInterface $response){
        $query = "SELECT username FROM xf_user WHERE username = '". $_POST['pseudo'] ."' LIMIT 1";
        $data = $this->container->mysql_forum->fetchRow($query);
        //On vérifie si il est inscrit sur le forum
        if (count($data) > 0){
            $collection = $this->container->mongo->test->vote;
            $cto = $collection->count(['name' => $_POST['pseudo']]);
            //n'a jamais voté -> on créer le fichier de vote
            if ($cto == 0){
                $insert = [
                    "name" => $_POST['pseudo'],
                    "ip" => $_SERVER['REMOTE_ADDR'],
                    "rpg" => ["number" => 0,"time" => 0],
                    "msf" => ["number" => 0,"time" => 0]
                ];
                $collection->insertOne($insert);
            }
            if ($_POST['vote'] == "rpg"){
                //vote tout les 3h
                $mongo = $collection->findOne(['name' => $_POST['pseudo']]);
                $date = new DateTime();
                if (($mongo['rpg']['time'] + 10800) <= $date->getTimestamp()){
                    return $response->write("http://www.rpg-paradize.com/?page=vote&vote=45397")->withStatus(200);
                }else{
                    $time = $mongo['rpg']['time'] + 10800;
                    return $response->write(date("H:i", $time))->withStatus(405);
                }
            }elseif ($_POST['vote'] == "msf"){
                //vote tout les 1.5h
                $mongo = $collection->findOne(['name' => $_POST['pseudo']]);
                $date = new DateTime();
                if (($mongo['msf']['time'] + 5400) <= $date->getTimestamp()){
                    return $response->write("https://serveur-prive.net/minecraft/deira-network-173/vote")->withStatus(200);
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
        $type = $type["type"];
        if ($type == "rpg"){
            if ($_POST['out'] != null || $_POST['out'] != ""){
                $out = $this->rpgapi->getOut();
                if ($_POST['out'] == $out){
                    $this->container->mongo->test->vote;
                    if ($this->container->session->exist('user')){
                        return $response->write("2")->withStatus(200);
                    }else{
                        return $response->write("2")->withStatus(403);
                    }
                }else{
                    return $response->write("Out invalide")->withStatus(405);
                }
            }
        }elseif ($type == "msf"){
            $API_id = 173; // ID de votre serveur
            $API_ip = $_SERVER['REMOTE_ADDR']; // Adresse IP de l'utilisateur
            $API_url = "https://serveur-prive.net/api/vote/$API_id/$API_ip";
            $API_call = @file_get_contents($API_url);

            if ($API_call == 1 || true){
                if ($this->container->session->exist('user')){
                    return $response->write("2")->withStatus(200);
                }else{
                    return $response->write("2")->withStatus(403);
                }
            }else{
                return $response->write("Vote invalid")->withStatus(405);
            }
        }
    }

}