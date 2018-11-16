<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;



class LoginController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //Récupération des serveur de l'utilisateur
        $data = DB::select("select * from logs where user_id = ? ORDER by date DESC LIMIT 20", [Auth::user()->id]);

        return view('panel.login',['data' => $data]);
    }





}
