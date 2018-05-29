<?php

namespace App\Controllers;

use App\Shoplinker;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Slim\App;
use MongoDB;

/**
 * Class ShopController
 * @package App\Controllers
 */
class ShopController extends Controller
{


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
		} else {
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


	public function starpass(ServerRequestInterface $request, ResponseInterface $response){

        $this->render($response, "shop.recharge.starpass", ['step' => 'starpass', 'width' => 77]);


    }

    public function starpassPost(ServerRequestInterface $request, ResponseInterface $response){
        // Déclaration des variables
        $ident=$idp=$ids=$idd=$codes=$code1=$code2=$code3=$code4=$code5=$datas='';
        $idp = 233211;
        // $ids n'est plus utilisé, mais il faut conserver la variable pour une question de compatibilité
        $idd = 415141;
        $ident=$idp.";".$ids.";".$idd;
        // On récupère le(s) code(s) sous la forme 'xxxxxxxx;xxxxxxxx'
        if(isset($_POST['code1'])) $code1 = $_POST['code1'];
        if(isset($_POST['code2'])) $code2 = ";".$_POST['code2'];
        if(isset($_POST['code3'])) $code3 = ";".$_POST['code3'];
        if(isset($_POST['code4'])) $code4 = ";".$_POST['code4'];
        if(isset($_POST['code5'])) $code5 = ";".$_POST['code5'];
        $codes=$code1.$code2.$code3.$code4.$code5;
        // On récupère le champ DATAS
        if(isset($_POST['DATAS'])) $datas = $_POST['DATAS'];
        // On encode les trois chaines en URL
        $ident=urlencode($ident);
        $codes=urlencode($codes);
        $datas=urlencode($datas);

        /* Envoi de la requête vers le serveur StarPass
        Dans la variable tab[0] on récupère la réponse du serveur
        Dans la variable tab[1] on récupère l'URL d'accès ou d'erreur suivant la réponse du serveur */
        $get_f=@file( "https://script.starpass.fr/check_php.php?ident=$ident&codes=$codes&DATAS=$datas" );
        if(!$get_f)
        {
            exit( "Votre serveur n'a pas accès au serveur de StarPass, merci de contacter votre hébergeur. " );
        }
        $tab = explode("|",$get_f[0]);

        if(!$tab[1]) $url = "https://script.starpass.fr/error.php";
        else $url = $tab[1];

        // dans $pays on a le pays de l'offre. exemple "fr"
                $pays = $tab[2];
        // dans $palier on a le palier de l'offre. exemple "Plus A"
                $palier = urldecode($tab[3]);
        // dans $id_palier on a l'identifiant de l'offre
                $id_palier = urldecode($tab[4]);
        // dans $type on a le type de l'offre. exemple "sms", "audiotel, "cb", etc.
                $type = urldecode($tab[5]);
        // vous pouvez à tout moment consulter la liste des paliers à l'adresse : https://script.starpass.fr/palier.php

        // Si $tab[0] ne répond pas "OUI" l'accès est refusé
        // On redirige sur l'URL d'erreur
        if( substr($tab[0],0,3) != "OUI" )
        {
            header( "Location: $url" );
            exit;
        }
        else
        {
            /* Le serveur a répondu "OUI"

            On place un cookie appelé CODE_BON et qui vaut la valeur 1
            Ce cookie est valide jusqu'à ce que l'internaute ferme son navigateur
            Dans les pages suivantes, nous testerons l'existence du cookie
            S'il existe, c'est que l'internaute est autorisé,
            sinon on le renverra sur une page d'erreur */
            setCookie( "CODE_BON", "1", 0 );
            // Si vous avez plusieurs documents, nommer le cookie plutôt 'code'+iDocumentId

            // vous pouvez afficher les variables de cette façon :
            // echo "idd : $idd / codes : $codes / datas : $datas / pays : $pays / palier : $palier / id_palier : $id_palier / type : $type";
        }


    }


    public function dedipass(ServerRequestInterface $request, ResponseInterface $response){

        $this->render($response, "shop.recharge.dedipass", ['step' => 'dedipass', 'width' => 77]);


    }

    public function dedipassPost(ServerRequestInterface $request, ResponseInterface $response){

        $code = isset($_POST['code']) ? preg_replace('/[^a-zA-Z0-9]+/', '', $_POST['code']) : '';
        if( empty($code) ) {
            return $this->redirect($response, $this->pathFor('shop.recharge.dedipass'));
        }
        else {
            $dedipass = file_get_contents('http://api.dedipass.com/v1/pay/?public_key=95576b4b99c5d76e20319817e24761ae&private_key=6d7ba3255b0a85cfe4f8d8d088ca35a1baf82f89&code=' . $code);
            $dedipass = json_decode($dedipass);
            if($dedipass->status == 'success') {
                // Le transaction est validée et payée.
                // Vous pouvez utiliser la variable $virtual_currency
                // pour créditer le nombre de Crystals.
                $virtual_currency = $dedipass->virtual_currency;


                //Insertion dans MongoDB
                $operation = $this->container->mongo->funds;
                $insert = [
                    "name" => $this->session->getProfile('username')['username'],
                    "date" => date("Y-m-d H:i:s"),
                    "gateway" => "dedipass",
                    "comment" => "Recharge en crédit du compte",
                    "price" => intval($dedipass->payout),
                    "credit" => intval($virtual_currency)
                ];
                $operation->insertOne($insert);


                echo 'Le code est valide et vous êtes crédité de ' . $virtual_currency . 'Crystals';
            }
            else {
                // Le code est invalide
                echo 'Le code '.$code.' est invalide';
            }
        }

        $this->render($response, "shop.recharge.dedipass", ['step' => 'dedipass', 'width' => 77]);


    }

























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




	public function getachat(ServerRequestInterface $request, ResponseInterface $response, $args)
	{
		//On vérifie si le joueur est connecté
		if (!$this->container->session->exist('user')) {
			return $response->write("Not connected")->withStatus(403);
		} else {
			if (in_array(17, $this->container->session->getProfile("user")['secondary_group_ids'])) {
				if (isset($args['id']) & !empty($args['id']) & $this->redis->exists('shop.prod.' . $args['id'])) {
					//Vérification si le produit éxiste
					$collection = $this->container->mongoServer->players;
					$data = $collection->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);
					$collec = $this->container->mongo->products;
					$dataprod = $collec->findOne(["_id" => new MongoDB\BSON\ObjectId($args['id'])]);

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
						$operation = $this->container->mongo->operation;
						//Prépartion de l'insertion de l'achat
                        if ($dataprod["promo"] == false){
                            $dataprod["promo_reduc"] = 0;
                        }
						$insert = [
							"unique-id" => $data['uniqueId'],
							"name" => $this->session->getProfile('username')['username'],
							"price" => intval($dataprod["price"]),
							"promo" => $dataprod["promo"],
							"promo_reduc" => $dataprod["promo_reduc"],
							"date" => date("Y-m-d H:i:s"),
							"product_name" => $dataprod["name"],
							"product_id" => $args['id'],
                            "in-game" => false
						];
						$operation->insertOne($insert);

						//$data , $datatype , $username
						$this->container->ShopLinker->sendShopData($dataprod,"BUY",$this->session->getProfile('username')['username']);

					} else {
						//C'est un clodo donc erreur
						return $response->write("Not enought")->withStatus(406);
					}
				} else {
					return $response->write("Product not found")->withStatus(404);
				}
			} else {
				return $response->write("Not linked")->withStatus(405);
			}
		}

	}



}