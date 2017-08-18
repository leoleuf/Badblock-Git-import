<?php
require '../vendor/autoload.php';

function dd($value = 'die lol'){
	die(var_dump($value));
}

//Get config from config file
$config = \App\Config::get();

$app = new \Slim\App(['settings' => $config['slim3']]);

require '../App/container.php';

//Slim Whoops middleware for error (dev)
$whoopsGuard = new \Zeuxisoo\Whoops\Provider\Slim\WhoopsGuard();
$whoopsGuard->setApp($app);
$whoopsGuard->setRequest($container['request']);
$whoopsGuard->setHandlers([]);
$whoopsGuard->install();
//End Slim Whoops middleware for error (dev)

//Includes Router
include ("../App/routes/web.php");
include ("../App/routes/api.php");

$app->run();
