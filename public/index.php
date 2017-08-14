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
//End Slim Whoops middleware for error (dev)

//Router
$app->get('/', \App\Controllers\PagesController::class . ':home')->setName('home');
$app->get('/articles', \App\Controllers\PostsController::class . ':all')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\PostsController::class . ':single')->setName('new');

$app->run();
