<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/08/2017
 * Time: 22:12
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class ProfileController extends Controller
{

    public function getprofile(RequestInterface $request, ResponseInterface $response,$pseudo){

		//sans cache
        $collection = $this->mongo->test->test;

        $user = $collection->findOne(['realName' => $pseudo['pseudo']]);

        if (empty($user)){
			return $this->container['notFoundHandler']($request, $response);
        }

        //return view
		return $this->render($response, 'user.profile', [
			'user' => $user
		]);
    }



}