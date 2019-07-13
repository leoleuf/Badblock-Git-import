<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Session;
use D3strukt0r\VotifierClient\ServerType\ClassicVotifier;
use D3strukt0r\VotifierClient\Vote;
use D3strukt0r\VotifierClient\VoteType\ClassicVote;


class VoteController extends Controller
{

    public function curl_get_async($url, $params)
    {
        $r = "";
        foreach ($params as $key => $val)
        {
            if ($r == "")
            {
                $r = "?";
            }
            else
            {
                $r .= "&";
            }
            $r .= $key."=".$val;
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_URL,
            $url.$r
        );
        curl_setopt($ch, CURLOPT_TIMEOUT_MS, 2000);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        $content = curl_exec($ch);
    }

    public function isABot() {

        return (
            isset($_SERVER['HTTP_USER_AGENT'])
            && preg_match('/bot|crawl|slurp|spider|mediapartners/i', $_SERVER['HTTP_USER_AGENT'])
        );
    }

    public function startsWith($haystack, $needle)
    {
        $length = strlen($needle);
        return (substr($haystack, 0, $length) === $needle);
    }

    public function pm()
    {
        // Get the IP address
        $ip = $_SERVER['REMOTE_ADDR'];

        if (isset($_SERVER['HTTP_CF_CONNECTING_IP']))
        {
            $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
        }

        if (isset($_POST['a']))
        {
            $pt = time();

            $c = DB::table('votebuttonclicks')
                ->where('ip', '=', $ip)
                ->where('tsmp', '>=', ($pt - 60))
                ->count();

            if (intval($_POST['b']) OR ($c == null && $c < 1))
            {
                $c = DB::table('votebuttonclicks')
                    ->where('ip', '=', $ip)
                    ->where('tsmp', '>=', ($pt - 60))
                    ->where('pubclick', '=', 0)
                    ->delete();

                $log = "V-CLICKOK";
                $dcl = -1;

                if (intval($_POST['c']) <= intval($_POST['a']))
                {
                    $dcl = intval($_POST['c']);
                }

                if(!intval($_POST['b'])) {
                    $log = "X-NOTCLICK
                        => Téléphone : " . (isMobile() ? "Oui" : "Non") . "
                        => Temps chargement/clic : " . intval($_POST['a']) . " ms
                        => Temps décalage/clic : " .($dcl != -1 ? $dcl." ms" : "Pas de décalage")."
                        => Temps dernier mouvement souris : " . intval($_POST['d']) . " ms
                        => Dbg: ".$_POST['e'];
                }

                if (isset($_SERVER["HTTP_CF_IPCOUNTRY"]))
                {
                    $log .= "
                    COUNTRY: ".$_SERVER['HTTP_CF_IPCOUNTRY'];
                }

                DB::table('votebuttonclicks')->insert([
                    'date' => date("Y-m-d H:i:s"),
                    'ip' => $ip,
                    'user_agent' => $_SERVER['HTTP_USER_AGENT'],
                    'dbg' => $log,
                    'tsmp' => time(),
                    'mobile' => isMobile(),
                    'timediff' => intval($_POST['a']),
                    'pubclick' => intval($_POST['b']),
                    'lastdecale' => $dcl,
                    'lastmouse' => intval($_POST['d'])
                ]);
            }
        }
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function vote($cat, $id)
    {
        $catName = encname($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[enctag($k)] = 0;
        }

        if (!in_array($catName, $l))
        {
            abort(404);
        }

        $id = encname($id);

        if (Redis::exists('server:' . $id)){

            $data = json_decode(Redis::get('server:'.$id));

            if (encname($data->cat) != encname($catName))
            {
                abort(404);
            }

            $playerstats = '';

            if (Redis::exists('playerstats:'.$data->id))
            {
                $playerstats = Redis::get('playerstats:'.$data->id);
            }

            // Get the IP address
            $ip = $_SERVER['REMOTE_ADDR'];

            if (isset($_SERVER['HTTP_CF_CONNECTING_IP']))
            {
                $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
            }

            $time = null;

            // If the vote is already done
            if (Redis::exists($data->id .':user:' . $ip)) {
                // We get the time
                $time = Redis::get($data->id .':user:' . $ip);
                // We add 1H30 to the current time
                $time = ($time + 5400) - time();
                // We return an error with the remaining time
                $time = gmdate("H:i:s", $time);
            }

            $tagsInfo = json_decode(Redis::get('tags:'.$catName));

            header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
            header("Cache-Control: post-check=0, pre-check=0", false);
            header("Pragma: no-cache");

                $key = 'vote'.$id;
                $value = session($key, '0');
                $value = intval($value);
                if ($value < time()) {
                    session([$key => time() + 600]);
                    return redirect('/' . $catName . '/' . $id);
                }

            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'playerstats' => $playerstats, 'timez' => $time]);
        }else{
            abort(404);
        }
    }

    public function post($cat, $id){
        $catName = encname($cat);

        if (!in_array(seocat($catName), config('tag.cat')))
        {
            abort(404);
        }

        $id = encname($id);

        // If the server doesn't exist
        if (!Redis::exists('server:' . $id)) {
            // So we redirect to the default page
            abort(404);
        }

        // We get the data
        $data = json_decode(Redis::get('server:' . $id));

        if (encname($data->cat) != encname($catName))
        {
            abort(404);
        }

        $playerstats = "";

        if (Redis::exists('playerstats:'.$data->id))
        {
            $playerstats = Redis::get('playerstats:'.$data->id);
        }
        else
        {
            $playerstats = "";
        }

        $tagsInfo = json_decode(Redis::get('tags:'.$catName));

        // Captcha check
        if (!isset($_POST['g-recaptcha-response']))
        {
            // We get the server data
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'captcha' => true, 'playerstats' => $playerstats]);
        }

        if (empty($_POST['g-recaptcha-response']))
        {
            // We get the server data
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'captcha' => true, 'playerstats' => $playerstats]);
        }

        $check = $this->check($_POST['g-recaptcha-response']);

        $check = json_decode($check);

        if ($check == null || $check == false)
        {
            // We get the server data
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'captcha' => true, 'playerstats' => $playerstats]);
        }

        if (!isset($check->success))
        {
            // We get the server data
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'captcha' => true, 'playerstats' => $playerstats]);
        }

        if (!$check->success)
        {
            // We get the server data
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'captcha' => true, 'playerstats' => $playerstats]);
        }

        // Get the IP address
        $ip = $_SERVER['REMOTE_ADDR'];

        if (isset($_SERVER['HTTP_CF_CONNECTING_IP']))
        {
            $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
        }

        $vsrv = null;

        if ($data->votetype == "VOTIFIER") {


            // Get the username
            if (!isset($_POST['username']) OR empty($_POST['username'])) {
                return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'validusername' => true, 'playerstats' => $playerstats]);
            }

            if (!isset($_POST['serverid']) OR strlen($_POST['serverid']) < 1) {
                return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'serverid' => true, 'playerstats' => $playerstats]);
            }

            $username = htmlspecialchars($_POST['username']);
            $serverid = htmlspecialchars($_POST['serverid']);

            $ar = (array) json_decode($data->votifierdata);

            if (!isset($ar[intval($serverid)]))
            {
                return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'serverid' => true, 'playerstats' => $playerstats]);
            }

            $vsrv = $ar[intval($serverid)];

            if (strlen($username) > 32 OR strlen($username) < 2) {
                return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'validusername' => true, 'playerstats' => $playerstats]);
            }
        }
        else
        {
            $username = null;
        }

        $data = json_decode(Redis::get('server:' . $id));

        // If the vote is already done
        if (Redis::exists($data->id .':user:' . $ip)) {
            // We get the time
            $time = Redis::get($data->id .':user:' . $ip);
            // We add 1H30 to the current time
            $time = ($time + 5400) - time();
            // We return an error with the remaining time
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'playerstats' => $playerstats, 'time' => gmdate("H:i:s", $time)]);
        }

        // If the vote is already done by username
        if ($data->votetype == "VOTIFIER" && $username != null && Redis::exists($data->id .':username:' . strtolower($username))) {
            // We get the time
            $time = Redis::get($data->id .':username:' . strtolower($username));
            // We add 1H30 to the current time
            $time = ($time + 5400) - time();
            // We return an error with the remaining time
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'playerstats' => $playerstats, 'time' => gmdate("H:i:s", $time)]);
        }

        // If the IP address isn't good
        if (!$this->isGoodIp($ip)) {
            // We return an error
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'vpn' => true, 'playerstats' => $playerstats]);
        }

        if (!$this->isGoodIp2($ip)) {
            // We return an error
            return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'vpn' => true, 'playerstats' => $playerstats]);
        }

        if ($data->votetype == "VOTIFIER")
        {
            $username = htmlspecialchars($_POST['username']);

            $serverType = new ClassicVotifier($vsrv->ip, intval($vsrv->port), $vsrv->key);
            $voteType = new ClassicVote($username, 'ServeurMultigames', $ip);
            $vote = new Vote($voteType, $serverType);

            try
            {
                $vote->send();
            }
            catch (\Exception $exception)
            {
                return view('front.vote', ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'votifiererror' => true, 'playerstats' => $playerstats]);
            }

            // Put the vote check
            Redis::set($data->id .':username:' . strtolower($username), time());
            // Set a TTL to the key
            Redis::expire($data->id .':username:' . strtolower($username), 5400);
        }

        // Put the vote
        Redis::set($data->id .':user:' . $ip, time());
        // Set a TTL to the key
        Redis::expire($data->id .':user:' . $ip, 5400);

        if ($data->votetype == "CALLBACK")
        {
            $this->curl_get_async($data->callback_url, [
                'servername' => $id,
                'ip' => $ip,
                'status' => 'SUCCESS',
                'nextvote' => 5400
            ]);
        }
        else
        {
            // Put the vote check
            Redis::set($data->id .':user:' . $ip.':check', time());
            // Set a TTL to the key
            Redis::expire($data->id .':user:' . $ip.':check', 5400);
        }

        // Get the server data as an object
        $server = json_decode(Redis::get('server:' . $id));
        // Increment the vote count
        $c = rand(1,30);
        $b = 1;
        if ($c > 20)
        {
            $b = 2;
        }
        
        for ($i = 0; $i < $b; $i++)
        {
            DB::table('vote_logs')->insert([
                'date' => date("Y-m-d H:i:s"),
                'ip' => $ip,
                'user_agent' => $_SERVER['HTTP_USER_AGENT'],
                'username' => $username,
                'server_id' => $data->id,
                'success' =>  true
            ]);
        }

        $server->votes = $server->votes + (rand(1, 2) - 1);
        // Update in the vote count in the database
        DB::table('server_list')
            ->where('id', '=', $data->id)
            ->update(
                ['votes' => $server->votes]);
        // Put new server data in Redis
        Redis::set('server:' . $id, json_encode($server));

        Session::put('vote', time() + 10);

        // Vote successful
        return redirect()->route( 'info', ['cat' => $catName, 'id' => $id])->with( ['tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'playerstats' => $playerstats] );
    }

    function check($input)
    {
        try {
            $data = [
                'secret' => "6Lf8amQUAAAAAB0k1eHNeM6t8OlIVmdN0KVYzqXH",
                'response' => $input,
                'remoteip' => $_SERVER['REMOTE_ADDR']
            ];

            $curl = curl_init("https://www.google.com/recaptcha/api/siteverify");
            curl_setopt($curl, CURLOPT_POST, true);
            curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, 2);
            curl_setopt($curl, CURLOPT_TIMEOUT, 2); //timeout in seconds
            $response = curl_exec($curl);
            curl_close($curl);
            return $response;
        }
        catch (Exception $v)
        {
            return null;
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

    public function pickInArray($arr, $num = 1) {
        shuffle($arr);

        $r = array();
        for ($i = 0; $i < $num; $i++) {
            $r[] = $arr[$i];
        }
        return $num == 1 ? $r[0] : $r;
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

}
