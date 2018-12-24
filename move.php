<?php
require __DIR__ . '/vendor/autoload.php';

use \MongoDB\Client;

$client = new \MongoDB\Client(
    'mongodb://wjayBSTtdN49ur:2WtM97v6kAeskALjrEZTp3M89H25Rn78BXZ8Z3sZA@node01-int.clusprv.badblock-network.fr:49857/admin'
);

$client->selectDatabase("admin");
$collection = $client->selectCollection("admin","players");

$Players = $collection->find([]);

$I = 0;

foreach ($Players as $player){
    $I++;
    $Grade = [];

    if (isset($player['game']) && isset($player['loginPassword'])){
        //Traitement grade
        foreach ((array) $player['permissions']['alternateGroups'] as $k => $row){
            if ($k == "gradeperso" || $k == "noel"){
                $expire = $row;
            }
            array_push($Grade, $k);
        }

        if ($player['permissions']['group'] == "gradeperso" || $player['permissions']['group'] == "noel"){
            $expire = $player['permissions']['end'];
        }
        array_push($Grade, $player['permissions']['group']);

        $Grades = [];
        foreach ($Grade as $g){
            if ($g == "gradeperso" || $g == "noel"){
                $Grades[$g] = $expire;
            }else{
                $Grades[$g] = -1;
            }
        }

        $Data = [
            "name" => $player['name'],
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
            "game" => $player['game'],
        ];

        if (isset($player['authKey'])){
            $Data['authKey'] = $player['authKey'];
        }
        if (isset($player['state'])){
            $Data['state'] = $player['state'];
        }

        if (isset($player['refer'])){
            $Data['refer'] = $player['refer'];
        }

        if (isset($player['realName'])){
            $Data['realName'] = $player['realName'];
        }

        $c = $client->selectCollection("admin","players_new");
        $c->insertOne($Data);


        echo $I . " / " .$player['name'] .  " \n";
    }


}
