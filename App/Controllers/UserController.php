<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Http\Request;

class UserController extends Controller
{

	public function getDashboard(RequestInterface $request, ResponseInterface $response, $args)
	{
		return $this->render($response, 'user.dashboard');
	}

	public function getProfile(RequestInterface $request, ResponseInterface $response, $args)
	{
		//sans cache
		$collection = $this->mongo->test->test;

		$user = $collection->findOne(['realName' => $args['pseudo']]);

		if (empty($user)) {
			//if user not found
			return $this->container['notFoundHandler']($request, $response);
		}

		$user->game->stats->tower['cpoints'] = $this->tower($user->game->stats->tower);
		$user->game->stats->rush['cpoints'] = $this->rushs($user->game->stats->rush);
		$user->game->stats->survival['cpoints'] = $this->survival($user->game->stats->survival);
		$user->game->stats->uhcspeed['cpoints'] = $this->uhcSpeed($user->game->stats->uhcspeed);
		$user->game->stats->pearlswar['cpoints'] = $this->pearlSwar($user->game->stats->pearlswar);
		$user->game->stats->spaceballs['cpoints'] = $this->spaceBall($user->game->stats->spaceballs);
		$user->game->stats->cts['cpoints'] = $this->cts($user->game->stats->cts);

		//return view
		return $this->render($response, 'user.profile', [
			'user' => $user]);

	}

	public function tower($var)
	{

		//Algo celon Xmalware

		$c1 = $var['marks'] + $var['wins'];
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}

	public function spaceBall($var)
	{

		//Algo celon Xmalware

		$c1 = $var['diamonds'] + $var['wins'];
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}

	public function rushs($var)
	{

		//Algo celon Xmalware

		$c1 = $var['brokenbeds'] + $var['wins'];
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}


	public function pearlSwar($var)
	{

		//Algo celon Xmalware

		$c1 = $var['wins'] * 1.5;
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}


	public function uhcSpeed($var)
	{

		//Algo celon Xmalware

		$c1 = $var['wins'] * 1.5;
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}

	public function cts($var)
	{

		//Algo celon Xmalware

		$c1 = $var['capturedflags'] + $var['wins'];
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}


	public function survival($var)
	{

		//Algo celon Xmalware

		$c1 = $var['wins'] * 1.5;
		//pas de division par zéro
		if ($var['deaths'] > 0) {
			$c2 = $var['kills'] / $var['deaths'];
		} else {
			$c2 = $var['kills'];
		}
		$c3 = $c1 * $c2;
		//pas de division par zéro
		if ($var['looses'] > 0) {
			$c4 = $c3 / $var['looses'];
		} else {
			$c4 = $c3;
		}

		//Arrondis

		$c4 = round($c4, 0, PHP_ROUND_HALF_UP);

		return $c4;

	}


}