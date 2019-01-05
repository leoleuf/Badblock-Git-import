<?php

/**
 * Created by Atom.
 * User: Hooki
 * Date: 05/01/2019
 * Time: 22:14
 */

namespace App\Http\Controllers\section;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class WarningController extends Controller
{

    public function index(){

        return view('section.warning.send_warning');

    }

    public function list()
    {
      return view('section.warning.list');
    }

    public function display($id)
    {
      $user = DB::table('warning')->where('id', '=', $id)->get();
      return view('section.warning.display', ['user' => $user]);
    }

    public function delete($id)
    {
      DB::table('warning')->where('id', '=', $id)->delete();
      return redirect('/section/avertissement-list');
    }

    public function send(Request $request)
    {

      DB::insert('INSERT INTO warning (warn_by, pseudo, title, text, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)', [
        $request->input('warn_by'),
        $request->input('pseudo'),
        $request->input('title'),
        $request->input('text'),
        date('Y:m:d H:m:s'),
        date('Y:m:d H:m:s')
      ]);
      return redirect('/');

    }

}
