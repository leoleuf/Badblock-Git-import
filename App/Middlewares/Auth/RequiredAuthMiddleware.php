<?php

namespace App\Middlewares\Auth;

use Slim\Http\Response;
use Psr\Http\Message\ServerRequestInterface;

class RequiredAuthMiddleware {

	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke(ServerRequestInterface $request, Response $response, $next)
	{
		//verify if the user is connected
		if ($this->container->session->exist('user')){
			return $next($request, $response);
		}else{
			return $response->withHeader('Location', $this->container->config['forum_url'] . '/login')->withStatus(302);
		}
	}
}