<?php
return [
	'redis' => [
		'host' => getenv('REDIS_HOST'),
		'password' => getenv('REDIS_PASSWORD'),
		'port' => getenv('REDIS_PORT')
	],
	'mysql' => [
		'host' => getenv('MYSQL_HOST'),
		'user' => getenv('MYSQL_USERNAME'),
		'password' => getenv('MYSQL_PASSWORD'),
		'database' => getenv('MYSQL_DATABASE'),
	],
	'mongo_db' => [
		'host' => getenv('MONGO_HOST'),
		'port' => getenv('MONGO_PORT'),
		'user' => getenv('MONGO_USERNAME'),
		'password' => getenv('MONGO_PASSWORD'),
		'database' => getenv('MONGO_DATABASE'),
		'authSource' => getenv('MONGO_SOURCE'),
	]
];