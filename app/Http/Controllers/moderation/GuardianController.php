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
            ->limit(20)
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
            'serveur',
            'server',
            'pd',
            'p d',
            'connasse',
            'enculé',
            'encule',
            'enculer',
            'bande de'


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
            'vegan'


        ];

        $goodW = [

            'merde j\'ai',
            'mrd j\'ai',
            'je suis con',
            'je suis trop con',
            'je suis vraiment trop con',
            'je suis qu\'un con',
            'je ne suis qu\'un con'

        ];

        $badP = 0;

        foreach ($tab as $val)
        {

            foreach ($goodW as $key3)
            {
                if(strpos($val, $key3) !== false)
                {
                    $badP--;
                }
            }

            foreach ($badW as $key)
            {
                if(strcasecmp($val, $key) == 0)
                {
                    $badP++;
                }
            }

            foreach ($badW2 as $key2)
            {
                if(strcasecmp($val, $key2) == 0)
                {
                    $badP = $badP + 2;
                }
            }

        }

        $msg = "";

        if($badP <= 0)
        {
            $msg = "N/A";
        }
        else if($badP == 1)
        {
            if($mute < 1)
            {
                $msg = "Avertissement";
            }
            else if($mute == 1)
            {
                $msg = ($mute * 3)." Heure(s) de Mute";
            }

        }
        else if($badP >= 2)
        {
            if($mute >= 1)
            {
                $msg = ($mute * 6)." Heure(s) de Mute";
            }
            else
            {
                $msg = "6 Heures de Mute";
            }
        }

        return $msg;

    }

}