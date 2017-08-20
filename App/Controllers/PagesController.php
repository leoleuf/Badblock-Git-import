<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PagesController extends Controller {

    public function getHome(RequestInterface $request, ResponseInterface $response){

//    	var_dump($this->container->minecraft->getStatus());
//    	var_dump($this->container->minecraft->getPlayers());

		/*$firstRow = $this->redis->getJson('website:first_row_posts');
		$secondRow = $this->redis->getJson('website:second_row_posts');

		$collection = $this->mongo->test->users;
		$insertOneResult = $collection->insertOne([
			'username' => 'username',
			'email' => 'admin@example.com',
			'name' => 'Admin User',
		]);

		printf("Inserted %d document(s)\n", $insertOneResult->getInsertedCount());

		var_dump($insertOneResult->getInsertedId());*/

		$this->render($response, 'pages/home.twig', [
        	'first_row' => $firstRow,
			'second_row' => $secondRow
		]);
    }

}