<?php

namespace App;
class Redis
{
	public $prefix = 'website:';

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
		return $this->client->get($this->prefix . $key);
	}

	public function set($key, $value)
	{
		return $this->client->set($this->prefix . $key, $value);
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
		return json_decode($this->get($key), $way);
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
		return $this->set($this->prefix . $key, $value);
	}
}