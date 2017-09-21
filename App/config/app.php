<?php
return [
	'app_name' => getenv('APP_NAME'),
	'app_debug' => (getenv('APP_DEBUG') ? true : false),
	'env_name' => getenv('APP_ENV_NAME'),
	'log' => [
		'level' => getenv('LOG_LEVEL'),
		'discord' => getenv('LOG_DISCORD'),
		'discord_webhooks' => [
			'https://discordapp.com/api/webhooks/349584143027535872/v3fRHkJVV-1nz7glxjv9HoIAU-uQNHxtRF7fOLA0d3_v6e_vKvuOnmykIZSK1JI3nqrI'
		],
		'path' => getenv('LOG_PATH')
	],
	'twig' => [
		'cache' => getenv('TWIG_CACHE')
	],
	'forum_url' => getenv('FORUM_URL'),
	'ts3_query' => 'ts3server://ts.badblock.fr?port=9987&addbookmark=BadBlock Teamspeak'
];