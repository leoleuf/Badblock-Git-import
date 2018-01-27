<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 14/01/2018
 * Time: 15:47
 */

namespace App\Http\Controllers\toenga;


class HomeController
{

    public function index(){
        return view('toenga.index');
    }


    public function treeroot($name){
        if ($name === "epic"){
            $data = [[
                'text' => 'SkyBlock',
                'children' => true
            ],[
                'text' => 'Faction',
                'children' => true
            ],[
                'text' => 'Box',
                'children' => true
            ],[
                'text' => 'Freebuild',
                'children' => false
            ]];
            return response()->json($data);
        }else{
            return response()->json([
                'text' => 'SkyBlock',
                'children' => true
            ]);
        }

    }


    public function treechild($name){

        if ($name === "epic"){
            $data = ["Child 1"];
            return response()->json($data);
        }

    }




    public function devhome(){

        return view('toenga.devindex');



    }







}