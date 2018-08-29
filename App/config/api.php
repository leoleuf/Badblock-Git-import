<?php
return [
	'xenforo_api' => [
		'endpoint' => getenv('XENFORO_API_ENDPOINT'),
	],
	'paypal' => [
		'username' => 'mail-facilitator_api1.matthieubessat.fr',
		'password' => 'ZKPFFYUATN5HHKP8',
		'signature' => 'AFcWxV21C7fd0v3bYYYRCpSSRl31ABSjjRb6oP.mId86wsFzMB2HaBfE',
		'prod' => false
	],
    'teamspeak' => [
        'ip' => getenv('TS_IP'),
        'port' => getenv('TS_PORT'),
        'username' => getenv('TS_USER'),
        'password' => getenv('TS_PASSWORD'),
        'query_port' => getenv('TS_QUERYPORT'),
    ]
];