<?php
return [
	'slim3' => [
		'displayErrorDetails' => getenv('SLIM_DISPLAY_ERRORS_DETAILS', false)
	],
	'log' => getenv('LOG_ENABLED', true),
	'twig' => [
		'cache' => getenv('TWIG_CACHE', '../tmp/cache')
	]
];