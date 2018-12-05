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

        $PlayerOne = DB::connection('mongodb_server')->collection('players')->where('name', '=', strtolower($_POST['search_player']))->first();
        $Player = DB::connection('mongodb_server')->collection('players')->where('name', 'like', strtolower($_POST['search_player']) .'%')->take(10)->get();
        $json = [];

        if ($PlayerOne != null){
            array_push($json, ['name' => $PlayerOne['name'], 'uniqueId' => $PlayerOne['uniqueId']]);
            foreach ($Player as $p){
                if ($p['name'] != $PlayerOne['name']){
                    array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
                }
            }

        }else{
            foreach ($Player as $p){
                array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
            }
        }



        return json_encode($json);
    }

    public function searchip(){
        $PlayerOne = DB::connection('mongodb_server')->collection('players')->where('lastIp', 'like', $_POST['search_player'] .'%')->first();
        $Player = DB::connection('mongodb_server')->collection('players')->where('lastIp', 'like', $_POST['search_player'] .'%')->take(10)->get();
        $json = [];

        if ($PlayerOne != null){
            array_push($json, ['name' => $PlayerOne['name'], 'uniqueId' => $PlayerOne['uniqueId']]);
            foreach ($Player as $p){
                if ($p['name'] != $PlayerOne['name']){
                    array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
                }
            }

        }else{
            foreach ($Player as $p){
                array_push($json, ['name' => $p['name'], 'uniqueId' => $p['uniqueId']]);
            }
        }
        return json_encode($json);
    }


    public function profile($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        if ($Player == null){
            return redirect('/');
        }

        //Get if connected
        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL,            "http://node01-int.clusprv.badblock-network.fr:8080/players/isConnected/" );
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );
        curl_setopt($ch, CURLOPT_POST,           1 );
        curl_setopt($ch, CURLOPT_POSTFIELDS,     "name=". $Player['name']);
        curl_setopt($ch, CURLOPT_HTTPHEADER,     array('Content-Type: application/json'));

        $result = curl_exec ($ch);

        $Player['online'] = json_decode($result)->connected;

        $Logs = DB::connection('mongodb')->collection('profile_logs')->where('uniqueId', $Player['uniqueId'])->orderBy('data', 'ASC')->get();

        return view('users.view')->with('Player', $Player)->with('Logs', $Logs);
    }




}