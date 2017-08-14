<?php
namespace App;

class Xenforo{

	public function __construct($container, $config)
	{
		$this->container = $container;
		$this->guzzle = $container->guzzle;
		/*
		 * config :
		 * endpoint
		 * hash
		 */
		$this->config = $config;
	}

	public function doGetRequest($action)
	{
		return $this->guzzle->request('GET', $this->config['endpoint'] . '?' . $action . '&hash=' . $this->config['hash']);
	}

	/**
	 * Get list of all posts associed to news
	 *
	 * Api request: {endpoint}?action=getThreads&node_id=85&order_by=post_date&hash=SKiaGWKSRojFBJaLjZbtSox4QWpRFfkS
	 */
	public function getAllNewsPosts()
	{
		$req = $this->doGetRequest('action=getThreads&node_id=85&order_by=post_date');
		return json_decode($req->getBody(), 1);
	}
}