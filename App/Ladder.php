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
			"http://node01-int.clusprv.badblock-network.fr:8080/" . $type,
			[
                'form_params' => $this->parse($data)
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
		return json_decode($this->sendData("players/isConnected/", ["name" => $player]), true);
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
		return $this->sendData("players/sendMessage/", ["name" => $player, "message" => $message]);
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

    public function encryptPassword($password) {
        $salt = substr(hash('whirlpool', uniqid(rand(), true)), 0, 12);
        $hash = strtolower(hash('whirlpool', $salt . $password));
        $saltPos = (strlen($password) >= strlen($hash) ? strlen($hash) - 1 : strlen($password));
        return substr($hash, 0, $saltPos) . $salt . substr($hash, $saltPos);
    }

    public function parse($output){
        $output = str_replace("&quot;","\"", $output);
        $output = str_replace("&amp;", "&", $output);
        $output = str_replace("&euro;", "€", $output);
        $output = str_replace("&lt;", "<", $output);
        $output = str_replace("&gt;", ">", $output);
        $output = str_replace("&oelig;", "oe", $output);
        $output = str_replace("&Yuml;", "Y", $output);
        $output = str_replace("&nbsp;", " ", $output);
        $output = str_replace("&iexcl;", "¡", $output);
        $output = str_replace("&cent;", "¢", $output);
        $output = str_replace("&pound;", "£", $output);
        $output = str_replace("&curren;", "¤", $output);
        $output = str_replace("&yen;", "¥", $output);
        $output = str_replace("&brvbar;", "¦", $output);
        $output = str_replace("&sect;", "§", $output);
        $output = str_replace("&uml;", "¨", $output);
        $output = str_replace("&copy;", "©", $output);
        $output = str_replace("&ordf;", "ª", $output);
        $output = str_replace("&laquo;", "«", $output);
        $output = str_replace("&not;", "¬", $output);
        $output = str_replace("&reg;", "®", $output);
        $output = str_replace("&masr;", "¯", $output);
        $output = str_replace("&deg;", "°", $output);
        $output = str_replace("&plusmn;", "±", $output);
        $output = str_replace("&sup2;", "²", $output);
        $output = str_replace("&sup3;", "³", $output);
        $output = str_replace("&acute;", "'", $output);
        $output = str_replace("&micro;", "µ", $output);
        $output = str_replace("&para;", "¶", $output);
        $output = str_replace("&middot;", "·", $output);
        $output = str_replace("&cedil;", "¸", $output);
        $output = str_replace("&sup1;", "¹", $output);
        $output = str_replace("&ordm;", "º", $output);
        $output = str_replace("&raquo;", "»", $output);
        $output = str_replace("&frac14;", "¼", $output);
        $output = str_replace("&frac12;", "½", $output);
        $output = str_replace("&frac34;", "¾", $output);
        $output = str_replace("&iquest;", "¿", $output);
        $output = str_replace("&Agrave;", "À", $output);
        $output = str_replace("&Aacute;", "À", $output);
        $output = str_replace("&Acirc;", "Â", $output);
        $output = str_replace("&Atilde;", "Ã", $output);
        $output = str_replace("&Auml;", "Ä", $output);
        $output = str_replace("&Aring;", "Å", $output);
        $output = str_replace("&Aelig", "Æ", $output);
        $output = str_replace("&Ccedil;", "Ç", $output);
        $output = str_replace("&Egrave;", "È", $output);
        $output = str_replace("&Eacute;", "É", $output);
        $output = str_replace("&Ecirc;", "Ê", $output);
        $output = str_replace("&Euml;", "Ë", $output);
        $output = str_replace("&Igrave;", "Ì", $output);
        $output = str_replace("&Iacute;", "Í", $output);
        $output = str_replace("&Icirc;", "Î", $output);
        $output = str_replace("&Iuml;", "Ï", $output);
        $output = str_replace("&eth;", "Ð", $output);
        $output = str_replace("&Ntilde;", "Ñ", $output);
        $output = str_replace("&Ograve;", "Ò", $output);
        $output = str_replace("&Oacute;", "Ó", $output);
        $output = str_replace("&Ocirc;", "Ô", $output);
        $output = str_replace("&Otilde;", "Õ", $output);
        $output = str_replace("&Ouml;", "Ö", $output);
        $output = str_replace("&times;", "Ö", $output);
        $output = str_replace("&Oslash;", "Ø", $output);
        $output = str_replace("&Ugrave;", "Ù", $output);
        $output = str_replace("&Uacute;", "Ú", $output);
        $output = str_replace("&Ucirc;", "Û", $output);
        $output = str_replace("&Uuml;", "Ü", $output);
        $output = str_replace("&Yacute;", "Ý", $output);
        $output = str_replace("&thorn;", "Þ", $output);
        $output = str_replace("&szlig;", "ß", $output);
        $output = str_replace("&agrave;", "à", $output);
        $output = str_replace("&aacute;", "á", $output);
        $output = str_replace("&acirc;", "â", $output);
        $output = str_replace("&atilde;", "ã", $output);
        $output = str_replace("&auml;", "ä", $output);
        $output = str_replace("&aring;", "å", $output);
        $output = str_replace("&aelig;", "æ", $output);
        $output = str_replace("&ccedil;", "ç", $output);
        $output = str_replace("&egrave;", "è", $output);
        $output = str_replace("&eacute;", "é", $output);
        $output = str_replace("&ecirc;", "ê", $output);
        $output = str_replace("&euml;", "ë", $output);
        $output = str_replace("&igrave;", "ì", $output);
        $output = str_replace("&iacute;", "í", $output);
        $output = str_replace("&icirc;", "î", $output);
        $output = str_replace("&iuml;", "ï", $output);
        $output = str_replace("&eth;", "ð", $output);
        $output = str_replace("&ntilde;", "ñ", $output);
        $output = str_replace("&ograve;", "ò", $output);
        $output = str_replace("&oacute;", "ó", $output);
        $output = str_replace("&ocirc;", "ô", $output);
        $output = str_replace("&otilde;", "õ", $output);
        $output = str_replace("&ouml;", "ö", $output);
        $output = str_replace("&divide;", "÷", $output);
        $output = str_replace("&oslash;", "ø", $output);
        $output = str_replace("&ugrave;", "ù", $output);
        $output = str_replace("&uacute;", "ú", $output);
        $output = str_replace("&ucirc;", "û", $output);
        $output = str_replace("&uuml;", "ü", $output);
        $output = str_replace("&yacute;", "ý", $output);
        $output = str_replace("&thorn;", "þ", $output);
        $output = str_replace("&yuml;", "ÿ", $output);
        $output = str_replace("&bull;", "•", $output);
        $output = str_replace("Â", "", $output);
        return $output;
    }


}