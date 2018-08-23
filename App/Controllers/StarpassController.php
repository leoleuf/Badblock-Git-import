<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/07/2018
 * Time: 11:34
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class StarpassController extends Controller
{


    public function index(RequestInterface $request, ResponseInterface $response){
        $this->render($response,'shop.recharge.starpass', []);
    }

    public function process(RequestInterface $request, ResponseInterface $response){
        $datas = $_GET['DATAS'];
        $PAYS = $_GET['PAYS'];
        $PALIER = $_GET['PALIER'];
        $ID_PALIER = $_GET['ID_PALIER'];
        $TYPE = $_GET['TYPE'];
        return $this->redirect($response, '/shop/recharge/success');
    }

}