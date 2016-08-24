<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	require('access_killer.php');
	
	$hostPattern = $patternizer[$hostname];
	$request = mysqli_fetch_assoc(mysqli_query($db, "SELECT * FROM vrack_cluster WHERE cluster_pattern = '".secure($db, $hostPattern)."'"));
	if (!$request) exit('Unknown cluster pattern.');
	$subnets = explode("|", $request['cluster_subnets']);
	array_push($subnets, "*");
	$request = mysqli_fetch_assoc(mysqli_query($db, "SELECT * FROM vrack_cluster WHERE cluster_pattern = '".secure($db, $hostname)."'"));
	$personalSubnets = explode("|", $request['cluster_subnets']);
	$subnets = array_merge($subnets, $personalSubnets);

?>