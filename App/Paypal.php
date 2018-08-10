<?php

namespace App;
/**
 *
 * API PAYPAL CREE PAR STONMINE - Derghoum Loïc
 *
 *
 * Cette api est a votre disposition, elle a été créé car la doc paypal est mal foutue et illisible
 *
 * Mais elle à été construite grâce à celle-ci.
 *
 * Une fonction semblabe au var_dumb a été ajouté en bas.
 *
 *
 *
 **/

class Paypal {

    // Remplisser les variables avec vos identifiants api paypal

    private $user = 'X';
    private $pwd = 'X';
    private $signature = "X-X-X";
    private $endpoint = "https://api-3t.paypal.com/nvp";
    //private $prod = 1;
    public $errors = array();

    public function __construct($user = false, $pwd = false, $signature = false, $prod = false){

        if($user){

            $this->user = $user;

        }
        if($pwd){

            $this->pwd = $pwd;

        }
        if($signature){

            $this->signature = $signature;

        }
        if($prod){

            $this->endpoint = preg_replace('/\.sandbox/', '', $this->endpoint);

        }

    }

    public function request($method, $params){

        $params = array_merge($params, array(
            'METHOD' => $method,
            'VERSION' => '74.0',
            'USER' => $this->user,
            'SIGNATURE' => $this->signature,
            'PWD' => $this->pwd,
        ));
        $params = http_build_query($params);
        $curl = curl_init();
        curl_setopt_array($curl, array(
            CURLOPT_URL => $this->endpoint,
            CURLOPT_POST => 1,
            CURLOPT_POSTFIELDS => $params,
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_SSL_VERIFYHOST => false,
            CURLOPT_VERBOSE => 1
        ));

        $response = curl_exec($curl);
        $responseArray = array();
        parse_str($response, $responseArray);
        //preint_r($responseArray);

        if(curl_errno($curl)){
            $this->errors = curl_error($curl);
            curl_close($curl);
            return false;
        }else{
            if($responseArray['ACK'] == "Success"){
                return $responseArray;
            }else{
                $this->errors = ($responseArray);
                curl_close($curl);
                return false;
            }

        }

    }



}
?>