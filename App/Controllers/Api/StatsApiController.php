<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 18/08/2017
 * Time: 23:04
 */

namespace App\Controllers\Api;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use DatePeriod;
use DateTime;
use DateInterval;

class StatsApiController extends \App\Controllers\Controller
{


    public function getCreateCacheStats(RequestInterface $request, ResponseInterface $response)
    {

        //Lecture du classement
        $query = "show tables;";

       // dd($this->container->mysql_rankeds->fetchRowManyCursor($query));
        foreach ($this->container->mysql_rankeds->fetchRowManyCursor($query) as $game)
        {
            $name = $game["Tables_in_rankeds"];
            $game = [];
            $query = 'SELECT * FROM '. $name .' ORDER by _points DESC';

            foreach ($this->container->mysql_rankeds->fetchRowManyCursor($query) as $result)
            {
                array_push($game,$result);
            }
            //Save Redis
            $nb = count($game);
            if (is_double($nb / 20)){
                $nb = round(($nb / 20)) + 1;
            }
            $n = 0;
            while ($nb != $n){
                $data = array_slice($game,($n * 20),20,true);
                $this->redis->setJson("stats:".$name .":". $n,$game);
            }
        }

        $this->log->info('"StatsApiController\getCreateCacheA"',' Success writing stats cache');

        return $response->write('Success writing stats cache')->withStatus(200);
    }

    public function jsonResp(RequestInterface $request, ResponseInterface $response){


        //Stats provenant du mongoDB dist
        $collection = $this->container->mongoServer->players;
        $register = $collection->count();
        $banA = $collection->count(['punish.ban' => true]);
        $muteA = $collection->count(['punish.mute' => true]);
        $banG = $collection->count(['punish.ban' => true,'punish.banner' => "Guardian"]);
        $banM = $banA - $banG;

        //Get du staff sur redis
        $staff = $this->redis->getjson('staff.number');

        //Count des line sur le forum
        $msg_forum = $this->container->mysql_forum->fetchRow('SELECT COUNT(*) FROM xf_post;')["COUNT(*)"];

        //Connecté sur TS
        $ts_connected = 0;

        //nombre d'articles
        $article = $this->redis->get('posts_count');

        //Guardian
        $cheat = ["KillAura",
                "ForceField",
                "Aimbot",
                "Fly",
                "Reach",
                "Heuristics",
                "SpeedHack",
                "Criticals",
                "FightSpeed",
                "KnockBack"];

        $guardian = [];
        foreach ($cheat as $row){
            $data = $this->container->mysql_guardian->fetchRow("SELECT COUNT(*) FROM logs WHERE cheat LIKE '%". $row ."%'")["COUNT(*)"];
            array_push($guardian, ['name' => $row,'number' => $data]);
        }

        $this->redis->setJson('stats:guardian', $guardian);

        //Ban total guardian
        $nmban = $this->container->mysql_guardian->fetchRow("SELECT COUNT(*) FROM logs WHERE type LIKE 'ban'")["COUNT(*)"];
        //Ban total du moi guardian
        $nmbanM = $this->container->mysql_guardian->fetchRow("SELECT COUNT(*) FROM logs WHERE type LIKE 'ban' AND date like '%". date("m/y") ."%'")["COUNT(*)"];
        //Ban total du jour guardian
        $nmbanJ = $this->container->mysql_guardian->fetchRow("SELECT COUNT(*) FROM logs WHERE type LIKE 'ban' AND date like '%". date("d/m/y") ."%'")["COUNT(*)"];

        $period = new DatePeriod(
            new DateTime(date("y-m-d", strtotime("-30 days"))),
            new DateInterval('P1D'),
            new DateTime(date("y-m-d",strtotime("+1 day")))
        );

        $stats = [];
        //array_push($period, new DateTime(date("y-m-d")));
        foreach ($period as $key => $value) {
            $data = $this->container->mysql_guardian->fetchRow("SELECT COUNT(*) FROM logs WHERE type LIKE 'ban' AND date like '%". date_format($value, "d/m/Y") ."%'")["COUNT(*)"];
            array_push($stats, ['date' => date_format($value, "d/m/y"), "number" => $data]);
        }
        $this->redis->setJson('stats:stats_guardian', $stats);

        $stats = [
            "registred" => $register,
            "ban" => $banA,
            "mute" => $muteA,
            "banMod" => $banM,
            "banGuardian" => $banG,
            "staff" => $staff,
            "message_forum" => $msg_forum,
            "ts_co" => $ts_connected,
            "article" => $article,
            "ban_g_total" => $nmban,
            "ban_m_total" => $nmbanM,
            "ban_j_total" => $nmbanJ,
        ];

        $this->redis->setJson('stats:stats_general', $stats);




    }




}


