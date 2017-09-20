<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');

$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');

$app->post('/login', \App\Controllers\SessionController::class . ':login')->setName('login');

$app->get('/profile/{pseudo}', \App\Controllers\UserController::class . ':getProfile')->setName('user.profile');

$app->group('/stats', function (){
	$this->get('', \App\Controllers\StatsController::class . ':home')->setName('stats.home');
	$this->get('/{game}', \App\Controllers\StatsController::class . ':game')->setName('stats-game-np');
	$this->get('/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('stats-game');
});

$app->group('/shop', function (){
	$this->get('', \App\Controllers\ShopController::class . ':getHome')->setName('shop.home');
	$this->get('/recharge', \App\Controllers\ShopController::class . ':getRecharge')->setName('shop.recharge');
});

$app->get('/play', \App\Controllers\PagesController::class . ':getPlay')->setName('play');
$app->get('/staff', \App\Controllers\PagesController::class . ':getStaff')->setName('staff');

$app->get('/dashboard', \App\Controllers\UserController::class . ':getDashboard')->setName('dashboard');


