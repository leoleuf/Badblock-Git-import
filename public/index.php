<?php
require '../vendor/autoload.php';

function dd($value){
	var_dump($value);
	die('end of debug');
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

//Router
$app->get('/', \App\Controllers\PagesController::class . ':home')->setName('home');
$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':single')->setName('single-post');

$app->run();
