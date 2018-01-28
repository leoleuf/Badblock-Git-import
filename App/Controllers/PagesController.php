<?php

namespace App\Controllers;

use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class PagesController extends Controller
{

	public function getHome(RequestInterface $request, ResponseInterface $response)
	{
        $info = $this->redis->getjson('ip_'.$request->getAttribute('ip_address'));

        $firstRow = $this->redis->getJson('first_row_posts');
		$secondRow = $this->redis->getJson('second_row_posts');
		$postsCount = $this->redis->get('posts_count');

		$this->render($response, 'pages.home', [
			'first_row' => $firstRow,
			'second_row' => $secondRow,
			'posts_count' => $postsCount,
            'info' => $info
		]);
	}

	public function getPlay(ServerRequestInterface $request, ResponseInterface $response)
	{
		$this->render($response, 'pages.play');
	}

	public function getTest(ServerRequestInterface $request, ResponseInterface $response)
	{
		$this->render($response, 'pages.play');
	}


    public function shuffle_assoc($array) {
        $keys = array_keys($array);

        shuffle($keys);

        foreach($keys as $key) {
            $new[$key] = $array[$key];
        }

        $array = $new;

        return $new;
    }

	public function getStaff(RequestInterface $request, ResponseInterface $response)
	{
		//récupération du cache
		$admin = $this->redis->getJson('staff.admin');
		$dev = $this->redis->getJson('staff.dev');
		$resp = $this->redis->getJson('staff.resp');
		$sup = $this->redis->getJson('staff.sup');
		$modo = $this->redis->getJson('staff.modo');
		$help = $this->redis->getJson('staff.helper');
		$modof = $this->redis->getJson('staff.modof');
		$staff = $this->redis->getJson('staff.staff');
		$nb = $this->redis->getJson('staff.number');

		$this->render($response, 'pages.staff', [
			'admin' => PagesController::shuffle_assoc($admin),
			'resp' => PagesController::shuffle_assoc($resp),
			'dev' => PagesController::shuffle_assoc($dev),
			'sup' => PagesController::shuffle_assoc($sup),
			'modo' => PagesController::shuffle_assoc($modo),
			'help' => PagesController::shuffle_assoc($help),
			'modof' => PagesController::shuffle_assoc($modof),
			'staff' => PagesController::shuffle_assoc($staff),
			'nb' => $nb,
		]);

	}

	public function getPodium(ServerRequestInterface $request, ResponseInterface $response)
	{
		return $this->render($response, 'stats.podium');
	}


    public function getinfo(ServerRequestInterface $request, ResponseInterface $response)
    {
        return $this->render($response, 'pages.info');
    }




}