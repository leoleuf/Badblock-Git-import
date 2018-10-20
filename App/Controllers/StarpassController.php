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

class StarpassController extends Controller
{


    public function index(RequestInterface $request, ResponseInterface $response){
        $cp = 1;
        $codepromo = "";
        $percentpromo = "";
        if ($this->container->session->exist('recharge-codepromo')) {
            $cp = $this->session->get('recharge-codepromo');
            $cp = strtolower($cp);
            if (isset($this->container->codepromo[$cp]))
            {
                $codepromo = strtoupper($cp);
                $percentpromo = intval($this->container->codepromo[$cp]);
                $cp = 1 + (intval($this->container->codepromo[$cp]) / 100);
            }
            else
            {
                $cp = 1;
            }
        }
        $this->render($response,'shop.recharge.starpass', ['cp' => $cp, 'codepromo' => $codepromo, 'percentpromo' => $percentpromo]);
    }

    public function showDocument(RequestInterface $request, ResponseInterface $response, $documentId){
        $documentId = $documentId['id'];

        if (!isset($this->container->config['paiement'][1]['offer'][$documentId]))
        {
            return $this->redirect($response, '/shop/recharge');
        }
        else
        {
            $offer = $this->container->config['paiement'][1]['offer'][$documentId];
        }

        return $this->render($response, 'shop.recharge.starpass-process', ['documentId' => $offer['document_id']]);
    }

    public function process(RequestInterface $request, ResponseInterface $response, $id)
    {
        $documentId = intval($id['id']);

        if (!$this->container->session->exist('recharge-username'))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        $name = $this->container->session->get('recharge-username');

        if (empty($name))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        if (!isset($this->container->config['paiement'][1]['offer'][$documentId]))
        {
            return $this->redirect($response, '/shop/recharge');
        }
        else
        {
            $offer = $this->container->config['paiement'][1]['offer'][$documentId];
        }

        if ($offer == null)
        {
            //return $this->redirect($response, '/shop/recharge/cancel');
        }

        // moche, merci starpass
        $ident=$idp=$ids=$idd=$codes=$code1=$code2=$code3=$code4=$code5=$datas='';

        if(isset($_POST['code1'])){$code1 = $_POST['code1'];}
        if(isset($_POST['code2'])) $code2 = ";".$_POST['code2'];
        if(isset($_POST['code3'])) $code3 = ";".$_POST['code3'];
        if(isset($_POST['code4'])) $code4 = ";".$_POST['code4'];
        if(isset($_POST['code5'])) $code5 = ";".$_POST['code5'];
        $codes=$code1.$code2.$code3.$code4.$code5;
        if(isset($_POST['DATAS'])) $datas = $_POST['DATAS'];

        $ccodes = $codes;
        $codes=urlencode($codes);
        $datas=urlencode($datas);



        $get_f=@file( "http://script.starpass.fr/check_php.php?ident=". $offer['private_id'] . ";;". $offer['document_id'] ."&codes=$codes&DATAS=$datas" );
        
        if (!$get_f)
        {
            exit( "Votre serveur n'a pas accès au serveur de StarPass, merci de contacter votre hébergeur. " );
        }

        $tab = explode("|",$get_f[0]);

        $pays = $tab[2];
        $palier = urldecode($tab[3]);
        $id_palier = urldecode($tab[4]);
        $type = urldecode($tab[5]);

        if( substr($tab[0],0,3) != "OUI" )
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        $virtual_currency = $offer['points'];

        $codepromo = "NULL";
        $cp = 1;
        if ($this->container->session->exist('recharge-codepromo')) {
            $cp = $this->session->get('recharge-codepromo');
            $cp = strtolower($cp);
            if (isset($this->container->codepromo[$cp]))
            {
                $codepromo = $cp;
                $cp = 1 + (intval($this->container->codepromo[$cp]) / 100);
            }
            else
            {
                $cp = 1;
            }
        }

        $virtual_currency *= $cp;

        $name = $this->container->session->get('recharge-username');
        // Sauvegarde dans mongoDB
        $date = date('Y-m-d H:i:s');

        $dat = [$date,$datas, $pays, $palier, $id_palier, $type];

        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);

        $data = [
            'uniqueId' => $user['uniqueId'],
            'date' => date('Y-m-d H:i:s'),
            'price' => intval($offer['price']),
            'gateway' => 'starpass',
            'pseudo' => $name,
            'points' => $virtual_currency,
            'transaction_id' => $ccodes
        ];

        $insertedId = $this->container->mongoUltra->funds->insertOne($data);
        $insertedId = $insertedId->getInsertedId()->__ToString();

        $resp = [
            'name' => $name,
            'date' => date("Y-m-d H:i:s")
        ];

        $this->container->mongoUltra->funds_logs->insertOne($resp);

        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);

        if ($money == null)
        {
            $data = [
                "uniqueId" => $user['uniqueId'],
                "points" => $virtual_currency
            ];
            $this->container->mongo->fund_list->insertOne($data);
            $this->container->session->set('points', $virtual_currency);
        }
        else
        {
            $money['points'] = $money['points'] + $virtual_currency;
            $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
            $this->container->session->set('points', $money['points']);
        }

        $doups = intval($virtual_currency * 0.1);
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

        try
        {
            $user = $this->xenforo->getUser($name);
        }
        catch (\Exception $e)
        {
            $user = null;
        }

        if ($user != null)
        {
            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-achat.html");
            $mailContent = str_replace("(username)", $this->container->session->get('recharge-username'), $mailContent);
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            $mailContent = str_replace("(lien)", $insertedId, $mailContent);
            $mail = new \App\Mail(true);
            $mail->sendMail($user["email"], "BadBlock - Paiement effectué", $mailContent);
        }

        $mailContent = $name." recharge +".$virtual_currency." pts boutique (".$offer['price']." € - starpass - code promo : ".$codepromo." - codes : ".$ccodes.")";
        $mail = new \App\Mail(true);
        $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);

        return $this->redirect($response, '/shop/recharge/success');
    }

}