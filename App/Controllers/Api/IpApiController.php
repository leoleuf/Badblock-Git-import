<?php

namespace App\Controllers\Api;


use Monolog\Handler\Mongo;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use MongoCursor;

class IpApiController extends \App\Controllers\Controller
{

    public function ip(RequestInterface $request, ResponseInterface $response)
    {
        // debug temporaire pour serveur-prive.net
        echo $_SERVER['HTTP_CF_CONNECTING_IP'];
    }

}