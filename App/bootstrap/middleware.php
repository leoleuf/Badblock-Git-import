<?php
/*
|--------------------------------------------------------------------------
| Middleware requirements
|--------------------------------------------------------------------------
|
| Add Middleware to App
|
*/

/*
|--------------------------------------------------------------------------
| Whoops errors format
| Must be APP_DEBUG = true
|--------------------------------------------------------------------------
*/
$whoopsGuard = new \Zeuxisoo\Whoops\Provider\Slim\WhoopsGuard();
$whoopsGuard->setApp($app);
$whoopsGuard->setRequest($container['request']);
$whoopsGuard->setHandlers([]);
$whoopsGuard->install();

/*
 *
|--------------------------------------------------------------------------
| Login Middleware
|--------------------------------------------------------------------------
*/
$app->add(new \App\Middlewares\Auth\LoginMiddleware($container));

/*
|--------------------------------------------------------------------------
| Client ip Middleware
|--------------------------------------------------------------------------
*/
$checkProxyHeaders = true;
$trustedProxies = ['10.0.0.1', '10.0.0.2'];
$app->add(new RKA\Middleware\IpAddress($checkProxyHeaders, $trustedProxies));

/*
|--------------------------------------------------------------------------
| Minecraft Ip generator Middleware
|--------------------------------------------------------------------------
*/
$app->add(new \App\Middlewares\IpGeneratorMiddleware($container));

\Validator\ValidationLanguage::setLang('fr');