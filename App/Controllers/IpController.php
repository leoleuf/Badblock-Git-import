<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 10/09/2017
 * Time: 17:20
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;



class IpController extends Controller
{


    public function getip(RequestInterface $request, ResponseInterface $response){

        $eu = array("FR", "ES", "Irix", "Linux");
        $na = array("Mac", "NT", "Irix", "Linux");


        $gi = geoip_open("C:\Users\MAT_3\PhpstormProjects\badblock\App\config\geoip.dat", GEOIP_STANDARD);


        $code = geoip_country_code_by_addr($gi, $_SERVER['REMOTE_ADDR']) . "\t";
        $pays = geoip_country_name_by_addr($gi, $_SERVER['REMOTE_ADDR']) . "\n";

        geoip_close($gi);

        if (in_array($code, $eu)) {
            echo "Go eu .babdb";

        }

        var_dump($_SERVER['REMOTE_ADDR']);
        var_dump($code);
        var_dump($pays);

    }




    public function getUserIP(){

        $ip = $_SERVER['REMOTE_ADDR'];

        $loc = json_decode(file_get_contents('https://ipinfo.io/'.$ip.'/json'), true);

        $pays = isset($loc['country']) ? $loc['country'] : 'Inconnu';
        $asn = isset($loc['org']) ? $loc['org'] : 'Inconnu';

        $result = array('ip'=>$ip,
        'pays'=>$pays,
        'asn'=>$asn,
        'real_ip'=>$real_ip);

        header('Content-Type: application/json');
        echo json_encode($result);

        $client  = @$_SERVER['HTTP_CLIENT_IP'];
        $forward = @$_SERVER['HTTP_X_FORWARDED_FOR'];
        $remote  = $_SERVER['REMOTE_ADDR'];

        if(filter_var($client, FILTER_VALIDATE_IP))
        {
            $ipcheck = $client;
        }
        elseif(filter_var($forward, FILTER_VALIDATE_IP))
        {
            $ipcheck = $forward;
        }
        else
        {
            $ipcheck = $remote;
        }

        return $ipcheck;



        }
}