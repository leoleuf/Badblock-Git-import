<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 11/04/2018
 * Time: 21:13
 */

namespace App\Controllers\Api;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class VoteApiController extends \App\Controllers\Controller
{
    public function cacheTop(RequestInterface $request, ResponseInterface $response){
        //Read top from mongoDB
        $mongo = $this->container->mongo->stats_vote;
        $date =  date("Y-m");
        $data = $mongo->findOne(["date" => $date]);

        //Write in redis
        $this->redis->setJson('vote.top', $data['players']);


        //Renvoie d'un code de succès
        $this->log->success('Api\VoteApiController::cacheTop',' Success writing vote cache');

        return $response->write('Success writing vote cache')->withStatus(200);
    }


}