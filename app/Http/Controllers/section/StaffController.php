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
                ['permissions.alternateGroups.modoforum' => ['$exists' => true]],
                ['permissions.alternateGroups.graphiste' => ['$exists' => true]],
                ['permissions.alternateGroups.redacteur' => ['$exists' => true]],
                ['permissions.alternateGroups.modocheat' => ['$exists' => true]],
                ['permissions.alternateGroups.manager' => ['$exists' => true]],
                ['permissions.alternateGroups.staff' => ['$exists' => true]]
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
                ['permissions.groups.bungee.admin' => ['$exists' => true]],
                ['permissions.groups.bungee.modo' => ['$exists' => true]],
                ['permissions.groups.bungee.supermodo' => ['$exists' => true]],
                ['permissions.groups.bungee.responsable' => ['$exists' => true]],
                ['permissions.groups.bungee.modocheat' => ['$exists' => true]],
                ['permissions.groups.bungee.modochat' => ['$exists' => true]],
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


        $Data = DB::connection('mongodb_server')->collection('players')->where($alt)->get();

        $Staff = [];

        foreach ($Data as $player){
            $Time = DB::connection('mongodb_server')
                ->collection('modoSessions')
                ->where('timestamp', '>=', strtotime(date('Y-m')) * 1000)
                ->where('playerUuid', '=', $player['uniqueId'])
                ->sum('totalTime');
            $Detect = false;
            $Grades = ['supermodo', 'modo', 'modochat', 'helper'];

            foreach ($Grades as $G){
                if (!$Detect){
                    if (isset($player['permissions']['groups']['bungee'][$G])){
                        $Grr = $G;
                        $Detect = true;
                    }
                }
            }
            if (!isset($Grr)){
                $Grr = "Staff";
            }

            array_push($Staff, ['name' => $player['name'], 'time' => $Time, 'grade' => $Grr]);
        }

        usort($Staff, create_function('$a, $b', '
        $a = $a["time"];
        $b = $b["time"];

        if ($a == $b) return 0;

        $direction = strtolower(trim("desc"));

        return ($a ' . ("desc" == 'desc' ? '>' : '<') .' $b) ? -1 : 1;
        '));

        return view('section.timestaff')->with('user', $Staff);
    }

}