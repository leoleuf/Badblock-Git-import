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


    function enctag($s)
    {
        $s = stripAccents($s);
        $s = strtolower($s);
        $s = str_replace(' ', '-', $s);
        $s = preg_replace('/[^ \w-.]/','', $s);
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