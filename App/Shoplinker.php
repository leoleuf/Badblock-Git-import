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
    public $queuePrefix = "shopLinker."; // Don't edit this field


    public function __construct($container, $config)
	{
		$this->container = $container;
        $this->ip = $config->ip;
        $this->port = $config->port;
        $this->username = $config->username;
        $this->password = $config->password;
        $this->virtualhost = $config->virtualhost;
        $this->connection = new AMQPStreamConnection($config->ip, $config->port, $config->username, $config->password, $config->virtualhost);
        $this->channel = $this->connection->channel();
	}



    /**
    dataType types:
    - BUY
    - VOTE
     **/

// Do not use
    private function sendShopDataPacket($rabbitCredentials, $shopQueue, $shopObject) {
        $queue = $queuePrefix.$shopQueue;
        $jsonObject = json_encode($shopObject);
        sendRabbitMessage($rabbitCredentials, $queue, $jsonObject);
    }


    public function sendRabbitMessage($rabbitCredentials, $queue, $jsonMessage) {
        $channel = $rabbitCredentials->channel;
        $message = (object) [
            'expire' => (time() + 604800) * 1000,
            'message' => $jsonMessage
        ];
        $msg = new AMQPMessage(json_encode($message));
        $channel->exchange_declare($msg, 'fanout', false, false, false, false);
        $channel->basic_publish($msg, $queue);
    }

    public function sendShopData($rabbitCredentials, $shopQueue, $dataType, $playerName, $displayName, $objectName) {
        $shopObject = (object) [
            'dataType' => $dataType,
            'playerName' => $playerName,
            'displayName' => $displayName,
            'objectName' => $objectName
        ];
        return sendShopDataPacket($rabbitCredentials, $shopQueue, $shopObject);
    }

    public function broadcastServer($rabbitCredentials,$server,$message){
        //Todo


    }






}