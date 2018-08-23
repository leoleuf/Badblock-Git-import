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
            return $this->redirect($response, '/shop/recharge/cancel');
        }
        else {
            $dedipass = file_get_contents('http://api.dedipass.com/v1/pay/?public_key='.getenv("DEDIPASS_PUBLIC_KEY")
                .'&private_key='.getenv("DEDIPASS_PRIVATE_KEY").'&code=' . $code);
            $dedipass = json_decode($dedipass);

            $name = "";
            if (isset($_POST['internal_username']))
            {
                $name = $_POST['internal_username'];
            }
            else
            {
                $name = $this->container->session->get('recharge-username');
            }

            if($dedipass->status == 'success') {
                $virtual_currency = $dedipass->virtual_currency;
                // Détection d'une quelconque action
                // Sauvegarde dans mongoDB
                unset($dedipass->key);
                unset($dedipass->public_key);
                $dedipass->name = strtolower($name);
                $dedipass->date = date('Y-m-d H:i:s');
                $this->container->mongo->funds_logs->insertOne($dedipass);

                $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);
                $data = [
                    'uniqueId' => $user['uniqueId'],
                    'date' => date('Y-m-d H:i:s'),
                    'price' => $dedipass->payout,
                    'gateway' => 'dedipass',
                    'pseudo' => $name,
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

                if ($this->container->session->exist('user')){
                    $mailContent = file_get_contents("../mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($this->session->get('user')["email"], "BadBlock - Rechargement", $mailContent);
                }

                return $this->redirect($response, '/shop/recharge/success');

            }
            else {
                return $this->redirect($response, '/shop/recharge/cancel');

            }
        }
    }

    // ig : dégueu mais pas d'idée ..
    public function processig(RequestInterface $request, ResponseInterface $response){
        $code = isset($_POST['code']) ? preg_replace('/[^a-zA-Z0-9]+/', '', $_POST['code']) : '';
        if( empty($code) ) {
            echo 'Vous devez saisir un code';
        }
        else {
            $dedipass = file_get_contents('http://api.dedipass.com/v1/pay/?public_key='.getenv("DEDIPASS_PUBLIC_KEY")
                .'&private_key='.getenv("DEDIPASS_PRIVATE_KEY").'&code=' . $code);
            $dedipass = json_decode($dedipass);

            $name = "";
            if (isset($_POST['internal_username']))
            {
                $name = $_POST['internal_username'];
            }
            else
            {
                $name = $this->container->session->get('recharge-username');
            }

            if($dedipass->status == 'success') {
                $virtual_currency = $dedipass->virtual_currency;
                // Détection d'une quelconque action
                // Sauvegarde dans mongoDB
                unset($dedipass->key);
                unset($dedipass->public_key);
                $dedipass->name = strtolower($name);
                $dedipass->date = date('Y-m-d H:i:s');
                $this->container->mongo->funds_logs->insertOne($dedipass);

                $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);
                $data = [
                    'uniqueId' => $user['uniqueId'],
                    'date' => date('Y-m-d H:i:s'),
                    'price' => $dedipass->payout,
                    'gateway' => 'dedipass',
                    'pseudo' => $name,
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

                if ($this->container->session->exist('user')){
                    $mailContent = file_get_contents("../mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($this->session->get('user')["email"], "BadBlock - Rechargement", $mailContent);
                }

                return $this->redirect303($response, '/shop/recharge/success');

            }
            else {
                return $this->redirect301($response, '/shop/recharge/cancel');

            }
        }
    }

}