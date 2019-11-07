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
                ['permissions.groups.bungee.staff' => ['$exists' => true]],
                ['permissions.groups.bungee.youtuber' => ['$exists' => true]],
                ['permissions.groups.bungee.miniyoutuber' => ['$exists' => true]],
                ['permissions.groups.bungee.developpeur' => ['$exists' => true]]
            ]
        ];


        $data = DB::connection('mongodb_server')->collection('players')->where($alt)->orderby('permissions.group')->get();

        return view('section.staff')->with('user', $data);
    }


    public function connection(){

        $Date = new \DateTime();
        $date = $Date->format("M");
        $date = str_replace('Jan', 'Janvier', $date);
        $date = str_replace('Feb', 'Février', $date);
        $date = str_replace('Mar', 'Mars', $date);
        $date = str_replace('Apr', 'Avril', $date);
        $date = str_replace('May', 'Mai', $date);
        $date = str_replace('Jun', 'Juin', $date);
        $date = str_replace('Jul', 'Juillet', $date);
        $date = str_replace('Aug', 'Août', $date);
        $date = str_replace('Sep', 'Septembre', $date);
        $date = str_replace('Oct', 'Octobre', $date);
        $date = str_replace('Nov', 'Novembre', $date);
        $date = str_replace('Dec', 'Décembre', $date);

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
            $LTime = [22.5, 20,20, 17.5, 12.5];
            if($Time > 0)
            {
                foreach ($Grades as $k => $G){

                    if (!$Detect){
                        if (isset($player['permissions']['groups']['bungee'][$G])){
                            $Grr = $G;
                            $NTime = $LTime[$k];
                            $Detect = true;

                            if ($Time == 0){
                                $Time = 1;
                            }

                            $wf = round(round(($Time / 60 / 60), 2) / $NTime  * 100, 2);
                            $Paid = $this->getApproximatelyPaid($Grr, $wf);

                            if(round(($Time / 60 / 60), 2) / $NTime  * 100 > 100)
                            {
                                $wf = 100;
                            }

                            if($wf <= 10) {
                                $color = "#e74c3c";
                            }
                            else if($wf >= ($NTime) * 0.90){
                                $color = "#2ecc71";
                            }
                            else{
                                $color = "#e67e22";
                            }



                            $Grr = str_replace("supermodo", "Super Modérateur", $Grr);
                            $Grr = str_replace("modocheat", "Modérateur-Cheat", $Grr);
                            $Grr = str_replace("modochat", "Modérateur-Chat", $Grr);
                            $Grr = str_replace("modo", "Modérateur", $Grr);
                            $Grr = str_replace("helper", "Helper", $Grr);

                        }
                    }
                }



                if (isset($Grr)){
                    array_push($Staff, ['name' => $player['name'],'ntime' => $NTime,'time' => $Time, 'grade' => $Grr, 'workFine' => $wf, 'color' => $color, 'Paid' => $Paid]);
                }
            }
        }

        sort($Staff);

        return view('section.timestaff', ['user' => $Staff, 'month' => $date]);
    }

    public function getApproximatelyPaid($grade, $obj)
    {

        if($obj >= 150)
        {
            $obj = 150;
        }

        $list = [
            '0' => ['name' => 'supermodo', 'paid' => 800],
            '1' => ['name' => 'modocheat', 'paid' => 600],
            '2' => ['name' => 'modo', 'paid' => 400],
            '3' => ['name' => 'modochat', 'paid' => 200],
            '4' => ['name' => 'helper', 'paid' => 0],
        ];

        foreach ($list as $rankList)
        {
            if($rankList['name'] == $grade)
            {
                return $rankList['paid'] * round($obj / 100, 2);
            }
        }

    }

}