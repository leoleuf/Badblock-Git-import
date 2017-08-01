<?php
namespace App;
class XenForo{

	public function __construct($container, $config)
	{
		$this->container = $container;
		/*
		 * config :
		 * endpoint
		 * hash
		 */
		$this->config = $config;
	}
}