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
        $obj = (object) array('1' => '50');

        $virtual_currency = $obj("1");
        // Sauvegarde dans mongoDB
        $starpass->date = date('Y-m-d H:i:s');
        $this->container->mongo->funds_logs->insertOne($starpass);

                $user = $this->container->mongoServer->players->findOne(['name' => strtolower($name)]);
                $data = [
                    'uniqueId' => $user['uniqueId'],
                    'date' => date('Y-m-d H:i:s'),
                    'price' => $PALIER,
                    'gateway' => 'starpass',
                    'pseudo' => $name,
                    'points' => $virtual_currency,
                    //'transaction_id' => $code
                ];

                $this->container->mongo->funds->insertOne($data);
                $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);
                if ($money == null){
                    $data = [
                        "uniqueId" => $user['uniqueId'],
                        "points" => $dedipass->virtual_currency
                    ];
                    $this->container->mongo->fund_list->insertOne($data);
                }else{
                    $money['points'] = $money['points'] + $dedipass->virtual_currency;
                    $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
                }

                if ($this->container->session->exist('user')){
                    $mailContent = file_get_contents("../mail-achat.html");
                    $mailContent = str_replace("(username)", $name, $mailContent);
                    $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                    $mail = new \App\Mail(true);
                    $mail->sendMail($this->session->get('user')["email"], "BadBlock - Rechargement", $mailContent);
                }

        return $this->redirect($response, '/shop/recharge/success');
    }

}