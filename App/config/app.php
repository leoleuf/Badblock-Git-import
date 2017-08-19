<?php
return [
	'log' => getenv('LOG_ENABLED', true),
	'twig' => [
		'cache' => getenv('TWIG_CACHE')
	],
	'forum_url' => getenv('FORUM_URL')
];