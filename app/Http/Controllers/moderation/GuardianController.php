<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class GuardianController extends Controller
{
    public function index() {
        return view('section.mod.guardian.index');
    }

    public function view($id){

        $Guardian = DB::connection('mysql_guardian')->table('logs')->where('id', '=', $id)->first();

        if ($Guardian == null){
            return redirect("/players");
        }

        return view("section.mod.guardian")->with('Data', $Guardian);
    }

    public function getUnprocessedMessages(Request $request) {
        $startTimestamp = intval($request->get('startTimestamp') ?? 0);
        $limit = intval($request->get('limit') ?? 50);
        $messages = self::getMessagesTable()
            ->where([
                ['processed', false],
                ['timestamp', '>=', $startTimestamp],
            ])
            ->orderBy('timestamp')
            ->limit($limit)
            ->get();
        return response()->json($messages);
    }

    public function setMessageOk($messageId) {
        $message = self::getMessagesTable()
            ->where('_id', $messageId)
            ->first();
        if($message) {
            //mark the message as processed
            $message['processed'] = true;
            dump($message);

            self::getMessagesTable()->where('_id', $message['_id'])->update($message);
        }

    }

    public function muteMessageSender($messageId, $duration) {
        $message = self::getMessagesTable()
            ->where('_id', $messageId)
            ->first();
        if($message) {
            //mark the message as processed
            $message['processed'] = true;
            $message['punished'] = true;
            self::getMessagesTable()->where('_id', $message['_id'])->update($message);

            //TODO : create the proof

        }

    }

    private static function getMessagesTable() {
        return DB::connection('mongodb_server')->collection('reportmessages');
    }

}