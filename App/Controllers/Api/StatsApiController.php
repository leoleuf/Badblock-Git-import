<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 18/08/2017
 * Time: 23:04
 */

namespace App\Controllers\Api;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class StatsApiController extends \App\Controllers\Controller
{


    public function getCreateCacheStats(RequestInterface $request, ResponseInterface $response)
    {

        // Noms des jeux & affichage
        $list_game = array(
            'towerrun' => 'TowerRun',
            'tower' => 'Tower',
            'rush' => 'Rush',
            'survivalgames' => 'SurvivalGames',
            'uhcspeed' => 'UHCSpeed',
            'capturethesheep' => 'CaptureTheSheep',
            'buildcontest' => 'BuildContest',
            'spaceballs' => 'SpaceBalls',
            'pearlswar' => 'PearlsWar'
        );

        //Lecture du classement ALL
        foreach ($list_game as $game)
        {
            $name = $game;
            $game = [];
            $query = 'SELECT * FROM ' . $name. '_all' . ' ORDER by _points DESC';
            foreach ($this->mysql->fetchRowManyCursor($query) as $result)
            {
                var_dump($result);
                array_push($game,$result);
            }
            //Save Redis
            $this->redis->setJson("stats:".$name,$game);
        }

        return $response->write('Success writing stats cache')->withStatus(200);

        $this->log->info('"StatsApiController\getCreateCacheA": Success writing stats cache');

    }




}


