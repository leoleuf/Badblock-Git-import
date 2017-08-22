<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales


$app->post('/login', \App\Controllers\SessionController::class . ':login');


$app->get('/', \App\Controllers\PagesController::class . ':getHome')->setName('home');
$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');

$app->get('/profile/{pseudo}', \App\Controllers\ProfileController::class . ':getprofile')->setName('user-profile');

$app->get('/user/', \App\Controllers\StatsController::class . ':home')->setName('user-home');
$app->get('/user/{game}/', \App\Controllers\StatsController::class . ':game')->setName('user-game-np');
$app->get('/user/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('user-game');



