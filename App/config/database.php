<?php
return [
	'redis' => [
		'host' => getenv('REDIS_HOST'),
		'password' => getenv('REDIS_PASSWORD'),
		'port' => getenv('REDIS_PORT')
	]
];