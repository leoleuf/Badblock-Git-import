<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');

$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');

$app->post('/login', \App\Controllers\SessionController::class . ':login')->setName('login.execute');

$app->get('/profile/{pseudo}', \App\Controllers\UserController::class . ':getProfile')->setName('user.profile');

$app->group('/stats', function (){
	$this->get('', \App\Controllers\StatsController::class . ':home')->setName('stats.home');
	$this->get('/{game}', \App\Controllers\StatsController::class . ':game')->setName('stats-game-np');
	$this->get('/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('stats-game');
});

$app->group('/shop', function (){
	$this->get('', \App\Controllers\ShopController::class . ':getHome')->setName('shop.home');
	$this->post('/achat/{id}', \App\Controllers\ShopController::class . ':getAchat')->setName('api.shop.achat');
	$this->get('/recharge', \App\Controllers\ShopController::class . ':getRecharge')->setName('shop.recharge');
	$this->get('/recharge/next', \App\Controllers\ShopController::class . ':getNextStepRecharge')->setName('shop.recharge.nextstep');
	$this->get('/recharge/start/{amount}/{username}/{payway}', \App\Controllers\ShopController::class . ':getRechargeStart')->setName('shop.recharge.start');
	$this->get('/paypal/execute', \App\Controllers\PaypalController::class . ':getPaypalExecute')->setName('shop.paypal.execute');
	$this->get('/paypal/cancel', \App\Controllers\PaypalController::class . ':getPaypalCancel')->setName('shop.paypal.cancel');
});

$app->get('/play', \App\Controllers\PagesController::class . ':getPlay')->setName('play');
$app->get('/staff', \App\Controllers\PagesController::class . ':getStaff')->setName('staff');

$app->get('/dashboard', \App\Controllers\UserController::class . ':getDashboard')->setName('dashboard')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->get('/logout', \App\Controllers\SessionController::class . ':getLogout')->setName('logout')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));


