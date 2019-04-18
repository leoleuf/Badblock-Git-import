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
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;

class GuardianController extends Controller
{
    private $dockerService;

    public function __construct(DockerService $dockerService)
    {
        $this->dockerService = $dockerService;
    }

    public function index() {
        return view('section.mod.guardian.index', ['data' => $this->getUnprocessedMessages()]);
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
        return DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('processed', '=', false)
            ->limit(100)
            ->orderBy('timestamp', 'DESC')
            ->get()
            ->toArray();
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


    public static function Osiris($msg, $user)
    {

        $msg = strtolower($msg);
        $tab = explode(" ", $msg);

        $player = DB::connection('mongodb_server')->collection('players')->where('name', $user)->get();

        foreach ($player as $punished)
        {
            if($punished['uniqueId'])
            {
                $player = $punished['uniqueId'];
            }
        }

        $casier = DB::connection('mongodb_server')->collection('punishments')->where('punishedUuid', $player)->get();

        $mute = 0;

        foreach($casier as $val)
        {
            if($val['type'] == "MUTE")
            {
                $mute++;
            }
        }

        $badW = [

            'test',
            'tg',
            'putes',
            'fils',
            'fils de',
            'fils de pute',
            'pute',
            'nique',
            'niquez',
            'niqué',
            'mére',
            'mère',
            'mères',
            'mrd',
            'merde',
            'connard',
            'connards',
            'nique vos mére fils de putes',
            'connasse',
            'enculé',
            'encule',
            'enculer',
            'bande de',
            'fdp',
            'putain',
            'ftg',
            'tgg',
            'con',
            't g',
            'salope',
            'couille',
            'suce',
            'tencul',
            'encule',
            'encul',
            'ptn',
            'ntm',
            'chatte',
            'chate',
            'mere',
            'daronne',
            'darone',
            'bites',
            'bite',
            'pere',
            'shlag',
            'chlag',
            'batard',
            'maman',
            'salop'


        ];

        $badW2 = [

            'pédale',
            'tapette',
            'tappete',
            'tapete',
            'femme',
            'femmes',
            'filles',
            'cuisine',
            'prostitués',
            'prostitué',
            'femelle',
            'lgbt',
            'vegan',
            'juifs',
            'gay',
            'pd',
            'p d',
            'arabe'

        ];

        $badW3 = [

            'serveur de merde',
            'server de merde',
            'server de mrd',
            'serveur de merde',
            'serv de merde'

        ];

        $goodW = [

            'merde j\'ai',
            'mrd j\'ai',
            'je suis con',
            'je suis trop con',
            'je suis vraiment trop con',
            'je suis qu\'un con',
            'je ne suis qu\'un con',

        ];

        $badP = 0;

        $st = [];

        foreach (DB::table('users')->get() as $staff)
        {
            array_push($goodW, $staff->name);
        }

        foreach ($tab as $val)
        {

            foreach ($goodW as $key3)
            {
                if(strpos($val, $key3) !== false)
                {
                    $badP--;
                    $val = '<span style="background-color: #E7E11A">'.$val.'</span>';
                }
            }

            foreach ($badW as $key)
            {
                if(strcasecmp($val, $key) == 0 || strpos($val, $key) !== false)
                {
                    $badP++;
                    $temp = $val;

                    $val = '<span style="background-color: #E7E11A">'.$val.'</span>';

                    if($key == "con" && str_replace("con", " ", $val) !== " ")
                    {
                        $badP--;
                        $val = $temp;
                    }

                }
            }

            foreach ($badW2 as $key2)
            {
                if(strcasecmp($val, $key2) == 0 || strpos($val, $key2) !== false)
                {
                    $badP = $badP + 2;
                    $val = '<span style="background-color: #E7E11A">'.$val.'</span>';
                }
            }

            foreach ($badW3 as $key4)
            {
                if(strcasecmp($val, $key4) == 0 || strpos($val, $key4) !== false)
                {
                    $badP = $badP + 100;
                    $val = '<span style="background-color: #E7E11A">'.$val.'</span>';
                }
            }

            array_push($st, $val);

        }

        $msg = " ";

        $str = implode(" ", $st);

        $type = "mute";

        $time = 1;

        if($badP <= 0)
        {
            $msg = "N/A";
        }
        else if($badP >= 1 && $badP <= 10)
        {
            if($mute < 1)
            {
                $msg = "Avertissement";
                $type = "warn";
            }
            else if($mute == 1)
            {
                $msg = "1 Heure(s) de Mute";
                $time = 1;
            }
            else if($mute >= 2)
            {
                $msg = (($mute * 3) - 3)." Heure(s) de Mute";
                $time = (($mute * 3) - 3);
            }
            else
            {
                $msg = "Avertissement";
                $type = "warn";
            }

        }
        else if($badP >= 2 && $badP <= 10)
        {
            if($mute >= 1)
            {
                $msg = ($mute * 6)." Heure(s) de Mute";
                $time = ($mute * 6);
            }
            else
            {
                $msg = "6 Heures de Mute";
                $time = 6;
            }
        }
        else if($badP >= 100)
        {
            if($mute == 0)
            {
                $mute = 1;
            }

            $msg = ($mute * 15)." Jours de Mute";
            $time = ($mute * 15);
        }

        return [

            'sanction' => $msg,
            'msg' => $str,
            'data' => [

                'type' => $type,
                'reason' => 'Langage (Guardianner)',
                'time' => $time

            ]
        ];

    }

    public function sanction($uuid){



        $message = DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $uuid)
            ->first();

        $Data = $this->Osiris($message['message'], $message['playerName']);

        if ($Data['data']['type'] == "mute" || $Data['data']['type'] == "warn"){
            $proof = [
                'punishedBy' => Auth::user()->name,
                'punishedPlayer' => $message['playerName'],
                'punishedMessage' => $message['message'],
                'punishType' => $Data['data']['type'],
                'punishReason' => $Data['data']['reason'],
                'punishTime' => $Data['data']['time']
            ];
            //Create proof
            DB::connection('mongodb_server')
                ->collection('chatfilter_proof')
                ->insert($proof);
        }

        $Data['data']['reason'] = $Data['data']['type'] . " => " . $Data['data']['reason'];

        if (!$message['processed']){
            if ($Data['data']['type'] == "mute"){
                $this->dockerService->mutePlayer($message['playerName'], $Data['data']['reason'], intval($Data['data']['time'] * 60 * 60 * 1000));
            }elseif ($Data['data']['type'] == "warn"){
                $this->dockerService->warnPlayer($message['playerName'], $Data['data']['reason']);
            }
        }

        $message['processed'] = true;
        $message['punished'] = false;
        //Update message
        DB::connection('mongodb_server')
            ->collection('reportmessages')
            ->where('_id', $message['_id'])
            ->update($message);

        return back();
    }


}