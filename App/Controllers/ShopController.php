<?php

namespace App\Controllers;

use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

/**
 * Class ShopController
 * @package App\Controllers
 */
class ShopController extends Controller
{
	public function getHome(ServerRequestInterface $request, ResponseInterface $response)
	{

		//get twig of home
		$serverlist = $this->redis->getJson('shop.listsrv');


		//On vérifie si il ya des promos
		$itempromo = $this->redis->getJson('shop.promo');
		if (count($itempromo) == 0) {
			$itempromo = false;
		}

		$this->render($response, 'shop.home', ['serverlist' => $serverlist, 'promo' => $itempromo]);

	}

	public function getRecharge(ServerRequestInterface $request, ResponseInterface $response)
	{
		$nbStep = 4;
		if (isset($_GET['step'])) {
			$step = $_GET['step'];
		} else {
			$step = 1;
		}

		if ($this->session->exist('recharge')) {
			$recharge = $this->session->get('recharge');
		} else {
			$recharge = [];
		}
		//get twig of recharge
		$this->render($response, "shop.recharge.step{$step}", [
			'width' => 100 / $nbStep * $step,
			'step' => $step,
			'payways' => $this->container['config']['shop']['payways'],
			'recharge' => $recharge
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
				//SAVING PAYWAY
				$this->session->set('recharge', ['payway' => $_GET['payway']]);

				//redirect to second step
				return $this->redirect($response, $this->pathFor('shop.recharge') . '?step=2');
				break;
			case 2:
				//SAVING AMOUNT
				//save in session the choice
				$this->session->set('recharge', array_merge(
						$this->session->get('recharge'), ['amount' => $_GET['amount']])
				);

				//redirect to third step
				return $this->redirect($response, $this->pathFor('shop.recharge') . '?step=3');
				break;

			case 3:
				//SAVING USERNAME
				//save in session the username
				$this->session->set('recharge', array_merge(
						$this->session->get('recharge'), ['username' => $_GET['username']])
				);

				$recharge = $this->session->get('recharge');

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

		if (isset($this->container['config']['shop']['payways'][$recharge['payway']]['table'][$recharge['amount']])) {
			$price = $this->container['config']['shop']['payways'][$recharge['payway']]['table'][$recharge['amount']];
		}else{
			$coef = $this->container['config']['shop']['payways'][$recharge['payway']]['coef'];
			$price = $coef * $recharge['amount'];
		}

		switch ($recharge['payway']) {
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


	public function getachat(ServerRequestInterface $request, ResponseInterface $response, $args)
	{
		//Vérification si le produit éxiste
		if (isset($args['id']) & !empty($args['id']) & $this->redis->exists('shop.prod.' . $args['id'])) {
			$collection = $this->mongo->test->players;
			$data = $collection->findOne(['name' => $this->session->getProfile('username')['username']]);
			$dataprod = $this->redis->getjson('shop.prod.' . $args['id']);


			//vérification si reduction
			if ($dataprod["promo"] == true) {
				$dataprod["price"] = $dataprod["price"] * ((100 + $dataprod["promo_reduc"]) / 100);;
			}


			if (!isset($data['shop_points'])) {
				$data['shop_points'] = 0;
			}

			//Vérification du prix
			if ($dataprod["price"] <= $data['shop_points']) {
				//On continue car il a les sous

			} else {
				//C'est un clodo donc erreur
				return $response->write("Not enought")->withStatus(406);
			}
		} else {
			return $response->write("Product not found")->withStatus(404);
		}


	}


}