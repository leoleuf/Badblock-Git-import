<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 01/06/2019
 * Time: 21:28
 */

namespace App\Http\Controllers\others;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class SetGradeOnNewServerController extends Controller
{

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
    }

}