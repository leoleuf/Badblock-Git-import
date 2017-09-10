<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class PagesController extends Controller
{

	public function getHome(RequestInterface $request, ResponseInterface $response)
	{
		$firstRow = $this->redis->getJson('first_row_posts');
		$secondRow = $this->redis->getJson('second_row_posts');
		$postsCount = $this->redis->get('posts_count');

		$this->render($response, 'pages.home', [
			'first_row' => $firstRow,
			'second_row' => $secondRow,
			'posts_count' => $postsCount
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

    public function getstaff(RequestInterface $request, ResponseInterface $response){
        //récupération du cache
        $admin = $this->redis->getJson('staff.admin');
        $dev = $this->redis->getJson('staff.dev');
        $resp = $this->redis->getJson('staff.resp');
        $sup = $this->redis->getJson('staff.sup');
        $modo = $this->redis->getJson('staff.modo');
        $help = $this->redis->getJson('staff.helper');
        $modof = $this->redis->getJson('staff.modof');
        $staff = $this->redis->getJson('staff.staff');


        var_dump($admin[0]);



        $this->render($response, 'pages.staff', [
            'admin' => $admin,
            'resp' => $resp,
            'dev' => $dev,
            'sup' => $sup,
            'modo' => $modo,
            'help' => $help,
            'modof' => $modof,
            'staff' => $staff,
        ]);

    }
}