<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 18/08/2017
 * Time: 23:04
 */

namespace App\Controllers;

use App\MinecraftServerQuery;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Slim\App;

class StatsController extends Controller
{

	public function home(RequestInterface $request, ResponseInterface $response)
	{
	    $connected = 0;
	    //debug
        if ($this->redis->exists('api.teamspeak.online')){
            $data = $this->redis->get('api.teamspeak.online');
        }else{
            $data = $this->container->teamspeak->online();
            $this->redis->setJson('api.teamspeak.online', $data);
            $this->redis->expire('api.teamspeak.online', 100);
        }


	    $c_ts = intval($data);


	    $guardian = $this->redis->getJson('stats:guardian');
	    $gstats = $this->redis->getJson('stats:stats_guardian');
	    $stats = $this->redis->getJson('stats:stats_general');
		$this->render($response, 'stats.home',['c_ts' => $c_ts,'connected' => $connected,'guardian' => $guardian,'gstats' => $gstats,'stats' => $stats]);
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



    public function search()
    {
        $data = $_POST["search_player"];
        if($this->redis->exists("search:" . $data)){
            return $this->redis->get("search:" . $data);
        }else{
            $resultR = [];
            $query = "SELECT username FROM xf_user WHERE username LIKE '". $data ."%' ORDER by username DESC LIMIT 10";
            foreach ($this->container->mysql_forum->fetchRowManyCursor($query) as $result)
            {
                array_push($resultR, $result);
            }
            $this->redis->setjson('search:' . $data, $resultR);
            $this->redis->expire('search:' . $data, 60);
            return json_encode($resultR);
        }
    }



}