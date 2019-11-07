<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 18/05/2019
 * Time: 01:23
 *

namespace App\Http\Controllers\others;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class ClearGradePersoController extends Controller
{

    public function index()
    {


        $where = ['$or' =>
            [
                ['permissions.groups.bungee.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.minigames.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.pvpbox.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.skyblock.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.freebuild.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.survie.gradeperso' => ['$exists' => true, '$lte' => 0]],
                ['permissions.groups.faction.gradeperso' => ['$exists' => true, '$lte' => 0]]
            ]
        ];

        $PlayersWithGradePerso = DB::connection('mongodb')->collection('players')->where($where)->orderBy('name', 'ASC')->get();

        return view('others.clearGradePerso', ['Players' => $PlayersWithGradePerso]);

    }
}*/