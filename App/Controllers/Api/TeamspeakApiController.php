<?php

namespace App\Controllers\Api;


use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class TeamspeakApiController extends \App\Controllers\Controller
{

    public function index(RequestInterface $request, ResponseInterface $response)
    {
        //Search all Teamspeak channel
        $Channels = $this->container->mongo->teamspeak_channel->find();

        $I = 0;
        foreach ($Channels as $channel){
            $User = $this->container->mongoServer->players->findOne(["uniqueId" => $channel['uniqueId']]);

            $check = false;

            if ($User != null){
                foreach ((array)$User['permissions']->groups->bungee as $k => $row) {
                    if ($k == "gradeperso") {
                        $check = true;
                    }
                }
                //Il est déjà Legend
                if ($check == false){
                    $this->container->mongoServer->players->deleteOne(["uniqueId" => $channel['uniqueId']]);
                    $Groups = $this->container->mongo->teamspeak_groups->find(["uniqueId" => $channel['uniqueId']]);
                    $this->container->teamspeak->removeGroup($Groups['group_id']);
                    $this->container->teamspeak->deleteChannel(intval($channel['channel_id']), 1);
                    $I++;
                }
            }
        }

        return "Deleted old Legend Teamspeak channel number : " . $I;

    }

}