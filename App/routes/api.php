<?php
// BadBlock Project 18/08/2017 Skript
// Route API

$app->group('/api', function(){
    $this->group('/cache', function() {
		$this->get('/all-posts', \App\Controllers\Api\BlogApiController::class . ':getCreateCacheAllPosts');
		$this->get('/all-staff', \App\Controllers\Api\StaffApiController::class . ':getCreateCacheAllStaff');
	});
	$this->group('/minecraft', function() {
		$this->get('/players', \App\Controllers\Api\MinecraftApiController::class . ':getPlayers');
	});
});
