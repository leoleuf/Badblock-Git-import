<?php

namespace App\Middlewares\Auth;

use App\Controllers\Api\BlogApiController;
use Dflydev\FigCookies\FigRequestCookies;
use Psr\Http\Message\ServerRequestInterface;
use Slim\App;

class LoginMiddleware
{
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
		$this->redis = $container->redis;
		$this->log = $container->log;
	}

	public function __invoke(ServerRequestInterface $request, $response, $next)
	{

//        if (!$this->redis->exists('init.ok')){
//            $this->log->success('"Login Controller"',' Initialisation du cache !');
//
//
//
//        }


	    //Unsafe method
//		//si le cookie existe, on ouvre une session
//		//en revanche si le cookie exsite mais une session est déjà ouverte, on ne fait rien
//		if (isset($_COOKIE['forum_user']) && !$this->container->session->exist('user')) {
//
//			//recupérer l'id utilisateur à partir du cookie
//			$xf_user = $_COOKIE['forum_user'];
//			$pos = strpos($xf_user, ',');
//			$userid = substr($xf_user, 0, $pos);
//
//			//recipérer le profile de l'utilisateur à partir de l'api
//			$user = $this->container->xenforo->getUser($userid);
//
//			//Transformation string en array
//            $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);
//
//            //mise de l'utilisateur en session
//			$this->container->session->set('user', [
//				'id' => $user['user_id'],
//				'username' => $user['username'],
//				'email' => $user['email'],
//				'user_group_id' => $user['user_group_id'],
//				'secondary_group_ids' => $user['secondary_group_ids'],
//				'custom_title' => $user['custom_title'],
//				'is_admin' => $user['is_admin'],
//				'is_banned' => $user['is_banned'],
//				'is_staff' => $user['is_staff'],
//				'is_moderator' => $user['is_moderator']
//			]);
//		}
//
    	return $next($request, $response);

	}
}