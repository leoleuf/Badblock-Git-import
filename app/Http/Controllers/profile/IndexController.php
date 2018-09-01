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
use Illuminate\Support\Facades\DB;
use App\Models\Operation;
use App\Models\Product;


class IndexController extends Controller
{
    public function index(){

        return view('users.search');
    }


    public function search(){

        $Player = DB::connection('mongodb_server')->collection('players')->where('name', 'like', strtolower($_POST['search_player']) .'%')->take(10)->get();
        $json = [];

        foreach ($Player as $p){
            array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
        }

        return json_encode($json);
    }

    public function searchip(){

        $Player = DB::connection('mongodb_server')->collection('players')->where('lastIp', 'like', $_POST['search_player'] .'%')->take(10)->get();
        $json = [];

        foreach ($Player as $p){
            array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
        }

        return json_encode($json);
    }


    public function profile($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        if ($Player == null){
            return redirect('/');
        }

        return view('users.view')->with('Player', $Player);
    }

}