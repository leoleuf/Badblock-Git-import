<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 11/04/2018
 * Time: 21:13
 */

namespace App\Controllers\Api;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class VoteApiController extends \App\Controllers\Controller
{
    public function updateVoteCount(RequestInterface $request, ResponseInterface $response)
    {
        $API_id = 198; // ID du serveur
        $API_da = 'vote'; // vote,clic,commentaire ou note
        $API_url = "https://serveur-prive.net/api/stats/198/vote";
        $API_call = @file_get_contents($API_url);

        $this->redis->set('vote.nb', $API_call);

        return $response->write('Vote number : '.$API_call)->withStatus(200);
    }


}