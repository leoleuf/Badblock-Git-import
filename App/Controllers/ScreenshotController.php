<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/06/2018
 * Time: 19:43
 */

namespace App\Controllers;
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class ScreenshotController extends Controller
{

    public function getPage(RequestInterface $request, ResponseInterface $response){
        $this->render($response, 'pages.screen');
    }



    public function getPost(RequestInterface $request, ResponseInterface $response){

    }



}