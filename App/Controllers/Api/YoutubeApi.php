<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 14/02/2019
 * Time: 14:02
 */

namespace App\Controllers\Api;


class YoutubeApi
{
    private static $API_KEY = "AIzaSyAPilPwo0-iZq3Elj7hYHpFzhI089h6dS0";

    public static function getChannel($channelID)
    {

        $INFO = \GuzzleHttp\json_decode(file_get_contents("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=".$channelID."&key=".self::$API_KEY))->items[0];

        return [

            'sub' => $INFO->statistics->subscriberCount,
            'img' => $INFO->snippet->thumbnails->default->url,
            'link' => "https://www.youtube.com/channel/".$channelID."/?sub_confirmation=1",
            'title' => $INFO->snippet->title

        ];
    }

    public static function getChannelList()
    {
        $array = [
            'UC042bAvKzEP4rrcWtl_bPDg',
            'UClZ4fjzTkwq2OBEg5lNYUpg',
            'UCLrKooc93isJCtR8R_QPqaw',
            'UCYnTsw53vtZoGzLfsmaVbog',
            'UCjpwkoMzhPpofZnvw6U8zJw',
            'UCQpjw27nBD7gPbJQBmWgZyw',
            'UCjI3xx4Uq6hXeImeAWTm84Q',
            'UCEqcSK-ZkZ2e6K9cmuFDltQ',
            'UCScJfUTftG77iBdIg6bmTPw',
            'UCxyidCPyj8RjPFgag-OZ0Yw',
            'UC3K0j1y_Y51LTPeHBSloLrg',
            'UCPP3FKFbnKT13jWCZBNopkQ',
            'UCUSjztLKURnO7vTMHJz-alQ',
            'UCO79hOCc5YMhW2ej3zfAZmw',
            'UC7Psxx4-vBu3Qtjwnx2U7yg'
        ];

        $sort = [];

        foreach ($array as $key => $value)
        {
            array_push($sort, self::getChannel($value));
        }

        return $sort;

    }

    public static function getTChannel($channelID)
    {
        return \GuzzleHttp\json_decode(file_get_contents("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=".$channelID."&key=".self::$API_KEY))->items[0];

    }



}
