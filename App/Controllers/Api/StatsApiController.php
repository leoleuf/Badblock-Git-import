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
        $this->log->success("StatsApiController\getCreateCacheStats",'Success writing stats cache');


        return $response->write('Success writing stats cache')->withStatus(200);


    }

    public function gdetCreateCacheStats(RequestInterface $request, ResponseInterface $response)
    {

        //Lecture du classement
        $query = "SELECT * FROM information_schema.TABLES WHERE (TABLE_SCHEMA = 'rankeds')";
        foreach ($this->mysql->fetchRowManyCursor($query) as $game)
        {
            $name = $game["TABLE_NAME"];
            $game = [];
            $query = 'SELECT * FROM '. $name .' ORDER by _points DESC';
            foreach ($this->mysql->fetchRowManyCursor($query) as $result)
            {
                array_push($game,$result);
            }
            //Save Redis
            $this->redis->setJson("stats:".$name,$game);
        }

        $this->log->info('"StatsApiController\getCreateCacheA": Success writing stats cache');


        return $response->write('Success writing stats cache')->withStatus(200);


    }




}


