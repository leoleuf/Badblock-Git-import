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
use Illuminate\Support\Facades\Redis;

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

    public function getUnprocessedMessages() {

        //Get unprocessed message from MongoDB
        $Message = DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('processed', '=', false)
            ->limit(20)
            ->get()
            ->toArray();

        return json_encode($Message);
    }

    public function setMessageOk($messageId) {
        //Get message for update
        $message = DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $messageId)
            ->first();

        $message['processed'] = true;
        $message['punished'] = false;
        //Update message
        DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $message['_id'])
            ->update($message);

        return "";
    }

    public function determineSanction($messageId) {
        //Get message for proced
        $message = DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $messageId)
            ->first();

        $Player = DB::connection('mongodb_server')
            ->collection('players')
            ->where('name', strtolower($message['playerName']))
            ->first();

        $Ptable = DB::connection('mongodb_server')
            ->collection('guardian_fetcher')
            ->get();

        $Fetchvalid = [];
        //For all fetcher
        $trmessage = strtolower($message['message']);
        foreach ($Ptable as $k => $fetch){
            //One regex
            if (!isset($fetch['complementary_words'])){
                $Exword = [];
                foreach ($fetch['words_base'] as $word){
                    array_push($Exword, '/\b$' . preg_quote($word) . "\b");
                }

                $Enword = [];
                foreach ($fetch['words_base'] as $word){
                    array_push($Enword, $word);
                }

                //Str to lower
                $trmessage = strtolower($trmessage);
                if (strlen($trmessage) < 7){
                    $trmessage = str_replace(' ', '', $trmessage);
                }

                $F = 0;
                foreach ($Exword as $wrd){
                    preg_match($wrd, $trmessage, $matches);
                    $F = $F + count($matches);
                }

                foreach ($Enword as $wrd){
                    if (strpos($trmessage,$wrd) !== false){
                        $F++;
                    }
                }

                if ($F > 0){
                    $m = true;
                }else{
                    $m = false;
                }

                array_push($Fetchvalid, ['name' => $fetch['type'], "match" => $m, "count" => $F]);
            }else{
                //Two regex
                $Exword = [];
                foreach ($fetch['words_base'] as $word){
                    array_push($Exword, '/\b$' . preg_quote($word) . "\b");
                }

                $Enword = [];
                foreach ($fetch['words_base'] as $word){
                    array_push($Enword, $word);
                }

                //Str to lower
                $trmessage = strtolower($trmessage);
                if (strlen($trmessage) < 7){
                    $trmessage = str_replace(' ', '', $trmessage);
                }

                $F = 0;
                foreach ($Exword as $wrd){
                    preg_match($wrd, $trmessage, $matches);
                    $F = $F + count($matches);
                }

                foreach ($Enword as $wrd){
                    if (strpos($trmessage,$wrd) !== false){
                        $F++;
                    }
                }
                //Start second regex
                if ($F > 0){
                    $Exword = [];
                    foreach ($fetch['complementary_words'] as $word){
                        array_push($Exword, '/^' . $word . "/");
                    }

                    $Enword = [];
                    foreach ($fetch['complementary_words'] as $word){
                        array_push($Enword, $word);
                    }

                    //Str to lower
                    $trmessage = strtolower($trmessage);
                    if (strlen($trmessage) < 7){
                        $trmessage = str_replace(' ', '', $trmessage);
                    }

                    $E = 0;
                    foreach ($Exword as $wrd){
                        preg_match($wrd, $trmessage, $matches);
                        $E = $E + count($matches);
                    }

                    foreach ($Enword as $wrd){
                        if (strpos($trmessage,$wrd) !== false){
                            $E++;
                        }
                    }
                }

                if ($E + $F >= 2){
                    $m = true;
                }else{
                    $m = false;
                }

                array_push($Fetchvalid, ['name' => $fetch['type'], "match" => $m, "count" => $E + $F]);
            }
        }

        //Search all old sanctions
        $Time = 0;
        foreach ($Fetchvalid as $k => $Fet) {
            if ($Fet['match']){
                $Count = DB::connection('mongodb_server')
                    ->collection('punishments')
                    ->where('punishedUuid', $Player['uniqueId'])
                    ->where('date', ">", date('d-m-Y H:i:s', strtotime('-2 months')))
                    ->where('isReasonKey', true)
                    ->where('reason', "bungee.commands.mod.warn.reason." . $Fet['name'])
                    ->count();

                if($Count > 0){
                    $Count = DB::connection('mongodb_server')
                        ->collection('punishments')
                        ->where('punishedUuid', $Player['uniqueId'])
                        ->where('date', ">", date('d-m-Y H:i:s', strtotime('-2 months')))
                        ->where('isReasonKey', true)
                        ->where('reason', "bungee.commands.mod.mute.reason." . $Fet['name'])
                        ->count();
                }

                $Conb = $Count;
                $Count = $Count - 1;
                if ($Conb > 0){
                    foreach ($Ptable as $k => $p){
                        if ($p['type'] == $Fet['name']){
                            $Count1 = $Count;
                            $Count2 = $Count;
                            if (isset($p['type_top'][$Count1])){
                                $Sanction_type = $p['type_top'][$Count1];
                            }else{
                                while (!isset($p['type_top'][$Count1])){
                                    $Count1 = $Count1 -1;
                                }
                                $Sanction_type = $p['type_top'][$Count1];
                            }

                            if (isset($p['time_top'][$Count2])){
                                $Sanction_time = $p['time_top'][$Count2];
                            }else{
                                while (!isset($p['time_top'][$Count2])){
                                    $Count2 = $Count2 -1;
                                }
                                $Sanction_time = $p['time_top'][$Count2];
                            }
                        }
                    }
                }else{
                    foreach ($Ptable as $k => $p){
                        if ($p['type'] == $Fet['name']){
                            $Sanction_type = $p['type_top'][0];
                            $Sanction_time = $p['time_top'][0];
                        }
                    }
                }
                if ($Sanction_time >= $Time){
                    $Time = $Sanction_time;
                    $DefTime = $Sanction_time;
                    $DefType = $Sanction_type;
                    $DefReason = $Fet['name'];
                }
            }
        }


        return [
            "type" => $DefType,
            "time" => $DefTime,
            "reason" => $DefReason,
            "fetcher" => $Fetchvalid,
            "message" => $trmessage
        ];

    }

    function jsonSanction($messageId){

        $reason = [
            'insult_taunt_server_name' => 'Insulte / Provocation / Citation de serveur',
            'hack_ddos_threat' => 'Menace (DDOS / Hack)',
            'staff_insult' => 'Insulte Staff / Menace staff / Irrespect staff / Mensonge staff',
            'slander' => 'Diffamation',
            'advertising' => 'Recrutement staff / Pub',
            'server_community_insult' => 'Insulte Serveur / Communautée',
            'discrimination' => 'Discrimination (homophobie, racisme, antisémitisme ...)'
        ];

        $type = [
            'warn' => "Avertissement",
            'mute' => "Mute"
        ];

        $Data = $this->determineSanction($messageId);

        if ($Data['time'] > 12){
            $Data['time'] = $Data['time'] / 24;
            $Data['time'] = $Data['time'] . 'Jour(s)';
        }else{
            $Data['time'] = $Data['time'] . 'heure(s)';
        }

        $return = json_encode([
            "type" => $type[$Data['type']],
            "time" => $Data['time'],
            "reason" => $reason[$Data['reason']],
            "fetcher" => $Data['fetcher'],
            "message" => $Data['message']
        ]);

        if ($return == false){
            return "";
        }else{
            return $return;
        }

    }

    public function sanction($uuid){

        $Data = $this->determineSanction($uuid);

        $message = DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $uuid)
            ->first();

        $proof = [
            'punishedBy' => Auth::user()->name,
            'punishedPlayer' => $message['playerName'],
            'punishedMessage' => $message['message'],
            'punishType' => $Data['type'],
            'punishReason' => $Data['reason'],
            'punishTime' => $Data['time']
        ];
        //Create proof
        DB::connection('mongodb_server')
            ->collection('chatfilter_proof')
            ->insert($proof);

        $Data['reason'] = "bungee.commands.mod."  . $Data['type'] . "." . $Data['reason'];

        if (!$message['processed']){
            if ($Data['type'] == "mute"){
                $this->dockerService->mutePlayer($message['playerName'], $Data['reason'], intval($Data['time'] * 60 * 60 * 1000));
            }elseif ($Data['type'] == "warn"){
                $this->dockerService->warnPlayer($message['playerName'], $Data['reason']);
            }
        }

        $message['processed'] = true;
        $message['punished'] = false;
        //Update message
        DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $message['_id'])
            ->update($message);

        return "";
    }

}