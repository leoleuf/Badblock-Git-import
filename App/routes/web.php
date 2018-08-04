<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');

$app->get('/don', \App\Controllers\PagesController::class . ':getDon')->setName('don');

$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');
$app->post('/article/{slug}/{uuid}/comment', \App\Controllers\BlogController::class . ':postComment')->setName('post-comment');


$app->get('/screenshot', \App\Controllers\ScreenshotController::class . ':getPage')->setName('get-screenshot');
$app->post('/screenshot', \App\Controllers\ScreenshotController::class . ':getPost')->setName('post-screenshot');



$app->get('/profile/{pseudo}', \App\Controllers\UserController::class . ':getProfile')->setName('user.profile');

$app->group('/stats', function (){
	$this->get('', \App\Controllers\StatsController::class . ':home')->setName('stats.home');
	$this->get('/games', \App\Controllers\StatsController::class . ':games')->setName('stats.games');
	$this->get('/podium', \App\Controllers\StatsController::class . ':podium')->setName('podium');
	$this->get('/{date}/{game}/', \App\Controllers\StatsController::class . ':game')->setName('stats-sp');
	$this->get('/{date}/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('stats-game');
});

$app->group('/shop', function (){
	$this->get('', \App\Controllers\ShopController::class . ':index')->setName('shop.home');

	$this->get('/test', \App\Controllers\ShopController::class . ':sendRabbitData');

	//Get for dev easy
	$this->get('/achat/{id}', \App\Controllers\ShopController::class . ':buy')->setName('api.shop.achat');
	$this->post('/achat/{id}', \App\Controllers\ShopController::class . ':buy')->setName('api.shop.achat');

	$this->get('/recharge', \App\Controllers\ShopController::class . ':getRecharge')->setName('shop.recharge');

	$this->get('/recharge/paypal', \App\Controllers\PaypalController::class . ':index')->setName('shop.recharge.paypal');
	$this->get('/recharge/paypal/{id}', \App\Controllers\PaypalController::class . ':execute')->setName('shop.recharge.paypal.exec');

	$this->get('/recharge/paypal-ipn', \App\Controllers\PaypalController::class . ':ipn')->setName('shop.recharge.paypal.ipn');


});

$app->group('/vote', function (){
    $this->get('', \App\Controllers\VoteController::class . ':getHome')->setName('vote.home');
    $this->post('/start', \App\Controllers\VoteController::class . ':start')->setName('vote.step');
    $this->post('/check/', \App\Controllers\VoteController::class . ':check')->setName('vote.step');
    $this->post('/end/{type}', \App\Controllers\VoteController::class . ':end')->setName('vote.step');
    $this->post('/loterie/{type}', \App\Controllers\VoteController::class . ':loterie')->setName('vote.loterie');
    $this->get('/lot', \App\Controllers\VoteController::class . ':recomp')->setName('vote.loterie');


    $this->get('/test', \App\Controllers\VoteController::class . ':top');
});

$app->get('/play', \App\Controllers\PagesController::class . ':getPlay')->setName('play');
$app->get('/staff', \App\Controllers\PagesController::class . ':getStaff')->setName('staff');

$app->get('/info', \App\Controllers\PagesController::class . ':getInfo')->setName('info');

$app->group('/dashboard', function (){
    $this->get('', \App\Controllers\UserController::class . ':getDashboard')->setName('dashboard');
    $this->get('/facture/{uid}', \App\Controllers\UserController::class . ':facture')->setName('dashboard-facture');

    $this->post('/changeserverpassword/', \App\Controllers\UserController::class . ':changepassserv')->setName('dashboard:passserv');
    $this->post('/changeserverconnect/', \App\Controllers\UserController::class . ':changeconnectmode')->setName('dashboard:changeconnectmode');
    $this->post('/teamspeak/', \App\Controllers\UserController::class . ':teamspeak')->setName('dashboard:teamspeak');


    $this->post('/custom/{method}', \App\Controllers\UserController::class . ':custom')->setName('dashboard:custom');

})->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container))->add(new App\Middlewares\Auth\RequiredLinkMiddleware($container));


$app->get('/link', \App\Controllers\LinkController::class . ':step1')->setName('link-1')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->post('/link', \App\Controllers\LinkController::class . ':poststep')->setName('link-post');



$app->get('/logout', function ($request, $response) {
    session_destroy();
    return $response->withRedirect('https://forum.badblock.fr/logout');
})->setName('logout');

