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
use Psr\Http\Message\RequestInterface;

class ModController extends Controller
{


    public function preuves(){

        $Sanctions = DB::connection('mongodb_server')->collection('punishments')
            ->where('proof', '=', [])
            ->where('punisher', '!=', "Console")
            ->where(function ($query) {
                $query->where('type', '=', "MUTE")
                    ->orWhere('type', '=', "KICK")
                    ->orWhere('type', '=', "BAN");
            })
            ->orderBy('timestamp', 'DESC')
            ->take(200)
            ->get()
            ->toArray();

        foreach ($Sanctions as $k => $san){
            $user = DB::connection('mongodb_server')->collection('players')->where('uniqueId' ,'=', $san['punishedUuid'])->first();
            $Sanctions[$k]['pseudo'] = $user['name'];
        }

        return view('section.mod.nopreuve', ['Sanctions' => $Sanctions]);

    }

    public function notif() {

        DB::table('notifications')->insert([
            'user_id' => NotificationsController::convertPseudoId($_POST['banner']),
            'title' => "Preuve(s) !",
            'link' => '/moderation',
            'icon' => 'https://image.flaticon.com/icons/svg/179/179386.svg',
            'text' => 'Oublie de preuve. Merci de corriger.',
            'active' => 1
        ]);

    }

    public function checked() {

        $Sanctions = DB::connection('mongodb_server')->collection('punishments')
            ->where('proof', '=', [])
            ->where('punisher', '!=', "Console")
            ->where(function ($query) {
                $query->where('type', '=', "MUTE")
                    ->orWhere('type', '=', "KICK")
                    ->orWhere('type', '=', "BAN");
            })
            ->orderBy('timestamp', 'DESC')
            ->take(200)
            ->get()
            ->toArray();

        $Sanction_ID = $Sanctions[$_POST["id"]]["_id"];

        $Screens = [
            "ip" => "127.0.0.1",
            "user" => 1,
            "date" => "2019-16-05 19:05",
            "file_name" => 'https://cdn.badblock.fr/upload/SNOWICE15580270250.png'
        ];

        $Insert = [
            'sanction_id' => $Sanction_ID,
            'date' => date('Y-m-d H:i:s'),
            'screens' => $Screens,
            'note' => "Rien"
        ];

        DB::connection('mongodb_server')->collection('sanctions')->insert($Insert);


        $Id = DB::connection('mongodb_server')->collection('sanctions')->where('sanction_id','=', $Sanction_ID)->first();

        DB::connection('mongodb_server')->collection('punishments')
            ->where('uuid', '=', $Sanction_ID)
            ->update([
                'proof' => $Id['_id']
            ]);

    }

}