<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 03/10/2017
 * Time: 15:00
 */

namespace App\Controllers\Api;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;


class ShopApiController extends \App\Controllers\Controller
{

    public function getCreateCacheShopList(RequestInterface $request, ResponseInterface $response){

        //Check MongoDB
        $cursor = $this->mongo->dev->server->findOne(['name' => "SkyBlock"]);
        //Array
        $listserver = [];
        //Lecture + push
        foreach ($cursor as $df) {
            array_push($listserver,array($df->id,$df->name));
        }
        //Mise en Cache

        $this->redis->setJson('shop.listsrv');

        //Renvoie d'un code de succès
        return $response->write('Success writing shop cache')->withStatus(200);
        $this->log->info('"StaffApiController\getCreateCacheAllStaff": Success writing shop cache');

    }



}