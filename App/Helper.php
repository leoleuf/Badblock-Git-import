<?php

function getConfig(){
    return [
        "urls" => [
            'home' => "https://badblock.fr",
            'store' => "https://store.badblock.fr"
        ],
        "vote_platforms" => [
            "top-serveurs" => [
                /**
                 * TODO
                 * Get API token, regular API call URL (GET request) :
                 * https://api.top-serveurs.net/v1/votes/check?server_token=:server_token&playername=:playername
                 */
                "api_url" => "https://api.top-serveurs.net/v1/votes/check",
                "api_token" => ""
            ]
        ]
    ];
}

/**
 * Don't add a / at the beginning when calling this function
 * @param $path
 * @return string
 */
function assets($path)
{
    return '../assets/' . $path;
}

function layouts($name)
{
    return include_once '../layouts/' . $name . '.php';
}

function db(){

    $credentials = [
        "host" => "127.0.0.1",
        "port" => "3306",
        "dbname" => "badblock_stats_dev",
        "user" => "root",
        "password" => ""
    ];

    //Init DB with provided credentials
    try {
        $pdo = new PDO("mysql:host=" . $credentials['host'] . ";port=" . $credentials['port'] . ";dbname=" . $credentials['dbname'], $credentials['user'], $credentials['password'], [
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
        ]);
    } catch (PDOException $PDOException){
        echo "<div class='alert-danger col-4'> Unable to reach database, please contact an administrator and provide the following error code : ".$PDOException->getMessage()."</div>";
        return die(503);
    }

    return $pdo;

}

function getMonth(int $month){

    switch ($month){

        case 1:
            return "Janvier";
            break;
        case 2:
            return "Février";
            break;
        case 3:
            return "Mars";
            break;
        case 4:
            return "Avril";
            break;
        case 5:
            return "Mai";
            break;
        case 6:
            return "Juin";
            break;
        case 7:
            return "Juillet";
            break;
        case 8:
            return "Août";
            break;
        case 9:
            return "Septembre";
            break;
        case 10:
            return "Octobre";
            break;
        case 11:
            return "Novembre";
            break;
        case 12:
            return "Décembre";
            break;

        default:
            return "Invalid number provided, please contact an administrator";
            break;
    }
}