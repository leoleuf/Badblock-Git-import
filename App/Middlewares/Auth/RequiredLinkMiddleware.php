<?php

namespace App\Middlewares\Auth;

use Slim\Http\Response;
use Psr\Http\Message\ServerRequestInterface;

class RequiredLinkMiddleware {

	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke(ServerRequestInterface $request, Response $response, $next)
	{


		//verify if the user is connected
		if ($this->container->session->getProfile("user")['secondary_group_ids'] == "17"){
			return $next($request, $response);
		}else{
			return $response->withHeader('Location', '/link')->withStatus(302);
		}
	}
}