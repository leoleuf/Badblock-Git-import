<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 27/03/2019
 * Time: 00:08
 */

namespace App\Http\Controllers\Infra;


use App\Http\Controllers\Controller;

class CloudFlareController extends Controller
{

    private $token;
    private $id;

    public function index()
    {
        $this->id = getenv('ZONE_ID_CLOUDFLARE');
        $this->token = getenv('TOKEN_CLOUDFLARE');

        return view('infra.cloudflare');

    }

    public function get_cloudflare_info($mode, $post = null)
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL,"https://api.cloudflare.com/client/v4/zones/".$this->id."/settings/".$mode);

        if($post) curl_setopt($ch, CURLOPT_POST, 1);

        curl_setopt($ch, CURLOPT_HTTPHEADER, [

            'X-Auth-Email: email@email.com',
            'X-Auth-Key: '.$this->token

        ]);

        $server_output = curl_exec($ch);

        curl_close ($ch);

        var_dump($server_output);

    }

}