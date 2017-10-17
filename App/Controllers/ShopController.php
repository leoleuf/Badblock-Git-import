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

        var_dump($serverlist);

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
		//get twig of recharge
		$this->render($response, "shop.recharge.step{$step}", [
			'width' => 100/$nbStep * $step,
			'step' => $step
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
		dd($this->session->get('recharge'));
	}
}