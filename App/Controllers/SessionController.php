<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 19/08/2017
 * Time: 22:28
 */

namespace App\Controllers;

use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;


class SessionController extends Controller
{
	public function login(ServerRequestInterface $request, ResponseInterface $response)
	{
		//si les variables sont passés
		if (isset($_POST['username']) & isset($_POST['password'])) {
			//si les variables sont non vide
			if (!empty($_POST['username']) & !empty($_POST['password'])) {
				//obtenir le cookie à partir de de l'api xenforo avec un username et un password
				$rep = $this->xenforo->getLogin($_POST['username'], $_POST['password'], $_SERVER['REMOTE_ADDR']);

				//la réponse est false si les mots de passe ou le username est correct
				if ($rep !== false) {
					//user
					$user = $this->xenforo->getUser($_POST['username']);

					//ajout du cookie
					$cookie = new SetCookie($rep['cookie_name'], $rep['cookie_id'], $rep['cookie_expiration'], $rep['cookie_path'], $rep['cookie_domain'], $rep['cookie_secure']);
					$response = $cookie->addToResponse($response);

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

					//redirect to home
					return $this->redirect($response, $this->pathFor('dashboard'));
				} else {
					//Erreur: Username ou mdp invalides
					$this->flash->addMessage('login_error', "Nom d'utilisateur ou mot de passe incorrect");

					//redirect to last page
					return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#login-modal');
				}
			}
		} else {
			return $response->write('Bad request')->withStatus(400);
		}
	}


	public function getLogout(ServerRequestInterface $request, ResponseInterface $response)
	{
		$this->session->destroy();
		return $response->withHeader('Location', $this->container->config['forum_url'] . '/logout')->withStatus(302);
	}

}