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

class TfaController extends Controller
{


    public function index(){
        //Alternate group
        return view('section.tfa', ['user' => DB::table('users')->get()]);
    }

    public static function getUserById($id)
    {
        return DB::table('users')->where('id', $id)->get()[0]->name;
    }

    public static function checkActiveTFA($user_id)
    {
        return DB::table('password_securities')->where('user_id', $user_id)->get()->isEmpty();
    }

}