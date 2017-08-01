<?php
require '../vendor/autoload.php';

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

$app->get('/', \App\Controllers\PagesController::class . ':home');

$app->run();
