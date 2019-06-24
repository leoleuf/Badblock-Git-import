<?php

namespace App\Controllers;

use App\Controllers\YoutubeApi as YoutubeAPI;
use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class RankingController extends Controller
{

    public function getHome(ServerRequestInterface $request, ResponseInterface $response)
    {
        return 'todo';
    }

    public function updateCache

    public function getMiniGame(ServerRequestInterface $request, ResponseInterface $response, $minigame)
    {
        $gameArray = [
            "tower" => "Tower"
        ];

        $displayName = $gameArray[$minigame['game']];

        return $this->render($response, 'ranking.minigame', [
            'displayName' => $displayName
        ]);
    }

}