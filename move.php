<?php
require __DIR__ . '/vendor/autoload.php';

use MongoDB;

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

    if (isset($player['game']) && isset($player['loginPassword']) && isset($player['permissions'])){
        echo $I . " / " .$player['name'] .  " \n";

        if (true){
            //Traitement grade
            foreach ((array) $player['permissions']['groups']['bungee'] as $k => $row){
                if ($k == "gradeperso" || $k == "noel"){
                    $expire = $row;
                }
                array_push($Grade, $k);
            }
            
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
                        "freebuild" => $Grades,
                        "survie" => $Grades,
                        "faction" => $Grades
                    ],
                    "permissions" => [
                        "bungee" => $Grades
                    ]
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

            if (isset($player['lastIp'])){
                $Data['lastIp'] = $player['lastIp'];
            }

            if (isset($player['realName'])){
                $Data['realName'] = $player['realName'];
            }

            if (isset($player['onlineMode'])){
                $Data['onlineMode'] = $player['onlineMode'];
            }

            if (isset($player['game']['other']['hub']['mountConfigs'])){
                unset($player['game']['other']['hub']['mountConfigs']);
            }

            try{
                $c = $client->selectCollection("admin","players_new_grade");
                $c->insertOne($Data);
            }catch (InvalidArgumentException $e){
                echo "Error > " . $I;
            }
        }
    }


}
