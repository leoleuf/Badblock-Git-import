<?php
// BadBlock Project 18/08/2017 Skript
// Route API

$app->group('/api', function(){
    $this->get('/login/{username}', \App\Controllers\Api\LoginApiDevController::class . ':login');
});
