<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 12/04/2019
 * Time: 00:30
 */

namespace App\Controllers;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class YoutubersController extends Controller
{

    private $API_KEY = "AIzaSyAPilPwo0-iZq3Elj7hYHpFzhI089h6dS0";

    public function getHome(RequestInterface $request, ResponseInterface $response)
    {
        $sort = [];

        foreach ($this->container->mongo->youtuber_list->find() as $key)
        {
            $INFO = \GuzzleHttp\json_decode(file_get_contents("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=".$key['youtuber_uuid']."&key=".$this->API_KEY))->items[0];
            $array = [

                'sub' => $INFO->statistics->subscriberCount,
                'img' => $INFO->snippet->thumbnails->default->url,
                'link' => "https://www.youtube.com/channel/".$key['youtuber_uuid']."/?sub_confirmation=1",
                'title' => $INFO->snippet->title

            ];

            array_push($sort, $array);

        }

        $this->render($response, 'pages.youtubeur', ['data' => $sort]);

    }
}