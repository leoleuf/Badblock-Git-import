<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class ModerationController extends Controller
{

    public function index(){

        return view('section.mod.moderation');

    }

    public function screen(){
        $Screen = DB::connection('mongodb')->collection('log_upload')->where('user', '=', Auth::user()->id)->orderBy('date', 'DESC')->take(10)->get();

        return json_encode($Screen);
    }

    public function sanction(){
        $Sanctions = DB::connection('mysql_casier')->table('sanctions')
            ->where('banner', '=', strtolower(Auth::user()->name))
            ->where('proof', '=', NULL)
            ->where(function ($query) {
                $query->where('type', '=', "ban")
                    ->orWhere('type', '=', "mute")
                    ->orWhere('type', '=', "warn")
                    ->orWhere('type', '=', "tempbanip")
                    ->orWhere('type', '=', "tempban");
            })
            ->orderBy('timestamp', 'DESC')
            ->take(10)
            ->get();

        return json_encode($Sanctions);
    }

    public function union(){


        $Screens = json_decode($_POST["screens"]);
        $Parse_Src = [];

        foreach ($Screens as $src){
            $Img = DB::connection('mongodb')->collection('log_upload')->find($src);
            if ($Img != null){
                array_push($Parse_Src, $Img);
            }
        }

        $Insert = [
            'sanction_id' => $_POST['sanc_id'],
            'date' => date('Y-m-d H:i:s'),
            'screens' => $Parse_Src,
            'note' => ''
        ];

        DB::connection('mongodb')->collection('sanctions')->insert($Insert);


        $Id = DB::connection('mongodb')->collection('sanctions')->where('sanction_id','=', $_POST['sanc_id'])->first();

        DB::connection('mysql_casier')->table('sanctions')
            ->where('id', '=', $_POST['sanc_id'])
            ->update([
                'proof' => $Id['_id']
            ]);


        return "[]";

    }

}