<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;



class VerifyController extends Controller
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

    public function pleaseverify($id)
    {
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            return view('panel.verify', ['data' => $server[0]]);
        }else{
            return redirect("/dashboard");
        }
    }

}
