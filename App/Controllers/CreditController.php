<?php
/**
 * Created by PhpStorm.
 * User: POINTURIER
 * Date: 04/08/2018
 * Time: 20:35
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class CreditController extends Controller
{

    public function paymentINT($mongoDB,RequestInterface $request, ResponseInterface $response,$price, $points, $mode)
    {

    }

    public function paymentCancel(RequestInterface $request, ResponseInterface $response)
    {
        $this->render($response, 'shop.recharge.recharge-cancel');
    }

    public function paymentSuccess(RequestInterface $request, ResponseInterface $response)
    {
        $this->render($response, 'shop.recharge.recharge-sucess');
    }


    public function stepRecharge(RequestInterface $request, ResponseInterface $response,$id = 1)
    {
        if (empty($id)){
            if ($this->container->session->exist('user')) {
                $player = $this->session->getProfile('username')['username'];
            }else{
                $player = "";
            }

            $this->render($response, 'shop.recharge.step-1', ['player' => $player]);
        }elseif($id['id'] == 2){
            $this->render($response, 'shop.recharge.step-2');
        }elseif($id['id'] == "paypal"){
            return $this->paypal($request,$response);
        }
    }

    public function postRecharge(RequestInterface $request, ResponseInterface $response){
        if (!empty($_POST['pseudo'])){
            $data = $this->container->mongoServer->players->findOne(['name' => strtolower($_POST['pseudo'])]);

            if ($data != null){
                $this->container->session->set('recharge-username', $_POST['pseudo']);
                $this->render($response, 'shop.recharge.step-2');
            }else{
                $this->render($response, 'shop.recharge.step-1', ['error' => $_POST['pseudo'] . ' ne s\'est jamais connecté sur le serveur !']);
            }
        }
    }

    public function paypal(RequestInterface $request, ResponseInterface $response){

        $this->render($response, 'shop.recharge.paypal');

    }








    
}