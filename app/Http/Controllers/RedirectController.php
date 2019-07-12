<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;


class RedirectController extends Controller
{

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */

    public function partenaires($name)
    {
        $name = strtolower($name);

        $partenaires = array(
            'clawnity' => 'http://clawnity.livehost.fr/index.php',
            'launcher-minecraft' => 'https://launcher-minecraft.com/fr/telecharger'
        );

        if (!isset($partenaires[$name]))
        {
            abort(404);
        }

        return '<!DOCTYPE html><html><head><meta name="robots" content="noindex, nofollow"><noscript><META http-equiv="refresh" content="0;URL=' . $partenaires[$name] . '"></noscript><title>Redirection</title></head><script>window.opener = null; location.replace("' . $partenaires[$name] . '")</script><script async src="https://www.googletagmanager.com/gtag/js?id=UA-122426050-1"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag(\'js\', new Date());gtag(\'config\', \'UA-122426050-1\');</script></head><body></body></html>';
    }

    public function redirect($cat, $id)
    {
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
        $server = Redis::get('server:' . $id);
        $server = json_decode($server);

        $ip = $_SERVER['REMOTE_ADDR'];

        if (isset($_SERVER['HTTP_CF_CONNECTING_IP'])) {
            $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
        }

        if (!Redis::exists($server->id . ':website:' . $ip)) {
            if ($this->isGoodIp($ip) && $this->isGoodIp2($ip)) {
                Redis::set($server->id . ':website:' . $ip, true);
                Redis::expire($server->id . ':website:' . $ip, 86400);
                $server->clicks = $server->clicks + 1;

                DB::table('server_list')
                    ->where('id', '=', $server->id)
                    ->update(
                        ['clicks' => $server->clicks]);

                // Put the click in logs
                DB::table('click_logs')->insert([
                    'date' => date("Y-m-d H:i:s"),
                    'ip' => $ip,
                    'user_agent' => $_SERVER['HTTP_USER_AGENT'],
                    'server_id' => $server->id
                ]);

                Redis::set('server:' . $id, json_encode($server));
            }

            // Redirect
            $wb = htmlspecialchars($server->website);
            if (isset($server->ad))
            {
                return '<!DOCTYPE html><html><head><meta name="robots" content="noindex, nofollow"><noscript><META http-equiv="refresh" content="0;URL=' . $wb . '"></noscript><title>Redirection</title><script>window.opener = null; location.replace("' . $wb . '")</script><script async src="https://www.googletagmanager.com/gtag/js?id=UA-122426050-1"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag(\'js\', new Date());gtag(\'config\', \'UA-122426050-1\');</script></head><body></body></html>';
            }
            return view('front.redirect', ['catName' => $catName, 'data' => $server, 'website' => $wb]);
            //return '<!DOCTYPE html><html><head><meta name="robots" content="noindex, nofollow"><noscript><META http-equiv="refresh" content="0;URL=' . $wb . '"></noscript><title>Redirection</title><script>window.opener = null; location.replace("' . $wb . '")</script><script async src="https://www.googletagmanager.com/gtag/js?id=UA-122426050-1"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag(\'js\', new Date());gtag(\'config\', \'UA-122426050-1\');</script></head><body></body></html>';
        } else {
            // Redirect
            $wb = htmlspecialchars($server->website);
            return '<!DOCTYPE html><html><head><meta name="robots" content="noindex, nofollow"><noscript><META http-equiv="refresh" content="0;URL=' . $wb . '"></noscript><title>Redirection</title><script>window.opener = null; location.replace("' . $wb . '")</script><script async src="https://www.googletagmanager.com/gtag/js?id=UA-122426050-1"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag(\'js\', new Date());gtag(\'config\', \'UA-122426050-1\');</script></head><body></body></html>';
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
