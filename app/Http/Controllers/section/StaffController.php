<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class StaffController extends Controller
{


    public function index(){
        //Alternate group
        $alt = ['$or' =>
            [
                ['permissions.groups.bungee.superviseur' => ['$exists' => true]],
                ['permissions.groups.bungee.helper' => ['$exists' => true]],
                ['permissions.groups.bungee.admin' => ['$exists' => true]],
                ['permissions.groups.bungee.modo' => ['$exists' => true]],
                ['permissions.groups.bungee.supermodo' => ['$exists' => true]],
                ['permissions.groups.bungee.responsable' => ['$exists' => true]],
                ['permissions.groups.bungee.builder' => ['$exists' => true]],
                ['permissions.groups.bungee.animateur' => ['$exists' => true]],
                ['permissions.groups.bungee.modoforum' => ['$exists' => true]],
                ['permissions.groups.bungee.graphiste' => ['$exists' => true]],
                ['permissions.groups.bungee.redacteur' => ['$exists' => true]],
                ['permissions.groups.bungee.modocheat' => ['$exists' => true]],
                ['permissions.groups.bungee.manager' => ['$exists' => true]],
                ['permissions.groups.bungee.staff' => ['$exists' => true]]
            ]
        ];


        $data = DB::connection('mongodb_server')->collection('players')->where($alt)->orderby('permissions.group')->get();

        return view('section.staff')->with('user', $data);
    }


    public function connection(){

        //Alternate group
        $alt = ['$or' =>
            [
                ['permissions.groups.bungee.superviseur' => ['$exists' => true]],
                ['permissions.groups.bungee.helper' => ['$exists' => true]],
                ['permissions.groups.bungee.modo' => ['$exists' => true]],
                ['permissions.groups.bungee.supermodo' => ['$exists' => true]],
                ['permissions.groups.bungee.modocheat' => ['$exists' => true]],
                ['permissions.groups.bungee.modochat' => ['$exists' => true]]
            ]
        ];


        $Data = DB::connection('mongodb_server')->collection('players')->where($alt)->get();

        $Staff = [];

        foreach ($Data as $player){
            $Time = DB::connection('mongodb_server')
                ->collection('modoSessions')
                ->where('timestamp', '>=', strtotime(date('Y-m')) * 1000)
                ->where('playerUuid', '=', $player['uniqueId'])
                ->sum('totalTime');
            /*$Punish = DB::connection('mongodb_server')
                ->collection('modoSessions')
                ->where('timestamp', '>=', strtotime(date('Y-m')) * 1000)
                ->where('playerUuid', '=', $player['uniqueId'])
                ->sum('punishments');
            $PunishTime = DB::connection('mongodb_server')
                ->collection('modoSessions')
                ->where('timestamp', '>=', strtotime(date('Y-m')) * 1000)
                ->where('playerUuid', '=', $player['uniqueId'])
                ->sum('punishmentTime');*/

            $Detect = false;
            $Grades = ['supermodo', 'modocheat','modo', 'modochat', 'helper'];
            $LTime = [45, 40,40, 35, 25];

            if($Time > 0)
            {
                foreach ($Grades as $k => $G){

                    if (!$Detect){
                        if (isset($player['permissions']['groups']['bungee'][$G])){
                            $Grr = $G;
                            $NTime = $LTime[$k];
                            $Detect = true;

                            $Grr = str_replace("supermodo", "SuperModérateur", $Grr);
                            $Grr = str_replace("modocheat", "Modérateur-Cheat", $Grr);
                            $Grr = str_replace("modochat", "Modérateur-Chat", $Grr);
                            $Grr = str_replace("modo", "Modérateur", $Grr);
                            $Grr = str_replace("helper", "Helper", $Grr);

                            if ($Time == 0){
                                $Time = 1;
                            }

                            if(round(($Time / 60 / 60), 2) / $NTime  * 100 > 100)
                            {
                                $wf = 100;
                            }
                            else
                            {
                                $wf = round(round(($Time / 60 / 60), 2) / $NTime  * 100, 1);
                            }

                            if($wf <= 10) {

                                $bc = "#C24023";
                                $color = "#983019";
                            }
                            else if($wf < $NTime / 2 && $wf > 10)
                            {
                                $bc = "#DCB522";
                                $color = "#B6961D";
                            }
                            else
                            {
                                $bc = "#57BB1E";
                                $color = "#499B1A";
                            }


                        }
                    }
                }



                if (isset($Grr)){
                    array_push($Staff, ['name' => $player['name'],'ntime' => $NTime,'time' => $Time, 'grade' => $Grr, 'workFine' => $wf, 'color' => $color]);
                }
            }
        }

        sort($Staff);

        return view('section.timestaff')->with('user', $Staff);
    }

}