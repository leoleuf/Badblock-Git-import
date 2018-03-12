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

    // Do not use
    private function sendShopDataPacket($shopQueue, $shopObject) {
        $queue = $this->queuePrefix.$shopQueue;
        $jsonObject = json_encode($shopObject);
        $this->sendRabbitMessage($queue, $jsonObject);
    }


    public function sendRabbitMessage($queue, $jsonMessage) {
        $channel = $this->channel;
        $message = (object) [
            'expire' => (time() + 604800) * 1000,
            'message' => $jsonMessage
        ];
        $msg = new AMQPMessage(json_encode($message));
        $channel->exchange_declare($msg, 'fanout', false, false, false, false);
        $channel->basic_publish($msg, $queue);
    }

    public function sendShopData($shopQueue, $dataType, $playerName, $displayName, $command, $price) {
        $shopObject = (object) [
            'dataType' => $dataType,
            'playerName' => $playerName,
            'displayName' => $displayName,
            'command' => $command,
            'ingame' => false,
            'price' => $price
        ];
        return $this->sendShopDataPacket($shopQueue, $shopObject);
    }


}