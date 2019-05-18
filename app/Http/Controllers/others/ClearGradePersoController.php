<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 18/05/2019
 * Time: 01:23
 */

namespace App\Http\Controllers\others;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class ClearGradePersoController extends Controller
{

    public function index()
    {

        $servers = [
            "bungee",
            "survie",
            "faction",
            "skyblock",
            "freebuild",
            "minigames",
            "pvpbox"
        ];

        $Players = DB::connection('mongodb_server')->collection('players')->get();
        $PlayersWithGradePerso = array();
        $PlayersWithGradePersoCounter = 0;
        foreach ($Players as $k => $player){
            $check = false;

            foreach ($servers as $server){
                if(isset($player['permissions']['groups'][$server]['gradeperso'])){
                    $check = true;
                }
            }

            if($check == true) {
                $PlayersWithGradePerso[$PlayersWithGradePersoCounter] = $player['name'];
                $PlayersWithGradePersoCounter++;
            }

        }

        $file = 'PlayerWithGradePerso.txt';
        $handle = fopen($file, 'w') or die('Cannot open file:  '.$file);
        $data = json_encode($PlayersWithGradePerso);
        fwrite($handle, $data);

        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename="'.basename($file).'"');
        header('Expires: 0');
        header('Cache-Control: must-revalidate');
        header('Pragma: public');
        header('Content-Length: ' . filesize($file));
        flush(); // Flush system output buffer
        readfile($file);

        return view('others.clearGradePerso');

    }
}