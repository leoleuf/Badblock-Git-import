<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class BlogApiController extends Controller
{
	/**
	 * Generate slugify from text (title)
	 *
	 * @param $text
	 * @return mixed|string
	 */
	public function slugify($text)
	{
		// replace non letter or digits by -
		$text = preg_replace('~[^\pL\d]+~u', '-', $text);

		// transliterate
		$text = iconv('utf-8', 'us-ascii//TRANSLIT', $text);

		// remove unwanted characters
		$text = preg_replace('~[^-\w]+~', '', $text);

		// trim
		$text = trim($text, '-');

		// remove duplicate -
		$text = preg_replace('~-+~', '-', $text);

		// lowercase
		$text = strtolower($text);

		if (empty($text)) {
			return 'n-a';
		}

		return $text;
	}

	/**
	 * Créer un cache redis pour afficher tous les posts
	 * Les infos enregistré sur les posts sont minimalistes
	 *
	 * @param ServerRequestInterface $request
	 * @param ResponseInterface $response
	 */
	public function createCacheAllPosts(ServerRequestInterface $request, ResponseInterface $response)
	{
		//Ici, on va créer un cache global pour tous les posts
		//le but est d'aller chercher les infos sur l'api xenforo,
		//de les traiter, les formater
		//de les enregistrer sur redis
		//1. obtenir tout les posts
		$posts = $this->xenforo->getAllNewsPosts();
		//2. Les traiter pour en obtenir le contenu
		$newPosts = [];
		$pinedRawPosts = [];
		$i = 0;

		//loggin
		$this->log->info('"createCacheAllPosts": Creating cache for ' . $posts['count'] . ' items...');

		while ($i < $posts['count']) {
			//on fait une requete sur ce post en particulier
			$post = $this->xenforo->getNewPost($posts['threads'][$i]['first_post_id']);
			//on va chercher les infos dont on a besoin
			//chercher, obtenir et nettoyer les postinfo

			//Chercher le thread id
			$threadId = $post['thread_id'];

			//Chercher le post id
			$postId = $post['post_id'];

			//Chercher le post date
			$postDate = $post['post_date'];
			//le transformer en date time correct
			$postDate = date("d-m-Y H:i:s", $postDate);

			//Generer un uuid
			$hash = hash('sha256', $postId . $threadId . $postDate);
			$uuid = substr(
				$hash,
				strlen($hash) - 8
			);

			//Chercher le title
			$title = $post['title'];

			//Chercher l'autheur
			$author = $post['username'];
			$authorId = $post['user_id'];

			//Chercher le post info
			//si on voir {!! dans le contenu
			$postInfoPos = strpos($post['message'], '[CODE]{!!');
			if ($postInfoPos !== false) {
				//on cherche la position de {!!
				$postInfoRaw = substr(
					$post['message'],
					$postInfoPos
				);
				//on enlève pour laisser le json
				$postInfoJson = str_replace('[/CODE]', '',
					str_replace('!!}', '',
						str_replace('[CODE]{!!', '', $postInfoRaw)
					)
				);
				//transformer en json
				$postInfo = json_decode($postInfoJson, 1);

				//on verifie le json si il conporte bien le patern par default
				if (isset($postInfo['thumb_url'], $postInfo['summary'], $postInfo['pined'], $postInfo['comments'])) {
					//valide
					//logging
					$this->log->success('"createCacheAllPosts": The item with id: ' . $threadId . ' has been valid parameters.');
				} else {
					//logging
					$this->log->error('"createCacheAllPosts": The item with id: ' . $threadId . ' has no valid parameters.');

					//stop the script
					return $response->write('The item with id: ' . $threadId . ' has no valid parameters.')->withStatus(400);
				}
			} else {
				$postInfo = [
					'thumb_url' => 'https://s4.postimg.org/emgrc4uel/image.jpg',
					'summary' => 'Une description est requise pour cette article',
					'pined' => false,
					'comments' => true
				];

				//logging
				$this->log->info('"createCacheAllPosts": The item with id: ' . $threadId . ' has no parameters.');
			}

			//definir le contenu
			//on redefini le post info sur message html
			$postInfoPos = strpos($post['message_html'], '<pre style="margin: 1em auto" title="Code">{!!');
			$postInfoRaw = substr(
				$post['message_html'],
				$postInfoPos
			);
			//on prend la longeur à partir du [CODE]{!!
			$postInfoLen = strlen($postInfoRaw);
			//on deduit la longueur
			$content = substr($post['message_html'], 0, -$postInfoLen);
			$this->log->info($content);

			$newPosts[$i] = [
				'uuid' => $uuid,
				'title' => $title,
				'slug' => $this->slugify($title),
				'author' => [
					'id' => $authorId,
					'name' => $author,
				],
				'thumb_url' => $postInfo['thumb_url'],
				'summary' => $postInfo['summary'],
				'created_at' => $postDate,
				'pined' => $postInfo['pined']
			];
			$singleNewPost = [
				'uuid' => $uuid,
				'title' => $title,
				'slug' => $this->slugify($title),
				'author' => [
					'id' => $authorId,
					'name' => $author,
				],
				'thumb_url' => $postInfo['thumb_url'],
				'summary' => $postInfo['summary'],
				'created_at' => $postDate,
				'pined' => $postInfo['pined'],
				'comment' => $postInfo['comments'],
				'content' => $content
			];

			//enregistrer sur redis un article en particul	ier
			$this->redis->setJson('website:post:' . $uuid, $singleNewPost);

			//si le poste est épinglé on enregistre dans un tableau
			array_push($pinedRawPosts, $newPosts[$i]);
			$i++;
		}

		//4. On enregistre le tout
		//enregistrer sur redis tout les articles
		$this->redis->setJson('website:all_posts', $newPosts);

		//enregistrer le cache pour la première ligne et la seconde ligne
		$firstRowPosts = [
			$newPosts[0],
			$newPosts[1]
		];
		$secondRowPosts = [
			$newPosts[2],
			$newPosts[3]
		];
		$this->redis->set('website:first_row_posts', $firstRowPosts);
		$this->redis->set('website:second_row_posts', $secondRowPosts);

		//enregister le cache pour les articles épinglés
		$this->redis->setJson('website:pined_posts', $pinedRawPosts);

		//return success
		return $response->write('Success writing cache')->withStatus(200);
	}
}