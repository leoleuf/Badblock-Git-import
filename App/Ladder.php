<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 26/10/2017
 * Time: 19:28
 */

namespace App;

/**
 * Class Ladder
 *
 * SDK for internal minecraft bungeecoord HTTP REST api
 *
 * @package App
 */
class Ladder
{
	private $scheme = 'http://';

	public function __construct($container, $config)
	{
		$this->container = $container;
		$this->guzzle = $container->guzzle;
		$this->config = $config;
	}

	/**
	 * Send data to a url without endpoint
	 *
	 * @param $type
	 * @param $data
	 * @return mixed
	 */
	private function sendData($type, $data)
	{
		$request = $this->guzzle->request(
			'POST',
			$this->scheme . $this->config->ip . ':' . $this['port'] . "/" . $type,
			[
				'form_params' => $data
			]
		);
		$response = $request->getBody();

		return $response;
	}

	/**
	 * Send a broadcast message to defined server
	 *
	 * @param $server
	 * @param $message
	 * @return mixed
	 */
	public function serverBroadcast($server, $message)
	{
		return $this->sendData("server/broadcast/", ["server" => $server, "message" => $message]);
	}

	/**
	 * Know if player exist in database of network
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerExists($player)
	{
		return json_encode($this->sendData("players/exists/", ["name" => $player]), true)->exist;
	}

	/**
	 * Know if the defined player is online
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerOnline($player)
	{
		//return json_decode($this->sendData("players/isConnected/", ["name" => $player]), true);
        return ['connected' => true];
	}

	/**
	 * Get player data
	 *
	 * @param $player
	 * @return string
	 */
	public function playerGetData($player)
	{
		return json_encode($this->sendData("players/getData/", ["name" => $player]), true);
	}

	/**
	 * unknow
	 * //TODO: make doc
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerGetConnectedServer($player)
	{
		return json_decode($this->sendData("players/getConnectedServer/", ["name" => $player]));
	}

	/**
	 * Send private message to a specified online player in the network
	 *
	 * @param $player
	 * @param $message
	 * @return mixed
	 */
	public function playerSendMessage($player, $message)
	{
		//return $this->sendData("players/sendMessage/", ["name" => $player, "message" => $message]);
        return true;
	}

	/**
	 * unknow
	 * //TODO: make doc
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerSendServerMessage($player, $message)
	{
		return $this->sendData("players/sendServerMessage/", ["name" => $player, "message" => $message]);
	}

	/**
	 * Unknow
	 * Update player data
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerUpdateData($player, $dataName, $dataData)
	{
		return $this->sendData("players/updateData/", ["name" => $player, $dataName => $dataData]);
	}

	/**
	 * Add player in a group
	 *
	 * @param $player
	 * @return mixed
	 */
	public function playerAddGroup($player, $group, $duration = -1)
	{
		return $this->sendData("players/addGroup/", ["name" => $player, "group" => $group, "duration" => $duration]);
	}


}