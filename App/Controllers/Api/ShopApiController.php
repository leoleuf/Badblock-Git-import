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
        $collection = $this->mongo->badblock->server;
        $collection1 = $this->mongo->badblock->categorie;
        $collection2 = $this->mongo->badblock->product;


        $cursor = $collection->find(['visibility' => true]);


        //Array
        $listserver = [];
        //Lecture + push
        foreach ($cursor as $df) {
            $current_listc = [];

            //Liste Catégorie Serveur
            $current_cursor = $collection1->find(['visibility' => true,'server' => $df->id]);

            foreach ($current_cursor as $current) {
                //Liste produits
                $current_listp = [];
                $current_cursor2 = $collection2->find(['visibility' => true,'cat' => $current->_id]);


                foreach ($current_cursor2 as $current2) {
                    array_push($current_listp,array($current2->_id,$current2->name,$current2->price,$current2->img));
                }
                array_push($current_listc,array($current->_id,$current->name,$current_listp));
            }



            array_push($listserver,array($df->id,$df->name,$current_listc));
        }
        //Mise en Cache


        $this->redis->setJson('shop.listsrv', $listserver);


        //Mise en cache produit seul
        $collection = $this->mongo->badblock->product;
        $curs = $collection->find(['visibility' => true]);
        foreach ($curs as $cur) {
            $this->redis->setJson('shop.prod.'.$cur["_id"], $cur);
        }


        //Renvoie d'un code de succès

        return $response->write('Success writing shop cache')->withStatus(200);

        $this->log->info('"StaffApiController\ShopApiController": Success writing shop cache');

    }



}