<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PageController::class . ':home')->setName('home');



$app->get('/logout', function ($request, $response) {
    setcookie("forum_session", "", time()-3600, '/',".badblock.fr");
    setcookie("forum_logged_in", "", time()-3600, '/',".badblock.fr");
    session_destroy();
    return $response->withRedirect('https://badblock.fr');
})->setName('logout');

