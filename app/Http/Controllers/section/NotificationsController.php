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
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

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

    public static function sendWarnMail($data)
    {
        $mail = new PHPMailer(true);

        $warn = DB::table('warning')->where('pseudo', $data['pseudo'])->count();

        $info = DB::table('warning')->where('pseudo', $data['pseudo'])->get()[0];

        try {
            //Server settings
            $mail->SMTPDebug = 2;                                       // Enable verbose debug output
            $mail->isSMTP();                                            // Set mailer to use SMTP
            $mail->Host       = 'mail.badblockmail.fr';  // Specify main and backup SMTP servers
            $mail->SMTPAuth   = true;                                   // Enable SMTP authentication
            $mail->Username   = 'community@badblockmail.fr';                // SMTP username
            $mail->Password   = 'L8etptugoCmtJcubCBMc6rBVQB7FuK8xrVdPc7LbbP2siLNNgmiVCv3VBkGR9Un7';                               // SMTP password
            $mail->SMTPSecure = 'tls';                                  // Enable TLS encryption, `ssl` also accepted
            $mail->Port       = 587;                                  // TCP port to connect to

            //Recipients
            $mail->setFrom('community@badblock.fr', env('PHP_MAILER_FROM_NAME'));
            $mail->addAddress(DB::table('users')->where('name', $data['pseudo'])->get()[0]->email, $data['pseudo']);     // Add a recipient

            // Content
            $mail->isHTML(true);                                  // Set email format to HTML
            $mail->Subject = 'Avertissement ('.$warn.'/3) - '.$info->title;
            $mail->Body    = 'Vous venez de recevoir un avertissement de la part de '.$data['warn_by'].' avec pour raison :'.$data['text'];
            $mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

            $mail->send();
        } catch (Exception $e) {
        }
    }

}
