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
        if ($this->container->session->exist('user')){
            if (in_array(17, $this->container->session->getProfile("user")['secondary_group_ids'])){


                return $next($request, $response);
            }else{
                return $response->withHeader('Location', '/link')->withStatus(302);
            }
        }else{
            return $response->withHeader('Location', $this->container->config['forum_url'] . '/login')->withStatus(302);
        }



	}
}