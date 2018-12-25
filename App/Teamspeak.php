<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 26/10/2017
 * Time: 19:28
 */

namespace App;

use \ts3admin;
use Noodlehaus\Exception;

class Teamspeak
{

    public function __construct($container, $config)
    {
        $this->container = $container;
        $this->redis = $container->redis;
        $this->ip = $config->ip;
        $this->port = $config->port;
        $this->username = $config->username;
        $this->password = $config->password;
        $this->query_port = $config->query_port;
        $this->client = new ts3admin($this->ip, $this->query_port);
    }

    public function connection(){
        if($this->client->getElement('success', $this->client->connect())) {
            //Login sur le query
            $login = $this->client->login($this->username, $this->password);
            if ($login['success'] == true){
                $this->client->selectServer($this->port);
            }else{
                $this->container->log->error('"App/TeamSpeak"',' Bad login ! User : ' . $this->username);
                throw new Exception('TeamSpeak : Bad Login');
            }
        }else{
            $login = $this->client->login($this->username, $this->password);
            if ($login['success'] == true){
                $this->client->selectServer($this->port);
            }
        }
    }

    public function online(){
        $this->connection();
        return $this->client->serverInfo()["data"]['virtualserver_clientsonline'];
    }

    //Create channel Teamspeak
    public function createChannel($Uid, $Name, $Password){
        $this->connection();
        $data = array();
        $data['CHANNEL_NAME'] = $Name;
        $data['CHANNEL_PASSWORD'] = $Password;
        $data['CHANNEL_FLAG_PERMANENT'] = 1;
        $data['CPID'] = 230;
        $data['CHANNEL_DESCRIPTION'] = "";

        $ChannelId = $this->client->channelCreate($data);

        $ClientId = $this->client->clientGetDbIdFromUid($Uid);
        //Proprio du canal
        $this->client->channelGroupAddClient(5, $ChannelId['data']['cid'], $ClientId['data']['cldbid']);

        return $ChannelId;
    }

    public function deleteChannel($Uid){
        $this->connection();
        $Del = $this->client->channelDelete($Uid, 1);
        return $Del;
    }

    public function customGroup($Name){
        $this->connection();
        $Id = $this->client->serverGroupAdd($Name, 0);


        return $Id['data']['sgid'];
    }

    public function removeGroup($Name){
        $this->connection();
        $Id = $this->client->serverGroupDelete($Name, 1);
        return $Id;
    }

    public function addtogroup($Uid, $GroupId){
        $this->connection();
        $ClientId = $this->client->clientGetDbIdFromUid($Uid);
        $Id = $this->client->serverGroupAddClient($GroupId, $ClientId['data']['cldbid']);
        return true;
    }


}