<?php
// BadBlock Project 18/08/2017 Skript
// Route API

$app->group('/api', function(){
$this->get('/login/{username}', \App\Controllers\Api\LoginApiDevController::class . ':login');
$this->get('/send/{pas}', \App\Controllers\Api\LoginApiDevController::class . ':test');
	$this->get('/getip', \App\Controllers\IpController::class . ':getIp');
    $this->group('/cache', function() {
		$this->get('/all-posts', \App\Controllers\Api\BlogApiController::class . ':getCreateCacheAllPosts');
		$this->get('/comments/{uuid}', \App\Controllers\Api\BlogApiController::class . ':getCreateCacheComment');
		$this->get('/all-staff', \App\Controllers\Api\StaffApiController::class . ':getCreateCacheAllStaff');
		$this->get('/shop-list', \App\Controllers\Api\ShopApiController::class . ':getCreateCacheShopList');
		$this->get('/stats-list', \App\Controllers\Api\StatsApiController::class . ':getCreateCacheStats');
	});
	$this->get('/post/comments/{uuid}', \App\Controllers\Api\BlogApiController::class . ':getPostComments');
	$this->group('/minecraft', function() {
		$this->get('/playertss', \App\Controllers\Api\MinecraftApiController::class . ':getPlayers');
		$this->get('/sta', \App\Controllers\Api\MinecraftApiController::class . ':getStats');
	});

    $this->group('/discord', function() {
        $this->get('/support/{id}/{title}/{user}/{type}', \App\DiscordHandler::class . ':sendForum');
    });

});
