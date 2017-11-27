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

    public function hash(){
        date_default_timezone_set('Europe/Paris');

        $time = date('Y-m-d h:i');
        var_dump($time);
        $time =  hash("gost",$time);
        $key = md5($time);
        return $key;
    }

	public function doGetRequest($action)
	{
		return $this->guzzle->request('GET', $this->config['endpoint'] . '?' . $action . '&hash=0d10529f32c6b081d5d189fd24fafe98');
	}



	public function getParsedBody($body)
	{
		return json_decode($body, 1);
	}

	/**
	 * Get list of all posts associed to news
	 *
	 * Api request: {endpoint}?action=getThreads&node_id=113&order_by=post_date&hash=SKiaGWKSRojFBJaLjZbtSox4QWpRFfkS
	 */
	public function getAllNewsPosts()
	{
		$rep = $this->doGetRequest('action=getThreads&node_id=113&order_by=post_date');

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


    public function addGroup($username,$group)
    {

        try {
            $rep = $this->doGetRequest('action=editUser&user='. $username .'&add_groups=' . $group);

            return $this->getParsedBody($rep->getBody());
        } catch (\Exception $exception) {
            return false;


        }

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