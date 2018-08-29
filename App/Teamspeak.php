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
        //Check de connection + login avant éxecution de commandes
        $this->online();
    }

    public function connection(){
        if($this->client->getElement('success', $this->client->connect())) {
            //Login sur le query
            $login = $this->client->login($this->username, $this->password);
            if ($login['success'] == true){
                $this->client->selectServer($this->port);
            }else{
                $this->container->log->error('"App/TeamSpeak"',' Bad login ! User : ' . $this->username);
                //throw new Exception('TeamSpeak : Bad Login');
            }
        }else{
             $this->container->log->error('"App/TeamSpeak"',' Connection could not be established to ' . $this->ip);
            //throw new Exception('Connection could not be established to ' . $this->ip);
        }
    }

    public function online(){
        if (!$this->redis->exists('api.teamspeak.online'))
        {
            $this->connection();
            $data = $this->client->serverInfo()["data"]['virtualserver_clientsonline'];
            $this->redis->setJson('api.teamspeak.online', $data);
            $this->redis->expire('api.teamspeak.online', 300);
        }
    }

    public function onlineNC(){
        $this->connection();

        return $this->client->serverInfo()["data"]['virtualserver_clientsonline'];
    }






}