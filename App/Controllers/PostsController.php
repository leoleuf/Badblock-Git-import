<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PostsController extends Controller {

	public function all(RequestInterface $request, ResponseInterface $response){
		$posts = [];
		$this->container->view->render($response, 'Pages/all-posts.twig', [
			'posts' => $posts
		]);
	}

	public function single(RequestInterface $request, ResponseInterface $response, $args){

	}

}