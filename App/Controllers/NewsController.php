<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class NewsController extends Controller {

	public function all(RequestInterface $request, ResponseInterface $response){
		$this->container->view->render($response, 'Pages/all-news.twig');
	}

	public function single(RequestInterface $request, ResponseInterface $response, $args){

	}

}