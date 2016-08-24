<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	require('../header.php');
	
	$hasAccess = false;
	
	// DON'T TOUCH
	$array = array();
	$patternizer = array();
	// Generate text
	$request = mysqli_query($db, "SELECT * FROM vrack_cluster");
	while ($data = mysqli_fetch_assoc($request)) {
		$dataMax = $data['cluster_amount'];
		$hostname = $data['cluster_pattern'];
		$array = array_merge($array, getHostsByCluster($hostname, $dataMax));
		$patternizer = array_merge($patternizer, getPatternizerByCluster($hostname, $dataMax));
	}
	
	$g = false;
	$hostname = null;
	if (isIPv6($_SERVER['REMOTE_ADDR'])) $g = true;
	foreach($array as $key) {
		$ip = gethostbyname($key);
		if ($ip == $_SERVER['REMOTE_ADDR']) {
			$g = true;
			$hostname = $key;
		}
	}
		
	$hasAccess = $g;
	
?>