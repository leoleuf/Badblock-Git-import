<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');
$app->get('/accueil', \App\Controllers\PagesController::class . ':getHome')->setName('home-old');

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

    // Dedipass part
    $this->get('/recharge/dedipass', \App\Controllers\DedipassController::class . ':index')->setName('shop.recharge.dedipass');
    $this->post('/recharge/dedipass-process', \App\Controllers\DedipassController::class . ':process')->setName('shop.recharge.dedipass.process');

    // starppass part
    $this->get('/recharge/starpass', \App\Controllers\StarpassController::class . ':index')->setName('shop.recharge.starpass');
    $this->get('/recharge/starpass-process', \App\Controllers\StarpassController::class . ':process')->setName('shop.recharge.starpass-process');

    $this->get('/recharge/cancel', \App\Controllers\CreditController::class . ':paymentCancel')->setName('shop.recharge.paypal.cancel');
	$this->get('/recharge/success', \App\Controllers\CreditController::class . ':paymentSuccess')->setName('shop.recharge.paypal.success');

});

$app->get('/svote', \App\Controllers\VoteController::class . ':voteRedirect')->setName('vote.server-redirect');

$app->group('/vote', function (){
    $this->get('', \App\Controllers\VoteController::class . ':getHome')->setName('vote.home');
    $this->get('/redirect', \App\Controllers\VoteController::class . ':voteRedirect')->setName('vote.redirect');
    $this->post('/award', \App\Controllers\VoteController::class . ':award')->setName('vote.award');
    $this->post('/playerexists', \App\Controllers\VoteController::class . ':playerexists')->setName('vote.playerexists');
});

$app->get('/launcher-minecraft', \App\Controllers\PagesController::class . ':getPlay')->setName('play');
$app->get('/launcher-minecraft/windows', \App\Controllers\PagesController::class . ':getPlayWindows')->setName('launcher-minecraft-windows');
$app->get('/launcher-minecraft/mac', \App\Controllers\PagesController::class . ':getPlayMac')->setName('launcher-minecraft-mac');
$app->get('/launcher-minecraft/linux', \App\Controllers\PagesController::class . ':getPlayLinux')->setName('launcher-minecraft-linux');
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

$app->get('/move', \App\Controllers\MoveController::class . ':step1')->setName('move-1')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->post('/move', \App\Controllers\MoveController::class . ':poststep')->setName('move-post')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));



$app->get('/logout', function ($request, $response) {
    session_destroy();
    return $response->withRedirect('https://forum.badblock.fr/logout');
})->setName('logout');

