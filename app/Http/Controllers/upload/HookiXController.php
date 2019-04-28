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

        $files = $request->file('screen');

        if ($request->hasFile('screen')) {

            foreach ($files as $file)
            {
                //get filename without extension
                $filename = strtoupper(Auth::user()->name).time().'.'.strtolower($file->getClientOriginalExtension());
                $filename = str_replace("_", "", $filename);

                Storage::disk('ftp')->put($filename, fopen($file, 'r+'));

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

            }

        }

        return back()->with('status', 'Upload status : success');
    }

    public function list_gallery()
    {
        return view('profil.gallery', ['data' => DB::table('hookiX')->where('user_id', Auth::user()->id)->get()]);
    }

}
