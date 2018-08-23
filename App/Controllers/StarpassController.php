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
        $this->render($response,'shop.recharge.starpass', []);
    }

    public function showDocument(RequestInterface $request, ResponseInterface $response, $documentId){
        $documentId = intval($documentId);

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
        $documentId = intval($id);

        if (!$this->container->session->exist('recharge-username'))
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
            return $this->redirect($response, '/shop/recharge/cancel');
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

        $ident=urlencode($offer['private_id'].';;'.$offer['document_id']);
        $codes=urlencode($codes);
        $datas=urlencode($datas);

        $get_f=@file( "http://script.starpass.fr/check_php.php?ident=$ident&codes=$codes&DATAS=$datas" );

        if(!$get_f)
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

        $name = $this->container->session->get('recharge-username');
        // Sauvegarde dans mongoDB
        $date = date('Y-m-d H:i:s');

        $dat = [$date,$datas, $pays, $palier, $id_palier, $type];
        $insertedId = $this->container->mongo->funds_logs->insert($dat);
        var_dump($insertedId);
        exit;
        return;

        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);
        $data = [
            'uniqueId' => $user['uniqueId'],
            'date' => date('Y-m-d H:i:s'),
            'price' => $palier,
            'gateway' => 'starpass',
            'pseudo' => $name,
            'points' => $virtual_currency,
            'transaction_id' => $datas
        ];

        $this->container->mongo->funds->insertOne($data);
        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);
        if ($money == null){
            $data = [
                "uniqueId" => $user['uniqueId'],
                "points" => $virtual_currency
            ];
            $this->container->mongo->fund_list->insertOne($data);
            $this->container->session->set('points', $virtual_currency);
        }else{
            $money['points'] = $money['points'] + $virtual_currency;
            $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
            $this->container->session->set('points', $money['points']);
        }

        if ($this->container->session->exist('user')){
            $mailContent = file_get_contents("https://badblock.fr/dist/mails/mail-achat.html");
            $mailContent = str_replace("(username)", $this->container->session->get('recharge-username'), $mailContent);
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            $mailContent = str_replace("(lien)", "test", $mailContent);
            $mail = new \App\Mail(true);
            $mail->sendMail($this->session->get('user')["email"], "BadBlock - Paiement effectué", $mailContent);
        }

        return $this->redirect($response, '/shop/recharge/success');
    }

}