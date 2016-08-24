<?php
	// DON'T TOUCH
	header("Content-Type: text/plain");
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	$firstTime = microtime(false);
	$a = $_SERVER['REMOTE_ADDR'];
	$db = mysqli_connect("01-sys.badblock-network.fr", "vrack", "cuUj43xfj57aPgAzgGG8XJsbVFPK5egKNNq9ZDn3Cx7uPu3rM2UzwhWVZrWnbyp6");
	mysqli_select_db($db, "vrack");
	
	function secure($db, $string) {
		return htmlspecialchars(mysqli_real_escape_string($db, $string));
	}
	
	function getHostsByCluster($pattern, $amount) {
		$result = array();
		for ($i = 1; $i <= $amount; $i++) {
			$max = 0;
			$as = "";
			for ($b = 0; $b < strlen($pattern); $b++) {
				if ($pattern[$b] == "*") {
					$as .= "*";
					$max++;
				}
			}
			if ($max > 0) {
				$addedZero = $max - strlen($i);
				$addedZeros = "";
				if ($addedZero > 0) {
					for ($a = 1; $a <= $addedZero; $a++) {
							$addedZeros .= "0";
					}
				}
				array_push($result, str_replace($as, $addedZeros.$i, $pattern));
			}else{
				array_push($result, $pattern);
			} 
		}
		return $result;
	}
	
	function getPatternizerByCluster($pattern, $amount) {
		$result = array();
		for ($i = 1; $i <= $amount; $i++) {
			$max = 0;
			$as = "";
			for ($b = 0; $b < strlen($pattern); $b++) {
				if ($pattern[$b] == "*") {
					$as .= "*";
					$max++;
				}
			}
			if ($max > 0) {
				$addedZero = $max - strlen($i);
				$addedZeros = "";
				if ($addedZero > 0) {
					for ($a = 1; $a <= $addedZero; $a++) {
							$addedZeros .= "0";
					}
				}
				$result[str_replace($as, $addedZeros.$i, $pattern)] = $pattern;
			}else{
				$result[$pattern] = $pattern;
			} 
		}
		return $result;
	}
	
	function getUserIP() {
		$client  = @$_SERVER['HTTP_CLIENT_IP'];
		$forward = @$_SERVER['HTTP_X_FORWARDED_FOR'];
		$remote  = $_SERVER['REMOTE_ADDR'];

		if(filter_var($client, FILTER_VALIDATE_IP))
		{
			$ip = $client;
		}
		elseif(filter_var($forward, FILTER_VALIDATE_IP))
		{
			$ip = $forward;
		}
		else
		{
			$ip = $remote;
		}

		return $ip;
	}
	
	function isIPv6($ip) {
	  if(strpos($ip, ":") !== false && strpos($ip, ".") === false) {
		 return true; //Pure format
	  }
	  elseif(strpos($ip, ":") !== false && strpos($ip, ".") !== false){
		return true; //dual format
	  }
	  else{
	  return false;
	  }
	}
	
	$_SERVER['REMOTE_ADDR'] = getUserIP();
	
?>