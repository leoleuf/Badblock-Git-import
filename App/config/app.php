<?php
return [
	'log' => getenv('LOG_ENABLED', true),
	'twig' => [
		'cache' => getenv('TWIG_CACHE', '../tmp/cache')
	]
];