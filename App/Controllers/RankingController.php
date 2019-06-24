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

        $game = $minigame['game'];

        $displayName = $gameArray[$minigame['game']];

        $months = array(
            'janvier',
            'février',
            'mars',
            'avril',
            'mai',
            'juin',
            'juillet',
            'août',
            'septembre',
            'octobre',
            'novembre',
            'décembre'
        );

        $month = $months[date("n") - 1];
        $date = $month."_".date("Y");

        $data = $this->redis->getJson("stats:".$displayName.'_'.$date);
        if ($data == null)
        {
            return $this->redirect($response, '/');
        }

        $rawId = "";
        $cMax = strlen((string)count($data));

        $p = 0;
        foreach($data as $key => $value)
        {
            $p++;
            $m = "";
            if (strlen((string)$p) < $cMax)
            {
                for ($l = 0; $l < $cMax - strlen((string)$p); $l++)
                {
                    $m = $m."0";
                }
            }

            $m = $m.$p;
            $data[$key]["position"] = $m;
        }

        $datatop = array_slice($data,0,3,true);
        return $this->render($response, 'ranking.minigame', [
            'displayName' => $displayName,
            'datatop' => $datatop,
            'data' => $data
        ]);
    }

}