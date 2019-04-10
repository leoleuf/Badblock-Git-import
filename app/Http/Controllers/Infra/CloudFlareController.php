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

    public function index()
    {

        return view('infra.cloudflare');

    }

    public function purge_all()
    {
        $this->sendCloudFlareRequest("purge_cache", true);
        echo "Cache purge : success";
    }

    private function sendCloudFlareRequest($mode, $post = null)
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL,"https://api.cloudflare.com/client/v4/zones/".env('CLOUDFLARE_ZONE_ID')."/".$mode);

        if($post) {

            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, '{"purge_everything":true}');

        }

        curl_setopt($ch, CURLOPT_HTTPHEADER, [

            'X-Auth-Email: '.env('CLOUDFLARE_EMAIL'),
            'X-Auth-Key: '.env('CLOUDFLARE_TOKEN'),
            'Content-Type: application/json'

        ]);

        $server_output = curl_exec($ch);

        curl_close ($ch);

        var_dump($server_output);

    }

}