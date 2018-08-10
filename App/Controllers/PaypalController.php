<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\RequestInterface;
use App\Paypal;

class PaypalController extends Controller
{
    public function startPaiement(RequestInterface $request, ResponseInterface $response,$id){
        //Search offer in array

        if (!isset($this->container->config['paiement']['paypal']['offer'][$id])){
            return $this->redirect($response, '/shop/recharge');
        }else{
            $offer = $this->container->config['paiement']['paypal']['offer'][$id];
        }

        $produit = array();

        $produit['Paypal']['Url'] = 'https://badblock.fr';                // URL de votre site
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';            // Redirection après payement
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';              // Redirection en cas d'annulation
        $produit['Paypal']['Prix'] = $offer['price'];                                // Prix de votre produit (doit etre en format XX.X, ex: 60.0 ou 19.99)
        $produit['Paypal']['OfferID'] = "Rechargement";                        // Donnez un id unique à votre offre sans espaces
        $produit['Paypal']['Offer'] = 'Rechagement Points Boutique';                    // Nom de votre produit( sera afficher sur paypal )
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
            $paypal = 'https://www.sandbox.paypal.com/websrc?cmd=_express-checkout&useraction=commit&token='.$response['TOKEN'];
            return $this->redirect($response, $paypal);
        }else{
            return $this->redirect($response, '/shop/recharge');
        }
    }


    public function process(RequestInterface $request, ResponseInterface $response){

        if(empty($_GET['offer']) || empty($_GET['Prix']) || empty($_GET['Offer']) || empty($_GET['Offer_desc']) || empty($_GET['Currency']) || empty($_GET['QTY'])){
            return $this->redirect($response, '/shop/recharge');
        }

        $produit = array();
        $produit['Paypal']['Prix'] = $_GET['Prix'];
        $produit['Paypal']['OfferID'] = $_GET['OfferID'];
        $produit['Paypal']['Offer'] = $_GET['Offer'];
        $produit['Paypal']['Offer_desc'] = $_GET['Offer_desc'];
        $produit['Paypal']['Currency'] = $_GET['Currency'];
        $produit['Paypal']['QTY'] = $_GET['QTY'];

        if(!isset($_GET['token']) || empty($_GET['token']) || !isset($_GET['PayerID']) || empty($_GET['PayerID'])){
            return $this->redirect($response, '/shop/recharge');
        }

        $paypal = new Paypal();
        $response = $this->request('GetExpressCheckoutDetails', array(
            'TOKEN' => $_GET['token']
        ));

        if($response){
            if($response['CHECKOUTSTATUS'] == 'PaymentActionCompleted'){
                // Détéction du payement
                return $this->redirect($response, '/shop/recharge');
            }
        }else{
            return $this->redirect($response, '/shop/recharge');
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
            $this->container->mongo->funds_logs->insertOne([$response]);

            return $this->redirect($response, '/shop/recharge/sucess');
        }else{
            return $this->redirect($response, '/shop/recharge');
        }

    }



}