<?php

namespace App\Http\Controllers\Upload;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Storage;
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
            $filename = time().'.'.request()->profile_image->getClientOriginalExtension();
                //pathinfo($filenamewithextension, PATHINFO_FILENAME);

            //get file extension
            $extension = $request->file('profile_image')->getClientOriginalExtension();

            //filename to store
            $filenametostore = $filename.'_'.uniqid().'.'.$extension;

            //Upload File to external server
            Storage::disk('ftp')->put($filenametostore, fopen($request->file('profile_image'), 'r+'));

            //Store $filenametostore in the database
        }

        return back()->with('status', "Image uploaded successfully.")->with('img', $filename);
    }


}
