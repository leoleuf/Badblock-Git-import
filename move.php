<?php
require __DIR__ . '/vendor/autoload.php';

use \MongoDB\Client;

$client = new \MongoDB\Client(
    'mongodb://wjayBSTtdN49ur:2WtM97v6kAeskALjrEZTp3M89H25Rn78BXZ8Z3sZA@node01-int.clusprv.badblock-network.fr:49857/admin'
);

$client->selectDatabase("admin");

$Players = $client->players;
$Players = $Players->find([])->toArray();

$I = 0;

foreach ($Players as $player){
    $I++;
    $Grade = [];

    //Traitement grade
    foreach ((array) $player['permissions']['alternateGroups'] as $k => $row){
        if ($k == "gradeperso"){
            $expire = $row;
        }
        array_push($Grade, $k);
    }

    if ($player['permissions']['group'] == "gradeperso"){
        $expire = $player['permissions']['end'];
    }
    array_push($Grade, $player['permissions']['group']);

    $Grades = [];
    foreach ($Grade as $g){
        if ($g == "gradeperso"){
            array_push($Grades,
                [
                    $g => $expire
                ]
            );
        }else{
            array_push($Grades,
                [
                    $g => -1
                ]
            );
        }
    }

    $Data = [
        "name" => $player['name'],
        "realName" => $player['realName'],
        "lastIp" => $player['lastIp'],
        "onlineMode" => $player['onlineMode'],
        "loginPassword" => $player['loginPassword'],
        "nickname" => "",
        "uniqueId" => $player['uniqueId'],
        "settings" => [
            "partyable" => "WITH_EVERYONE",
            "friendListable" => "YES",
        ],
        "punish" => [
            "ban" => null,
            "mute" => null,
            "warn" => null
        ],
        "permissions" => [
            "groups" => [
                "bungee" => $Grades,
                "minigames" => $Grades,
                "pvpbox" => $Grades,
                "skyblock" => $Grades,
                "freebuild" => $Grades
            ],
            "permissions" => []
        ],
        "authKey" => $player['authKey'],
        "refer" => $player['refer'],
        "state" => $player['state'],
        "game" => $player['game'],
    ];

    $client->players_new->insertOne($Data);


    echo $player['name'] . " => " . $I;
}
