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

        return view('users.view')->with('Player', $Player);
    }


    public function save($uuid){
        $Player = DB::connection('mongodb_server')->collection('players')->where('uniqueId', $uuid)->first();

        if (!empty($_POST['password'])){
            $password = $_POST['password'];

            $salt = substr(hash('whirlpool', uniqid(rand(), true)), 0, 12);
            $hash = strtolower(hash('whirlpool', $salt . $password));
            $saltPos = (strlen($password) >= strlen($hash) ? strlen($hash) - 1 : strlen($password));
            $Player['loginPassword'] =  substr($hash, 0, $saltPos) . $salt . substr($hash, $saltPos);
            unset($Player['_id']);

            DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player, ['upsert' => true]);
        }


        if (!empty($_POST['onlinemode'])){
            $Player['onlineMode'] = $_POST['onlinemode'];
            unset($Player['_id']);

            DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player, ['upsert' => true]);
        }

        if (empty($_POST['authKey'])){
            unset($Player['_id']);
            unset($Player['authKey']);
            DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player, ['upsert' => true]);
        }

        if (!empty($_POST['authKey'])){
            unset($Player['_id']);
            $Player['authKey'] = $_POST['authKey'];
            DB::connection('mongodb_server')->collection('players')->where('name', $Player['name'])->update($Player, ['upsert' => true]);
        }

        return redirect('/profile/' . $uuid);

    }

}