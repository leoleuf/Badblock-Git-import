<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 19/08/2017
 * Time: 22:28
 */

namespace App\Controllers;

use App\XenForo;
use Dflydev\FigCookies\Cookie;
use function FastRoute\TestFixtures\empty_options_cached;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class SessionController extends Controller
{

	public function login(RequestInterface $request, ResponseInterface $response)
	{
		//si les variables sont passés
		if (isset($_POST['username']) & isset($_POST['password'])) {
			//si les variables sont non vide
			if (!empty($_POST['username']) & !empty($_POST['password'])) {
				//obtenir le cookie à partir de de l'api xenforo avec un username et un password
				$rep = $this->xenforo->getLogin($_POST['username'], $_POST['password'], $_SERVER['REMOTE_ADDR']);

				//la réponse est false si les mots de passe ou le username est correct
				if ($rep !== false) {
					//ajout du cookies
//					Cookie::create($rep['cookie_name'], $rep['hash']);

					setcookie($rep['cookie_name'], $rep['hash']);
					//redirect to home
					return $this->redirect($response, 'home');
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

}