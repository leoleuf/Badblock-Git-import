<?php
// BadBlock Project 18/08/2017 Skript
// Route API

$app->group('/api', function(){
    $this->get('/create-cache-all-posts', \App\Controllers\BlogApiController::class . ':getCreateCacheAllPosts');
    $this->get('/create-cache-all-staff', \App\Controllers\StaffApiController::class . ':getCreateCacheAllStaff');
});
