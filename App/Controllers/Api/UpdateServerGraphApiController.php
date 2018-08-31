<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

use xPaw\MinecraftPing;
use xPaw\MinecraftPingException;

class UpdateServerGraphApiController extends \App\Controllers\Controller
{

    public function update(ServerRequestInterface $request, ResponseInterface $response,$args){

        if ($this->container->redis->exists('api.mc.player_graph')){
            return $response->withJson(["players" => ['now' => $this->container->redis->get('api.mc.player_graph')]]);
        }else{
            try
            {
                $Query = new MinecraftPing( 'play.badblock.fr', 25565 );

                $online = $Query->Query()["players"]["online"];

                if ($online == null || empty($online))
                {
                    return $response->write('Server down');
                }

                $this->container->redis->set('api.mc.player_graph', $online);
                $this->container->redis->expire('api.mc.player_graph', 3600);


                file_put_contents("servergraphs.dat", "[".time()."000,".$online."],", FILE_APPEND);
                return $response->write($online);


                $Query->Close();

            }
            catch( MinecraftPingException $e )
            {
                //
            }
            finally
            {
            }
        }
    }

}