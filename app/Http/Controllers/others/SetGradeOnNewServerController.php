<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 01/06/2019
 * Time: 21:28


namespace App\Http\Controllers\others;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class SetGradeOnNewServerController extends Controller
{

    /*
    public function index($row)
    {

        /*
        $newServerName = "practice";
        $refServer = "bungee";

        //To add player's grade, best config is 25 25
        $NumberToGetInOneSubRow = 25;
        $NumberToGetInOneRow = 25;
        $servers = array(
            'bungee', 'survie', 'pvpbox', 'freebuild', 'faction', 'minigames', 'skyblock', 'rushffa', 'practice', 'hub', 'build'
        );
        $Players = array();

        $where = ['$or' =>
            [
                /*
                 ['permissions.groups.' . $refServer . '.vip' => ['$exists' => true]]

                 ['permissions.groups.'.$refServer.'.vip+' => ['$exists' => true]]

                 ['permissions.groups.'.$refServer.'.mvp' => ['$exists' => true]]

                 ['permissions.groups.'.$refServer.'.mvp+' => ['$exists' => true]]
                ['permissions.groups.'.$refServer.'.gradeperso' => ['$exists' => true]]


                ['permissions.groups.'.$refServer.'.miniyoutuber' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.youtuber' => ['$exists' => true]]
                *
                ['permissions.groups.skyblock.islander' => ['$exists' => true]],
                ['permissions.groups.skyblock.skylander' => ['$exists' => true]],
                ['permissions.groups.skyblock.skylord' => ['$exists' => true]],
                ['permissions.groups.skyblock.skyheaven' => ['$exists' => true]],
                ['permissions.groups.skyblock.skymaster' => ['$exists' => true]]

                /*
                ['permissions.groups.'.$refServer.'.staff' => ['$exists' => true]]

                ['permissions.groups.'.$refServer.'.badfriend' => ['$exists' => true]]

                /*
    public function index(){

        $newServerName = "rushffa";
        $refServer = "bungee";

        $where = ['$or' =>
            [
                ['permissions.groups.'.$refServer.'.vip' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.vip+' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.mvp' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.mvp+' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.gradeperso' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.miniyoutuber' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.partenaire' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.badfriend' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.youtuber' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.staff' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.redacteur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.developpeur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.animateur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.modoforum' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.graphiste' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.builder-test' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.builder' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.helper' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.modochat' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.modo' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.modocheat' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.supermodo' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.developpeur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.superviseur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.redacteur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.modoforum' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.responsable' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.admin' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.manager' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.builder' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.graphiste' => ['$exists' => true]]


            ]
        ];


        for ($i = $NumberToGetInOneRow * $row; $i < $NumberToGetInOneRow * ($row + 1); $i++) {
            $temp = DB::connection('mongodb_server')->collection('players')->where($where)->select('name', 'permissions')->skip($NumberToGetInOneSubRow * $i)->take($NumberToGetInOneSubRow)->get();
            if ($temp->isEmpty()) {
                break;
            }
            array_push($Players, $temp);
        }

        /**
         *
         * Script servant à sortir une liste de noms de joueurs
         *
         *
        $my_file = 'Liste.txt';
        $handle = fopen($my_file, 'a') or die('Cannot open file:  '.$my_file);
        $data = "";
        foreach($Players as $row){
            foreach ($row as $player){
                $data .= $player['name'].', ';
            }
        }
        fwrite($handle, $data);
        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename="'.basename($my_file).'"');
        header('Expires: 0');
        header('Cache-Control: must-revalidate');
        header('Pragma: public');
        header('Content-Length: ' . filesize($my_file));
        flush(); // Flush system output buffer
        readfile($my_file);


        /**
         *
         * Script servant à copier les grades Bungee sur le nouveau serveur
         *
         * *
        foreach ($Players as $TabRow) {
            foreach ($TabRow as $player) {

                $permisions = array();
                foreach ($player['permissions']['groups'][$refServer] as $key => $perm) {
                    $permisions[$key] = $perm;
                }
                foreach ($servers as $server) {
                    DB::connection('mongodb_server')->collection('players')->where('name', '=', $player['name'])->update([
                        '$unset' => [
                            'permissions.groups.' . $server => ['miniyoutuber', 'youtuber']
                        ]
                    ]);
                }
            }
        }

        /**
         *
         * Script servant à enlever des grades sur tous les serveurs
         *
         * *
        foreach ($Players as $TabRow) {
            foreach ($TabRow as $player) {
                foreach ($servers as $server) {
                    DB::connection('mongodb_server')->collection('players')->where('name', '=', $player['name'])->update([
                        '$unset' => [
                            'permissions.groups.' . $server.'miniyoutuber' => "",
                            'permissions.groups.' . $server.'youtuber' => ""
                        ]
                    ]);
                }
            }
        }
        /**
         *
         * Script servant à retirer les grades Legend pour ceux expirés
         *
        foreach ($Players as $TabRow) {
            foreach ($TabRow as $player) {
                foreach($servers as $server){
                    if(isset($player['permissions']['groups'][$server]) && isset($player['permissions']['groups'][$server]['gradeperso'])){
                        if($player['permissions']['groups'][$server]['gradeperso'] < time()*1000 && $player['permissions']['groups'][$server]['gradeperso'] > 0){

                            DB::connection('mongodb_server')->collection('players')->where('name', '=', $player['name'])->update([

                                '$unset' => [
                                    'permissions.groups.'.$server.'.gradeperso' => ""
                                ]
                            ]);


                        }
                    }
                }
            }
        }


        $row++;
        return redirect('/setGradesOnNewServer/'.$row);
                ['permissions.groups.'.$refServer.'.superviseur' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.responsable' => ['$exists' => true]],
                ['permissions.groups.'.$refServer.'.manager' => ['$exists' => true]]
            ]
        ];

        $Players = DB::connection('mongodb_server')->collection('players')->where($where)->get();

        foreach ($Players as $player) {

            $permisions = array();
            foreach ($player['permissions']['groups'][$refServer] as $key => $perm){
                $permisions[$key] = $perm;
            }
            DB::connection('mongodb_server')->collection('players')->where('name', '=', $player['name'])->update([
                'permissions.groups.'.$newServerName => $permisions
            ]);
        }

        return view('others.setNewServerGrade', ['Players' => $Players]);
>>>>>>> 847eb807a3fb1c439fb7e1c8a08431e7d087b4db
    }

}*/