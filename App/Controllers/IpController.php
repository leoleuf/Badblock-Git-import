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

            $db = array(
                'Geo4' => geoip_open("../App/config/geoip.dat", GEOIP_STANDARD),
                'Geo6' => geoip_open("../App/config/GeoIPv6.dat", GEOIP_STANDARD),
            );

            $isIPv6 = filter_var($addr, FILTER_VALIDATE_IP, FILTER_FLAG_IPV6);
            if ($isIPv6) {
                $location = geoip_country_code_by_addr_v6($db, $addr);
            } else {
                $location = geoip_country_code_by_addr($db, $addr);
            }

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


        var_dump($pays);

        return json_encode($result);


	}
}
