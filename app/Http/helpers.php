<?php

    function stripAccents($str)
    {
        return strtr(utf8_decode($str), utf8_decode('àáâãäçèéêëìíîïñòóôõöùúûüýÿÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ'), 'aaaaaceeeeiiiinooooouuuuyyAAAAACEEEEIIIINOOOOOUUUUY');
    }

    function encname($s)
    {
        $s = stripAccents($s);
        $s = strtolower($s);
        $s = str_replace(' ', '-', $s);
        $s = preg_replace('/[^ \w-]/','', $s);
        return $s;
    }

    function iptest($ip)
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

    function _bot_detected() {
        if (!isset($_SERVER['HTTP_USER_AGENT']))
        {
            return true;
        }

        if (!isset($_SERVER["HTTP_CF_IPCOUNTRY"]) OR (strtolower($_SERVER["HTTP_CF_IPCOUNTRY"]) != "fr" && strtolower($_SERVER["HTTP_CF_IPCOUNTRY"]) != "be"))
        {
            return true;
        }

        if (isset($_SERVER['HTTP_USER_AGENT']) && preg_match('/bot|crawl|slurp|spider|google|mediapartners/i', $_SERVER['HTTP_USER_AGENT']))
        {
            return true;
        }

      /*  $l = session('blek');

        if ($l == NULL)
        {
            // Get the IP address
            $ip = $_SERVER['REMOTE_ADDR'];

            if (isset($_SERVER['HTTP_CF_CONNECTING_IP']))
            {
                $ip = $_SERVER['HTTP_CF_CONNECTING_IP'];
            }

            $l = !iptest($ip);
            session(['blek' => $l]);
        }
*/
        return false;
    }

    function isMobile() {
        if (!isset($_SERVER['HTTP_USER_AGENT']))
        {
            return false;
        }
        return preg_match(
            "/(android|webos|avantgo|iphone|ipad|ipod|blackberry|iemobile|bolt|boost|cricket|docomo|fone|hiptop|mini|opera mini|kitkat|mobi|palm|phone|pie|tablet|up\.browser|up\.link|webos|wos)/i",
            $_SERVER["HTTP_USER_AGENT"]);
    }

    function enctag($s)
    {
        $s = stripAccents($s);
        $s = strtolower($s);
        $s = str_replace(' ', '-', $s);
        $s = str_replace( '.', '-', $s);
        $s = preg_replace('/[^ \w.-]/','', $s);
        return $s;
    }

    function seocat($s)
    {
        try {
            $s = encname($s);
            return config('tag.seocat')[$s];
        }catch (Exception $e)
        {
            return '-error-';
        }
    }