<?php

namespace App\Controllers\Api;


use Monolog\Handler\Mongo;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use MongoCursor;

class ShopApiController extends \App\Controllers\Controller
{


    public function upCache(RequestInterface $request, ResponseInterface $response){

        $filter  = [];
        $options = ['sort' => ['power' => -1]];
        $data = [];
        $key = 0;

        //Search all server by power
        $server_list = $this->container->mongo->server_list->find($filter, $options);

        //Big for for push all item in an bug array
        foreach ($server_list as $serv){
            array_push($data, ["server" => ['name' => $serv->name, "icon" => $serv->icon], "cat" => []]);
            //Search all category of the server
            $filter  = ['server_id' => new \MongoDB\BSON\ObjectId($serv->_id)];
            $options = ['sort' => ['power' => -1]];
            $cat_list = $this->container->mongo->category_list->find($filter, $options);
            foreach ($cat_list as $cat){
                //Search item of the cat
                $filter  = ['cat_id' => new \MongoDB\BSON\ObjectId($cat->_id)];
                $options = ['sort' => ['power' => -1]];
                $product_list = $this->container->mongo->product_list->find($filter, $options);
                array_push($data[$key]['cat'], ["name" => $cat->name, "sub-name" => $cat->subname, "items" => $product_list]);
            }
        }

        $this->redis->setJson('shop', $data);

        echo "OK";
    }

}