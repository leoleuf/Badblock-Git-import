<?php

namespace App;
class Redis
{
	/**
	 * Redis constructor.
	 *
	 * @param \Predis\Client $client
	 */
	public function __construct(\Predis\Client $client)
	{
		$this->client = $client;
	}

	public function get($key)
	{
		return $this->client->get($key);
	}

	public function set($key, $value)
	{
		return $this->client->set($key, $value);
	}

	/**
	 * Get decoded json from redis key
	 *
	 * @param $key
	 * @param int $way
	 * @return mixed
	 */
	public function getJson($key, $way = 1)
	{
		return json_decode($this->client->get($key), $way);
	}

	/**
	 * Set key in redis with Json value
	 *
	 * @param $key
	 * @param $value
	 * @return mixed
	 */
	public function setJson($key, $value)
	{
		$value = json_encode($value);
		var_dump($key);
		dd($value);
		return $this->client->set($key, $value);
	}
}