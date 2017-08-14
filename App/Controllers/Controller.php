<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class Controller{
	public $container;
	protected $xenforo;

	public function __construct($container)
	{
		$this->container = $container;
		$this->xenforo = $container['xenforo'];
	}

	public function redirect(ResponseInterface $response, $location){
		return $response->withStatus(302)->withHeader('Location', $location);
	}

	public function render(ResponseInterface $response, $file, $params = []){
		$this->container->view->render($response, $file, $params);
	}

	public function pathFor($name){
		return $this->container->router->pathFor($name);
	}
}