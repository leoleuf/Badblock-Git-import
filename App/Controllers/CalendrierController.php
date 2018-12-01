<?php

namespace App\Controllers;

use Illuminate\Support\Facades\Auth;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use HansOtt\PSR7Cookies\SetCookie;
use Validator\Validator;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;

class CalendrierController extends Controller
{

    public function index(RequestInterface $request, ResponseInterface $response){
        //Foreach all month
        $month = 12;
        $year = 2018;

        for($d=1; $d<=31; $d++)
        {
            $time=mktime(12, 0, 0, $month, $d, $year);
            if (date('m', $time)==$month)
                $list[]=date('d', $time);
        }

        $day = intval(date('d'));

        //Display recomp
        $Recomp = $this->container->mongo->calendrier->find([])->toArray();

        foreach ($Recomp as $r){
            if ($r['date'] > $day){
                $r['name'] = "";
            }
        }

        $Username = strtolower($this->session->getProfile('username')['username']);



        return $this->render($response, 'calendrier.index', ['date' => $list, 'day' => $day, 'recomp' => $Recomp, 'user' => $Username]);

    }

    public function get(RequestInterface $request, ResponseInterface $response){
        $user = $_POST['username'];
        $d = date('d');

        if ($this->redis->get('calendrier:' . date('d'). ":". $user) == "1" && $user != "fluorl"){
            echo 'Vous avez déjà récupéré votre récompense !';
        }else{
            $Recomp = $this->container->mongo->calendrier->findOne(['date' => "$d"]);
            echo $Recomp->name;

            //Connection to rabbitMQ server
            $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
            $channel = $connection->channel();
            $sanction = (object) [
                'dataType' => 'BUY',
                'playerName' => $user,
                'displayName' => $Recomp->name,
                'command' => $Recomp->command,
                'ingame' => false,
                'price' => 0
            ];
            $message = (object) [
                'expire' => (time() + 604800) * 1000,
                'message' => json_encode($sanction)
            ];
            $msg = new AMQPMessage(json_encode($message));
            $channel->basic_publish($msg, 'shopLinker.'.$Recomp->queue);
            $channel = $connection->channel();
            $channel->queue_declare('guardian.broadcast', false, false, false, false);
            $message = (object) [
                'expire' => (time() + 604800) * 1000,
                'message' => '&6[Info] &b'.$user.' &aa gagné '.$Recomp->name.' &aen ouvrant son calendrier de l\'avent.'
            ];
            $msg = new AMQPMessage(json_encode($message));
            $channel->basic_publish($msg, '', 'guardian.broadcast');
            $message = (object) [
                'expire' => (time() + 604800) * 1000,
                'message' => '&d⇝ Go vite sur https://badblock.fr/calendrier'
            ];
            $msg = new AMQPMessage(json_encode($message));
            $channel->basic_publish($msg, '', 'guardian.broadcast');

            $this->redis->set('calendrier:' . date('d'). ":". $user, 1);
        }

    }



}