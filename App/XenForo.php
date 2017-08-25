<?php

namespace App;

class XenForo
{

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

	public function getParsedBody($body)
	{
		return json_decode($body, 1);
	}

	/**
	 * Get list of all posts associed to news
	 *
	 * Api request: {endpoint}?action=getThreads&node_id=85&order_by=post_date&hash=SKiaGWKSRojFBJaLjZbtSox4QWpRFfkS
	 */
	public function getAllNewsPosts()
	{
		return $this->getParsedBody($this->doGetRequest('action=getThreads&node_id=85&order_by=post_date'));
	}

	/**
	 * @param $postId
	 * @return mixed
	 */
	public function getNewPost($postId)
	{
		return $this->getParsedBody($this->doGetRequest('action=getPost&value=' . $postId));
	}

	/**
	 * @param $username
	 * @param $password
	 * @param $ip
	 * @return mixed
	 */
	public function getLogin($username, $password, $ip)
	{
		try {
			$rep = $this->doGetRequest('action=login&username=' . $username . '&password=' . $password . '&ip_address=' . $ip);

			return $this->getParsedBody($rep->getBody());
		} catch (\Exception $exception) {

			return false;
		}
	}
}