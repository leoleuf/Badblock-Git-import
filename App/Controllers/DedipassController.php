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

                $insertedId = $this->container->mongo->funds->insertOne($data);
                $insertedId = $insertedId->getInsertedId()->__ToString();

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
                    $mailContent = file_get_contents("https://badblock.fr/dist/mails/mail-achat.html");
                    $mailContent = str_replace("(username)", $this->container->session->get('recharge-username'), $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($this->session->get('user')["email"], "BadBlock - Paiement effectué", $mailContent);

                    $mailContent = $name." recharge +".$dedipass->virtual_currency." pts boutique (".$dedipass->payout." € - dedipass site)";
                    $mail = new \App\Mail(true);
                    $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);
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
                    'gateway' => 'dedipass-ingame',
                    'pseudo' => $name,
                    'points' => $dedipass->virtual_currency,
                    'transaction_id' => $code
                ];

                $insertedId = $this->container->mongo->funds->insertOne($data);
                $insertedId = $insertedId->getInsertedId()->__ToString();

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

                $user = $this->xenforo->getUser($name);
                $dzb = "";
                if ($user != null) {
                    $mailContent = file_get_contents("https://badblock.fr/dist/mails/mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($user["email"], "BadBlock - Paiement effectué", $mailContent);

                    $mailContent = $name." recharge +".$dedipass->virtual_currency." pts boutique (".$dedipass->payout." € - dedipass en jeu)";
                    $mail = new \App\Mail(true);
                    $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);
                    $dzb .= "§bUn mail a été envoyé à §e".$user['email']." §bcontenant la facture et ta preuve d'achat en cas de problème.";
                }
                else
                {
                    $dzb .= "§eNous t'invitons à t'inscrire sur le site de BadBlock pour avoir une facture et une preuve d'achat la prochaine fois.";
                }

                echo "§a§lCode valide. Vous avez été crédité de ".$dedipass->virtual_currency." points boutiques. ".$dzb;
            }
            else {
                echo "§cCode entré invalide.";
            }
        }
    }

}