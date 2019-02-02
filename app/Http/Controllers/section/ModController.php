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
use Illuminate\Http\Request;
use App\Http\Controllers\NotificationController;

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
            ->take(100)
            ->get()
            ->toArray();


        return view('section.mod.nopreuve', ['Sanctions' => $Sanctions]);

    }

    public function notif(Request $request) {

        DB::table('notifications')->insert([
            'user_id' => NotificationsController::convertPseudoId($request->input('punisher')),
            'title' => "Preuve",
            'link' => '/moderation',
            'icon' => 'https://image.flaticon.com/icons/svg/179/179386.svg',
            'text' => 'Vous venez de recevoir un avertissement.',
            'active' => 1

        ]);

    }

}