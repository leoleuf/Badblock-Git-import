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
        //Server List
        $collection = $this->mongo->test->server;
        $collection1 = $this->mongo->test->category;
        $collection2 = $this->mongo->test->products;


        $cursor = $collection->find(['visibility' => true]);


        //Array
        $listserver = [];
        $itempromo = [];
        //Lecture + push
        foreach ($cursor as $df) {

            $current_listc = [];

            //Liste Catégorie Serveur
            $current_cursor = $collection1->find(['visibility' => true,'server' => (string) $df->_id]);


            foreach ($current_cursor as $current) {
                //Liste produits
                $current_listp = [];
                $current_cursor2 = $collection2->find(['visibility' => true,'cat' => (string) $current->_id]);


                foreach ($current_cursor2 as $current2) {
                    //Si le produit est en promo
                    if ($current2->promo == true){
                        $current2->np = $current2->price * ((100+$current2->promo_reduc) / 100);
                        array_push($itempromo,array($current2->_id,$current2->name,$current2->price,$current2->img,$current2->promo_reduc,$current2->np,$current2->description));
                        array_push($current_listp,array($current2->_id,$current2->name,$current2->price,$current2->img,$current2->promo,$current2->promo_reduc,$current2->np,$current2->description));
                    }else{
                        $current2->promo = false;
                        array_push($current_listp,array($current2->_id,$current2->name,$current2->price,$current2->img,$current2->promo,$current2->description));
                    }


                }
                array_push($current_listc,array($current->_id,$current->name,$current_listp));
            }

            array_push($listserver,array($df->_id,$df->name,$current_listc));
        }
        //Mise en Cache


        $this->redis->setJson('shop.listsrv', $listserver);


        //Mise en cache produit seul
        $collection = $this->mongo->test->products;
        $curs = $collection->find(['visibility' => true]);
        foreach ($curs as $cur) {
            $this->redis->setJson('shop.prod.'.$cur["_id"], $cur);
        }

        $this->redis->setJson('shop.promo',$itempromo);




        //Renvoie d'un code de succès

        $this->log->success('StaffApiController\ShopApiController',' Success writing shop cache');

        return $response->write('Success writing shop cache')->withStatus(200);


    }



}