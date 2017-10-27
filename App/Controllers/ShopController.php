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



	public function getachat(ServerRequestInterface $request, ResponseInterface $response,$args){
	    //Vérification si le produit éxiste
            if (isset($args['id'])&!empty($args['id'])&$this->redis->exists('shop.prod.'.$args['id'])){

                $collection = $this->mongo->badblock->dat_users;
                $data = $collection->findOne(['name' => $this->session->getProfile('username')['username']]);
                $dataprod = $this->redis->getjson('shop.prod.'.$args['id']);

                //Vérification du prix
                if ($dataprod["price"] <= $data['shop_points']){
                    //On continue car il a les sous
                    //vérifions si le joueur est connecté au serveur
                    if ($this->ladder->playerOnline($this->session->getProfile('username')['username'])['connected'] == true){
                            //vérification si il est sur le bon serveur
                            if ($this->ladder->playerGetConnectedServer($this->session->getProfile('username')['username'])->server == $dataprod['server_name']){
                                //Vérification si l'achat et réel ou virtuel
                                if ($dataprod['type'] == "item"){
                                    //Vérif anti usebug
                                    if ($dataprod["price"] <= $data['shop_points']){
                                        //On retire les sous
                                        $pts = $data['shop_points'] - $dataprod["price"];
                                        $result = $collection->findOneAndUpdate(['name' => $this->session->getProfile('username')['username']],['$set' => ["shop_points" => $pts]]);
                                        //Real / Item
                                        //Send alert to the serveur
                                        $this->ladder->serverBroadcast($dataprod['server_name'],$this->session->getProfile('username')['username']." vient d'acheter ".$dataprod['qty']." ".$dataprod['name']);
                                        return $response->write("ok")->withStatus(200);
                                    }else{
                                        return $response->write("Not enought")->withStatus(406);
                                    }
                                }elseif ($dataprod['type'] == "grade"){
                                    //Vérif anti usebug
                                    if ($dataprod["price"] <= $data['shop_points']){
                                        //On retire les sous
                                        $pts = $data['shop_points'] - $dataprod["price"];
                                        $result = $collection->findOneAndUpdate(['name' => $this->session->getProfile('username')['username']],['$set' => ["shop_points" => $pts]]);
                                        //Real / Item
                                        //Send alert to the serveur
                                        $this->ladder->playerAddGroup($dataprod['server_name'],$this->session->getProfile('username')['username'],"diamant");
                                        $this->ladder->serverBroadcast("hub_15","essaie");
                                        return $response->write("ok")->withStatus(200);
                                    }else{
                                        return $response->write("Not enought")->withStatus(406);
                                    }
                                }else{
                                    //Vérif anti usebug
                                    if ($dataprod["price"] <= $data['shop_points']){
                                        //On retire les sous
                                        $pts = $data['shop_points'] - $dataprod["price"];
                                        $result = $collection->findOneAndUpdate(['name' => $this->session->getProfile('username')['username']],['$set' => ["shop_points" => $pts]]);

                                        //Virtuel grade / kitc
                                        //Send alert to the serveur
                                        $this->ladder->serverBroadcast($dataprod['server_name'],$this->session->getProfile('username')['username']." vient d'acheter le ".$dataprod['name']);
                                        return $response->write("ok")->withStatus(200);
                                    }else{
                                        return $response->write("Not enought")->withStatus(406);
                                    }
                                }
                            }else{
                                return $response->write("Not connected")->withStatus(409);
                            }
                    }else{
                        return $response->write("Not connected")->withStatus(409);
                    }
                }else{
                    //C'est un clodo donc erreur
                    return $response->write("Not enought")->withStatus(406);
                }
            }else{
                return $response->write("Product not found")->withStatus(404);
            }


    }





}