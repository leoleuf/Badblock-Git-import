<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 13/07/2018
 * Time: 14:47
 */

namespace App\Http\Controllers;


use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use App\Http\Controllers\Minecraft\MinecraftPing;
use App\Http\Controllers\Minecraft\MinecraftPingException;


class CacheController extends Controller
{
    public function index(){
        $currentDate = date("Y-m-d");

        $rDate = date("m-Y", strtotime("first day of previous month"));
        $resets = DB::table('resets')
            ->where('date', '=', $rDate)
            ->get()->toArray();

        if ($resets == null || count($resets) == 0)
        {
            DB::table('resets')->insert(
                [
                    'date' => $rDate,
                    'doneDate' => date("d/m/Y H:i:s"),
                    'cacheIp' => $_SERVER['REMOTE_ADDR']
                ]
            );
            DB::table("server_list")->update(['votes' => 0, 'clicks' => 0, 'copy' => 0]);
        }


        //Quelques chiffres
        $server = DB::select('select count(id) AS count from server_list');
        $votes = DB::select('select sum(votes) AS votes, sum(clicks) AS clicks, sum(copy) as copy from server_list');
        $serverCount = number_format($server[0]->count, 0, ',', ' ');
        $voteCount = number_format($votes[0]->votes, 0, ',', ' ');
        $clickCount = number_format($votes[0]->clicks, 0, ',', ' ');
        $copyCount = number_format($votes[0]->copy, 0, ',', ' ');

        $data = [
            'serveurCount' => $serverCount,
            'voteCount' => $voteCount,
            'clickCount' => $clickCount,
            'copyCount' => $copyCount
        ];

        Redis::set("about", json_encode($data));

        $topServers = DB::table('pub')
            ->where('date', '=', $currentDate)
            ->get()->toArray();

        foreach (config('tag.cat') as $tg) {
            $k = encname($tg);

            //Gestion du nombre de pages
            //10 Seveur par page
            $count = DB::table('server_list')
                ->where('actived', '=', 1)
                ->where('hidden', '=', 0)
                ->where('cat', '=', $k)
                ->count();

            $server = DB::table('server_list')
                ->where('actived', '=', 1)
                ->where('hidden', '=', 0)
                ->where('cat', '=', $k)
                ->orderByRaw('votes DESC')
                ->get();

            $server = $server->toArray();
            $sArray = array();

            $n = 0;
            foreach ($server as $m => $j)
            {
               /* if ($j->votes > 0)
                {*/
                    $sArray[$n] = $j;
                    $n++;
                //}
            }

            $page = ceil(count($sArray) / 35);

            if (count($sArray) < 35)
            {
                $page = 1;
            }

            //Set nb page Redis
            Redis::set('page:'.$k.':number', $page);

            $topServer = array();
            $io = 0;
            foreach($topServers as $p => $o) {
                $ts = DB::table('server_list')
                    ->where('id', '=', $o->server)
                    ->where('cat', '=', $k)
                    ->orderBy('id', 'ASC')
                    ->get();
                $ne = false;
                if ($ts != null) {
                    $ts = $ts->toArray();
                    if ($ts != null && count($ts) > 0) {
                        $topServer[$io] = $ts[0];
                        $topServer[$io]->ad = true;
                        $ne = true;
                    }
                }
                if ($ne) {
                    $io++;
                }
            }

            $i = 0;
            $c = 0;
            //Split en 10 par 10
            while ($i < $page) {

                $data = array_slice($sArray, $c, (max($i, 1) * 35));
                $v = array();
                if ($topServer != null && count($topServer) > 0)
                {
                    $istrk = 0;
                    foreach ($topServer as $po => $oo)
                    {
                        $v[$istrk] = $oo;
                        $istrk++;
                    }
                }

                $data = array_merge($v, $data);
                Redis::set('page:'.$k.':data:' . ($i + 1), json_encode($data));
                $i++;
                $c = $c + 35;
            }

            $server = DB::table('server_list')
                ->where('actived', '=', 1)
                ->where('cat', '=', $k)
                ->orderByRaw('votes DESC')
                ->get();

            var_dump("cat: ".$k." : ".count($server)." servers");

            foreach ($server as $row) {
                $comment = DB::select("select * from comments WHERE server_id =" . $row->id . " ORDER by date LIMIT 10");
                $row->comment = $comment;
                $row->description = nl2br(htmlspecialchars($row->description));

                if (filter_var($row->website, FILTER_VALIDATE_URL) && intval($row->verified) == 1) {
                    try {
                        $options = array(
                            'http' => array(
                                'timeout' => 10,
                                'method' => "GET",
                                'header' => "Accept-language: en\r\n" .
                                    "User-Agent: Mozilla/5.0 (SMG, https://serveur-multigames.net/minecraft)\r\n"
                            )
                        );

                        $context = stream_context_create($options);
                        $t = @file_get_contents($row->website, false, $context);
                        $t = strtolower($t);

                        if (strpos($t, 'serveur-multigames.net') === false) {
                            if (intval($row->retries) < 50) {
                                DB::table('server_list')
                                    ->where('id', '=', $row->id)
                                    ->update(
                                        [
                                            'retries' => intval($row->retries) + 1
                                        ]
                                    );
                            } else {
                                DB::table('server_list')
                                    ->where('id', '=', $row->id)
                                    ->update(
                                        [
                                            'verified' => '0'
                                        ]
                                    );
                            }
                        } else {
                            DB::table('server_list')
                                ->where('id', '=', $row->id)
                                ->update(
                                    [
                                        'retries' => '0'
                                    ]
                                );
                        }
                    }
                    catch (Exception $e)
                    {
                        if (intval($row->retries) < 50) {
                            DB::table('server_list')
                                ->where('id', '=', $row->id)
                                ->update(
                                    [
                                        'retries' => intval($row->retries) + 1
                                    ]
                                );
                        } else {
                            DB::table('server_list')
                                ->where('id', '=', $row->id)
                                ->update(
                                    [
                                        'verified' => '0'
                                    ]
                                );
                        }
                    }
                }

                if ($k == "minecraft" && $row->ip != null && !Redis::exists('playerstats:'.$row->id.':cache'))
                {
                    $o = explode(":", $row->ip);
                    try
                    {
                        $p = isset($o[1]) ? intval($o[1]) : 25565;
                        $Query = new MinecraftPing( $o[0], $p);

                        $o = $Query->Query();
                        if (isset($o) && isset($o['players']) && isset($o['players']['online']))
                        {
                            $v = intval($o['players']['online']);
                            $vo = '';
                            if (Redis::exists('playerstats:'.$row->id)) {
                                $vo = Redis::get('playerstats:' . $row->id);
                            }
                            $vo .= '['.(time() * 1000).','.$v.'],';
                            Redis::set('playerstats:' . $row->id, $vo);
                            Redis::set('playerstats:' . $row->id.':cache', 'ok');
                            Redis::expire('playerstats:' . $row->id.':cache', 1800);
                        }
                    }
                    catch( MinecraftPingException $e )
                    {
                    }
                    finally
                    {
                    }
                }
                Redis::set('server:' . encname($row->name), json_encode($row));
            }

            $tags = array();

            if (isset(config('tag.tag')[$tg])) {
                $tag = config('tag.tag')[$tg];

                foreach ($tag as $row) {
                    $row = enctag($row);
                    $data = DB::select("select * from server_list WHERE actived = 1 AND cat = '" . $k . "' AND hidden = 0 AND tag LIKE '%" . $row . "%' ORDER by votes DESC");


                    $count = DB::table('server_list')
                        ->where('actived', '=', 1)
                        ->where('tag', 'LIKE', "%" . $row . "%")
                        ->where('hidden', '=', 0)
                        ->where('cat', '=', $k)
                        ->count();

                    $tags[$row] = $count;
                    $page = ceil($count / 20);
                    $i = 0;
                    $c = 0;
                    //Split en 10 par 10
                    while ($i < $page) {
                        $data = array_slice($data, $c, (($i + 1) * 20));
                        $v = array();
                        if ($topServer != null && count($topServer) > 0)
                        {
                            $istrk = 0;
                            foreach ($topServer as $po => $oo)
                            {
                                $v[$istrk] = $oo;
                                $istrk++;
                            }
                        }

                        $data = array_merge($v, $data);
                        Redis::set('tag:' . $k . ':' . $row . ":" . ($i + 1), json_encode($data));
                        $i++;
                        $c = $c + 20;
                    }
                }
            }

            Redis::set('tags:'.$k, json_encode($tags));
        }

    }

}