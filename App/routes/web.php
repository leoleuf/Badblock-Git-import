<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');

$app->get('/accueil', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/', 301);
});


$app->get('/mentions-legales', \App\Controllers\PagesController::class . ':getMT');
$app->get('/cgu', \App\Controllers\PagesController::class . ':getCgu');
$app->get('/cgv', \App\Controllers\PagesController::class . ':getCgv');
$app->get('/charte-de-confidentialite', \App\Controllers\PagesController::class . ':getCdc');


$app->get('/accueil/', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/', 301);
});

$app->get('/bbnews', \App\Controllers\PagesController::class . ':bbnews');
$app->get('/bbnew/{uuid}', \App\Controllers\RedirectController::class . ':link');


$app->get('/jouer', \App\Controllers\PagesController::class . ':getPlayAdwords')->setName('play-adwords');
$app->get('/youtube', \App\Controllers\PagesController::class . ':getYoutube')->setName('youtube-info');

$app->get('/youtubeur', \App\Controllers\PagesController::class . ':getYoutubeurs')->setName('youtubeurs');

$app->get('/article[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');
$app->post('/article/{slug}/{uuid}/comment', \App\Controllers\BlogController::class . ':postComment')->setName('post-comment');

$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts-old');
$app->get('/article/lire/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post-old');
$app->post('/article/lire/{slug}/{uuid}/comment', \App\Controllers\BlogController::class . ':postComment')->setName('post-comment-old');


$app->get('/screenshot', \App\Controllers\ScreenshotController::class . ':getPage')->setName('get-screenshot');
$app->post('/screenshot', \App\Controllers\ScreenshotController::class . ':getPost')->setName('post-screenshot');



$app->get('/profile/{pseudo}', \App\Controllers\UserController::class . ':getProfile')->setName('user.profile');

$app->group('/stats', function (){
	$this->get('', \App\Controllers\StatsController::class . ':home')->setName('stats.home');
	$this->get('/games', \App\Controllers\StatsController::class . ':games')->setName('stats.games');
	$this->get('/podium', \App\Controllers\StatsController::class . ':podium')->setName('podium');
	$this->get('/{date}/{game}', \App\Controllers\StatsController::class . ':game')->setName('stats-sp');
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
    $this->post('/recharge/dedipass-process-ig', \App\Controllers\DedipassController::class . ':processig')->setName('shop.recharge.dedipass.processig');

    // starppass part
    $this->get('/recharge/starpass', \App\Controllers\StarpassController::class . ':index')->setName('shop.recharge.starpass');
    $this->post('/recharge/starpass/{id}/process', \App\Controllers\StarpassController::class . ':process')->setName('shop.recharge.starpass-process');
    $this->get('/recharge/starpass/{id}', \App\Controllers\StarpassController::class . ':showDocument')->setName('shop.recharge.starpass-showdocument');

    $this->get('/recharge/cancel', \App\Controllers\CreditController::class . ':paymentCancel')->setName('shop.recharge.paypal.cancel');
	$this->get('/recharge/success', \App\Controllers\CreditController::class . ':paymentSuccess')->setName('shop.recharge.paypal.success');

});

$app->get('/badblock', \App\Controllers\VoteController::class . ':badblock')->setName('vote.server-redirect');
$app->get('/svote', \App\Controllers\VoteController::class . ':voteRedirectCustom')->setName('vote.server-redirect');


$app->group('/vote', function (){
    $this->get('', \App\Controllers\VoteController::class . ':getHome')->setName('vote.home');
    $this->get('/', \App\Controllers\VoteController::class . ':getHome')->setName('vote.home2');
    $this->get('/redirect', \App\Controllers\VoteController::class . ':voteRedirect')->setName('vote.redirect');
    $this->post('/award', \App\Controllers\VoteController::class . ':award')->setName('vote.award');
    $this->post('/playerexists', \App\Controllers\VoteController::class . ':playerexists')->setName('vote.playerexists');
});

$app->group('/jeux', function (){

    $this->get('/', function($request, $response)
    {
        return $response->withRedirect('/stats', 301);
    });

    $this->get('', function($request, $response)
    {
        return $response->withRedirect('/stats', 301);
    });

    $this->get('/skyblock', \App\Controllers\DiscoverController::class . ':skyblock')->setName('discover.skyblock');
    $this->get('/freebuild', \App\Controllers\DiscoverController::class . ':freebuild')->setName('discover.freebuild');

});

$app->get('/launcher-minecraft', \App\Controllers\PagesController::class . ':getPlay')->setName('play');

// Avoid duplicate content
$app->get('/store', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/shop', 301);
});

$app->get('/nous-rejoindre', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/launcher-minecraft', 301);
});

$app->get('/play', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/jouer', 301);
});

$app->get('/nous-rejoindre/', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/launcher-minecraft', 301);
});

// temp
$app->get('/launcher', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/launcher-minecraft', 301);
});

$app->get('/launcher-minecraft/mac', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/launcher-minecraft', 301);
});

$app->get('/launcher-minecraft/linux', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/launcher-minecraft', 301);
});

$app->get('/store/', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/shop', 301);
});

// accessible index page

$app->get('/index', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/', 301);
});

$app->get('/index.php', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/', 301);
});

$app->get('/index.html', function($request, $response)
{
    return $response->withRedirect('https://badblock.fr/', 301);
});

$app->get('/staff', \App\Controllers\PagesController::class . ':getStaff')->setName('staff');

$app->get('/info', \App\Controllers\PagesController::class . ':getInfo')->setName('info');

$app->group('/dashboard', function (){
    $this->get('', \App\Controllers\UserController::class . ':getDashboard')->setName('dashboard');
    $this->get('/refer', \App\Controllers\UserController::class . ':refer')->setName('dashboard:refer');
    $this->post('/refer/submit', \App\Controllers\UserController::class . ':referSubmit')->setName('dashboard:referSubmit');
    $this->post('/refer/manage', \App\Controllers\UserController::class . ':referManage')->setName('dashboard:referManage');
    $this->get('/facture/{uid}', \App\Controllers\UserController::class . ':facture')->setName('dashboard-facture');

    $this->post('/changeserverpassword', \App\Controllers\UserController::class . ':changepassserv')->setName('dashboard:passserv');
    $this->post('/changeserverconnect', \App\Controllers\UserController::class . ':changeconnectmode')->setName('dashboard:changeconnectmode');
    $this->post('/teamspeak', \App\Controllers\UserController::class . ':teamspeak')->setName('dashboard:teamspeak');
    $this->post('/custom/{method}', \App\Controllers\UserController::class . ':custom')->setName('dashboard:custom');

    $this->get('/reward/namemc', \App\Controllers\UserController::class . ':rewardNameMC')->setName('dashboard:rewardNameMC');
    $this->get('/reward/twitter-1', \App\Controllers\UserController::class . ':rewardTwitter1')->setName('dashboard:rewardTwitter1');
    $this->get('/reward/twitter-2', \App\Controllers\UserController::class . ':rewardTwitter2')->setName('dashboard:rewardTwitter2');

})->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container))->add(new App\Middlewares\Auth\RequiredLinkMiddleware($container));


$app->get('/link', \App\Controllers\LinkController::class . ':step1')->setName('link-1')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->get('/link/{step}', \App\Controllers\LinkController::class . ':poststep')->setName('link-1')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->post('/link', \App\Controllers\LinkController::class . ':poststep')->setName('link-post');

$app->get('/move', \App\Controllers\MoveController::class . ':step1')->setName('move-1')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->get('/move/1/{uuid}', \App\Controllers\MoveController::class . ':process_step2')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->get('/move/2/{uuid}', \App\Controllers\MoveController::class . ':process_step4')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));
$app->post('/move', \App\Controllers\MoveController::class . ':poststep')->setName('move-post')->add(new App\Middlewares\Auth\RequiredAuthMiddleware($container));



$app->get('/logout', function ($request, $response) {
    setcookie("forum_session", "", time()-3600, '/',".badblock.fr");
    setcookie("forum_logged_in", "", time()-3600, '/',".badblock.fr");
    session_destroy();
    return $response->withRedirect('https://badblock.fr');
})->setName('logout');

