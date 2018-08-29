<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use HansOtt\PSR7Cookies\SetCookie;
use Validator\Validator;

class BlogController extends Controller
{

	public function getAllPosts(RequestInterface $request, ResponseInterface $response, $args)
	{
		/*
         * Pager fanta
         */
		//set query page
		if (isset($args['p'])) {
			$p = $args['p'];
		} else {
			$p = 1;
		}

		$array = $this->redis->getJson('all_posts');
		if (empty($array)) {
			$haveToPaginate = false;
			$nbResults = 0;
			$nbPage = 0;
			$pages = 0;
			$currentPage = 0;
			$currentPageResults = [];
		} else {
			$adapter = new \Pagerfanta\Adapter\ArrayAdapter($array);
			$pagerfanta = new \Pagerfanta\Pagerfanta($adapter);
			$pagerfanta->setMaxPerPage(9); // 10 by default
			$pagerfanta->setCurrentPage($p);
			$haveToPaginate = $pagerfanta->haveToPaginate();
			$currentPage = $pagerfanta->getCurrentPage();
			$nbResults = $pagerfanta->getNbResults();
			$currentPageResults = $pagerfanta->getCurrentPageResults();
			$nbPage = $pagerfanta->getNbPages();
			$pages = [];
			$i = 1;
			while ($i <= $nbPage) {
				array_push($pages, $i);
				$i++;
			}
		}

		$this->render($response, 'blog.all', [
			'haveToPaginate' => $haveToPaginate,
			'nbNews' => $nbResults,
			'nbPage' => $nbPage,
			'pages' => $pages,
			'currentPage' => $currentPage,
			'news' => $currentPageResults
		]);
	}

	public function getPost(RequestInterface $request, ResponseInterface $response, $args)
	{

        //search in redis cache for single cache
		if ($this->redis->exists('post:' . $args['uuid'])) {
            if (isset($_COOKIE['article_view'])){
                $cook = (array) json_decode($_COOKIE['article_view']);
                if (!in_array($args['uuid'],$cook)){
                    //Insertion d'un vue sur le compteur
                    $collection = $this->container->mongo->blog;
                    $count_data = $collection->count(["uid" => $args['uuid'],['view' => ['$all' => [$_SERVER['REMOTE_ADDR']]]]]);
                    if ($count_data == 0){
                        $data = $collection->updateOne(["uid" => $args['uuid']], ['$push' => ["view" => $_SERVER['REMOTE_ADDR']]]);
                    }
                    array_push($cook, $args['uuid']);

                    //vérification du nombre de vue dans redis
                    if ($this->redis->exists('post:' . $args['uuid'].":view")){
                        $view_count = $this->redis->get('post:' . $args['uuid'].":view");
                        $this->redis->set('post:' . $args['uuid'].":view", $view_count + 1);
                    }else{
                        $this->redis->set('post:' . $args['uuid'].":view", 1);
                    }

                    setcookie('article_view', json_encode($cook), -1, '/');
                }
            }else{
                //Insertion d'un vue sur le compteur
                $collection = $this->container->mongo->blog;
                $count_data = $collection->count(["uid" => $args['uuid'],['view' => ['$all' => [$_SERVER['REMOTE_ADDR']]]]]);
                if ($count_data == 0){
                    $data = $collection->updateOne(["uid" => $args['uuid']], ['$push' => ["view" => $_SERVER['REMOTE_ADDR']]]);
                    //vérification du nombre de vue dans redis
                    if ($this->redis->exists('post:' . $args['uuid'].":view")){
                        $view_count = $this->redis->get('post:' . $args['uuid'].":view");
                        $this->redis->set('post:' . $args['uuid'].":view", $view_count + 1);
                    }else{
                        $this->redis->set('post:' . $args['uuid'].":view", 1);
                    }
                }
                //ajout du cookie
                setcookie('article_view', json_encode($args['uuid']), -1, '/');
            }

            $post = $this->redis->getJson('post:' . $args['uuid']);

            $post["view"] = $this->redis->get('post:' . $args['uuid'].":view");

            $this->render($response, 'blog.post', [
				'post' => $post
			]);
		} else {
			return $this->container['notFoundHandler']($request, $response);
		}
	}

	public function postComment(ServerRequestInterface $request, ResponseInterface $response, $args)
	{
		//search in redis cache for single cache
		if ($this->redis->exists('post:' . $args['uuid'])) {
			$post = $this->redis->getJson('post:' . $args['uuid']);
			//validator
			$validator = new Validator($request->getParsedBody());
			$validator->required('content');
			$validator->notEmpty('content');
			$validator->length('content', 5, 355);
			if ($validator->isValid()) {
				if ($this->session->exist('user')){
					//insert comment in xenforo
					$xenforoResponse = $this->container['xenforo']->createPost($validator->getValue('content'), $this->session->get('user')['id'], $post['xenforo_thread_id']);
					//insert comment in redis cache
					array_push($post['comments'], $xenforoResponse);
					$this->container['redis']->setJson('post:' . $args['uuid'], $post);
					$this->flash->addMessage('success', 'Votre commentaire à bien été enregistré !');
					return $this->redirect($response, $this->pathFor('single-post', [
						'slug' => $args['slug'],
						'uuid' => $args['uuid']
					]));
				}
				else {
					return $response->write('You must be auth for post new comment. Code error "lefuturiste-2018-01-02-NUDSKQS" line 100 at BlogController.php')->withStatus(401);
				}
			} else {
				$this->flash->addManyMessages('errors', $validator->getErrors());
				return $this->redirect($response, $this->pathFor('single-post', [
					'slug' => $args['slug'],
					'uuid' => $args['uuid']
				]));
			}
		} else {
			return $this->container['notFoundHandler']($request, $response);
		}
	}

}