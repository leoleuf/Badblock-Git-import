<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class BlogController extends Controller {

	public function getAllPosts(RequestInterface $request, ResponseInterface $response, $args){
		/*
         * Pager fanta
         */
		//set query page
		if (isset($args['p'])){
			$p = $args['p'];
		}else{
			$p = 1;
		}

		$array = $this->xenforo->getAllNewsPosts()['threads'];
		if(empty($array)){
			$haveToPaginate = false;
			$nbResults = 0;
			$nbPage = 0;
			$pages = 0;
			$currentPage = 0;
			$currentPageResults = [];
		}else{
			$adapter = new \Pagerfanta\Adapter\ArrayAdapter($array);
			$pagerfanta = new \Pagerfanta\Pagerfanta($adapter);
			$pagerfanta->setMaxPerPage(1); // 10 by default
			$pagerfanta->setCurrentPage($p);
			$haveToPaginate = $pagerfanta->haveToPaginate();
			$currentPage = $pagerfanta->getCurrentPage();
			$nbResults = $pagerfanta->getNbResults();
			$currentPageResults = $pagerfanta->getCurrentPageResults();
			$nbPage = $pagerfanta->getNbPages();
			$pages = [];
			$i = 1;
			while($i <= $nbPage){
				array_push($pages, $i);
				$i++;
			}
		}
		/**
		 *
		 * 'thread_id' => int 107
		'node_id' => int 85
		'title' => string 'Je suis un article' (length=18)
		'reply_count' => int 0
		'view_count' => int 0
		'user_id' => int 65
		'username' => string 'le_futuriste' (length=12)
		'post_date' => int 1502790864
		'sticky' => int 0
		'discussion_state' => string 'visible' (length=7)
		'discussion_open' => int 1
		'discussion_type' => string '' (length=0)
		'first_post_id' => int 744
		'first_post_likes' => int 0
		'last_post_date' => int 1502790864
		'last_post_id' => int 744
		'last_post_user_id' => int 65
		'last_post_username' => string 'le_futuriste' (length=12)
		'prefix_id' => int 0
		'tags' => string 'a:0:{}' (length=6)
		'scheduled_state' => string '' (length=0)
		'absolute_url' => string 'https://dev-forum.badblock.fr/threads/107/' (length=42)
		 */
		$this->render($response, 'blog/all.twig', [
			'haveToPaginate' => $haveToPaginate,
			'nbNews' => $nbResults,
			'nbPage' => $nbPage,
			'pages' => $pages,
			'currentPage' => $currentPage,
			'news' => $currentPageResults
		]);
	}

	public function single(RequestInterface $request, ResponseInterface $response, $args){

	}

}