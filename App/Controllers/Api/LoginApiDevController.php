<?php

namespace App\Controllers\Api;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class LoginApiDevController extends \App\Controllers\Controller
{

    public function login(ServerRequestInterface $request, ResponseInterface $response,$args){

         if ($this->container->config['app_debug'] == 1){
             //user
             $user = $this->xenforo->getUser($args['username']);

             $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);

             //mise de l'utilisateur en session
             $this->session->set('user', [
                 'id' => $user['user_id'],
                 'username' => $user['username'],
                 'email' => $user['email'],
                 'user_group_id' => $user['user_group_id'],
                 'secondary_group_ids' => [17],
                 'custom_title' => $user['custom_title'],
                 'is_admin' => $user['is_admin'],
                 'is_banned' => $user['is_banned'],
                 'is_staff' => $user['is_staff'],
                 'is_moderator' => $user['is_moderator']
             ]);


             return $response->write('Ok login')->withStatus(200);
         }else{
             return $response->write('Nope')->withStatus(200);
         }


    }


    public function test(ServerRequestInterface $request, ResponseInterface $response,$args){
        $this->ladder->playerSendMessage("fluor","re e e e");
        var_dump(http_build_query(array("name" => "Fluor", "message" => "dd")));


    }



}