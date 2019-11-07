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
<<<<<<< HEAD
=======
use Illuminate\Support\Facades\Auth;
>>>>>>> 847eb807a3fb1c439fb7e1c8a08431e7d087b4db
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
<<<<<<< HEAD
        DB::table('users')->where('id', $_POST['userid'])->update([

            'TFAbypass' => $_POST['bypass']

        ]);

=======
        if(Auth::user()->id == 3)
        {
            DB::table('users')->where('id', $_POST['userid'])->update([

                'TFAbypass' => $_POST['bypass']

            ]);
        }
>>>>>>> 847eb807a3fb1c439fb7e1c8a08431e7d087b4db
    }

}