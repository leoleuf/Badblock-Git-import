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
            $todolists[$i]->receivers_done = explode(',', $todolists[$i]->receivers_done);
            for($j = 0; $j < count($todolists[$i]->receivers_done); $j++){
                $todolists[$i]->receivers_done[$j] = NotificationsController::convertIDPseudo($todolists[$i]->receivers_done[$j]);
            }
        }

        return view('profil.todolists', ["Todolists" => $todolists, "UserID" => $user_name, "MaxCharactersLength" => $maxCharactersLength]);

    }

    public function done(){

        if(isset($_POST)){
            if(isset($_POST['todoID'])){

                $currentTodo = DB::table('todolists')->where('id', strip_tags($_POST['todoID']))->get();

                if(strlen($currentTodo[0]->receivers_done) == 0){
                    $doneReceiverToInsert = Auth::user()->id;
                }
                else {
                    $doneReceiverToInsert = $currentTodo[0]->receivers_done.", ".Auth::user()->id;
                }

                DB::table('todolists')->where('id', strip_tags($_POST['todoID']))->update([
                   "receivers_done" => $doneReceiverToInsert
                ]);

                DB::table('notifications')->insert([
                    "user_id" => $currentTodo[0]->author,
                    "title" => "Todolist",
                    "text" => Auth::user()->name." a annoncé avoir fini la tâche todolist que vous lui avez assigné",
                    "created_at" => NOW()
                ]);
            }
        }

    }
}
