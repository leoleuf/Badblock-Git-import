<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class PermissionsController extends Controller
{


    public function index(){

        $Perm = DB::connection('mongodb_server')->collection('permissions')->orderBy('power', 'desc')->get();

        return view('section.permissions', ['data' => $Perm]);
    }

    public function create(){

        return view('section.permcreate', ["id" => uniqid()]);

    }

    public function create_perm(){

        $Perm = DB::connection('mongodb_server')
            ->collection('permissions')
            ->insert($_POST['data']);

    }

    public function edit($uid){

        $Perm = DB::connection('mongodb_server')
            ->collection('permissions')
            ->where('_id', '=',$uid)
            ->first();

        return view('section.permedit', ['data' => $Perm]);
    }

    public function save($uid){

        $Perm = DB::connection('mongodb_server')
            ->collection('permissions')
            ->where('_id', '=',$uid)
            ->first();

        $Perm = DB::connection('mongodb_server')
            ->collection('permissions_backup')
            ->insert([
                'date' => date('Y-m-d H:m:i'),
                'user' => Auth::user()->name,
                'content' => $Perm
            ]);

        unset($_POST['data']['_id']);

        $Perm = DB::connection('mongodb_server')
            ->collection('permissions')
            ->where('_id', '=',$uid)
            ->update($_POST['data']);



    }

}

