<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class ShopController extends Controller
{
	public function getHome(ServerRequestInterface $request, ResponseInterface $response)
	{
		//get twig of home
        $serverlist = $this->redis->getJson('shop.listsrv');

        var_dump($serverlist[0]);

        $this->render($response, 'shop.home',['serverlist' => $serverlist]);

	}

	public function getRecharge(ServerRequestInterface $request, ResponseInterface $response)
	{
		$nbStep = 4;
		if (isset($_GET['step'])) {
			$step = $_GET['step'];
		} else {
			$step = 1;
		}
		$priceUniq = 0.01;
		//get twig of recharge
		$this->render($response, "shop.recharge.step{$step}", [
			'width' => 100/$nbStep * $step,
			'step' => $step,
			'price' => $priceUniq
		]);
	}

	public function getNextStepRecharge(ServerRequestInterface $request, ResponseInterface $response)
	{
		if (isset($_GET['step'])) {
			$step = $_GET['step'];
		} else {
			$step = 1;
		}
		switch ($step) {
			case 1:
				//save in session the choice
				$this->session->set('recharge', [
							'amount' => $_GET['amount']
						]);

				//redirect to second step
				return $this->redirect($response, $this->pathFor('shop.recharge') . '?step=2');
				break;
			case 2:
				//save in session the username
				$this->session->set('recharge', array_merge(
					$this->session->get('recharge'), ['username' => $_GET['username']])
				);

				//redirect to third step
				return $this->redirect($response, $this->pathFor('shop.recharge') . '?step=3');
				break;

			case 3:
				//save in session the payway
				$this->session->set('recharge', array_merge(
						$this->session->get('recharge'), ['payway' => $_GET['payway']])
				);
				$recharge = array_merge($this->session->get('recharge'), ['payway' => $_GET['payway']]);
				//redirect to start page
				return $this->redirect($response, $this->pathFor('shop.recharge.start', [
					'amount' => $recharge['amount'],
					'username' => $recharge['username'],
					'payway' => $recharge['payway']
				]));
				break;
		}
	}

	public function getRechargeStart(ServerRequestInterface $request, ResponseInterface $response, $args)
	{
		$recharge = $this->session->get('recharge');
		//get price of 1
		$priceUniq = 0.01;
		$price = $priceUniq * $recharge['amount'];
		switch ($recharge['payway']){
			case 'paypal':
				//invoke paypal and generate url
				return $this->redirect($response, $this->container->paypal->setExpressCheckout([
					[
						'name' => "Recharge de {$recharge['amount']}",
						'description' => "",
						'price' => $price,
						'count' => 1
					]
				], [
					'RETURNURL' => $this->container->config['base_url'] . $this->pathFor('shop.paypal.execute'),
					'CANCELURL' => $this->container->config['base_url'] . $this->pathFor('shop.paypal.cancel'),
					'PAYMENTREQUEST_0_AMT' => $price, //TTC+PORT
					'PAYMENTREQUEST_0_CURRENCYCODE' => 'EUR',
					'PAYMENTREQUEST_0_SHIPPINGAMT' => 0, //SHIPPING PRICE
					'PAYMENTREQUEST_0_ITEMAMT' => $price, //TTC
				]));
				break;

			default:
				return $response->write('Payment way not found')->withStatus(404);
				break;
		}
	}



	public function getachat(ServerRequestInterface $request, ResponseInterface $response, $args){

        var_dump($args);


    }





}