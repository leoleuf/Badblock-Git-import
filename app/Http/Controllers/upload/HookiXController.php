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

        if($request->hasFile('profile_image')) {

            //get filename with extension
            $filenamewithextension = $request->file('profile_image')->getClientOriginalName();

            //get filename without extension
            $filename = strtoupper(Auth::user()->name).time().'.'.request()->profile_image->getClientOriginalExtension();
            $filename = str_replace("_", "", $filename);

            //get file extension
            $extension = $request->file('profile_image')->getClientOriginalExtension();


            //Upload File to external server
            Storage::disk('ftp')->put($filename, fopen($request->file('profile_image'), 'r+'));

            //Store $filenametostore in the database
        }

        DB::table('hookiX')->insert([

            'user_id' => Auth::user()->id,
            'name' => $filename,
            'link' => 'https://cdn.badblock.fr/upload/'.$filename,
            'date_post' => date("Y-m-d H:m:s")

        ]);

        DB::connection('mongodb')->collection('log_upload')->insert([
            'ip' => \Request::ip(),
            'user' => strtolower(Auth::user()->name),
            'date' => date('Y-m-d H:m:s'),
            'file_name' => $filename
        ]);

        $back = back()->with('status', "Nom de votre Image : <strong>".$filename."</strong> Lien : <a href=\"https://cdn.badblock.fr/upload/".$filename."\">Screen</a>")->with('img', $filename);

        return $back;
    }

    public function list_gallery()
    {
        return view('profil.gallery', ['data' => DB::table('hookiX')->where('user_id', Auth::user()->id)->get()]);
    }

}
