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
              // Détection d'une quelconque action
              // Sauvegarde dans mongoDB
              unset($dedipass->key);
              unset($dedipass->public_key);
              $dedipass->name = strtolower($this->container->session->get('recharge-username'));
              $dedipass->date = date('Y-m-d H:i:s');
              $this->container->mongo->funds_logs->insertOne($dedipass);

              $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->container->session->get('recharge-username'))]);
              $data = [
                  'uniqueId' => $user['uniqueId'],
                  'date' => date('Y-m-d H:i:s'),
                  'price' => $dedipass->payout,
                  'gateway' => 'dedipass',
                  'pseudo' => $this->container->session->get('recharge-username'),
                  'points' => $dedipass->virtual_currency,
                  'transaction_id' => $code
              ];

              $this->container->mongo->funds->insertOne($data);
              $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);
              if ($money == null){
                  $data = [
                      "uniqueId" => $user['uniqueId'],
                      "points" => $dedipass->virtual_currency
                  ];
                  $this->container->mongo->fund_list->insertOne($data);
              }else{
                  $money['points'] = $money['points'] + $dedipass->virtual_currency;
                  $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
              }

              return $this->redirect($response, '/shop/recharge/sucess');


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