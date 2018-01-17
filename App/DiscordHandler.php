<?php
namespace App;

class DiscordHandler
{



    public function __construct($container)
    {
        $this->container = $container;
    }

    public function debug($controller,$text){
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
        $curl = curl_init("https://discordapp.com/api/webhooks/373808432324542464/g_ZJQXYA0yPj7LyHebSQZA14eAbLxB7w8idL50weFHX-rSGpdI-cu-fiu0gbHl9BIa8F");
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);
    }
}