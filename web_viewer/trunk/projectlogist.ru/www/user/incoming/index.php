<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>Движение накладных</title>
	<link rel="stylesheet" type="text/css" href="./style.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/protoplasm.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/datepicker/datepicker.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/timepicker/timepicker.css">
	<script type="text/javascript" src="../../common_files/JSfunctions.js"></script>
	<script type="text/javascript" src="../../common_files/sessvars.js"></script>
	<script type="text/javascript" src="../../common_files/json.js"></script>
	<script type="text/javascript" src="../../common_files/protoplasm.js"></script>
</head>
<body>
	<div id="container">
		<div id="top-page">
<?php
	echo "\n";
	require_once "../../common_files/top-page.php";
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
	elseif ($_COOKIE["UserTypeID"]==="0004"){
		include_once "content.php";
	}
	else {
		$number = 3;
		include_once "../../common_files/no_rights.php";
	}
?>
		<div id="foot-page"><?php require_once '../../common_files/foot-page.php'; ?></div>
	</div>
	<div class="window-phone">
		<div class="window-dialog">
			<p style="margin: 0;">Время прибытия</p>
			<div style="text-align: left;width: 80%;margin: 10px auto;">
				<label>
					<input type="radio" name="dateVariant" checked onclick="document.getElementsByName('date')[0].disabled='disabled';"> сейчас
				</label>
				<br>
				<label>
					<input type="radio" name="dateVariant" onclick="document.getElementsByName('date')[0].removeAttribute('disabled');"> другое время:
					<input type="text" name="date" class="datepicker" disabled>
				</label>
			</div>
			<div style="margin:5px 0;">
				<button>Ок</button>
				<button>Отмена</button>
			</div>
		</div>
	</div>
</body>
</html>