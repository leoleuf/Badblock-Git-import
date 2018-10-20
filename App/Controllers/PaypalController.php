<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\RequestInterface;
use App\Paypal;
use App\Controllers\CreditController;

class PaypalController extends Controller
{
    public function startPaiement(RequestInterface $request, ResponseInterface $response,$id){
        //Search offer in array
        $id = $id['id'];

        if (!$this->container->session->exist('recharge-username'))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        $name = $this->container->session->get('recharge-username');

        if (empty($name))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        if (!isset($this->container->config['paiement'][0]['offer'][$id])){
            return $this->redirect($response, '/shop/recharge/cancel#5');
        }else{
            $offer = $this->container->config['paiement'][0]['offer'][$id];
        }

        $produit = array();

        $produit['Paypal']['Url'] = 'https://badblock.fr';                // URL de votre site
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';            // Redirection après payement
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';              // Redirection en cas d'annulation
        $produit['Paypal']['Prix'] = $offer['price'];                                // Prix de votre produit (doit etre en format XX.X, ex: 60.0 ou 19.99)
        $produit['Paypal']['OfferID'] = $id;                        // Donnez un id unique à votre offre sans espaces
        $produit['Paypal']['Offer'] = 'FluorL';
        $produit['Paypal']['Offer_desc'] = 'Fluorl';
        $produit['Paypal']['Currency'] = 'EUR';                           // Code de votre monnaie( en majuscule ).
        $produit['Paypal']['QTY'] = 1;                                    // Quantité( 1 par défault )( Le prix sera multiplié par la quantité).

        $paypal = new Paypal();

        $params = array(
            'RETURNURL' => $produit['Paypal']['Url'].$produit['Paypal']['Process'].'?offer='.$produit['Paypal']['OfferID'].'&Prix='.$produit['Paypal']['Prix'].'&Offer='.$produit['Paypal']['Offer'].'&Offer_desc='.$produit['Paypal']['Offer_desc'].'&Currency='.$produit['Paypal']['Currency'].'&QTY='.$produit['Paypal']['QTY'],
            'CANCELURL' => $produit['Paypal']['Url'].$produit['Paypal']['Cancel'],

            'PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_CURRENCYCODE' => $produit['Paypal']['Currency'],
            'PAYMENTREQUEST_0_SHIPPINGAMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_SOFTDESCRIPTOR' => $produit['Paypal']['Offer_desc'],
            'PAYMENTREQUEST_0_DESC' => $name . ' - Rechargement PB - ' . $offer['points'],
            'PAYMENTREQUEST_0_CUSTOM' => $name . ' - Rechargement PB - ' . $offer['points'],

            'NOTETOBUYER' => 'Vos points boutique seront livrés en moins de 5 minutes, en cas de problème lors du paiement merci d\'ouvrir un ticket sur https://badblock.fr/forum/support/open'
        );


        $resp = $paypal->request('SetExpressCheckout', $params);

        if($resp){
            // Remplacer www.paypal.com par www.sandbox.paypal.com pour utiliser la sandbox
            $link = 'https://www.paypal.com/websrc?cmd=_express-checkout&useraction=commit&token='.$resp['TOKEN'];
            return $this->redirect($response, $link);
        }else{
            return $this->redirect($response, '/shop/recharge/cancel#6');
        }
    }


    public function process(RequestInterface $request, ResponseInterface $response){
        if(!isset($_GET['offer']) || !isset($_GET['Prix']) || !isset($_GET['Offer']) || !isset($_GET['Offer_desc']) || !isset($_GET['Currency']) || !isset($_GET['QTY'])){
            return $this->redirect($response, '/shop/recharge/cancel#4');
        }

        if (!isset($this->container->config['paiement'][0]['offer'][$_GET['offer']])){
            return $this->redirect($response, '/shop/recharge/cancel#5');
        }else{
            $offer = $this->container->config['paiement'][0]['offer'][$_GET['offer']];
        }

        if (!$this->container->session->exist('recharge-username'))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        $name = $this->container->session->get('recharge-username');

        if (empty($name))
        {
            return $this->redirect($response, '/shop/recharge/cancel');
        }

        $produit = array();
        $produit['Paypal']['Prix'] = $_GET['Prix'];
        $produit['Paypal']['OfferID'] = $_GET['offer'];
        $produit['Paypal']['Offer'] = 'FluorL';                    // Nom de votre produit( sera afficher sur paypal )
        $produit['Paypal']['Offer_desc'] = 'Fluorl';
        $produit['Paypal']['Currency'] = $_GET['Currency'];
        $produit['Paypal']['QTY'] = $_GET['QTY'];
        $produit['Paypal']['Url'] = 'https://badblock.fr';
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';


        if(!isset($_GET['token']) || empty($_GET['token']) || !isset($_GET['PayerID']) || empty($_GET['PayerID'])){
            return $this->redirect($response, '/shop/recharge/cancel#3');
        }

        $paypal = new Paypal();
        $resp = $paypal->request('GetExpressCheckoutDetails', array(
            'TOKEN' => $_GET['token']
        ));

        if($resp){
            if($resp['CHECKOUTSTATUS'] !== 'PaymentActionNotInitiated'){
                // oups?
                return $this->redirect($response, '/shop/recharge/cancel#1');
            }
        }else{
            return $this->redirect($response, '/shop/recharge/cancel#2');
        }

        $params = array(
            'TOKEN' => $_GET['token'],
            'PAYERID' => $_GET['PayerID'],
            'PAYMENTACTION' => 'Sale',

            'RETURNURL' => $produit['Paypal']['Url'].$produit['Paypal']['Process'].'?offer='.$produit['Paypal']['OfferID'].'&Prix='.$produit['Paypal']['Prix'].'&Offer='.$produit['Paypal']['Offer'].'&Offer_desc='.$produit['Paypal']['Offer_desc'].'&Currency='.$produit['Paypal']['Currency'].'&QTY='.$produit['Paypal']['QTY'],
            'CANCELURL' => $produit['Paypal']['Url'].$produit['Paypal']['Cancel'],

            'PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_CURRENCYCODE' => $produit['Paypal']['Currency'],
            'PAYMENTREQUEST_0_SHIPPINGAMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_SOFTDESCRIPTOR' => $produit['Paypal']['Offer_desc'],
            'PAYMENTREQUEST_0_DESC' => $name . ' - Rechargement PB - ' . $offer['points'],
            'PAYMENTREQUEST_0_CUSTOM' => $name . ' - Rechargement PB - ' . $offer['points'],

            'NOTETOBUYER' => 'Vos points boutique seront livrés en moins de 5 minutes, en cas de problème lors du paiement merci d\'ouvrir un ticket sur https://badblock.fr/forum/support/open'
        );

        $resp = $paypal->request('DoExpressCheckoutPayment', $params);

        if($resp){
            if (!isset($resp['ACK']) || $resp['ACK'] !== 'Success')
            {
                return $this->redirect($response, '/shop/recharge/cancel#9');
            }

            // Detection d'une quelconque action
            // Sauvegarde dans mongoDB
            $resp['name'] = strtolower($this->container->session->get('recharge-username'));
            $resp["date"] = date('Y-m-d H:i:s');
            $this->container->mongoUltra->funds_logs->insertOne($resp);

            $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->container->session->get('recharge-username'))]);

            $data = [
                'uniqueId' => $user['uniqueId'],
                'date' => date('Y-m-d H:i:s'),
                'price' => intval($resp["PAYMENTINFO_0_AMT"]),
                'gateway' => 'paypal',
                'pseudo' => $this->container->session->get('recharge-username'),
                'points' => $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points'],
                'transaction_id' => $resp['PAYMENTINFO_0_TRANSACTIONID']
            ];

            $insertedId = $this->container->mongoUltra->funds->insertOne($data);
            $insertedId = $insertedId->getInsertedId()->__ToString();

            $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);

            $cp = 1;
            $codepromo = "NULL";
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

            $ptstoadd = $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points'];
            $ptstoadd *= $cp;

            if ($money == null){
                $data = [
                    "uniqueId" => $user['uniqueId'],
                    "points" => $ptstoadd
                ];

                $this->container->session->set('points', $ptstoadd);
                $this->container->mongo->fund_list->insertOne($data);

            }else{
                $money['points'] = $money['points'] + $ptstoadd;
                $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
                $this->container->session->set('points', $money['points']);
            }

            $doups = intval($ptstoadd * 0.1);
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
                $user = $this->xenforo->getUser($this->container->session->get('recharge-username'));
            }catch (\Exception $e){
                $user = null;
            }

            if ($user != null) {
                $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-achat.html");
                $mailContent = str_replace("(username)", $this->container->session->get('recharge-username'), $mailContent);
                $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                $mailContent = str_replace("(lien)", $insertedId, $mailContent);
                $mail = new \App\Mail($this->container);
                $mail->sendMail($user["email"], "BadBlock - Paiement effectué", $mailContent);
            }

            $mailContent = $this->session->get('recharge-username')." recharge +".$ptstoadd." pts boutique (".$resp["PAYMENTINFO_0_AMT"]." € - paypal - Code promo : ".$codepromo.")";
            $mail = new \App\Mail($this->container);
            $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);

            return $this->redirect($response, '/shop/recharge/success');
        }else{
            return $this->redirect($response, '/shop/recharge');
        }

    }



}