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


class Docker
{


	public function __construct($container)
	{
		$this->container = $container;

        $connection = new AMQPStreamConnection($this->container->config['rabbit']['ip'], $this->container->config['rabbit']['port'], $this->container->config['rabbit']['username'], $this->container->config['rabbit']['password'], $this->container->config['rabbit']['virtualhost']);
        $this->channel = $connection->channel();

	}

	public function publishData($queue , $message){
        $this->channel->exchange_declare($queue, 'fanout', false, false, false, false);

        $obj = (object)[
            'expire' => (time() + 604800) * 1000,
            'message' => json_encode($message)
        ];

        $msg = new AMQPMessage(json_encode($obj));
        $this->channel->basic_publish($msg, $queue);

        return true;
    }

    public function sendData($queue , $message){
        $this->channel->queue_declare($queue, false, false, false, false);

        $obj = (object)[
            'expire' => (time() + 604800) * 1000,
            'message' => json_encode($message)
        ];

        $msg = new AMQPMessage(json_encode($obj));
        $this->channel->basic_publish($msg, '', $queue);

        return true;
    }



    public function sendPrivateMessage($Username, $message){
        $message = str_replace("&", "§", $message);
        $Object = [
            'playerName' => $Username,
            'type' => "SEND_MESSAGE",
            'content' => $message
        ];

        $this->publishData("bungee.processing.players.linkQueue", $Object);
    }

    public function broadcast($message){
        $message = str_replace("&", "§", $message);
        $Object = [
            'type' => "BROADCAST",
            'content' => $message
        ];

        $this->publishData("bungee.processing.bungee.linkQueue", $Object);
    }

	public function kickPlayer($Username, $Reason){
        $Object = [
            'playerName' => $Username,
            'type' => "KICK",
            'content' => $Reason
        ];

        $this->publish("bungee.processing.players.linkQueue", $Object);
    }

    public function banPlayer($Username, $Reason, $Time){
        $message = [
            'punishmentType' => "BAN",
            'playerName' => $Username,
            'reason' => $Reason,
            'isKey' => false,
            'time' => $Time
        ];

        $this->sendData('bungee.punishment', $message);
    }

}