<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

use xPaw\MinecraftPing;
use xPaw\MinecraftPingException;


class MinecraftApiController extends \App\Controllers\Controller
{

	public function getPlayers(ServerRequestInterface $request, ResponseInterface $response){
        if ($this->container->redis->exists('api.mc.players') == 1 && intval($this->container->redis->get('api.mc.players')) != 0 && intval($this->container->redis->get('api.mc.players')) != null){
            return $response->withJson(["players" => ['now' => intval($this->container->redis->get('api.mc.players'))]]);
        }else{
            try
            {
                $Query = new MinecraftPing( 'play.badblock.fr', 25565 );
                $online = $Query->Query()["players"]["online"];


                //REGARDE MIRO
                if (intval($online) != 0 && intval($online) != null){
                    $this->container->redis->set('api.mc.players', $online);
                    $this->container->redis->expire('api.mc.players', 5);
                }else{
                    $online = file_get_contents('https://minecraft-api.com/api/ping/playeronline.php?ip=play.badblock.fr&port=25565');
                    $this->container->redis->set('api.mc.players', intval($online));
                    $this->container->redis->expire('api.mc.players', 5);
                }

                return $response->withJson(["players" => ['now' => $online]]);
            }
            catch( MinecraftPingException $e )
            {
                //
            }
            finally
            {
                if( $Query )
                {
                    $Query->Close();
                }
            }
        }
	}

    public function getStats(ServerRequestInterface $request, ResponseInterface $response)
    {
        $data = [];
        return $response->withJson($data);
    }
}