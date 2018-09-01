<?php

namespace App\Controllers;

use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class DiscoverController extends Controller
{

	public function getHome(RequestInterface $request, ResponseInterface $response)
	{
        $this->render($response, 'discover.home');
	}

    public function skyblock(ServerRequestInterface $request, ResponseInterface $response)
    {
        $this->render($response, 'decouvrez.skyblock');
    }

}