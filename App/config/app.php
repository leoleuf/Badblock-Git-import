<?php
return [
	'log' => getenv('LOG_ENABLED', true),
	'twig' => [
		'cache' => getenv('TWIG_CACHE')
	],
	'forum_url' => getenv('FORUM_URL'),
	'ts3_query' => 'ts3server://ts.badblock.fr?port=9987&addbookmark=BadBlock Teamspeak'
];