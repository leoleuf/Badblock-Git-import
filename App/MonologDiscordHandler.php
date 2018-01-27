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
	 * @param int $webHooks
	 * @param bool|int $level
	 * @param bool $bubble
	 * @internal param \GuzzleHttp\Client $guzzle
	 * @internal param bool $webhooks
	 */
	public function __construct($webHooks, $level = Logger::DEBUG, $bubble = true)
	{
		$this->webhooks = $webHooks;
		parent::__construct($level, $bubble);
	}

	/**
	 * @param array $record
	 */
	protected function write(array $record)
	{
		foreach ($this->webhooks AS $webHook) {
			$data = [
				"username" => "Logger Site",
				"embeds" => [
					[
						"url" => "https://dev-web.badblock.fr",
						"title" => "[Info] Logs Site",
						"description" => $record['message'],
						"color" => 1
					]
				]
			];

			$curl = curl_init($webHook);
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
			curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
			curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
			curl_exec($curl);
		}
	}
}