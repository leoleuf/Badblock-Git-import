<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class LoginApiDevController extends \App\Controllers\Controller
{

    public function login(ServerRequestInterface $request, ResponseInterface $response,$args){

        date_default_timezone_set('Europe/Paris');
        $time = date('Y-m-d h:i');
            echo date_default_timezone_get();
            var_dump($time);
            $time =  hash("gost",$time);
            $key = md5($time);

                //user
        $user = $this->xenforo->getUser($args['username']);

        $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);

        //mise de l'utilisateur en session
        $this->session->set('user', [
            'id' => 84,
            'username' => $user['username'],
            'email' => $user['email'],
            'user_group_id' => $user['user_group_id'],
            'secondary_group_ids' => $user['secondary_group_ids'],
            'custom_title' => $user['custom_title'],
            'is_admin' => $user['is_admin'],
            'is_banned' => $user['is_banned'],
            'is_staff' => $user['is_staff'],
            'is_moderator' => $user['is_moderator']
        ]);


        return $response->write('Ok login')->withStatus(200);


    }


}