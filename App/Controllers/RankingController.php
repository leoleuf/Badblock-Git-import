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

    public function getMiniGame(ServerRequestInterface $request, ResponseInterface $response, $minigame)
    {
        $gameArray = [
            "tower" => "Tower"
        ];

        $displayName = $gameArray[$minigame['game']];

        $data = $this->redis->getJson("stats:".$game["game"].'_all'.":1");

        return $this->render($response, 'ranking.minigame', [
            'displayName' => $displayName
        ]);
    }

}