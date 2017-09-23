<?php
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class IpController extends Controller
{
	public function getIp(RequestInterface $request, ResponseInterface $response){

        $addr = '2a01:cb19:81d2:9900:a44a:e8b4:85d:5e53';


        $db6 = geoip_open("../App/config/GeoIPv6.dat", GEOIP_STANDARD);
        $db4 = geoip_open("../App/config/geoip.dat", GEOIP_STANDARD);


        $isIPv6 = filter_var($addr, FILTER_VALIDATE_IP, FILTER_FLAG_IPV6);
        if ($isIPv6) {
            $code = geoip_country_code_by_addr_v6($db6, $addr);
        } else {
            $code = geoip_country_code_by_addr($db4, $addr);
        }

        geoip_close($db4);
        geoip_close($db6);



	}
}
