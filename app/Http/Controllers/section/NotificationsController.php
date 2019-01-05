<?php

/**
 * Created by Atom.
 * User: Hooki
 * Date: 05/01/2019
 * Time: 18:20
 */

namespace App\Http\Controllers\section;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class NotificationsController extends Controller
{

    public function index(){

        return view('section.notification.send_notif');

    }

    public function send(Request $request) {

      DB::insert('INSERT INTO notifications (user_id, title, link, text, active, created_at, updated_at, click_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)', [
        $this->convertPseudoId($request->input('pseudo')),
        $request->input('title'),
        $request->input('link'),
        $request->input('text'),
        1,
        date('Y:m:d H:m:s'),
        date('Y:m:d H:m:s'),
        date('Y:m:d H:m:s')
      ]);
      return $this->index();
    }

    private function convertPseudoId($pseudo)
    {
      return DB::table('users')->where('name', $pseudo)->value('id');
    }

}
