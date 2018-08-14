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
	$this->post('/achat/{id}', \App\Controllers\ShopController::class . ':buy')->setName('api.shop.achat');

	$this->get('/recharge', \App\Controllers\CreditController::class . ':stepRecharge')->setName('shop.recharge');
	$this->post('/recharge', \App\Controllers\CreditController::class . ':postRecharge')->setName('shop.recharge.post');
	$this->get('/recharge/step-{id}', \App\Controllers\CreditController::class . ':stepRecharge')->setName('shop.recharge.step');

	//Paypal part
	$this->get('/recharge/paypal/{id}', \App\Controllers\PaypalController::class . ':startPaiement')->setName('shop.recharge.paypal.start');
	$this->get('/recharge/paypal-process', \App\Controllers\PaypalController::class . ':process')->setName('shop.recharge.paypal.process');

    $this->get('/recharge/dedipass', \App\Controllers\DedipassController::class . ':index')->setName('shop.recharge.dedipass');
    $this->get('/recharge/dedipass-process', \App\Controllers\DedipassController::class . ':process')->setName('shop.recharge.dedipass.process');



    $this->get('/recharge/cancel', \App\Controllers\CreditController::class . ':paymentCancel')->setName('shop.recharge.paypal.cancel');
	$this->get('/recharge/sucess', \App\Controllers\CreditController::class . ':paymentSuccess')->setName('shop.recharge.paypal.sucess');

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

$app->get('/launcher-minecraft', \App\Controllers\PagesController::class . ':getPlay')->setName('launcher-minecraft');
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

