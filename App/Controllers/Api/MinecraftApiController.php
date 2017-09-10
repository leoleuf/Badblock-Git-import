<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class MinecraftApiController extends \App\Controllers\Controller
{
	public function getPlayers(ServerRequestInterface $request, ResponseInterface $response)
	{
		return $response->withJson($this->mc->getPlayers());
	}
}