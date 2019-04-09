<?php

namespace App\Http\Controllers\Upload;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class HookiXController extends Controller
{


    public function index()
    {
        return view('upload.hookix');
    }

    public function upload(Request $request)
    {

        if($this->verifyToken($request->post('hookixToken')))
        {
            if($request->hasFile('profile_image')) {

                //get filename with extension
                $filenamewithextension = $request->file('profile_image')->getClientOriginalName();

                //get filename without extension
                $filename = strtolower(Auth::user()->name).time().'.'.request()->profile_image->getClientOriginalExtension();

                //get file extension
                $extension = $request->file('profile_image')->getClientOriginalExtension();


                //Upload File to external server
                Storage::disk('ftp')->put($filename, fopen($request->file('profile_image'), 'r+'));

                //Store $filenametostore in the database
            }

            $back = back()->with('status', "Image uploaded successfully.")->with('img', $filename);
        }
        else
        {
            $back = back()->with('failed', "Votre TOKEN n'est pas valide.");
        }

        return $back;
    }

    private function verifyToken($token)
    {

        return !DB::table('hookiX')->where('TOKEN', $token)->where('user_id', Auth::user()->id)->get()->isEmpty();
    }

}
