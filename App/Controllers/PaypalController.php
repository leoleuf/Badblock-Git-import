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

        if (!isset($this->container->config['paiement'][0]['offer'][$id])){
            return $this->redirect($response, '/shop/recharge/test-1');
        }else{
            $offer = $this->container->config['paiement'][0]['offer'][$id];
        }

        $produit = array();

        $produit['Paypal']['Url'] = 'https://badblock.fr';                // URL de votre site
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';            // Redirection après payement
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';              // Redirection en cas d'annulation
        $produit['Paypal']['Prix'] = $offer['price'];                                // Prix de votre produit (doit etre en format XX.X, ex: 60.0 ou 19.99)
        $produit['Paypal']['OfferID'] = $id;                        // Donnez un id unique à votre offre sans espaces
        $produit['Paypal']['Offer'] = 'Rechargement Points Boutique';                    // Nom de votre produit( sera afficher sur paypal )
        $produit['Paypal']['Offer_desc'] = 'Rechargement de '. $offer['points'] . 'points boutique';    // Offre de votre produit.
        $produit['Paypal']['Currency'] = 'EUR';                           // Code de votre monnaie( en majuscule ).
        $produit['Paypal']['QTY'] = 1;                                    // Quantité( 1 par défault )( Le prix sera multiplié par la quantité).

        $paypal = new Paypal();

        $params = array(
            'RETURNURL' => $produit['Paypal']['Url'].$produit['Paypal']['Process'].'?offer='.$produit['Paypal']['OfferID'].'&Prix='.$produit['Paypal']['Prix'].'&Offer='.$produit['Paypal']['Offer'].'&Offer_desc='.$produit['Paypal']['Offer_desc'].'&Currency='.$produit['Paypal']['Currency'].'&QTY='.$produit['Paypal']['QTY'],
            'CANCELURL' => $produit['Paypal']['Url'].$produit['Paypal']['Cancel'],

            'PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_CURRENCYCODE' => $produit['Paypal']['Currency'],
            'PAYMENTREQUEST_0_SHIPPINGAMT' => 0.0,
            'PAYMENTREQUEST_0_IMTEMAMT' => $produit['Paypal']['Prix'],

            'L_PAYMENT_PAYMENTREQUEST_0_NAME' => $produit['Paypal']['Offer'],
            'L_PAYMENT_PAYMENTREQUEST_0_DESC' => $produit['Paypal']['Offer_desc'],
            'L_PAYMENT_PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'L_PAYMENT_PAYMENTREQUEST_0_QTY' => $produit['Paypal']['QTY']

        );

        $resp = $paypal->request('SetExpressCheckout', $params);

        if($resp){
            // Remplacer www.paypal.com par www.sandbox.paypal.com pour utiliser la sandbox
            $link = 'https://www.paypal.com/websrc?cmd=_express-checkout&useraction=commit&token='.$resp['TOKEN'];
            return $this->redirect($response, $link);
        }else{
            var_dump($paypal->errors);
            exit;
            return;
        }
    }


    public function process(RequestInterface $request, ResponseInterface $response){
        if(isset($_GET['offer']) || isset($_GET['Prix']) || isset($_GET['Offer']) || isset($_GET['Offer_desc']) || isset($_GET['Currency']) || isset($_GET['QTY'])){
            return $this->redirect($response, '/shop/recharge/fail-1-test');
        }

        $produit = array();
        $produit['Paypal']['Prix'] = $_GET['Prix'];
        $produit['Paypal']['OfferID'] = $_GET['offer'];
        $produit['Paypal']['Offer'] = $_GET['Offer'];
        $produit['Paypal']['Offer_desc'] = $_GET['Offer_desc'];
        $produit['Paypal']['Currency'] = $_GET['Currency'];
        $produit['Paypal']['QTY'] = $_GET['QTY'];
        $produit['Paypal']['Url'] = 'https://badblock.fr';
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';


        if(!isset($_GET['token']) || empty($_GET['token']) || !isset($_GET['PayerID']) || empty($_GET['PayerID'])){
            return $this->redirect($response, '/shop/recharge/fail-2');
        }

        $paypal = new Paypal();
        $resp = $paypal->request('GetExpressCheckoutDetails', array(
            'TOKEN' => $_GET['token']
        ));

        if($resp){
            if($resp['CHECKOUTSTATUS'] == 'PaymentActionCompleted'){
                // Détéction du payement
                return $this->redirect($response, '/shop/recharge/fail-3');
            }
        }else{
            return $this->redirect($response, '/shop/recharge/fail-4');
        }

        $params = array(
            'TOKEN' => $_GET['token'],
            'PAYERID' => $_GET['PayerID'],
            'PAYMENTACTION' => 'Sale',

            'RETURNURL' => $produit['Paypal']['Url'].$produit['Paypal']['Process'].'?offer='.$produit['Paypal']['OfferID'].'&Prix='.$produit['Paypal']['Prix'].'&Offer='.$produit['Paypal']['Offer'].'&Offer_desc='.$produit['Paypal']['Offer_desc'].'&Currency='.$produit['Paypal']['Currency'].'&QTY='.$produit['Paypal']['QTY'],
            'CANCELURL' => $produit['Paypal']['Url'].$produit['Paypal']['Cancel'],

            'PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_CURRENCYCODE' => $produit['Paypal']['Currency'],
            'PAYMENTREQUEST_0_SHIPPINGAMT' => 0.0,
            'PAYMENTREQUEST_0_IMTEMAMT' => $produit['Paypal']['Prix'],
            'PAYMENTREQUEST_0_SOFTDESCRIPTOR' => $produit['Paypal']['Offer_desc'],

            'L_PAYMENT_PAYMENTREQUEST_0_NAME' => $produit['Paypal']['Offer'],
            'L_PAYMENT_PAYMENTREQUEST_0_DESC' => $produit['Paypal']['Offer_desc'],
            'L_PAYMENT_PAYMENTREQUEST_0_AMT' => $produit['Paypal']['Prix'],
            'L_PAYMENT_PAYMENTREQUEST_0_QTY' => $produit['Paypal']['QTY']

        );

        $resp = $paypal->request('DoExpressCheckoutPayment', $params);

        if($resp){
            // Detection d'une quelconque action
            // Sauvegarde dans mongoDB
            $resp['name'] = strtolower($this->container->session->get('recharge-username'));
            $resp["date"] = date('Y-m-d H:i:s');
            $this->container->mongo->funds_logs->insertOne($resp);

            $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->container->session->get('recharge-username'))]);

            $data = [
                'uniqueId' => $user['uniqueId'],
                'date' => date('Y-m-d H:i:s'),
                'price' => $resp["PAYMENTINFO_0_AMT"],
                'gateway' => 'paypal',
                'pseudo' => $this->container->session->get('recharge-username'),
                'points' => $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points'],
                'transaction_id' => $resp['PAYMENTINFO_0_TRANSACTIONID']
            ];

            $this->container->mongo->funds->insertOne($data);

            $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);

            if ($money == null){
                $data = [
                    "uniqueId" => $user['uniqueId'],
                    "points" => $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points']
                ];

                $this->container->session->set('points', $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points']);
                $this->container->mongo->fund_list->insertOne($data);

            }else{
                $money['points'] = $money['points'] + $this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points'];
                $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
                $this->container->session->set('points', $money['points']);
            }


            if ($this->container->session->exist('user')){
                $mailContent = file_get_contents("../mail-achat.html");
                $mailContent = str_replace("(username)", $this->container->session->get('recharge-username'), $mailContent);
                $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                $mail = new \App\Mail(true);
                $mail->sendMail($this->session->get('user')["email"], "BadBlock - Rechargement", $mailContent);
            }

            return $this->redirect($response, '/shop/recharge/sucess');
        }else{
            return $this->redirect($response, '/shop/recharge');
        }

    }



}