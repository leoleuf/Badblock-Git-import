<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 19/08/2017
 * Time: 22:28
 */

namespace App\Controllers;

use HansOtt\PSR7Cookies\SetCookie;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use App\TfaBin;


class SessionController extends Controller
{
	public function login(ServerRequestInterface $request, ResponseInterface $response)
	{
		//si les variables sont passés
		if (isset($_POST['username']) & isset($_POST['password'])) {
			//si les variables sont non vide
			if (!empty($_POST['username']) & !empty($_POST['password'])) {
				//obtenir le cookie à partir de de l'api xenforo avec un username et un password
				$rep = $this->xenforo->getLogin($_POST['username'], $_POST['password'], $_SERVER['REMOTE_ADDR']);
				//la réponse est false si les mots de passe ou le username est incorrect
				if($rep == "tfa") {
                    $_SESSION["tfa_name"] = $_POST['username'];
                    $_SESSION["tfa_pass"] = $_POST['password'];
					//redirect to last page
					return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#tfa-modal');
				} elseif($rep == "bad") {
                    //Erreur: Username ou mdp invalides
                    $this->flash->addMessage('login_error', "Nom d'utilisateur ou mot de passe incorrect");
                    //Add log in forum database
                    $user = $this->xenforo->getUser($_POST['username']);
                    //Insert in forum database for plugin login security
                    $ip = $this->convertIpStringToBinary($request->getAttribute('ip_address'));
                    $data = [
                        'user_id'   => $user["user_id"],
                        'attempt_ip' => $ip,
                        'attempt_type'  => "login",
                        'attempt_timestamp'  => microtime(true),
                        'attempt_success'  => false,
                        'tfa_required'  => 0,
                        'tfa_success'  => 0,
                        'tfa_timestamp'  => 0,
                    ];
                    $this->container->mysql_forum->insert('lsec_attempt', $data);

                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#login-modal');
                }else{
                        //Get user in fact
                        $user = $this->xenforo->getUser($_POST['username']);
                        //Insert in forum database for plugin login security
                        $ip = $this->convertIpStringToBinary($request->getAttribute('ip_address'));
                        $data = [
                            'user_id'   => $user["user_id"],
                            'attempt_ip' => $ip,
                            'attempt_type'  => "login",
                            'attempt_timestamp'  => microtime(true),
                            'attempt_success'  => true,
                            'tfa_required'  => 0,
                            'tfa_success'  => 0,
                            'tfa_timestamp'  => 0,
                        ];
                        $this->container->mysql_forum->insert('lsec_attempt', $data);

                        //Directly login
                        //user
                        //ajout du cookie
                        $cookie = new SetCookie($rep['cookie_name'], $rep['cookie_id'], $rep['cookie_expiration'], $rep['cookie_path'], $rep['cookie_domain'], $rep['cookie_secure']);
                        $response = $cookie->addToResponse($response);

                        //String to array for secondary group
                        $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);

                        //mise de l'utilisateur en session
                        $this->session->set('user', [
                            'id' => $user['user_id'],
                            'username' => $user['username'],
                            'email' => $user['email'],
                            'user_group_id' => $user['user_group_id'],
                            'secondary_group_ids' => $user['secondary_group_ids'],
                            'custom_title' => $user['custom_title'],
                            'is_admin' => $user['is_admin'],
                            'is_banned' => $user['is_banned'],
                            'is_staff' => $user['is_staff'],
                            'is_moderator' => $user['is_moderator']
                        ]);

                        //redirect to home
                        return $this->redirect($response, $this->pathFor('dashboard'));
                    }
				}
		}elseif(isset($_POST['code']) && !empty($_POST['code'])) {
		    //Check du code TFA
            $data = $this->container->mysql_forum->fetchRow('SELECT user_id FROM xf_user WHERE username = "'. $_SESSION["tfa_name"] .'";')['user_id'];
            $data = $this->container->mysql_forum->fetchRow('SELECT * FROM xf_user_tfa WHERE user_id = "'. $data .'" and provider_id = "totp";')['provider_data'];
            $data = explode('s:16:"', $data);
            $data = explode("\";s:8", $data[1]);

            $secretkey = $data[0];
            $currentcode = $_POST['code'];

            if (TfaBin::verify($secretkey,$currentcode)) {
                $rep = $this->xenforo->getLogin($_SESSION['tfa_name'], $_SESSION['tfa_pass'], $_SERVER['REMOTE_ADDR'],true);
                //user
                $user = $this->xenforo->getUser($_SESSION["tfa_name"]);

                //Insert in forum database for plugin login security
                $ip = $this->convertIpStringToBinary($request->getAttribute('ip_address'));
                $data = [
                    'user_id'   => $user["user_id"],
                    'attempt_ip' => $ip,
                    'attempt_type'  => "login",
                    'attempt_timestamp'  => microtime(true),
                    'attempt_success'  => true,
                    'tfa_required'  => 1,
                    'tfa_success'  => 1,
                    'tfa_timestamp'  => 0,
                ];
                $this->container->mysql_forum->insert('lsec_attempt', $data);

                //Directly login
                //user
                //ajout du cookie
                $cookie = new SetCookie($rep['cookie_name'], $rep['cookie_id'], $rep['cookie_expiration'], $rep['cookie_path'], $rep['cookie_domain'], $rep['cookie_secure']);
                $response = $cookie->addToResponse($response);

                //String to array for secondary group
                $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);

                //mise de l'utilisateur en session
                $this->session->set('user', [
                    'id' => $user['user_id'],
                    'username' => $user['username'],
                    'email' => $user['email'],
                    'user_group_id' => $user['user_group_id'],
                    'secondary_group_ids' => $user['secondary_group_ids'],
                    'custom_title' => $user['custom_title'],
                    'is_admin' => $user['is_admin'],
                    'is_banned' => $user['is_banned'],
                    'is_staff' => $user['is_staff'],
                    'is_moderator' => $user['is_moderator']
                ]);

                //redirect to home
                return $this->redirect($response, $this->pathFor('dashboard'));
            } else {
                $this->flash->addMessage('login_error', "Code incorrect !");
                //Add log in forum database
                $user = $this->xenforo->getUser($_SESSION["tfa_name"]);
                //Insert in forum database for plugin login security
                $ip = $this->convertIpStringToBinary($request->getAttribute('ip_address'));
                $data = [
                    'user_id'   => $user["user_id"],
                    'attempt_ip' => $ip,
                    'attempt_type'  => "login",
                    'attempt_timestamp'  => microtime(true),
                    'attempt_success'  => false,
                    'tfa_required'  => 1,
                    'tfa_success'  => 0,
                    'tfa_timestamp'  => microtime(true),
                ];
                $this->container->mysql_forum->insert('lsec_attempt', $data);
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#tfa-modal');
            }

        } else {
			return $response->write('Bad request')->withStatus(400);
		}
	}


	public function getLogout(ServerRequestInterface $request, ResponseInterface $response)
	{
		$this->session->destroy();
		return $response->withHeader('Location', $this->container->config['forum_url'] . '/logout')->withStatus(302);
	}

    /**
     * Converts a string based IP (v4 or v6) to a 4 or 16 byte string.
     * This tries to identify not only 192.168.1.1 and 2001::1:2:3:4 style IPs,
     * but integer encoded IPv4 and already binary encoded IPs. IPv4
     * embedded in IPv6 via things like ::ffff:192.168.1.1 is also detected.
     *
     * @param string|int $ip
     *
     * @return bool|string False on failure, binary data otherwise
     */
    public function convertIpStringToBinary($ip)
    {
        $originalIp = $ip;
        $ip = trim($ip);

        if (strpos($ip, ':') !== false)
        {
            // IPv6
            if (preg_match('#:(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})$#', $ip, $match))
            {
                // embedded IPv4
                $long = ip2long($match[1]);
                if (!$long)
                {
                    return false;
                }

                $hex = str_pad(dechex($long), 8, '0', STR_PAD_LEFT);
                $v4chunks = str_split($hex, 4);
                $ip = str_replace($match[0], ":$v4chunks[0]:$v4chunks[1]", $ip);
            }

            if (strpos($ip, '::') !== false)
            {
                if (substr_count($ip, '::') > 1)
                {
                    // ambiguous
                    return false;
                }

                $delims = substr_count($ip, ':');
                if ($delims > 7)
                {
                    return false;
                }

                $ip = str_replace('::', str_repeat(':0', 8 - $delims) . ':', $ip);
                if ($ip[0] == ':')
                {
                    $ip = '0' . $ip;
                }
            }

            $ip = strtolower($ip);

            $parts = explode(':', $ip);
            if (count($parts) != 8)
            {
                return false;
            }

            foreach ($parts AS &$part)
            {
                $len = strlen($part);
                if ($len > 4 || preg_match('/[^0-9a-f]/', $part))
                {
                    return false;
                }

                if ($len < 4)
                {
                    $part = str_repeat('0', 4 - $len) . $part;
                }
            }

            $hex = implode('', $parts);
            if (strlen($hex) != 32)
            {
                return false;
            }

            return $this->convertHexToBin($hex);
        }
        else if (strpos($ip, '.'))
        {
            // IPv4
            if (!preg_match('#(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})#', $ip, $match))
            {
                return false;
            }

            $long = ip2long($match[1]);
            if (!$long)
            {
                return false;
            }

            return $this->convertHexToBin(
                str_pad(dechex($long), 8, '0', STR_PAD_LEFT)
            );
        }
        else if (strlen($ip) == 4 || strlen($ip) == 16)
        {
            // already binary encoded
            return $ip;
        }
        else if (is_numeric($originalIp) && $originalIp < pow(2, 32))
        {
            // IPv4 as integer
            return $this->convertHexToBin(
                str_pad(dechex($originalIp), 8, '0', STR_PAD_LEFT)
            );
        }
        else
        {
            return false;
        }
    }

    /**
     * Converts a hex string to binary
     *
     * @param string $hex
     *
     * @return string
     */
    public static function convertHexToBin($hex)
    {
        if (function_exists('hex2bin'))
        {
            return hex2bin($hex);
        }

        $len = strlen($hex);

        if ($len % 2)
        {
            trigger_error('Hexadecimal input string must have an even length', E_USER_WARNING);
        }

        if (strspn($hex, '0123456789abcdefABCDEF') != $len)
        {
            trigger_error('Input string must be hexadecimal string', E_USER_WARNING);
        }

        return pack('H*', $hex);
    }

    /**
     * Converts a binary string containing IPv4 or v6 data to a printable/human
     * readable version. If shortening is enabled, IPv6 data will be collapsed
     * as much as possible.
     *
     * @param string $ip Binary IP data
     * @param bool $shorten
     *
     * @return bool|string
     */
    public static function convertIpBinaryToString($ip, $shorten = true)
    {
        if (strlen($ip) == 4)
        {
            // IPv4
            $parts = array();
            foreach (str_split($ip) AS $char)
            {
                $parts[] = ord($char);
            }

            return implode('.', $parts);
        }
        else if (strlen($ip) == 16)
        {
            // IPv6
            $parts = array();
            $chunks = str_split($ip);
            for ($i = 0; $i < 16; $i += 2)
            {
                $char1 = $chunks[$i];
                $char2 = $chunks[$i + 1];

                $part = sprintf('%02x%02x', ord($char1), ord($char2));
                if ($shorten)
                {
                    // reduce this to the shortest length possible, but keep 1 zero if needed
                    $part = ltrim($part, '0');
                    if (!strlen($part))
                    {
                        $part = '0';
                    }
                }
                $parts[] = $part;
            }

            $output = implode(':', $parts);
            if ($shorten)
            {
                $output = preg_replace('/((^0|:0){2,})(.*)$/', ':$3', $output);
                if (substr($output, -1) === ':' && (strlen($output) == 1 || substr($output, -2, 1) !== ':'))
                {
                    $output .= ':';
                }
            }

            return strtolower($output);
        }
        else if (preg_match('/^[0-9]+$/', $ip))
        {
            return long2ip($ip + 0);
        }
        else
        {
            return false;
        }
    }




}