<?php
namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class ShopController extends Controller
{
	public function getHome(ServerRequestInterface $request, ResponseInterface $response)
	{
		//get twig of home
		$this->render($response, 'shop.home');
	}

	public function getRecharge(ServerRequestInterface $request, ResponseInterface $response)
	{
		//get twig of recharge
		$this->render($response, 'shop.recharge');
	}
}