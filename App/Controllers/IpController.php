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
        $ip = $_SERVER['REMOTE_ADDR'];


        if (!$this->redis->exists("ip_".$ip)){
            //Variables
            $result =  [];
            $eu = array("FR", "ES", "PT", "GB", "IE", "BE", "NL", "LU", "DE", "IT", "DK", "SE", "CZ", "AT", "SI", "HR", "BA", "ME", "PL", "SK", "HU", "HR", "RS", "BG", "MK", "AL", "GR", "TR", "CY", "MT", "EE", "LV", "LT", "FI");
            $na = array("US", "CA", "MX");

            //Geo IP
            $code = "FR";
            $gi = geoip_open("C:\Users\MAT_3\PhpstormProjects\badblock\App\config\geoip.dat", GEOIP_STANDARD);

            $code = geoip_country_code_by_addr($gi, $ip);
            $pays = geoip_country_name_by_addr($gi, $ip);
            geoip_close($gi);

            //Check Pays
            if (in_array($code, $eu)) {
                $iptos = "eu.badblock.fr";
            }elseif (in_array($code, $na)){
                $iptos = "na.badblock.fr";
            }else{
                $iptos = "play.badblock.fr";
            }

<<<<<<< HEAD
        var_dump($req);
=======
            //Copille et envoie
            array_push($result,$iptos,$code,$pays);

            //Mise en cache
            $this->redis->setJson('ip_'.$ip, $result);
            $this->redis->expire('ip_'.$ip, 36);

            return json_encode($result);

        }else{
            return json_encode($this->redis->getjson('ip_'.$request->getAttribute('ip_address')));
        }
>>>>>>> d0f9c757dbc68ceba183fccde6f02a326e47cc65



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