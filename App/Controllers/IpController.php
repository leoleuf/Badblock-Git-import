<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class IpController extends Controller
{
	public function getIp(RequestInterface $request, ResponseInterface $response){
		$ip = $_SERVER['REMOTE_ADDR'];

	        //Variables
			$result =  [];
			$eu = array("FR", "ES", "PT", "GB", "IE", "BE", "NL", "LU", "DE", "IT", "DK", "SE", "CZ", "AT", "SI", "HR", "BA", "ME", "PL", "SK", "HU", "HR", "RS", "BG", "MK", "AL", "GR", "TR", "CY", "MT", "EE", "LV", "LT", "FI");
			$na = array("US", "CA", "MX");

			//Geo IP
			$code = "FR";
			$gi = geoip_open("../App/config/GeoIPv6.dat", GEOIP_STANDARD);

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

			//Copille et envoie
			array_push($result,$iptos,$code,$pays);

			//Mise en cache
			$this->redis->setJson('ip_'.$ip, $result);
			$this->redis->expire('ip_'.$ip, 3600);
			
        var_dump($pays);

        return json_encode($result);


	}
}
