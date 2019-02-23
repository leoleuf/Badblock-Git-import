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
        return \GuzzleHttp\json_decode(file_get_contents("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=".$channelID."&key=".self::$API_KEY))->items[0]->snippet;
    }

}
