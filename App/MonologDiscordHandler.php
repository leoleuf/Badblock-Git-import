<?php
namespace App;

use Monolog\Logger;
use Monolog\Handler\AbstractProcessingHandler;

class MonologDiscordHandler extends AbstractProcessingHandler
{
	private $initialized = false;
	private $webhooks;
	private $statement;

	/**
	 * MonologDiscordHandler constructor.
	 * @param \GuzzleHttp\Client $guzzle
	 * @param bool $webhooks
	 * @param int $level
	 * @param bool $bubble
	 */
	public function __construct($webhooks, $level = Logger::DEBUG, $bubble = true)
	{
		$this->webhooks = $webhooks;
		parent::__construct($level, $bubble);

		$this->write($webhooks);
	}

	/**
	 * @param array $record
	 */
	protected function write(array $record)
	{
//
        $data = array("username" => "Logger Site","embeds" => array(0 => array(
            "url" => "https://dev-web.badblock.fr",
            "title" => "[Info] Logs Site",
            "description" => "",
            "color" => 1
		)));

        $curl = curl_init("https://discordapp.com/api/webhooks/373808432324542464/g_ZJQXYA0yPj7LyHebSQZA14eAbLxB7w8idL50weFHX-rSGpdI-cu-fiu0gbHl9BIa8F");
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);



    }
}