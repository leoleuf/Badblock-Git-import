<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 13/06/2019
 * Time: 17:49
 */

namespace App\Http\Controllers\others;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class BordelController extends Controller
{

    public function starPerm(){

        $where = ['$or' =>
            [
                ['permissions.permissions.bungee.*' => ['$exists' => true]]
            ]
        ];

        $Players = DB::connection('mongodb_server')->collection('players')->where($where)->orderBy('name', 'ASC')->get();

        dd($Players);

        return view('others.starPerm', ['Players' => $Players]);
    }

}