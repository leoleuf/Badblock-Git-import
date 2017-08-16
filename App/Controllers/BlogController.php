<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class BlogController extends Controller
{

	public function getAllPosts(RequestInterface $request, ResponseInterface $response, $args)
	{
		/*
         * Pager fanta
         */
		//set query page
		if (isset($args['p'])) {
			$p = $args['p'];
		} else {
			$p = 1;
		}

		$array = json_decode($this->redis->get('website:all_posts'), 1);
		if (empty($array)) {
			$haveToPaginate = false;
			$nbResults = 0;
			$nbPage = 0;
			$pages = 0;
			$currentPage = 0;
			$currentPageResults = [];
		} else {
			$adapter = new \Pagerfanta\Adapter\ArrayAdapter($array);
			$pagerfanta = new \Pagerfanta\Pagerfanta($adapter);
			$pagerfanta->setMaxPerPage(10); // 10 by default
			$pagerfanta->setCurrentPage($p);
			$haveToPaginate = $pagerfanta->haveToPaginate();
			$currentPage = $pagerfanta->getCurrentPage();
			$nbResults = $pagerfanta->getNbResults();
			$currentPageResults = $pagerfanta->getCurrentPageResults();
			$nbPage = $pagerfanta->getNbPages();
			$pages = [];
			$i = 1;
			while ($i <= $nbPage) {
				array_push($pages, $i);
				$i++;
			}
		}

		$this->render($response, 'blog/all.twig', [
			'haveToPaginate' => $haveToPaginate,
			'nbNews' => $nbResults,
			'nbPage' => $nbPage,
			'pages' => $pages,
			'currentPage' => $currentPage,
			'news' => $currentPageResults
		]);
	}

	public function getPost(RequestInterface $request, ResponseInterface $response, $args)
	{
		//search in redis cache for single cache
		$post = $this->redis->getJson('website:post:' . $args['uuid']);
		$this->render($response, 'blog/post.twig', [
			'post' => $post
		]);
	}

}