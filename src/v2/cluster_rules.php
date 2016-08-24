<?php
	
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	require('cluster_fetch_subnets.php');
	
	$rules = array();
	foreach($subnets as $subnet) {
		if (empty($subnet)) continue;
		$subnetsRulesRequest = mysqli_query($db, "SELECT * FROM vrack_rules WHERE rule_cluster = '".secure($db, $subnet)."'");
		while($subnetsRulesData = mysqli_fetch_assoc($subnetsRulesRequest)) {
			array_push($rules, json_encode($subnetsRulesData));
		}
    }
	
	echo json_encode($rules);
	
?>