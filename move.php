<?php
require __DIR__ . '/vendor/autoload.php';

use MongoDB;

$client = new \MongoDB\Client(
    'mongodb://wjayBSTtdN49ur:2WtM97v6kAeskALjrEZTp3M89H25Rn78BXZ8Z3sZA@node01-int.clusprv.badblock-network.fr:49857/admin'
);

$client->selectDatabase("admin");
$collection = $client->selectCollection("admin","players");

//Alternate group
$alt = ['$or' =>
    [
        ['permissions.groups.bungee.vip' => ['$exists' => true]],
        ['permissions.groups.bungee.vip+' => ['$exists' => true]],
        ['permissions.groups.bungee.mvp' => ['$exists' => true]],
        ['permissions.groups.bungee.mvp+' => ['$exists' => true]]
    ]];

$Count = $collection->count($alt);

echo "";
echo "";
echo "$Count player have groups";
echo "";
echo "";

$Players = $collection->find($alt);

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
                "groups" => [
                    "bungee" => $Grades,
                    "minigames" => $Grades,
                    "pvpbox" => $Grades,
                    "skyblock" => $Grades,
                    "freebuild" => $Grades,
                    "survie" => $Grades,
                    "faction" => $Grades
                ],
                "permissions" => $player['permissions']['permissions']
            ];


            try{
                $collection = $client->selectCollection("admin","players");
                $end = $collection->updateOne(["_id" => $player['_id']], ['$set' => ["permissions" => $Data]]);

            }catch (InvalidArgumentException $e){
                echo "Error > " . $I;
            }
        }
    }


}