<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 20/03/2019
 * Time: 22:35
 */

namespace App\Http\Controllers\Animation;

use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class AnimationController extends Controller
{
    public function index()
    {

        $list = DB::connection('mongodb')->collection('automessages')->get();

        if(!isset($list[0]['message'])) {
            //S'il n'y a rien en base de données, on créer un array vide, qui empêche une génération d'erreur parce que l'offset 0 n'existe pas
            $list = array();
            $list[0]["message"] = array();
        }

        return view('section.animation.msg', ['list' => $list[0]['message']]);
    }

    public function setIgMsg(Request $request)
    {
        $list = DB::connection('mongodb')->collection('automessages')->get()[0]['message'];

        array_push($list, $request->input('msg'));

        DB::connection('mongodb')->collection('automessages')->update(['message' => $list]);
        return redirect('/animation/msg-anim');
    }

    public function changeMessage(Request $request){

        $list = DB::connection('mongodb')->collection('automessages')->get()[0]['message'];

        $list[$request->input('newMessage_key')] = $request->input('newMessage');

        DB::connection('mongodb')->collection('automessages')->update(['message' => $list]);
        return redirect('/animation/msg-anim');
    }

    public function deleteMessage(Request $request){

        $list = DB::connection('mongodb')->collection('automessages')->get()[0]['message'];
        $messageToDelete = $request->input('deleteMessage_ID');

        unset($list[$messageToDelete]);

        foreach ($list as $key => $message){

            if($key > $messageToDelete){
                $list[$key-1] = $list[$key];
            }
            unset($list[$key]);
        }

        DB::connection('mongodb')->collection('automessages')->update(['message' => $list]);
        return redirect('/animation/msg-anim');
    }

}