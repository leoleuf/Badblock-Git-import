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
use App\User;
use Illuminate\Support\Facades\Auth;

class NotificationsController extends Controller
{

    public function index(){

        return view('section.notification.send_notif');

    }

    public function send(Request $request) {

      DB::insert('INSERT INTO notifications (user_id, title, link, text, active) VALUES (?, ?, ?, ?, ?)', [
        $this->convertPseudoId($request->input('pseudo')),
        $request->input('title'),
        $request->input('link'),
        $request->input('text'),
        1
      ]);
      return redirect('/');
    }

    public static function convertPseudoId($pseudo)
    {
      return DB::table('users')->where('name', $pseudo)->value('id');
    }

    public static function system_send($data, $pseudo)
    {

        DB::table('notifications')->insert([

            'user_id' => self::convertPseudoId($pseudo),
            'title' => $data['title'],
            'link' => $data['link'],
            'icon' => 'https://image.flaticon.com/icons/svg/179/179386.svg',
            'text' => $data['text'],
            'active' => 1,
            'created_at' => date('Y-m-d H:m:s'),
            'updated_at' => date('Y-m-d H:m:s'),
            'click_at' => null

        ]);
    }

    public static function system_send_group($data, $group)
    {
        $group_id = DB::table('roles')->where('name', $group)->get()[0]->id;

        foreach (DB::table('role_users')->where('role_id', $group_id)->get() as $key => $value)
        {
            DB::table('notifications')->insert([

                'user_id' => $value->user_id,
                'title' => $data['title'],
                'link' => $data['link'],
                'icon' => 'https://image.flaticon.com/icons/svg/179/179386.svg',
                'text' => $data['text'],
                'active' => 1,
                'created_at' => date('Y-m-d H:m:s'),
                'updated_at' => date('Y-m-d H:m:s'),
                'click_at' => null

            ]);
        }

    }

}
