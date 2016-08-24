<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	require('cluster_fetch_subnets.php');
	
	if (!isset($_GET['subnet']) OR empty($_GET['subnet']) OR $_GET['subnet'] == "*") exit("Invalid args.");
	$subnet = secure($db, $_GET['subnet']);
	$neededSubnet = str_replace("NEEDED_", "", $subnet);
	if (!strcasecmp($neededSubnet, $subnet)) exit('Invalid args.');
	if (!in_array($neededSubnet, $subnets)) {
		exit('Access denied.');
	}
	
	$clusters = array();
	
	$request = mysqli_query($db, "SELECT * FROM vrack_cluster");
	while($data = mysqli_fetch_assoc($request)) {
		$dsubnets = explode("|", $data['cluster_subnets']);
		if (in_array($subnet, $dsubnets)) {
			$dataMax = $data['cluster_amount'];
			$hostname = $data['cluster_pattern'];
			$clusters = array_merge($clusters, getHostsByCluster($hostname, $dataMax));
		}
	}
	
	echo json_encode($clusters);
	
?>