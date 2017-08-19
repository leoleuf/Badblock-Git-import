<?php
// BadBlock Project 18/08/2017 Skript
// Route Principales


$app->get('/', \App\Controllers\PagesController::class . ':home')->setName('home');
$app->get('/articles[/{p}]', \App\Controllers\BlogController::class . ':getAllPosts')->setName('all-posts');
$app->get('/article/{slug}/{uuid}', \App\Controllers\BlogController::class . ':getPost')->setName('single-post');

$app->get('/stats/', \App\Controllers\StatsController::class . ':home')->setName('stats-home');
$app->get('/stats/{game}/{page}', \App\Controllers\StatsController::class . ':game')->setName('stats-game');



