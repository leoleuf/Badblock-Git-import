<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/02/2018
 * Time: 18:15
 */

namespace App\Controllers;

use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class VoteController extends Controller
{

    public function getHome(RequestInterface $request, ResponseInterface $response){

        return $this->render($response, 'vote.index');

    }

    public function step(RequestInterface $request, ResponseInterface $response,$step){

        //Traitement des votes RPG paradize
        if ($step['id'] == "1"){
            var_dump($this->rpgapi->getOut());
        }

    }

}