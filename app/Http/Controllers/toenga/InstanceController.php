<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 14/01/2018
 * Time: 15:47
 */

namespace App\Http\Controllers\toenga;
use Illuminate\Support\Facades\Redis;
Use Illuminate\Support\Facades\Request;
Use Illuminate\Support\Facades\Auth;



class InstanceController
{

    public function index($uid){
        return view('toenga.instance')->with("uuid",$uid);
    }


    public function tokenWS($uid){
        $token = base64_encode(bin2hex(random_bytes(50)));
        $key = base64_encode(bin2hex(random_bytes(30)));
        $perm = 2;

        Redis::set('toenga:'. $token .':console',json_encode(["name" => $uid,"user" => Auth::user()->name,"ip" => Request::ip(),"perm" => $perm,'key' => $key]));
        Redis::expire('toenga:'. $token .':console', 30);



        return response()->json([
            'host' => 'localhost:8090/ws',
            'token' => $token,
            'perm' => $perm,
            'key' => $key,
        ]);
    }









}