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
	    $guardian = $this->redis->getJson('stats:guardian');
	    $gstats = $this->redis->getJson('stats:gstats');
	    var_dump($gstats);
		$this->render($response, 'stats.home',['guardian' => $guardian,'gstats' => $gstats]);
	}

	public function games(RequestInterface $request, ResponseInterface $response)
	{
		$this->render($response, 'stats.games');
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

        $months = array(
            'janvier',
            'février',
            'mars',
            'avril',
            'mai',
            'juin',
            'juillet',
            'août',
            'septembre',
            'octobre',
            'novembre',
            'décembre'
        );

        //pas de page renseigner
		if (!isset($game["page"])) {
			$page = "1";
		}else{
            $page = $game["page"];
        }

        if ($game["date"] === "all"){
            //Vérification si le jeux écist
            if (isset($list[$game["game"]])) {
                //Régulation vers fonction
                $this->lecture($game["game"],$page,$response);
            }else {
                //Erreur 404
                return $response->withStatus(404);
            }
        }else{
            $data = explode("_", $game["date"]);
            if (in_array(strtolower($data[0]), $months)){
                    //check if stat are invalid
                    $sql = $this->mysql->fetchrow("SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_NAME = '". $game['game'] ."_". strtolower($game['date']) ."')");
                    if($sql["count(*)"] > 0){
                        //ok
                        $this->lecture($game["game"]."_".strtolower($game['date']),$page,$response);
                    }else{
                        //Erreur 404
                        return $response->withStatus(404);
                    }
                }else{
                //Erreur 404
                return $response->withStatus(404);
            }

            }
	}


    public function lecture($game,$page,$response)
    {
        if(is_numeric($page)){
            var_dump($page);
            //page top
            if ($page === '1'){
                $nb1 = $page * 2 * 10-2;
                $nb2 = $nb1 - 20+2;
                //Lecture du cache
                $data = $this->redis->getJson("stats:".$game);
                //Slice de l'array
                $datatop = array_slice($data,0,3,true);
                $data = array_slice($data,3,17,true);
                //Affichage de la page
                $this->render($response, 'stats.table',['data' => $data,'datatop' => $datatop,'name' => "Statistiques ".$game]);
            //page avec page > 1
            }else{
                $nb1 = $page * 2 * 10-2;
                $nb2 = $nb1 - 20+2;
                //Lecture du cache
                $data = $this->redis->getJson("stats:".$game);
                //Slice de l'array
                $data = array_slice($data,$nb2,20,true);
                var_dump($data);
                //Affichage de la page
                $this->render($response, 'stats.tablepage',['data' => $data,'nb' => $nb2,'name' => "Statistiques ".$game]);
            }

        }





    }



    public function cache()
    {

    }



}