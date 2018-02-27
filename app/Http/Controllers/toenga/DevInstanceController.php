<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 14/01/2018
 * Time: 15:47
 */

namespace App\Http\Controllers\toenga;
use Illuminate\Support\Facades\Redis;
Use Illuminate\Http\Request;



class DevInstanceController
{

    public function index(){
        return view('toenga.devindex');
    }


    public function tokenWS($uuid){
        $token = bin2hex(random_bytes(50));

        Redis::set('toenga:'. $uuid .':console:token',$token);
        Redis::set('toenga:'. $uuid .':console:user'. $token, Auth::user()->name);
        Redis::set('toenga:'. $uuid .':console:ip', Request::ip());




        return response()->json([
            'host' => 'node02-dev.cluster.badblock-network.fr:40899',
            'token' => $token,
        ]);
    }









}