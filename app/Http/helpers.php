<?php

    function stripAccents($str) {
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

    function _bot_detected() {
        if (!isset($_SERVER['HTTP_USER_AGENT']))
        {
            return true;
        }

        if (!isset($_SERVER["HTTP_CF_IPCOUNTRY"]) OR (strtolower($_SERVER["HTTP_CF_IPCOUNTRY"]) != "fr" && strtolower($_SERVER["HTTP_CF_IPCOUNTRY"]) != "be" &&
            strtolower($_SERVER["HTTP_CF_IPCOUNTRY"]) != "ch"))
        {
            return true;
        }

        return (
            isset($_SERVER['HTTP_USER_AGENT'])
            && preg_match('/bot|crawl|slurp|spider|mediapartners/i', $_SERVER['HTTP_USER_AGENT'])
        );
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