<?php

namespace App\Middlewares;

class LoginMiddleware
{

	public function __invoke($request, $response, $next)
	{
		//si le cookie existe, on ouvre une session
		//en revanche si le cookie exsite mais une session est déjà ouverte, on ne fait rien

		//dissalow
		/*$cookies = RequestCookies::createFromRequest($request);

		if ($cookies->has('badblockauth_user') && !$this->container->session->exist('user')) {

		}*/

		return $next($request, $response);

	}
}