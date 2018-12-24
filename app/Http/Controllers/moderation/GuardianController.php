<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class GuardianController extends Controller
{
    public function view($id){

        $Guardian = DB::connection('mysql_guardian')->table('logs')->where('id', '=', $id)->first();

        if ($Guardian == null){
            return redirect("/players");
        }

        return view("section.mod.guardian")->with('Data', $Guardian);
    }

}