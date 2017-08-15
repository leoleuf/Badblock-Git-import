<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class BlogApiController extends Controller
{
	public function emptyCacheAllPosts(ServerRequestInterface $request, ResponseInterface $response)
	{
		//le but est d'aller chercher les infos sur l'api xenforo,
		//de les traiter, les formater
		//de les enregistrer sur redis
		//1. obtenir tout les posts
		$posts = $this->xenforo->getAllNewsPosts();
		//2. Les traiter pour en obtenir le contenu
		$i = 0;
		while ($i < $posts['count']) {
			//on fait une requete sur ce post en particulier
			$post = $this->xenforo->getNewPost($posts['threads'][$i]['first_post_id']);
			//on va chercher les infos dont on a besoin
			//chercher, obtenir et nettoyer les postinfo
			dd(stripos($post['message'], '{!!'));
		}
	}
}