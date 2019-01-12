<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/01/2018
 * Time: 15:10
 */

namespace App\Http\Controllers\profile;


use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use App\Models\Operation;
use App\Models\Product;


class ActionController extends Controller
{


    public function resetPassword($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        $Player['loginPassword'] = "";
        unset($Player['_id']);

        DB::connection('mongodb')->collection('profile_logs')->insert([
            'date' => date('Y-m-d H:i:s'),
            'action' => "Password reset !",
            'uniqueId' => $uuid,
            'user' => Auth::user()->name
        ]);

        DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player);
    }

    public function resetTfa($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        unset($Player['_id']);
        $Player['authKey'] = "";

        DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player);

        DB::connection('mongodb')->collection('profile_logs')->insert([
            'date' => date('Y-m-d H:i:s'),
            'action' => "TFA reset",
            'uniqueId' => $uuid,
            'user' => Auth::user()->name
        ]);

        return "OK";
    }

    public function resetOm($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        $Player['onlineMode'] = false;
        unset($Player['_id']);
        DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player);

        DB::connection('mongodb')->collection('profile_logs')->insert([
            'date' => date('Y-m-d H:i:s'),
            'action' => "Actived offline mode",
            'uniqueId' => $uuid,
            'user' => Auth::user()->name
        ]);

        return "OK";
    }

    public function resetOl($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        $Player['onlineMode'] = true;
        unset($Player['_id']);
        DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player);

        DB::connection('mongodb')->collection('profile_logs')->insert([
            'date' => date('Y-m-d H:i:s'),
            'action' => "Actived online mode",
            'uniqueId' => $uuid,
            'user' => Auth::user()->name
        ]);

        return "OK";
    }

}