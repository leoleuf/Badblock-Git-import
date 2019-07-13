<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
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

    public function resetTFA(Request $request)
    {
        DB::table('password_securities')->where('user_id', $request->post('user_id'))->delete();

        return back();

    }

    public function bypassTFA()
    {
        if(Auth::user()->id == 3)
        {
            DB::table('users')->where('id', $_POST['userid'])->update([

                'TFAbypass' => $_POST['bypass']

            ]);
        }
    }

}