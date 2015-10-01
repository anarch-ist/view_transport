<?php ob_start(); ?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>Работа с накладными</title>
	<link rel="stylesheet" type="text/css" href="./style.css">
	<script type="text/javascript" src="../../common_files/sessvars.js"></script>
</head>
<body>
	<div id="container">
		<div id="top-page">
			<?php
				echo "\n";
				require "../../common_files/top-page.php";
			?>
		</div>
<?php
	include_once "../../common_files/functions.php";
	if (!isset($_COOKIE["UserTypeID"])||!isset($_COOKIE["UserID"])||!md5IsEqual()) {
		foreach($_COOKIE as $key => $value) {
			setcookie($key,null,null,"/");
		}
		include_once "../../common_files/authorize.php";
	}
	elseif ($_COOKIE["UserTypeID"]==="0001"){
		include_once "content.php";
	}
	else {
		$number = 1;
		include_once "../../common_files/no_rights.php";
	}
?>
		<div id="foot-page"><?php include_once '../../common_files/foot-page.php'; ?></div>
	</div>
</body>
</html>