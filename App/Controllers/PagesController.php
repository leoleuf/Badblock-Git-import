<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PagesController extends Controller {

    public function getHome(RequestInterface $request, ResponseInterface $response){

//    	var_dump($this->container->minecraft->getStatus());
//    	var_dump($this->container->minecraft->getPlayers());

		$firstRow = $this->redis->getJson('website:first_row_posts');
		$secondRow = $this->redis->getJson('website:second_row_posts');
		$postsCount = $this->redis->get('website:posts_count');

		$this->render($response, 'pages.home', [
        	'first_row' => $firstRow,
			'second_row' => $secondRow,
			'posts_count' => $postsCount
		]);
    }

}