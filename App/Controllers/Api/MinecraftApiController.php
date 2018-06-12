<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

use xPaw\MinecraftPing;
use xPaw\MinecraftPingException;


class MinecraftApiController extends \App\Controllers\Controller
{
	public function getPlayers(ServerRequestInterface $request, ResponseInterface $response)
	{

        try
        {
            $Query = new MinecraftPing( 'play.badblock.fr', 25565 );

            dd($this->container->teamspeak->onlineNC());


            return $response->withJson(["players" => ['now' => $Query->Query()["players"]["online"]]]);

        }
        catch( MinecraftPingException $e )
        {
            echo $e->getMessage();
        }
        finally
        {
            if( $Query )
            {
                $Query->Close();
            }
        }
	}

    public function getStats(ServerRequestInterface $request, ResponseInterface $response)
    {
        $data = [];
        return $response->withJson($data);
    }
}