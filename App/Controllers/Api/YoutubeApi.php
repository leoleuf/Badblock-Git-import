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

    public static function getTChannel($channelID)
    {
        return \GuzzleHttp\json_decode(file_get_contents("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=".$channelID."&key=".self::$API_KEY))->items[0];

    }



}
