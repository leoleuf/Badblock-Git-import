<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PagesController extends Controller {

    public function home(RequestInterface $request, ResponseInterface $response){
    	var_dump($this->container->minecraft->getStatus());
    	var_dump($this->container->minecraft->getPlayers());
        $this->container->view->render($response, 'pages/home.twig');
    }

}