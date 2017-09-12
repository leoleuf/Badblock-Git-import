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
| Login Middleware
|--------------------------------------------------------------------------
*/
$app->add(new \App\Middlewares\LoginMiddleware());

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
