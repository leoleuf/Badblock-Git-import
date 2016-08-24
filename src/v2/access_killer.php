<?php

	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);
	require('access_checker.php');
	if (!$hasAccess) {
		header('HTTP/1.1 403 Nope.');
		exit('Nope.');
	}

?>