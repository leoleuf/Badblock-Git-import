<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PagesController extends Controller {

    public function home(RequestInterface $request, ResponseInterface $response){
//    	var_dump($this->container->minecraft->getStatus());
//    	var_dump($this->container->minecraft->getPlayers());

		$firstRow = $this->redis->getJson('website:first_row_posts');
		$secondRow = $this->redis->getJson('website:second_row_posts');
        $this->container->view->render($response, 'Pages/home.twig', [
        	'first_row' => $firstRow,
			'second_row' => $secondRow
		]);
    }

}