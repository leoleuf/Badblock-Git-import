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
		$this->render($response, 'stats.home');
	}

	public function podium(RequestInterface $request, ResponseInterface $response)
	{
		$this->render($response, 'stats.podium');
	}

    public function game(RequestInterface $request, ResponseInterface $response, $game, $page = NULL)
	{
        // Noms des jeux & affichage
        $list = array(
            'towerrun' => 'Tower Run',
            'tower' => 'Tower',
            'rush' => 'Rush',
            'survivalgames' => 'SurvivalGames',
            'uhcspeed' => 'UHCSpeed',
            'capturethesheep' => 'CaptureTheSheep',
            'buildcontest' => 'BuildContest',
            'spaceballs' => 'SpaceBalls',
            'pearlswar' => 'PearlsWar'
        );

		//Régulation vers fonction
        //pas de page renseigner
		if (!isset($page)) {
			$page = 1;
		}

        //Vérification si le jeux écist
        if (isset($list[$game["game"]])) {
            $this->lecture($game["game"]);
        }else {
			//Erreur 404
			return $response->withStatus(404);
		}
	}


    public function lecture($game)
    {


    }



    public function cache()
    {

    }



}