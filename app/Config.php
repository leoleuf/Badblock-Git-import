<?php

namespace App;

class Config
{
	public static function get()
	{
		//get env
		$env = new \App\Environment();

		//get configs
		$commonConfig = new \Noodlehaus\Config('../app/config/common');
		$envConfig = new \Noodlehaus\Config('../app/config/' .
			$env->getEnvironment());

		//merge config
		$config = array_merge($commonConfig->all(), $envConfig->all());

		//finish and return
		return $config;
	}
}