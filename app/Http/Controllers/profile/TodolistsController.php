<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 13/02/2019
 * Time: 16:54
 */

namespace App\Http\Controllers\profile;


use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\section\NotificationsController;

class TodolistsController
{
    public function index(){

        $todolists = DB::table('todolists')->get();
        $user_name = Auth::user()->name;
        $maxCharactersLength = 200;

        for($i = 0; $i < count($todolists); $i++) {
            $todolists[$i]->author = NotificationsController::convertIDPseudo($todolists[$i]->author);
            $todolists[$i]->receivers = explode(',', $todolists[$i]->receivers);
            for($j = 0; $j < count($todolists[$i]->receivers); $j++){
                $todolists[$i]->receivers[$j] = NotificationsController::convertIDPseudo($todolists[$i]->receivers[$j]);
            }
        }

        return view('profil.todolists', ["Todolists" => $todolists, "UserID" => $user_name, "MaxCharactersLength" => $maxCharactersLength]);

    }
}
