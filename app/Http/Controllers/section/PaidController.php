<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class PaidController extends Controller
{


    public function index($section){

        $sections = ["forum","redaction","moderation","graphisme","animation","developpement"];

        $ar_paid = ['25',"12","13","27","26","24","4","29"];
        $ar_paidpb = ['25' => 300,"12" => 200,"13" => 100,"27" => 300,"26" => 300,"24" => 300,"4" => 500,"29" => 500];

        $group_rel = ["forum" => 8,"redaction" => 24,"moderation" => "25 OR user_group_id = 12 OR user_group_id = 13","graphisme" => "26 OR user_group_id = 4","animation" => "27 OR user_group_id = 29","developpement" => 14];

        if (in_array($section, $sections)){
            $result = DB::connection('mysql_forum')->select("select * from xf_user where user_group_id = " . $group_rel[$section] . " ORDER by username");
        }
        foreach ($result as $row){
            if (in_array($row->user_group_id, $ar_paid)){
                $row->pb = $ar_paidpb[$row->user_group_id];
            }else{
                $row->pb = 100;
            }
        }

        return view('section.paid', ['user' => $result,'name' => $section]);
    }

    public function save($section, Request $request){

        $sections = ["forum","redaction","moderation","graphisme","animation"];

        $group_rel = ["forum" => 8,"redaction" => 24,"moderation","graphisme" => 26,"animation" => 27,"developpement" => 14];

        if (in_array($section, $sections)){
            $result = DB::connection('mysql_forum')->select("select * from xf_user where user_group_id = " . $group_rel[$section]);
            //Foreach pour les paie
            foreach ($result as $row){
                if (!empty($request->input('pb_'. $row->user_id))){

                    //Enregistrement de l'opération
                    $Funds = new Funds;
                    $Funds->points = intval($request->input('pb_'. $row->user_id));
                    $Funds->price = 0;
                    $Funds->name = $row->username;
                    $Funds->gateway = "badblock";
                    $Funds->comment = "Paie du" . date("Y-m") . " Commentaire : " . $request->input('comment_'. $row->user_id);
                    $Funds->date = date("Y-m-d h:i:s");
                    $Funds->save();



                }
            }

        }

        return redirect('/');
    }

}