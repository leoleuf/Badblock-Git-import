<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/07/2018
 * Time: 11:34
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;



class DedipassController extends Controller
{


    public function index(RequestInterface $request, ResponseInterface $response){
        $this->render($response,'shop.recharge.dedipass', []);
    }

    public function process(RequestInterface $request, ResponseInterface $response){
        $code = isset($_POST['code']) ? preg_replace('/[^a-zA-Z0-9]+/', '', $_POST['code']) : ''; 
        if( empty($code) ) { 
          echo 'Vous devez saisir un code'; 
        } 
        else { 
          $dedipass = file_get_contents('http://api.dedipass.com/v1/pay/?public_key=1ebee64135e73413e44fcf6f9d9903b6&private_key=a5c1b01b3e98fd3a57e32e6f6577b3580ff53ed6&code=' . $code); 
          $dedipass = json_decode($dedipass); 
          if($dedipass->status == 'success') { 
            $virtual_currency = $dedipass->virtual_currency;
              // Detection d'une quelconque action
              // Sauvegarde dans mongoDB
              $this->container->mongo->funds_logs->insertOne($dedipass);

          } 
          else { 
            // Le code est invalide 
            $this->render($response,'shop.recharge.dedipass-process', [
                'status' => $dedipass->status
            ]);
          } 
        }     
    }

}