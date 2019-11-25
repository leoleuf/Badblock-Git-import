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
    $pdo = new PDO("mysql:host=" . $credentials['host'] . ";port=" . $credentials['port'] . ";dbname=" . $credentials['dbname'], $credentials['user'], $credentials['password'], [
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
    ]);

    return $pdo;

}