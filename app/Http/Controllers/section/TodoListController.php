<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 09/02/2019
 * Time: 19:47
 */

namespace App\Http\Controllers\section;

use App\Http\Controllers\section\NotificationsController;
use App\Models\Todo;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;


class TodoListController
{
    public function index(){

        $todoLists = DB::table('todolists')->orderBy('id', 'desc')->get();
        $users = DB::table('users')->select('name')->get()->toArray();

        for($i = 0; $i < count($todoLists); $i++) {
            $todoLists[$i]->author = NotificationsController::convertIDPseudo($todoLists[$i]->author);
            $todoLists[$i]->receivers = explode(',', $todoLists[$i]->receivers);
            for($j = 0; $j < count($todoLists[$i]->receivers); $j++){
                $todoLists[$i]->receivers[$j] = NotificationsController::convertIDPseudo($todoLists[$i]->receivers[$j]);
            }
        }

        for($i = 0; $i < count($users); $i++) {
            $users[$i] = $users[$i]->name;
        }
        usort($users, "strnatcasecmp");

        //Définit le maximum de caractères affichés dans un card
        $maxCharacterLength = 200;

        //Utiliser la fonction explode() pour récupérer tous les utilisateurs à qui la todolist à été envoyée

        return view('section.todolist', ["Todolists" => $todoLists, "Users" => $users, "MaxCharacterLength" => $maxCharacterLength]);
    }

    /*
         id INT 255 PRIMARY AUTO_INCREMENT
         author VARCHAR 255
         title VARCHAR 255
         content LONGTEXT
         deadline DATE
         priority INT 255
         receivers LONGTEXT
         */

    public function createOrModifyTodo(){

        if(isset($_POST['receivers'])) {
            foreach ($_POST['receivers'] as $i => $receiver) {
                $_POST["receivers"][$i] = strip_tags(NotificationsController::convertPseudoId($receiver));
            }
        }

        //Utiliser la fonction implode() pour créer un string à partir de l'array receivers

        if(isset($_POST['comesFrom'])){

            if($_POST['comesFrom'] == "create"){
                DB::table('todolists')->insert([
                    'title' => substr(strip_tags($_POST['title']), 0, 254),
                    'author' => NotificationsController::convertPseudoId(Auth::user()->name),
                    'content' => strip_tags($_POST['content']),
                    'started_at' => NOW(),
                    'deadline' => strip_tags($_POST['deadline']),
                    'priority' => strip_tags($_POST['priority']),
                    'receivers' => implode(",", $_POST["receivers"])
                ]);
            }

            elseif($_POST['comesFrom'] == "modify"){
                DB::table('todolists')->where('id', strip_tags($_POST['todoID']))->update([
                    'title' => substr(strip_tags($_POST['title']), 0, 254),
                    'author' => NotificationsController::convertPseudoId(Auth::user()->name),
                    'content' => strip_tags($_POST['content']),
                    'started_at' => NOW(),
                    'deadline' => strip_tags($_POST['deadline']),
                    'priority' => strip_tags($_POST['priority']),
                    'receivers' => implode(",", $_POST["receivers"])
                ]);
            }

            elseif($_POST['comesFrom'] == "delete"){
                DB::table('todolists')->where('id', strip_tags($_POST['todoID']))->delete();
            }

        }

    }
}