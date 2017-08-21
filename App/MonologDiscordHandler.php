<?php
namespace App;

use Monolog\Logger;
use Monolog\Handler\AbstractProcessingHandler;

class MonologDiscordHandler extends AbstractProcessingHandler
{
	private $initialized = false;
	private $guzzle;
	private $statement;

	public function __construct(\GuzzleHttp\Client $guzzle, $level = Logger::DEBUG, $bubble = true)
	{
		$this->guzzle = $guzzle;
		parent::__construct($level, $bubble);
	}

	protected function write(array $record)
	{
		$content = '[' . $record['datetime']->format('Y-m-d H:i:s') . '] ' . getenv('APP_NAME') . '.' . getenv('APP_ENV_NAME') . '.' . $record['level_name'] . ': ' . $record['message'];

		$req = $this->guzzle->request('POST', 'https://discordapp.com/api/webhooks/349118443162959872/ZpBd9tJtsBF5bK_gX4kYkwDWOgD2xYtQs3e7CaHzvdXPr1LXfgK4d_5vi3lH6n7-7pM5', [
			'form_params' => [
				'content' => $content
			]
		]);
	}
}