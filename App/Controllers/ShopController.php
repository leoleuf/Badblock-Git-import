<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 25/07/2018
 * Time: 11:34
 */

namespace App\Controllers;

use MongoDB\Exception\Exception;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class ShopController extends Controller
{


    function index(){
        //TODO by TomDev
    }


    function buy(RequestInterface $request, ResponseInterface $response, $argument){
        //Check if user is connected
        if (!$this->container->session->exist('user')){
            return $response->write("User not connected !")->withStatus(401);
        }

        if (!isset($argument['id'])){
            return $response->write("Invalid ID for product buy !")->withStatus(403);
        }

        //Try make an mongoID is very sensitive
        try {
            $id = new \MongoDB\BSON\ObjectId($argument['id']);
        }catch (\MongoDB\Driver\Exception\InvalidArgumentException $ex){
            return $response->write("Invalid ID for product buy ER : ObjectID not valid !")->withStatus(403);
        }

        //Search data product in mongoDB is very slow
        $product = $this->container->mongo->product_list->find(['_id' => $id]);
        if ($product == null){
            return $response->write("Product doesn't exist !")->withStatus(404);
        }

        //Check if player have money


        dd($argument);



    }



    public function haveMoney($username, $amount){
        //Search data player
        $player = $this->container->mongoServer->player->find(['name' => strtolower($this->session->getProfile('username')['username'])]);

        if ($player == null){
            return "Not found";
        }

        //Search money of player
        $player = $this->container->mongo->fund_list->find(['name' => strtolower($this->session->getProfile('username')['username'])]);






    }



}