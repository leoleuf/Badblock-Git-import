<?php

namespace App;

class Helper
{

    public static function getEnv($var = NULL){

        $env = json_decode(file_get_contents("../App/env.json"), true);

        if(!$var)
            return $env;
        else {
            $vPath = explode(".", $var);
            $vPathLenght = count($vPath);

            $requested_param = $env;
            for($i = 0; $i < $vPathLenght; $i++)
                $requested_param = $requested_param[$vPath[$i]];

            return $requested_param;
        }

    }

    public static function getConfig(){

        return include_once("config.php");

    }

    public static function redirect($url){

        header("Location: ".$url."");

    }

}