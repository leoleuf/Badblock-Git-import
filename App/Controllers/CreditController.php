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


    public function paymentCancel(RequestInterface $request, ResponseInterface $response){
        $this->render($response, 'pages.play');
    }

    public function paymentSuccess(RequestInterface $request, ResponseInterface $response){
        $this->render($response, 'pages.play');
    }

    public function paymentINT($pseudo, $price, $points, $mode){

    }



    public function stepRecharge(RequestInterface $request, ResponseInterface $response,$id = 1){
        if (empty($id)){
            $this->render($response, 'shop.recharge.step-1');
        }elseif($id['id'] == 2){
            $this->render($response, 'shop.recharge.step-2');
        }else{
            $this->render($response, 'shop.recharge.step-3');
        }
    }








    
}