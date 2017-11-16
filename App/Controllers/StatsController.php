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


    /**
     * @param RequestInterface $request
     * @param ResponseInterface $response
     * @param $game
     * @param null $page
     * @return static
     */
    public function game(RequestInterface $request, ResponseInterface $response, $game)
	{
        // Noms des jeux & affichage
        $list = array(
            'TowerRun' => 'Tower Run',
            'Tower' => 'Tower',
            'Rush' => 'Rush',
            'SurvivalGames' => 'SurvivalGames',
            'UHCSpeed' => 'UHCSpeed',
            'CaptureTheSheep' => 'CaptureTheSheep',
            'BuildContest' => 'BuildContest',
            'SpaceBalls' => 'SpaceBalls',
            'PearlsWars' => 'PearlsWars'
        );

        //pas de page renseigner
		if (!isset($game["page"])) {
			$page = "1";
		}else{
            $page = $game["page"];
        }

        //Vérification si le jeux écist
        if (isset($list[$game["game"]])) {
            //Régulation vers fonction
            $this->lecture($game["game"],$page,$response);
        }else {
			//Erreur 404
			return $response->withStatus(404);
		}
	}


    public function lecture($game,$page,$response)
    {
        if(is_numeric($page)){
            var_dump($page);
            //page top
            if ($page === '1'){
                $nb1 = $page * 2 * 10-2;
                $nb2 = $nb1 - 20+3;
                //Lecture du cache
                $data = $this->redis->getJson("stats:".$game);
                //Slice de l'array
                $datatop = array_slice($data,0,3,true);
                $data = array_slice($data,3,18,true);
                //Affichage de la page
                $this->render($response, 'stats.table',['data' => $data,'datatop' => $datatop]);
            //page avec page > 1
            }else{
                $nb1 = $page * 2 * 10-2;
                $nb2 = $nb1 - 20+3;
                //Lecture du cache
                $data = $this->redis->getJson("stats:".$game);
                //Slice de l'array
                $data = array_slice($data,$nb2,20,true);
                var_dump($data);
                //Affichage de la page
                $this->render($response, 'stats.tablepage',['data' => $data,'nb' => $nb2]);
            }

        }





    }



    public function cache()
    {

    }



}