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
	private $naServerIp = "play.badblock.fr";

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
        if ($this->container->session->exist('user')){
            if (!$this->container->session->exist('points')){
                //Search data player
                $player = $this->container->mongoServer->players->findOne(['name' => strtolower($this->container->session->getProfile('username')['username'])]);
                if ($player == null){
                    return false;
                }
                //Search money of player
                $player = $this->container->mongo->fund_list->findOne(['uniqueId' => $player->uniqueId]);
                if (!isset($player->points)){
                    $shoppoints = 0;
                }else{
                    $shoppoints = $player->points;
                }
                $this->container->session->set('points', $shoppoints);
            }else{
                $shoppoints = $this->container->session->get('points');
            }
        }else{
            $shoppoints = "0";
        }


        $ip = $_SERVER['REMOTE_ADDR'];

        if (isset($_SERVER['CF_CONNECTING_IP']))
        {
            $ip = $_SERVER['CF_CONNECTING_IP'];
        }

        if ($ip == "127.0.0.1")
        {
            $this->container->session->set('eula', true);
            $eula = true;
        }
        else if ($ip == "::1")
        {
            $this->container->session->set('eula', true);
            $eula = true;
        }
        else 
        {
            //$ips = $this->container->mongoServer->ips->count(['name' => $ip]);

            var_dump($ip);


            if (!$this->container->session->exist('eula'))
            {

                if ($ips == null)
                {
                    $eula = false;
                }
                else
                {
                    $eula = true;
                }

                $this->container->session->set('eula', $eula);
            }
            else
            {
                $eula = $this->container->session->get('eula');
            }
        }

        // Ajout de l'EULA aux variables globales twig
        $twig = $this->container->view->getEnvironment();
        $twig->addGlobal('eula', $eula);
        $twig->addGlobal('points', $shoppoints);
        $twig->addGlobal('currentUrl', "http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);

        // If the key doesn't exist in cache
        if (!$this->container->session->exist('mcIp')) {
            $result = [];

            //Geo IP
            //Get list of countries

            //open geoip file
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

            $this->container->session->set('mcIp', $result[0]);
            $generatedIp = $result[0];

        } else {
            $generatedIp = $this->container->session->get('mcIp');
        }

        //ajout de l'ip généré aux variables globales twig
        $twig = $this->container->view->getEnvironment();
        $twig->addGlobal('mc_ip', $generatedIp);

        //return next
		return $next($request, $response);
	}
}