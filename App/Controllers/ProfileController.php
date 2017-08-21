<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/08/2017
 * Time: 22:12
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class ProfileController extends Controller
{

    public function getprofile(RequestInterface $request, ResponseInterface $response,$pseudo){


        $collection = $this->mongo->test->test;

        $cursor = $collection->findOne(['realName' => $pseudo['pseudo']]);


        if (empty($cursor)){
            return $response->withStatus(404);
        }


        var_dump($cursor->game->stats);


    }



}