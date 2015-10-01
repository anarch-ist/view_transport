<?php ob_start();?><!DOCTYPE html>
<!-- Разработка Андрея Баранова™ -->
<html>
<head>
	<meta charset="UTF-8">
	<title>Добавление данных</title>
</head>
<body>
	<h4 style="font-weight:normal">Добавление данных...</h4>
<?php
	include_once "../../../common_files/functions.php";
	if (!isset($_POST["FirstName"])) {
		echo "</body></html>";
		die (error_reporting);
	}
	if ($_POST["from"]!=null) {
		$currentSite=$_POST["from"];
	}
	else {
		$currentSite=siteName();
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	
	$query="SELECT MAX(`UserID`) FROM `users`";
	$result=mysql_query($query);
	$row=mysql_fetch_array($result);
	$currID=1+$row["MAX(`UserID`)"];
	if ($currID==1) {
		$currID=getMinID('UserID');
	}
	$query="SELECT `FirstName`, `LastName`, `Patronymic`,`NickName` FROM `users` WHERE ";
	$query.="`LastName` = '".$_POST["LastName"]."';";
	$result=mysql_query($query);
	if (!$result) {
		echo "ошибка БД";
		mysql_close();
		echo "</body></html>";
		die (error_reporting);
	}
	$count=mysql_num_rows($result);
	$NickName = $_POST["LastName"];
	if ($count!==0) {
		$NickName .= '0'.$count;
	}
	echo $NickName;
	$query="INSERT INTO `users` (`UserID`, `UserTypeID`, `FirstName`, `LastName`, `Patronymic`,`NickName`, `PassMD5`, `Telephone`, `Email`, `LastModBy`,`IsEnabled`) VALUES ('";
	$query.=$currID."', '".$_POST["UserTypeID"]."', '".$_POST["FirstName"]."', '".$_POST["LastName"]."', '".$_POST["Patronymic"]."', '".$NickName."', '".(($_POST["Pass"]!=='') ? md5($_POST["Pass"]) : '')."', '".$_POST["Telephone"]."', '".$_POST["Email"]."', '".$_COOKIE["UserID"]."','1');";	//TODO: make good query!
	$result=mysql_query($query);
	if (!$result) {
		echo "ошибка добавления данных в БД";
		mysql_close();
		echo "</body></html>";
		die (error_reporting);
	}
	mysql_close();
	echo ("<script type=\"text/javascript\">location.replace('".$currentSite."');</script>\n");
?>
</body>
</html>