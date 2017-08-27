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

	public function home(RequestInterface $request, ResponseInterface $response)
	{
		$this->container->view->render($response, 'pages/home-stats.twig');
	}

	public function game(RequestInterface $request, ResponseInterface $response, $game, $page = NULL)
	{
		//Régulation vers fonction
		if (!isset($page)) {
			$page = 1;
		}
		$game = $game['game'];
		//Tower
		if ($game == "tower") {
			$this->tower($page);
			//Rush
		} elseif ($game == "rush") {
			$this->rush($page);
			//Capture the flags
		} elseif ($game == "cts") {
			$this->cts($page);
			//Survival Games Solo
		} elseif ($game == "SurvivalGamesSolo") {
			$this->sgs($page);
			//Survival Games Team
		} elseif ($game == "SurvivalGamesTeams") {
			$this->sgt($page);
			//Pvp-Box
		} elseif ($game == "pvpbox") {
			$this->pvpbox($page);
			//Faction
		} elseif ($game == "Faction") {
			$this->faction($page);
			//UHC Solo
		} elseif ($game == "uhcsolo") {
			$this->uhcsolo($page);
			//UHC Team
		} elseif ($game == "uhcteam") {
			$this->uhcteam($page);
			//Space Balls
		} elseif ($game == "SpaceBalls") {
			$this->spaceball($page);
			//DayZ
		} elseif ($game == "dayz") {
			$this->dayz($page);
		} else {
			//Erreur 404
			return $response->withStatus(404);
		}
	}


	public function tower($page)
	{

		$collection = $this->mongo->test->test;

		$cursor = $collection->find()->MongoCursor::sort(array("name" => 0));


        foreach ($cursor as $document) {
            echo $document["name"] . "\n";
        }
	}


    public function cache()
    {

        $collection = $this->mongo->test->test;

        $cursor = $collection->find();


        foreach ($cursor as $document) {
            echo $document["name"] . "\n";
        }
    }



}