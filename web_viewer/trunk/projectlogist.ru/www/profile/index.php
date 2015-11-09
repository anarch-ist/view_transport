<?php ob_start(); ?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title><?php if ($_GET['edit']==true) echo 'редактирование данных пользователя'; else echo 'просмотр данных пользователя'?></title>
	<link rel="stylesheet" type="text/css" href="style.css">
	<link rel="stylesheet" type="text/css" href="../common_files/protoplasm.css">
	<link rel="stylesheet" type="text/css" href="../common_files/datepicker/datepicker.css">
	<link rel="stylesheet" type="text/css" href="../common_files/timepicker/timepicker.css">
	<script type="text/javascript" src="../common_files/JSfunctions.js"></script>
	<script type="text/javascript" src="../common_files/sessvars.js"></script>
	<script type="text/javascript" src="../common_files/json.js"></script>
	<script type="text/javascript" src="../common_files/protoplasm.js"></script>
</head>
<body>
	<div id="container">
		<div id="top-page">
<?php
	echo "\n";
	require_once "../common_files/top-page.php";
?>
		</div>
<?php
	include_once "../common_files/functions.php";
	if (!isset($_COOKIE["UserTypeID"])||!isset($_COOKIE["UserID"])||!md5IsEqual()) {
		foreach($_COOKIE as $key => $value) {
			setcookie($key,null,null,"/");
		}
		include_once "../../common_files/authorize.php";
	}
	else {
		include_once "content.php";
	}
?>
		<div id="foot-page"><?php require_once '../common_files/foot-page.php'; ?></div>
	</div>
</body>
</html>