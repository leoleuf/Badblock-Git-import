<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Session;


class InfoController extends Controller
{

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public static function info($cat, $id, $erreur = null)
    {
        $catName = encname($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[enctag($k)] = 0;
        }

        $l = array_keys($l);

        if (!in_array($catName, $l))
        {
            abort(404);
        }

        $id = encname($id);

        if (Redis::exists('server:' . encname($id))){
            $data = json_decode(Redis::get('server:' . encname($id)));

            if (encname($data->cat) != encname($catName))
            {
                abort(404);
            }

            $playerstats = '';

            if (Redis::exists('playerstats:'.$data->id))
            {
                $playerstats = Redis::get('playerstats:'.$data->id);
            }

            $tagsInfo = json_decode(Redis::get('tags:'.$catName));

            session(['vote'.$data->id => time() + 600]);

            if (Session::exists('vote'))
            {
                Session::remove('vote');
                Session::put('votelistok', time() + 10);
                return view('front.info', ['vote' => true, 'tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'erreur' => $erreur, 'playerstats' => $playerstats]);
            }

            return view('front.info', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'erreur' => $erreur, 'playerstats' => $playerstats]);
        }else{
            abort(404);
        }

    }

    public function isGoodIp($ip)
    {
        try
        {
            $apiKey = getenv('IP_DETECTOR_KEY');
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_URL, 'https://api.ipwarner.com/' . $ip);
            curl_setopt($ch, CURLOPT_HTTPHEADER, array('API-Key: ' . $apiKey));
            curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 2);
            curl_setopt($ch, CURLOPT_TIMEOUT, 2); //timeout in seconds
            $result = curl_exec($ch);
            curl_close($ch);

            if ($result == null) {
                return true;
            }

            $result = trim($result);

            $obj = json_decode($result, true);

            if ($obj == null) {
                return true;
            }
            if (isset($obj['error'])) {
                return true;
            }

            return $obj['goodIp'] == '1';
        }
        catch (Exception $v)
        {
            return true;
        }
    }

    public function isGoodIp2($ip)
    {
        try
        {
            $apiKey = "MTU4NTpTMXV2TXdZbFJ0YlVZOGV2aGo3dUV0dG4zOVprTWVldQ==";

            $ch = curl_init();
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_URL, 'https://v2.api.iphub.info/ip/'.$ip);
            curl_setopt($ch, CURLOPT_HTTPHEADER, array('X-Key: ' . $apiKey));
            curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 2);
            curl_setopt($ch, CURLOPT_TIMEOUT, 2); //timeout in seconds
            $result = curl_exec($ch);
            curl_close($ch);

            if ($result == null) {
                return true;
            }

            $result = trim($result);

            $obj = json_decode($result, true);

            if ($obj == null) {
                return true;
            }

            if (!isset($obj['block'])) {
                return true;
            }

            return $obj['block'] == '0';
        }
        catch (Exception $v)
        {
            return true;
        }
    }

    public function pickInArray($arr, $num = 1) {
        shuffle($arr);

        $r = array();
        for ($i = 0; $i < $num; $i++) {
            $r[] = $arr[$i];
        }
        return $num == 1 ? $r[0] : $r;
    }

    public function ip($cat, $id)
    {
        $catName = encname($cat);

        $id = encname($id);

        if (Redis::exists('server:' . encname($id))){
            $data = json_decode(Redis::get('server:' . encname($id)));

            if (encname($data->cat) != encname($catName))
            {
                abort(404);
            }

            $ip = $_SERVER['REMOTE_ADDR'];

            if (isset($_SERVER['HTTP_CF_CONNECTING_IP']))
            {
                $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
            }

            if ($this->isGoodIp($ip) && $this->isGoodIp2($ip)){
                if (!Redis::exists($data->id.':copy:' . $ip)) {
                    Redis::set($data->id . ':copy:' . $ip, true);
                    Redis::expire($data->id . ':copy:' . $ip, 86400);
                    $data->copy = $data->copy + 1;

                    DB::table('server_list')
                        ->where('id', '=', $data->id)
                        ->update(
                            ['copy' => $data->copy]);

                    // Put the click in logs
                    DB::table('copy_logs')->insert([
                        'date' => date("Y-m-d H:i:s"),
                        'ip' => $ip,
                        'user_agent' => $_SERVER['HTTP_USER_AGENT'],
                        'server_id' => $data->id
                    ]);

                    Redis::set('server:' . $id, json_encode($data));
                }
            }

            return 'ok';
        }else{
            abort(404);
        }

    }

}
