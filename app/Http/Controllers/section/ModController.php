<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class ModController extends Controller
{


    public function preuves(){

        $Sanctions = DB::connection('mongodb_server')->collection('punishments')
            ->where(function($query){

                $where = ['$or' =>
                    [
                        ['permissions.groups.bungee.modochat' => ['$exists' => true]],
                        ['permissions.groups.bungee.modo' => ['$exists' => true]],
                        ['permissions.groups.bungee.modocheat' => ['$exists' => true]],
                        ['permissions.groups.bungee.supermodo' => ['$exists' => true]]
                    ]
                ];

                $staff = DB::connection('mongodb_server')->collection('players')->where($where)->select('name')->get();

                $query->where('punisher', '=', $staff[0]['name']);
                foreach ($staff as $player){
                    $query->orWhere('punisher', 'like', $player['name']);
                }
            })
            ->where('proof', '=', [])
            ->where('punisher', '!=', "Console")
            ->where(function ($query) {
                $query->orWhere('type', '=', "KICK")
                    ->orWhere('type', '=', "BAN");
            })
            ->where('timestamp', '>=', time()*1000-1209600000)
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

    public function checked(Request $request) {

        $Sanctions = DB::connection('mongodb_server')->collection('punishments')
            ->where('proof', '=', [])
            ->where('punisher', '!=', "Console")
            ->where(function ($query) {
                $query->Where('type', '=', "KICK")
                    ->orWhere('type', '=', "BAN");
            })
            ->orderBy('timestamp', 'DESC')
            ->get()
            ->toArray();

        $Sanction_ID = $Sanctions[$_POST["id"]]["_id"];

        $Screens = [
            "ip" => $request->ip(),
            "user" => Auth::user()->id,
            "date" => date("d/m/Y G")."h".date("i:s"),
            "file_name" => ''
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

    public function top(){

        $where = ['$or' =>
            [
                ['permissions.groups.bungee.modochat' => ['$exists' => true]],
                ['permissions.groups.bungee.modo' => ['$exists' => true]],
                ['permissions.groups.bungee.modocheat' => ['$exists' => true]],
                ['permissions.groups.bungee.supermodo' => ['$exists' => true]]
            ]
        ];

        $staff = DB::connection('mongodb_server')->collection('players')->where($where)->select('name')->get();

        $result = array();

        foreach ($staff as $player){
            $result[$player['name']] = DB::connection('mongodb_server')->collection('punishments')
                ->where('proof', '=', [])
                ->where('punisher', 'like', $player['name'])
                ->where(function ($query) {
                    $query->Where('type', '=', "KICK")
                        ->orWhere('type', '=', "BAN");
                })
                ->where('timestamp', '>=', time()*1000-1209600000)
                ->orderBy('timestamp', 'DESC')
                ->count();
        }

        foreach ($result as $key => $counter){
            if($counter == 0){
                unset($result[$key]);
            }
        }

        return view('section.mod.top', ['result' => $result]);

    }

}