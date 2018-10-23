<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

use xPaw\MinecraftPing;
use xPaw\MinecraftPingException;


class MinecraftApiController extends \App\Controllers\Controller
{

	public function getPlayers(ServerRequestInterface $request, ResponseInterface $response){
        if ($this->container->redis->exists('api.mc.player') && intval($this->container->redis->get('api.mc.player')) != 0){
            return $response->withJson(["players" => ['now' => intval($this->container->redis->get('api.mc.player'))]]);
        }else{
            try
            {
                $Query = new MinecraftPing( 'play.badblock.fr', 25565 );

                $online = $Query->Query()["players"]["online"];

                if (intval($online) != 0){
                    $this->container->redis->set('api.mc.player', $online);
                    $this->container->redis->expire('api.mc.player', 5);
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