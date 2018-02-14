<?php
return [
	'redis' => [
		'host' => getenv('REDIS_HOST'),
		'password' => getenv('REDIS_PASSWORD'),
		'port' => getenv('REDIS_PORT')
	],
	'mysql_rankeds' => [
		'host' => getenv('MYSQL_RANKEDS_HOST'),
		'user' => getenv('MYSQL_RANKEDS_USERNAME'),
		'password' => getenv('MYSQL_RANKEDS_PASSWORD'),
		'database' => getenv('MYSQL_RANKEDS_DATABASE'),
	],
    'mysql_box' => [
        'host' => getenv('MYSQL_BOX_HOST'),
        'user' => getenv('MYSQL_BOX_USERNAME'),
        'password' => getenv('MYSQL_BOX_PASSWORD'),
        'database' => getenv('MYSQL_BOX_DATABASE'),
    ],
    'mysql_guardian' => [
        'host' => getenv('MYSQL_GUARDIAN_HOST'),
        'user' => getenv('MYSQL_GUARDIAN_USERNAME'),
        'password' => getenv('MYSQL_GUARDIAN_PASSWORD'),
        'database' => getenv('MYSQL_GUARDIAN_DATABASE'),
    ],
    'mysql_forum' => [
        'host' => getenv('MYSQL_HOST'),
        'user' => getenv('MYSQL_USERNAME'),
        'password' => getenv('MYSQL_PASSWORD'),
        'database' => getenv('MYSQL_DATABASE'),
    ],
	'mongo_local' => [
		'host' => getenv('MONGO_HOST'),
		'port' => getenv('MONGO_PORT'),
		'user' => getenv('MONGO_USERNAME'),
		'password' => getenv('MONGO_PASSWORD'),
		'database' => getenv('MONGO_DATABASE'),
		'authSource' => getenv('MONGO_SOURCE'),
	],
    'mongo_dist' => [
        'host' => getenv('MONGO_DIST_HOST'),
        'port' => getenv('MONGO_DIST_PORT'),
        'user' => getenv('MONGO_DIST_USERNAME'),
        'password' => getenv('MONGO_DIST_PASSWORD'),
        'database' => getenv('MONGO_DIST_DATABASE'),
        'authSource' => getenv('MONGO_DIST_SOURCE'),
    ],
    'ladder' => [
        'ip' => getenv('LADDER_IP'),
        'port' => getenv('LADDER_PORT'),
    ]


];