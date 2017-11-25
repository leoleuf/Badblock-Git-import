<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Http\Request;

class UserController extends Controller
{

	public function getDashboard(RequestInterface $request, ResponseInterface $response)
	{
        //sans cache
        $collection = $this->mongo->badblock->dat_users;

        $user = $collection->findOne(['realName' => $this->session->getProfile('username')['username']]);



        //return view
        return $this->render($response, 'user.dashboard', [
            'user' => $user]);
	}

	public function getProfile(RequestInterface $request, ResponseInterface $response, $args)
	{
        if (empty($args['pseudo'])) {
            //if user not found
            return $this->container['notFoundHandler']($request, $response);
        }

		//sans cache
		$collection = $this->mongo->admin->players;

		$user = $collection->findOne(['name' => $args['pseudo']]);

		$user["punish"]["muteEnd"] = round($user["punish"]["muteEnd"] / 1000);
		$user["punish"]["banEnd"] = round($user["punish"]["banEnd"] / 1000);



		//return view
		return $this->render($response, 'user.profile', [
			'user' => $user]);

	}





}