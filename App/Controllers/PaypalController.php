<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\RequestInterface;

class PaypalController extends Controller
{
	public  function index(RequestInterface $request, ResponseInterface $response){

	    dd($this->container->config['paypal']);
    }


    public  function execute(RequestInterface $request, ResponseInterface $response, $id){
	    $id = $id['id'];

	    if (!isset($this->container->config['paiement']['paypal'][$id])){
	        return $this->redirect($response, "/shop/recharge");
        }

        // After Step 1
        $apiContext = new \PayPal\Rest\ApiContext(
            new \PayPal\Auth\OAuthTokenCredential(
                'AXckGcSUbDIlHxQ-_RI4QCqU6OHrlhhhpxj3880-SJGqiJr4hAWVV-SC58TS_S6eoQ721sv5_rpGT77s',     // ClientID
                'EJBa4GGjXQR1SRltECYczEU4huzwpTUPUGyeuJ52DZmP9e2NAzDGL1-DSKfzTk4ZgaYX8z_4FsdIgSxv'      // ClientSecret
            )
        );

        // After Step 2
        $payer = new \PayPal\Api\Payer();
        $payer->setPaymentMethod('paypal');

        $amount = new \PayPal\Api\Amount();
        $amount->setTotal('1.00');
        $amount->setCurrency('EUR');

        $transaction = new \PayPal\Api\Transaction();
        $transaction->setAmount($amount);

        $redirectUrls = new \PayPal\Api\RedirectUrls();
        $redirectUrls->setReturnUrl("https://badblock.fr/recharge/succes")
            ->setCancelUrl("https://badblock.fr/recharge/cancel");

        $payment = new \PayPal\Api\Payment();
        $payment->setIntent('sale')
            ->setPayer($payer)
            ->setTransactions(array($transaction))
            ->setRedirectUrls($redirectUrls);

        // After Step 3
        try {
            $payment->create($apiContext);

            return $this->redirect($response,$payment->getApprovalLink());
        }
        catch (\PayPal\Exception\PayPalConnectionException $ex) {
            // This will print the detailed information on the exception.
            //REALLY HELPFUL FOR DEBUGGING
            echo $ex->getData();
        }

    }

    public function ipn(RequestInterface $request, ResponseInterface $response){

	    if (\PaypalIPN::ver)





    }

}