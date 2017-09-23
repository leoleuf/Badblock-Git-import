<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class IpController extends Controller
{
	public function getIp(RequestInterface $request, ResponseInterface $response){

		    $addr = $_SERVER['REMOTE_ADDR'];

            $db = array(
                'Geo4' => geoip_open("../App/config/geoip.dat", GEOIP_STANDARD),
                'Geo6' => geoip_open("../App/config/GeoIPv6.dat", GEOIP_STANDARD),
            );

            $isIPv6 = filter_var($addr, FILTER_VALIDATE_IP, FILTER_FLAG_IPV6);
            if ($isIPv6) {
                $code = geoip_country_code_by_addr_v6($db, $addr);
            } else {
                $code = geoip_country_code_by_addr($db, $addr);
            }

            var_dump($code->country_code);



	}
}
