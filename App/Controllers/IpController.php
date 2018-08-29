<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class IpController extends Controller
{
	public function getIp(RequestInterface $request, ResponseInterface $response)
    {
		$ip = $_SERVER['REMOTE_ADDR'];

		if (isset($_SERVER['CF_CONNECTING_IP']))
        {
            $ip = $_SERVER['CF_CONNECTING_IP'];
        }

        // If the key doesn't exist in cache
		if (!$this->container->session->exist('mcIp')) {
			$result = [];

			// Geo IP
			// Get list of countries

			// Open geoip file
			$db6 = geoip_open("../App/config/GeoIPv6.dat", GEOIP_STANDARD);
			$db4 = geoip_open("../App/config/geoip.dat", GEOIP_STANDARD);

			$isIPv6 = filter_var($ip, FILTER_VALIDATE_IP, FILTER_FLAG_IPV6);
			if ($isIPv6) {
				$code = geoip_country_code_by_addr_v6($db6, $ip);
				$pays = geoip_country_name_by_addr_v6($db6, $ip);
			} else {
				$code = geoip_country_code_by_addr($db4, $ip);
				$pays = geoip_country_name_by_addr($db4, $ip);
			}

			geoip_close($db4);
			geoip_close($db6);


			// Check Pays
			if (in_array($code, $this->euAllowed))
			{
				$iptos = $this->euServerIp;
			}
			elseif (in_array($code, $this->naAllowed))
            {
				$iptos = $this->naServerIp;
			}
			else
			{
				$iptos = $this->defaultServerIp;
			}

			// Compile et envoie
			array_push($result, $iptos, $code, $pays);

            $this->container->session->set('mcIp', $result);
			$generatedIp = $result[0];

		} else {
			$generatedIp = $this->container->session->get('mcIp');
		}

		//ajout de l'ip généré aux variables globales twig
		$twig = $this->container->view->getEnvironment();
		$twig->addGlobal('mc_ip', $generatedIp);

		//return next
		return $response->withJson([
			'ip' => $generatedIp
		]);

	}
}
