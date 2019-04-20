<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 13/04/2019
 * Time: 21:57
 */

namespace App\Http\Controllers\Infra;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;

class ServerManageController extends Controller
{
    public function index()
    {
        return view('infra.server_manage', ['data' => DB::connection('mongodb_server')->collection('serverInfo')->get()]);
    }

    public function motd(Request $request)
    {
        DB::connection('mongodb_server')->collection('serverInfo')->update([

            'description' => $request->input('motd')

        ]);
    }

}