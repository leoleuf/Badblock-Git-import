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

            if (!$this->container->session->exist('recharge-username'))
            {
                return $this->redirect($response, '/shop/recharge/cancel');
            }

            $name = $this->container->session->get('recharge-username');

            if (empty($name))
            {
                return $this->redirect($response, '/shop/recharge/cancel');
            }

            if($dedipass->status == 'success') {
                $virtual_currency = $dedipass->virtual_currency;
                // Détection d'une quelconque action
                // Sauvegarde dans mongoDB
                unset($dedipass->key);
                unset($dedipass->public_key);
                $dedipass->name = strtolower($name);
                $dedipass->date = date('Y-m-d H:i:s');
                $this->container->mongoUltra->funds_logs->insertOne($dedipass);

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
                    $this->container->session->set('points', $dedipass->virtual_currency);
                    $this->container->mongo->fund_list->insertOne($data);
                }else{
                    $money['points'] = $money['points'] + $dedipass->virtual_currency;
                    $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
                    $this->container->session->set('points', $money['points']);
                }

                $doups = intval($dedipass->virtual_currency * 0.1);
                $refers = $this->container->mongoServer->refers->find(["uniqueId" => $user['uniqueId']]);

                foreach ($refers as $key => $value)
                {

                    if (!isset($value['state']) OR $value['state'] != "CONFIRMED")
                    {
                        continue;
                    }

                    $v = $value['receiver'];

                    $otherUser = $this->container->mongoServer->players->findOne(["uniqueId" => $v]);
                    if ($otherUser != null)
                    {
                        $data = [
                            'uniqueId' => $otherUser['uniqueId'],
                            'date' => date('Y-m-d H:i:s'),
                            'price' => 0,
                            'gateway' => 'Gain de la part de '.$value['receiver'],
                            'pseudo' => $otherUser['name'],
                            'points' => $doups
                        ];

                        $this->container->mongoUltra->funds->insertOne($data);

                        $resp = [
                            'name' => $otherUser['name'],
                            'date' => date("Y-m-d H:i:s")
                        ];

                        $this->container->mongoUltra->funds_logs->insertOne($resp);

                        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $otherUser['uniqueId']]);

                        if ($money == null)
                        {
                            $data = [
                                "uniqueId" => $otherUser['uniqueId'],
                                "points" => $doups
                            ];
                            $this->container->mongo->fund_list->insertOne($data);
                            $this->container->session->set('points', $doups);
                        }
                        else {
                            $money['points'] = $money['points'] + $doups;
                            $this->container->mongo->fund_list->updateOne(["uniqueId" => $otherUser['uniqueId']], ['$set' => ["points" => $money['points']]]);
                            $this->container->session->set('points', $money['points']);
                        }


                        try
                        {
                            $otherUser_xen = $this->xenforo->getUser($otherUser['name']);
                        }
                        catch (\Exception $e)
                        {
                            $otherUser_xen = null;
                        }

                        if ($otherUser_xen != null)
                        {
                            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-gain.html");
                            $mailContent = str_replace("(username)", $otherUser['name'], $mailContent);
                            $mailContent = str_replace("(gain)", $doups, $mailContent);
                            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                            $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                            $mail = new \App\Mail(true);
                            $mail->sendMail($otherUser_xen["email"], "Vous avez reçu de l'argent", $mailContent);
                        }

                        $mailContent = $name." gagne +".$doups." pts boutique (par ".$user['name'].")";
                        $mail = new \App\Mail(true);
                        $mail->sendMail("xmalware2@gmail.com", "BadBlock - Gain Parrainage", $mailContent);
                    }
                }

                try {
                    $user = $this->xenforo->getUser($name);
                }catch (\Exception $e){
                    $user = null;
                }

                if ($user != null) {
                    $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($user["email"], "BadBlock - Paiement effectué", $mailContent);
                }

                $mailContent = $name." recharge +".$dedipass->virtual_currency." pts boutique (".$dedipass->payout." € - dedipass site)";
                $mail = new \App\Mail(true);
                $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);

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
                $this->container->mongoUltra->funds_logs->insertOne($dedipass);

                $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);
                $data = [
                    'uniqueId' => $user['uniqueId'],
                    'date' => date('Y-m-d H:i:s'),
                    'price' => intval($dedipass->payout),
                    'gateway' => 'dedipass-ingame',
                    'pseudo' => $name,
                    'points' => $dedipass->virtual_currency,
                    'transaction_id' => $code
                ];

                $insertedId = $this->container->mongoUltra->funds->insertOne($data);
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

                $doups = intval($dedipass->virtual_currency * 0.1);
                $refers = $this->container->mongoServer->refers->find(["uniqueId" => $user['uniqueId']]);

                foreach ($refers as $key => $value)
                {

                    if (!isset($value['state']) OR $value['state'] != "CONFIRMED")
                    {
                        continue;
                    }

                    $v = $value['receiver'];

                    $otherUser = $this->container->mongoServer->players->findOne(["uniqueId" => $v]);
                    if ($otherUser != null)
                    {
                        $data = [
                            'uniqueId' => $otherUser['uniqueId'],
                            'date' => date('Y-m-d H:i:s'),
                            'price' => 0,
                            'gateway' => 'Gain de la part de '.$value['receiver'],
                            'pseudo' => $otherUser['name'],
                            'points' => $doups
                        ];

                        $this->container->mongoUltra->funds->insertOne($data);

                        $resp = [
                            'name' => $otherUser['name'],
                            'date' => date("Y-m-d H:i:s")
                        ];

                        $this->container->mongoUltra->funds_logs->insertOne($resp);

                        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $otherUser['uniqueId']]);

                        if ($money == null)
                        {
                            $data = [
                                "uniqueId" => $otherUser['uniqueId'],
                                "points" => $doups
                            ];
                            $this->container->mongo->fund_list->insertOne($data);
                            $this->container->session->set('points', $doups);
                        }
                        else {
                            $money['points'] = $money['points'] + $doups;
                            $this->container->mongo->fund_list->updateOne(["uniqueId" => $otherUser['uniqueId']], ['$set' => ["points" => $money['points']]]);
                            $this->container->session->set('points', $money['points']);
                        }


                        try
                        {
                            $otherUser_xen = $this->xenforo->getUser($otherUser['name']);
                        }
                        catch (\Exception $e)
                        {
                            $otherUser_xen = null;
                        }

                        if ($otherUser_xen != null)
                        {
                            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-gain.html");
                            $mailContent = str_replace("(username)", $otherUser['name'], $mailContent);
                            $mailContent = str_replace("(gain)", $doups, $mailContent);
                            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                            $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                            $mail = new \App\Mail(true);
                            $mail->sendMail($otherUser_xen["email"], "Vous avez reçu de l'argent", $mailContent);
                        }

                        $mailContent = $name." gagne +".$doups." pts boutique (par ".$user['name'].")";
                        $mail = new \App\Mail(true);
                        $mail->sendMail("xmalware2@gmail.com", "BadBlock - Gain Parrainage", $mailContent);
                    }
                }

                $user = $this->xenforo->getUser($name);
                $dzb = "";
                if ($user != null) {
                    $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($user["email"], "BadBlock - Paiement effectué", $mailContent);
                }
                else
                {
                    $dzb .= "§eNous t'invitons à t'inscrire sur le site de BadBlock pour avoir une facture et une preuve d'achat la prochaine fois.";
                }

                $mailContent = $name." recharge +".$dedipass->virtual_currency." pts boutique (".$dedipass->payout." € - dedipass en jeu)";
                $mail = new \App\Mail(true);
                $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);
                $dzb .= "§bUn mail a été envoyé à §e".$user['email']." §bcontenant la facture et ta preuve d'achat en cas de problème.";

                echo "§a§lCode valide. Vous avez été crédité de ".$dedipass->virtual_currency." points boutiques. ".$dzb;
            }
            else {
                echo "§cCode entré invalide.";
            }
        }
    }

}