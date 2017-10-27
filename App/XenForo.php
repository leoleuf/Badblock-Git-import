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
		$rep = $this->doGetRequest('action=getThreads&node_id=85&order_by=post_date');

		return $this->getParsedBody($rep->getBody());
	}

	/**
	 * @param $postId
	 * @return mixed
	 */
	public function getNewPost($postId)
	{
		$rep = $this->doGetRequest('action=getPost&value=' . $postId);

		return $this->getParsedBody($rep->getBody());
	}

	public function getUser($username)
	{
		$rep = $this->doGetRequest('action=getUser&value=' . $username);

		return $this->getParsedBody($rep->getBody());
	}

	/**
	 * @param $username
	 * @param $password
	 * @param $ip
	 * @return mixed
	 */
	public function getLogin($username, $password, $ip)
	{
<<<<<<< HEAD

        try {
=======
		try {
//			dd('action=login&username=' . $username . '&password=' . $password . '&ip_address=' . $ip);
>>>>>>> 9c790b7f138f72a71cb9526f0d485d5617d5e46e
			$rep = $this->doGetRequest('action=login&username=' . $username . '&password=' . $password . '&ip_address=' . $ip);

			return $this->getParsedBody($rep->getBody());
		} catch (\Exception $exception) {
             return false;


		}
	}
}