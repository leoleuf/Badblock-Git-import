<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

class ModController extends Controller
{


    public function preuves(){

        $Sanctions = DB::connection('mongodb_server')->collection('punishments')
            ->where('proof', '=', [])
            ->where(function ($query) {
                $query->where('type', '=', "MUTE")
                    ->orWhere('type', '=', "KICK")
                    ->orWhere('type', '=', "UNBAN")
                    ->orWhere('type', '=', "BAN")
                    ->orWhere('type', '=', "WARN");
            })
            ->orderBy('timestamp', 'DESC')
            ->take(10)
            ->get()
            ->toArray();


        return view('section.mod.nopreuve', ['Sanctions' => $Sanctions]);

    }

}