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
        $produit['Paypal']['Offer'] = 'Rechargement Points Boutique';                    // Nom de votre produit( sera afficher sur paypal )
        $produit['Paypal']['Offer_desc'] = 'Rechargement de points boutique';    // Offre de votre produit.
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
            return $this->redirect($response, '/shop/recharge/cancel#6');
        }
    }


    public function process(RequestInterface $request, ResponseInterface $response){
        

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
        $produit['Paypal']['Offer'] = $_GET['Offer'];
        $produit['Paypal']['Offer_desc'] = $_GET['Offer_desc'];
        $produit['Paypal']['Currency'] = $_GET['Currency'];
        $produit['Paypal']['QTY'] = $_GET['QTY'];
        $produit['Paypal']['Url'] = 'https://badblock.fr';
        $produit['Paypal']['Process'] = '/shop/recharge/paypal-process';
        $produit['Paypal']['Cancel'] = '/shop/recharge/cancel';


        $paypal = new Paypal();
        $resp = $paypal->request('GetExpressCheckoutDetails', array(
            'TOKEN' => $_GET['token']
        ));

        if (!$resp){
            return var_dump($resp);
        }

        if($resp){
            if($resp['CHECKOUTSTATUS'] !== 'PaymentActionNotInitiated'){
                // oups?
                return $this->redirect($response, '/shop/recharge/cancel#1');
            }
        }else{
            return $this->redirect($response, '/shop/recharge/cancel#22');
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

            $mailContent = $this->session->get('recharge-username')." recharge +".$this->container->config['paiement'][0]['offer'][$produit['Paypal']['OfferID']]['points']." pts boutique (".$resp["PAYMENTINFO_0_AMT"]." € - paypal)";
            $mail = new \App\Mail($this->container);
            $mail->sendMail("xmalware2@gmail.com", "BadBlock - Rechargement", $mailContent);

            return $this->redirect($response, '/shop/recharge/success');
        }else{
            return $this->redirect($response, '/shop/recharge');
        }

    }



}