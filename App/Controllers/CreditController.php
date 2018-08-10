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


    
}