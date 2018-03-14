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


    public function sendShopData($shopQueue, $dataType, $playerName, $displayName, $command, $price) {
        $shopObject = (object) [
            'dataType' => "BUY",
            'playerName' => "Fluor",
            'displayName' => "Grade Gold",
            'command' => "perms user ",
            'ingame' => false,
            'price' => "200"
        ];

        $queue = "shopLinker.hub";
        $jsonObject = json_encode($shopObject);

        $message = (object)[
            'expire' => (time() + 604800) * 1000,
            'message' => $jsonObject
        ];

        $msg = new AMQPMessage(json_encode($message));
        $this->channel->basic_publish($msg,$queue);
    }


}