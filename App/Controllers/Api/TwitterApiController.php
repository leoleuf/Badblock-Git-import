<?php

namespace App\Controllers\Api;


use Monolog\Handler\Mongo;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use MongoCursor;

class TwitterApiController extends \App\Controllers\Controller
{


    public function upCache(RequestInterface $request, ResponseInterface $response){

        $collection = $this->container->mongoServer->players;
        $cursor = $collection->find(array("token" => array('$ne' => null)));

        $consumer_key = "JgQHyz4RwedCWVdyUD5VLM8Rw";
        $consumer_secret = "EFvvPXbwd5ANxlKysETsvkq8tJGFxZ43xp304ou9zuc7igPtNy";

        foreach ($cursor as $key => $value) {
            /*$oauth_verifier = $value['oauth_verifier'];
            $oauth_token = $value['oauth_token'];*/

            $token = $value['token'];
            $secret = $value['secret'];
            $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret, $token, $secret);
            $content = $connection->get("account/verify_credentials");

            if ($content != null && $content->errors != null)
            {
                sleep(2);
                echo 'ERR '.$value['name'].PHP_EOL;
                continue;
            }

            $connection->post("favorites/create", array('id' => '1072435177928962048'));
            echo 'OK '.$value['name'].PHP_EOL;
            sleep(2);
        }

        echo 'DONE!';

    }

}