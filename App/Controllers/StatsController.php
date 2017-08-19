<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 18/08/2017
 * Time: 23:04
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class StatsController extends Controller
{

    public function home(RequestInterface $request, ResponseInterface $response){
        $this->container->view->render($response, 'pages/home-stats.twig');
    }

    public function game($game,$page){
        //régulation vers fonction
        if($game == "tower2v2"){
            $this->tower2v2($page);
        }elseif ($game == "tower2v2"){
            $this->tower4v4($page);
        }elseif($game == "tower4v4"){
            $this->tower2v2($page);
        }elseif ($game == "tower2v2"){
            $this->tower2v2($page);
        }elseif ($game == "tower2v2"){
            $this->tower2v2($page);
        }
    }


    public function tower2v2($page){

    }


}