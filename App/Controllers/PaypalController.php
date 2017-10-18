<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class PaypalController extends Controller
{
	public function getPaypalExecute(ServerRequestInterface $request, ResponseInterface $response)
	{
		$recharge = $this->session->get('recharge');
		$priceUniq = 0.01;
		$price = $priceUniq * $recharge['amount'];
		$paypalResponse = $this->container->paypal->doExpressCheckoutPayment([
			[
				'name' => "Recharge de {$recharge['amount']}",
				'description' => "",
				'price' => $price,
				'count' => 1
			]
		], [
			'TOKEN' => $_GET['token'],
			'PAYERID' => $_GET['PayerID'],
			'PAYMENTACTION' => 'Sale',
			'PAYMENTREQUEST_0_AMT' => $price,
			'PAYMENTREQUEST_0_CURRENCYCODE' => 'EUR',
			'PAYMENTREQUEST_0_SHIPPINGAMT' => 0,
			'PAYMENTREQUEST_0_ITEMAMT' => $price
		]);
		if ($paypalResponse['ACK'] == "Success"){
			//payment success
			$this->log->info('New paypal payment success!');
			dd('success');
			//TODO: Redirect to success page
		}else {
			$this->log->error('New paypal payment abort!');
			//TODO: Show an error page
			return $response->write('Error while read paypal api response: you payment is rejected. The token is bad or the order is bad, please contact our team support@badblock.fr')->withStatus(500);
		}
	}

	public function getPaypalCancel(ServerRequestInterface $request, ResponseInterface $response)
	{
		//TODO: Show an error page
		dd('cancel');
	}
}