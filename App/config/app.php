<?php
return [
	'app_name' => getenv('APP_NAME'),
	'app_debug' => (getenv('APP_DEBUG') ? true : false),
	'env_name' => getenv('APP_ENV_NAME'),
	'base_url' => getenv('BASE_URL'),
	'log' => [
		'level' => getenv('LOG_LEVEL'),
		'discord' => getenv('LOG_DISCORD'),
		'discord_webhooks' => [
			'https://discordapp.com/api/webhooks/373808432324542464/g_ZJQXYA0yPj7LyHebSQZA14eAbLxB7w8idL50weFHX-rSGpdI-cu-fiu0gbHl9BIa8F'
		],
		'path' => getenv('LOG_PATH')
	],
	'twig' => [
		'cache' => getenv('TWIG_CACHE')
	],
	'forum_url' => getenv('FORUM_URL'),
	'ts3_query' => 'ts3server://ts.badblock.fr?port=9987&addbookmark=BadBlock'
];