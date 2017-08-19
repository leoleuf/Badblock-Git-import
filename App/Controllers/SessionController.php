<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 19/08/2017
 * Time: 22:28
 */

namespace App\Controllers;

use function FastRoute\TestFixtures\empty_options_cached;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class SessionController extends Controller
{

    public function login(RequestInterface $request, ResponseInterface $response){
        //Vérification des variables
        if(isset($_POST['username'])&isset($_POST['password'])){
            if (!empty($_POST['username'])&!empty($_POST['username'])){

            }
        }else{
            return $response->write('Bad request')->withStatus(400);
        }


    }



}