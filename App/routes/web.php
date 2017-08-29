<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales

$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');

$app->get('/staff', \App\Controllers\PagesController::class . ':pagestaff')->setName('staff');

$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');

$app->post('/login', \App\Controllers\SessionController::class . ':login')->setName('login');
$app->get('/cache/', \App\Controllers\CacheController::class . ':setcache')->setName('ssss');


$app->get('/profile/{pseudo}', \App\Controllers\ProfileController::class . ':getprofile')->setName('user-profile');

$app->get('/stats/', \App\Controllers\StatsController::class . ':home')->setName('stats-home');
$app->get('/stats/{game}/', \App\Controllers\StatsController::class . ':game')->setName('stats-game-np');
$app->get('/stats/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('stats-game');

$app->get('/play/test', \App\Controllers\PagesController::class . ':getPlay')->setName('play');
$app->get('/test/{hello}', \App\Controllers\PagesController::class . ':getTest')->setName('test');



