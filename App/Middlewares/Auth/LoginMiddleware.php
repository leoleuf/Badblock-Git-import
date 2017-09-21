<?php

namespace App\Middlewares\Auth;

use Dflydev\FigCookies\Cookie;
use Dflydev\FigCookies\FigRequestCookies;
use HansOtt\PSR7Cookies\RequestCookies;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Http\Cookies;

class LoginMiddleware
{
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke(ServerRequestInterface $request, $response, $next)
	{
		$_COOKIE['forum_user'] = '65%2Cdc4fae9d0e0679e1ed23ade66fcaa70f5eb737f4';
		//si le cookie existe, on ouvre une session
		//en revanche si le cookie exsite mais une session est déjà ouverte, on ne fait rien
		$cookies = RequestCookies::createFromRequest($request);

		if ($cookies->has('forum_user') && !$this->container->session->exist('user')) {

			//recupérer l'id utilisateur à partir du cookie
			$pos = strpos('%', FigRequestCookies::get($request, 'forum_user'));
			$userid = substr(FigRequestCookies::get($request, 'forum_user'), 0, $pos);

			//recipérer le profile de l'utilisateur à partir de l'api
			$user = $this->container->xenforo->getUser($userid);

			//mise de l'utilisateur en session
			$this->session->set('user', [
				'id' => $user['user_id'],
				'username' => $user['username'],
				'email' => $user['email'],
				'user_group_id' => $user['user_group_id'],
				'secondary_group_ids' => $user['secondary_group_ids'],
				'custom_title' => $user['custom_title'],
				'is_admin' => $user['is_admin'],
				'is_banned' => $user['is_banned'],
				'is_staff' => $user['is_staff'],
				'is_moderator' => $user['is_moderator']
			]);

			return $next($request, $response);
		}

		return $next($request, $response);

	}
}