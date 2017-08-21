<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 18/08/2017
 * Time: 23:04
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class StatsController extends Controller
{

    public function home(RequestInterface $request, ResponseInterface $response){
        $this->container->view->render($response, 'pages/home-stats.twig');
    }

    public function game(RequestInterface $request, ResponseInterface $response,$game,$page = null){
        //Régulation vers fonction
        if(!isset($page)){
          $page = 1;
        }
        $game = $game['game'];
        //Tower
        if($game == "tower"){
            $this->tower($page);
        //Rush
        }elseif ($game == "rush"){
            $this->rush($page);
        //Capture the flags
        }elseif($game == "cts"){
            $this->cts($page);
        //Survival Games Solo
        }elseif ($game == "SurvivalGamesSolo"){
            $this->sgs($page);
        //Survival Games Team
        }elseif ($game == "SurvivalGamesTeams"){
            $this->sgt($page);
        //Pvp-Box
        }elseif ($game == "pvpbox"){
            $this->pvpbox($page);
        //Faction
        }elseif ($game == "Faction"){
            $this->faction($page);
        //UHC Solo
        }elseif ($game == "uhcsolo"){
            $this->uhcsolo($page);
        //UHC Team
        }elseif ($game == "uhcteam"){
            $this->uhcteam($page);
        //Space Balls
        }elseif ($game == "SpaceBalls"){
            $this->spaceball($page);
        //DayZ
        }elseif ($game == "dayz"){
            $this->dayz($page);
        }else{
            //Erreur 404
            return $response->withStatus(404);
        }
    }


    public function tower($page){
//        $m = new $this->mongo;
//        $c = $m->selectCollection("phpt", "find");
//
//        $r = $c->find( array( "name" => array( "$gte" => 42 ) ) );
		/*
		insert One document
		$collection = $this->mongo->test->users;

		$insertOneResult = $collection->insertOne([
			'username' => 'admin',
			'email' => 'admin@example.com',
			'name' => 'Admin User',
		]);

		printf("Inserted %d document(s)\n", $insertOneResult->getInsertedCount());

		var_dump($insertOneResult->getInsertedId());*/

		$collection = $this->mongo->test->users;

		$document = $collection->find(['_id' => '5999e7c8b6182c29a83df7c6']);

		var_dump($document);
    }


}