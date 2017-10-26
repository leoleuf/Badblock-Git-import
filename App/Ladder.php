<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 26/10/2017
 * Time: 19:28
 */

namespace App;


class Ladder
{
    public function __construct($container, $config)
    {
        $this->container = $container;
        $this->guzzle = $container->guzzle;

        $this->config = $config;
    }



    // Do not use
    function sendData($type, $data) {
        $curl = curl_init("http://". $this->config['ip'].":". $this->config['port']."/".$type);
        curl_setopt($curl, CURLOPT_POST, true);
        curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        $response = curl_exec($curl);
        curl_close($curl);
        return $response;
    }

    // Broadcast to a server
    function serverBroadcast($server, $message) {
        return $this->sendData("server/broadcast/", array("server" => $server, "message" => $message));
    }

    // If player exists
    function playerExists($player) {
        return json_encode($this->sendData("players/exists/", array("name" => $player)), true)->exist;
    }

    // If player is online
    function playerOnline($player) {
        return json_decode($this->sendData("players/isConnected/", array("name" => $player)), true);
    }

    // Get player data
    function playerGetData($player) {
        return json_encode($this->sendData("players/getData/", array("name" => $player)), true);
    }

    // Get player server
    function playerGetConnectedServer($player) {
        return json_decode($this->sendData("players/getConnectedServer/", array("name" => $player)));
    }

    // Player send message
    function playerSendMessage($player, $message) {
        return $this->sendData("players/sendMessage/", array("name" => $player, "message" => $message));
    }

    // Player send server message
    function playerSendServerMessage($player, $message) {
        return $this->sendData("players/sendServerMessage/", array("name" => $player, "message" => $message));
    }

    // Player update data
    function playerUpdateData($player, $dataName, $dataData) {
        return $this->sendData("players/updateData/", array("name" => $player, $dataName => $dataData));
    }

    // Player update data
    function playerAddGroup($player, $group, $duration = -1) {
        return $this->sendData("players/addGroup/", array("name" => $player, "group" => $group, "duration" => $duration));
    }


}