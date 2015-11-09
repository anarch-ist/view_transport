<?php
	ob_start();
	include_once "../common_files/functions.php";
	foreach($_COOKIE as $key => $value) {
		setcookie($key,null,null,"/");
	}
	unset($_SESSION);
	header('Location:'.siteName());
?>