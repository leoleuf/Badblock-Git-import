<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 26/10/2017
 * Time: 19:28
 */

namespace App;

use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;


class Shoplinker
{


    public function __construct($container, $config)
	{
		$this->container = $container;
        $this->ip = $config->ip;
        $this->port = $config->port;
        $this->username = $config->username;
        $this->password = $config->password;
        $this->virtualhost = $config->virtualhost;
        $this->queuePrefix = "shopLinker."; // Don't edit this field
        $this->connection = new AMQPStreamConnection($config->ip, $config->port, $config->username, $config->password, $config->virtualhost);
        $this->channel = $this->connection->channel();
	}


    /**
    dataType types:
    - BUY
    - VOTE
     **/


    public function sendShopData($data,$dataType,$player) {
        $shopObject = (object) [
            'dataType' => $dataType,
            'playerName' => $player,
            'displayName' => $data["name"],
            'command' => $data["command"],
            'ingame' => false,
            'price' => $data["price"]
        ];

        $queue = "shopLinker.".$data["queue"];
        $jsonObject = json_encode($shopObject);

        $message = (object)[
            'expire' => (time() + 604800) * 1000,
            'message' => $jsonObject
        ];

        $msg = new AMQPMessage(json_encode($message));
        $this->channel->basic_publish($msg,$queue);

        //Broadcast on all serveur
        $this->channel->queue_declare('guardian.broadcast', false, false, false, false);
        //Vérification si le produti est en promo
        if ($data['promo'] == true){
            $msg = (object) [
                'expire' => (time() + 604800) * 1000,
                'message' => "&6[Boutique] &c$player&6 à acheté l'offre &b". $data["name"]."&6 en échange de &b". $data["price"]." points boutique &d&kii&r&4(". $data['promo_reduc'] ."%)&d&kii"
            ];
        }else{
            $msg = (object) [
                'expire' => (time() + 604800) * 1000,
                'message' => "&6[Boutique] &c$player&a à acheté l'offre &b". $data["name"]."&6 en échange de &b". $data["price"]." points boutique &a!"
            ];
        }

        $msg = new AMQPMessage(json_encode($msg));
        $this->channel->basic_publish($msg, '', 'guardian.broadcast');
    }


}