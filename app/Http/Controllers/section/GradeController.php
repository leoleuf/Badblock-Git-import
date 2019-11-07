<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 03/06/2019
 * Time: 15:22
 */

namespace App\Http\Controllers\section;


use App\Http\Controllers\Controller;

class GradeController extends Controller
{

    public function index(){



    }

    public function search(){

        $refServer = "bungee";

        //To add player's grade, best config is 25 25
        $NumberToGetInOneSubRow = 25;
        $NumberToGetInOneRow = 25;
        $servers = array(
            'bungee', 'survie', 'pvpbox', 'freebuild', 'faction', 'minigames', 'skyblock', 'rushffa'
        );
        $Players = array();

        $where = ['$and' =>
            [
                ['permissions.groups.' . $refServer . '.vip' => ['$exists' => true]]
            ]
        ];

        for ($i = $NumberToGetInOneRow * $row; $i < $NumberToGetInOneRow * ($row + 1); $i++) {
            $temp = DB::connection('mongodb_server')->collection('players')->where($where)->select('name', 'permissions')->skip($NumberToGetInOneSubRow * $i)->take($NumberToGetInOneSubRow)->get();
            if ($temp->isEmpty()) {
                break;
            }
            array_push($Players, $temp);
            if($temp->count() < $NumberToGetInOneSubRow){
                dd($Players);
            }
        }

    }
}