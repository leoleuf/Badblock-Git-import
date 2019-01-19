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
            $data = str_replace("\"", "", $data);
            $c_ts = intval($data);
        }else{
            $c_ts = 0;
        }

	    $guardian = $this->redis->getJson('stats:guardian');
	    $gstats = $this->redis->getJson('stats:stats_guardian');
	    $stats = $this->redis->getJson('stats:stats_general');

	    try {
            $playergraph = file_get_contents("servergraphs.dat");
        }catch (\Exception $e){
            $playergraph = "";
        }

        $staff = $this->redis->getJson('staff.list');
        $nb = $this->redis->getJson('staff.number');

        foreach ($staff as $key => $row){
            shuffle($staff[$key]['data']);
        }

		$this->render($response, 'stats.home',['c_ts' => $c_ts,'connected' => $connected,'guardian' => $guardian,'gstats' => $gstats,'stats' => $stats, 'playergraph' => $playergraph, 'staff' => $staff,
            'nb' => $nb]);
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
            'UHCSpeed' => 'UHCSpeed',
            'SpaceBalls' => 'SpaceBalls',
            'Bedwars' => 'Bedwars'
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
            //Vérification si le jeux existe
            if (isset($list[$game["game"]])) {
                if($page == "1"){
                    $data = $this->redis->getJson("stats:".$game["game"].'_all'.":1");
                    if ($data == null){
                        //Erreur 404
                        return $this->redirect($response, '/stats');
                    }
                    //Slice de l'array
                    $datatop = array_slice($data,0,3,true);
                    $data = array_slice($data,3,17,true);
                    //Affichage de la page
                    $this->render($response, 'stats.table',['data' => $data,'datatop' => $datatop,'name' => "Statistiques ".$game['game']]);
                }else{
                    $data = $this->redis->getJson("stats:".$game.":".$page);
                    if ($data == null){
                        //Erreur 404
                        return $this->redirect($response, '/stats');
                    }
                    $nb1 = $page * 2 * 10-2;
                    $nb2 = $nb1 - 20+2;
                    $this->render($response, 'stats.tablepage',['data' => $data,'nb' => $nb2,'name' => "Statistiques ".$game]);
                }
            }else {
                //Erreur 404
                return $this->redirect($response, '/stats');
            }
        }else{
            $data = explode("_", $game["date"]);
            if (in_array(strtolower($data[0]), $months)){
                //Vérification si le jeux existe
                if (isset($list[$game["game"]])) {
                    if($page == "1"){
                        $data = $this->redis->getJson("stats:".$game["game"].'_'.$game["date"].":1");
                        if ($data == null){
                            //Erreur 404
                            return $this->redirect($response, '/stats');
                        }
                        //Slice de l'array
                        $datatop = array_slice($data,0,3,true);
                        $data = array_slice($data,3,17,true);
                        //Affichage de la page
                        $this->render($response, 'stats.table',['data' => $data,'datatop' => $datatop,'name' => "Statistiques ".$game['game']]);
                    }else{
                        $data = $this->redis->getJson("stats:".$game["game"].'_'.$game["date"].":".$page);
                        if ($data == null){
                            //Erreur 404
                            return $this->redirect($response, '/stats');
                        }
                        $nb1 = $page * 2 * 10-2;
                        $nb2 = $nb1 - 20+2;
                        $this->render($response, 'stats.tablepage',['data' => $data,'nb' => $nb2,'name' => "Statistiques ".$game]);
                    }
                }else {
                    //Erreur 404
                    return $this->redirect($response, '/stats');
                }
            }else{
                //Erreur 404
                return $this->redirect($response, '/stats');
            }
		}
	}


    public function search()
    {
        $data = $_POST["search_player"];
        if ($this->redis->exists("search:" . $data))
        {
            return $this->redis->get("search:" . $data);
        }

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