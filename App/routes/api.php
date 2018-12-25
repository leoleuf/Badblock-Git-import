<?php
// BadBlock Project 18/08/2017 Skript
// Route API

$app->group('/api', function(){
    $this->get('/login/{username}', \App\Controllers\Api\LoginApiDevController::class . ':login');
    $this->get('/send/{pas}', \App\Controllers\Api\LoginApiDevController::class . ':test');
    $this->get('/update-server-graph', \App\Controllers\Api\UpdateServerGraphApiController::class . ':update');
    $this->get('/ip', \App\Controllers\Api\IpApiController::class . ':ip');
	$this->get('/getip', \App\Controllers\IpController::class . ':getIp');
    $this->group('/cache', function() {
        $this->get('/shop', \App\Controllers\Api\ShopApiController::class . ':upCache');
        $this->get('/twitter', \App\Controllers\Api\TwitterApiController::class . ':upCache');
		$this->get('/all-posts', \App\Controllers\Api\BlogApiController::class . ':getCreateCacheAllPosts');
		$this->get('/all-staff', \App\Controllers\Api\StaffApiController::class . ':getCreateCacheAllStaff');
		$this->get('/stats-list', \App\Controllers\Api\StatsApiController::class . ':getCreateCacheStats');
        $this->get('/all-stats', \App\Controllers\Api\StatsApiController::class . ':jsonResp');
        $this->get('/vote', \App\Controllers\Api\VoteApiController::class . ':updateVoteCount');

        $this->get('/comments/{uuid}', \App\Controllers\Api\BlogApiController::class . ':getCreateCacheComment');
    });
	$this->get('/post/comments/{uuid}', \App\Controllers\Api\BlogApiController::class . ':getPostComments');
	$this->group('/minecraft', function() {
		$this->get('/players', \App\Controllers\Api\MinecraftApiController::class . ':getPlayers');
		$this->get('/teamspeak', \App\Controllers\Api\MinecraftApiController::class . ':teamspeak');
		$this->get('/sta', \App\Controllers\Api\MinecraftApiController::class . ':getStats');
	});

    $this->get('/clearts', \App\Controllers\Api\TeamspeakApiController::class . ':index');


    $this->group('/discord', function() {
        $this->get('/support/{id}/{title}/{user}/{type}', \App\DiscordHandler::class . ':sendForum');
    });


    $this->group('/stats', function() {
        $this->get('/json', \App\Controllers\Api\StatsApiController::class . ':jsonResp');
        $this->post('/search', \App\Controllers\StatsController::class . ':search');
    });


});
