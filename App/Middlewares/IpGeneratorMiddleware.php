<?php

namespace App\Middlewares;

/**
 * PSR7 & PSR15 Middleware for generate Server ip from location of client
 *
 * Class IpGeneratorMiddleware
 * @package App\Middlewares
 */
class IpGeneratorMiddleware
{
	/**
	 * The default server ip
	 *
	 * @var string
	 */
	private $defaultServerIp = "play.badblock.fr";

	/**
	 * The north american server ip
	 *
	 * @var string
	 */
	private $naServerIp = "na.badblock.fr";

	/**
	 * The europeans server ip
	 *
	 * @var string
	 */
	private $euServerIp = "eu.badblock.fr";

	/**
	 * List of europeans countries allowed for europeans server ip
	 *
	 * @var array
	 */
	private $euAllowed = ["FR", "ES", "PT", "GB", "IE", "BE", "NL", "LU", "DE", "IT", "DK", "SE", "CZ", "AT", "SI", "HR", "BA", "ME", "PL", "SK", "HU", "HR", "RS", "BG", "MK", "AL", "GR", "TR", "CY", "MT", "EE", "LV", "LT", "FI"];

	/**
	 * List of north american countries allowed for north american server ip
	 *
	 * @var array
	 */
	private $naAllowed = ["US", "CA", "MX"];

	/**
	 * Slim container object
	 */
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke($request, $response, $next)
	{
		$ip = $_SERVER['REMOTE_ADDR'];
		//if the key doesn't exist in cache
		if (!$this->container->redis->exists("ip_" . $ip)) {
			$result = [];

			//Geo IP
			//Get list of countries

			//open geoip file
			$gi = geoip_open("../App/config/geoip.dat", GEOIP_STANDARD);
			$code = geoip_country_code_by_addr($gi, $ip);
			$pays = geoip_country_name_by_addr($gi, $ip);
			geoip_close($gi);

			//Check Pays
			if (in_array($code, $this->euAllowed)) {

				$iptos = $this->euServerIp;

			} elseif (in_array($code, $this->naAllowed)) {

				$iptos = $this->naServerIp;

			} else {

				$iptos = $this->defaultServerIp;

			}

			//compile et envoie
			array_push($result, $iptos, $code, $pays);

			//Mise en cache
			$this->container->redis->setJson('ip_' . $ip, $result);
			$this->container->redis->expire('ip_' . $ip, 36);

			$generatedIp = $result[0];

		} else {
			$generatedIp = $this->container->redis->getjson('ip_' . $ip)[0];
		}

		//ajout de l'ip généré aux variables globales twig
		$twig = $this->container->view->getEnvironment();
		$twig->addGlobal('mc_ip', $generatedIp);

		//return next
		return $next($request, $response);
	}
}