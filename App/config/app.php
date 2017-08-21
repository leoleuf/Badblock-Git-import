<?php
return [
	'app_name' => getenv('APP_NAME'),
	'app_debug' => getenv('APP_DEBUG'),
	'env_name' => getenv('APP_ENV_NAME'),
	'log' => [
		'level' => getenv('LOG_LEVEL'),
		'discord' => getenv('LOG_DISCORD'),
		'path' => getenv('LOG_PATH')
	],
	'twig' => [
		'cache' => getenv('TWIG_CACHE')
	],
	'forum_url' => getenv('FORUM_URL'),
	'ts3_query' => 'ts3server://ts.badblock.fr?port=9987&addbookmark=BadBlock Teamspeak'
];