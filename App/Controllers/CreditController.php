<?php
/**
 * Created by PhpStorm.
 * User: POINTURIER
 * Date: 04/08/2018
 * Time: 20:35
 */

namespace App\Controllers;


class CreditController extends Controller
{

    private $request;

    private $offers = [
        'PAYPAL' => [
            950 => 10.0,
            2300 => 25.0
        ],
        'DEDIPASS' => [
            950 => 10.0,
            2300 => 25.0
        ]
    ];

    public function paymentCancel(){
        return $this->redirect();
    }

    public function  paymentSuccess(){
        return $this->redirect();
    }

    public function paymentError(){
        return $this->redirect();
    }

    
}