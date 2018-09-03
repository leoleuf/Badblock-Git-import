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
        'port' => getenv('MYSQL_RANKEDS_PORT'),
        'password' => getenv('MYSQL_RANKEDS_PASSWORD'),
		'database' => getenv('MYSQL_RANKEDS_DATABASE'),
	],
    'mysql_casier' => [
        'host' => getenv('MYSQL_CASIER_HOST'),
        'user' => getenv('MYSQL_CASIER_USERNAME'),
        'port' => getenv('MYSQL_CASIER_PORT'),
        'password' => getenv('MYSQL_CASIER_PASSWORD'),
        'database' => getenv('MYSQL_CASIER_DATABASE'),
    ],
    'mysql_guardian' => [
        'host' => getenv('MYSQL_GUARDIAN_HOST'),
        'user' => getenv('MYSQL_GUARDIAN_USERNAME'),
        'port' => getenv('MYSQL_GUARDIAN_PORT'),
        'password' => getenv('MYSQL_GUARDIAN_PASSWORD'),
        'database' => getenv('MYSQL_GUARDIAN_DATABASE'),
    ],
    'mysql_forum' => [
        'host' => getenv('MYSQL_HOST'),
        'user' => getenv('MYSQL_USERNAME'),
        'port' => getenv('MYSQL_PORT'),
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
    'mongo_ultra' => [
        'host' => getenv('MONGO_ULTRA_HOST'),
        'port' => getenv('MONGO_ULTRA_PORT'),
        'user' => getenv('MONGO_ULTRA_USERNAME'),
        'password' => getenv('MONGO_ULTRA_PASSWORD'),
        'database' => getenv('MONGO_ULTRA_DATABASE'),
        'authSource' => getenv('MONGO_ULTRA_SOURCE'),
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
    ],
    'rabbit' => [
        'ip' => getenv('RABBIT_IP'),
        'port' => getenv('RABBIT_PORT'),
        'username' => getenv('RABBIT_USERNAME'),
        'password' => getenv('RABBIT_PASSWORD'),
        'virtualhost' => getenv('RABBIT_VIRTUALHOST'),
    ],
    'ftp' => [
        'ip' => getenv('FTP_IP'),
        'user' => getenv('FTP_USER'),
        'password' => getenv('FTP_PASSWORD')
    ]


];