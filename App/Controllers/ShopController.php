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
        if(count($itempromo) == 0){
            $itempromo = false;
        }

        $this->render($response, 'shop.home',['serverlist' => $serverlist,'promo' => $itempromo]);

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
	    //On vérifie si le joueur est connecté
        if(!$this->container->session->exist('user')){
            return $response->write("Not connected")->withStatus(403);
        }else{
            if (in_array(17, $this->container->session->getProfile("user")['secondary_group_ids'])){
                if (isset($args['id'])&!empty($args['id'])&$this->redis->exists('shop.prod.'.$args['id'])){
                    //Vérification si le produit éxiste
                    $collection = $this->container->mongoServer->test->players;
                    $data = $collection->findOne(['name' => $this->session->getProfile('username')['username']]);
                    $dataprod = $this->redis->getjson('shop.prod.'.$args['id']);
                    //vérification si reduction
                    if($dataprod["promo"] == true){
                        $dataprod["price"] = $dataprod["price"] * ((100+$dataprod["promo_reduc"]) / 100);;
                    }
                    if (!isset($data['shop_points'])){
                        $data['shop_points'] = 0;
                    }
                    //Vérification du prix
                    if ($dataprod["price"] <= $data['shop_points']){
                        //On continue car il a les sous
                        $operation = $this->container->mongo->test->operation;
                        //Prépartion de l'insertion de l'achat
                        $insert = [
                            "unique-id" => $data['uniqueId'],
                            "name" => $this->session->getProfile('username')['username'],
                            "price" => intval($dataprod["price"]),
                            "promo" => $dataprod["promo"],
                            "date" => date("Y-m-d H:i:s"),
                            "product_name" => $dataprod["name"],
                            "product_id" => $args['id']
                        ];
                        $operation->insertOne($insert);


                    }else{
                        //C'est un clodo donc erreur
                        return $response->write("Not enought")->withStatus(406);
                    }
                }else{
                    return $response->write("Product not found")->withStatus(404);
                }
            }else{
                return $response->write("Not linked")->withStatus(405);
            }
        }

    }

    public function send(){

    }






}