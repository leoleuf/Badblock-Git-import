<?php

namespace App\Middlewares;

/**
 * PSR7 & PSR15 Middleware for generate Server ip from location of client
 *
 * Class IpGeneratorMiddleware
 * @package App\Middlewares
 */
class VoteCountMiddleware
{

	/**
	 * Slim container object
	 */
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke($request, $response, $next)
	{
        $twig = $this->container->view->getEnvironment();
        $twig->addGlobal('voteCount', $this->container->redis->get('vote.nb'));

        //return next
		return $next($request, $response);
	}
}