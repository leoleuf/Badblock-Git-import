<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 04/11/2017
 * Time: 17:01
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class PageController extends Controller
{

    public function home(RequestInterface $request, ResponseInterface $response)
    {
        return $this->render($response, 'pages.home');
    }


}