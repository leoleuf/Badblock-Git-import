<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use App\Services\DockerService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class GuardianController extends Controller
{
    private $dockerService;

    public function __construct(DockerService $dockerService)
    {
        $this->dockerService = $dockerService;
    }

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
        $messages = self::getMessagesCollection()
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
        $message = self::getMessagesCollection()
            ->where('_id', $messageId)
            ->first();
        if($message) {
            $this->processMessage($message, false);
        }

    }

    public function muteMessageSender($messageId, $duration) {
        $message = self::getMessagesCollection()
            ->where('_id', $messageId)
            ->first();
        if($message) {
            $this->processMessage($message, true);
            $this->createProof($message, 'mute ' . $duration);
            if($this->dockerService->isConnected())
                $this->dockerService->mutePlayer($message['playerName'], '', $this->getSanctionExpire($duration));
        }
    }

    public function banMessageSender($messageId, $duration) {
        $message = self::getMessagesCollection()
            ->where('_id', $messageId)
            ->first();
        if($message) {
            $this->processMessage($message, true);
            $this->createProof($message, 'ban ' . $duration);
            if($this->dockerService->isConnected())
                $this->dockerService->banPlayer($message['playerName'], '', $this->getSanctionExpire($duration));
        }
    }

    /**
     * Retourne le timestamp (en millisecondes) à laquelle une sanction doit expirer en fonction de sa durée
     *
     * @param $durationString a string following this format (1 heure, x heures, 1 jour ou x jours
     * @return int
     */
    private function getSanctionExpire($durationString) {
        $parts = explode(' ', $durationString);
        $duration = intval($parts[0]);
        $unit = $parts[1];
        $multiplier = 60 * 60 * 1000;
        if(preg_match('/heure/', $unit)) {
           // nothing to do
        } else if(preg_match('/jour/', $unit)) {
            $multiplier = $multiplier * 24;
        }

        return time() * 1000 + $duration * $multiplier;
    }

    private function processMessage($message, $punished) {
        $message['processed'] = true;
        $message['punished'] = $punished;
        self::getMessagesCollection()->where('_id', $message['_id'])->update($message);
    }

    private function createProof($message, $punition) {
        $proof = [
            'punishedBy' => Auth::user()->name,
            'punishedPlayer' => $message['playerName'],
            'punishedMessage' => $message['message'],
            'punition' => $punition
        ];
        self::getProofsCollection()->insert($proof);
    }

    private static function getMessagesCollection() {
        return DB::connection('mongodb_server')->collection('reportmessages');
    }

    private static function getProofsCollection() {
        return DB::connection('mongodb_server')->collection('chatfilter_proof');
    }

}