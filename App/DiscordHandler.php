<?php
namespace App;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class DiscordHandler
{



    public function __construct($container)
    {
        $this->container = $container;
    }

    public function debug($controller,$text){
        return $this->info($controller,$text);
    }

    public function warning($controller,$text){
        return $this->info($controller,$text);
    }


	public function info($controller,$text)
	{
        $data = array("username" => "Logger Site","embeds" => array(0 => array(
            "url" => "http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]",
            "title" => "INFO : " . $controller,
            "description" => $text,
            "color" => 5788507
		)));

        $this->sendData($data);
    }

    public function error($controller,$text)
    {
        $data = array("username" => "Logger Site","embeds" => array(0 => array(
            "url" => "http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]",
            "title" => "ERROR : " . $controller,
            "description" => $text,
            "color" => 16711680
        )));

        $this->sendData($data);
    }



    public function success($controller,$text)
    {
        $data = array("username" => "Logger Site","embeds" => array(0 => array(
            "url" => "http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]",
            "title" => "SUCCESS : " . $controller,
            "description" => $text,
            "color" => 65280
        )));

        $this->sendData($data);
    }



    private function sendData($data){
        $curl = curl_init(getenv('DISCORD_WH'));
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);
    }



    public function sendForum(RequestInterface $request, ResponseInterface $response,$data){
        if ($data['type'] == 1){
            $tt = "Nouveau Ticket !";
        }else{
            $tt = "Réponse Ticket";
        }

        $data = array("username" => "Forum","embeds" => array(0 => array(
            "url" => "https://dev-forum.badblock.fr/support/list",
            "title" => $tt,
            "description" => ":new: [". $data["title"] ."]   <:boss:371644291812032512> : " . $data['user'],
            "color" => 65280
        )));

        $curl = curl_init("https://canary.discordapp.com/api/webhooks/371229543316324352/-wQdOTC_vWHQiYyhCnH9862GP1KhOmTl4tv5OHT6QN8OQ34a6LmNUvxefk9Ob6D1kQIJ");
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);


    }


}