<?php
	if (!isset($_POST["from"])||!isset($_POST["FIO"])) die (error_reporting);
	include_once "../common_files/functions.php";
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	$pass = ($_POST['Password']!='') ? md5($_POST['Password']) : '';
	$userData = array();
	$query="SELECT `UserID`, `UserTypeID` FROM `users` WHERE concat(`LastName`,' ',`FirstName`,' ',`Patronymic`)='".$_POST["FIO"]."' AND `PassMD5`='".$pass."' AND `UserStatus` = 'ACTIVE'";	//Checking of user existing - auth with FIO
	$result=mysql_query($query);
	$i=0;
	while($row=mysql_fetch_array($result)) {
		$j=0;
		foreach ($row as $key=>$value) {
			if (gettype($key)!="integer") {
				setcookie($key, $value,0,"/");
				$i++;
				$userData[$j] = $value;
				$j++;
			}
		}
	}
	if (!$result||$i!==1) {
		$query="SELECT `UserID`, `UserTypeID`, `FirstName`, `LastName`, `Patronymic` FROM `users` WHERE `NickName`='".$_POST["FIO"]."' AND `PassMD5`='".$pass."' AND `UserStatus` = 'ACTIVE'";	//Checking of user existing - auth with nickname
		$result=mysql_query($query);
		$i=0;
		while($row=mysql_fetch_array($result)) {
			$j=0;
			foreach ($row as $key=>$value) {
				if (gettype($key)!="integer") {
					setcookie($key, $value,0,"/");
					$i++;
					$userData[$j] = $value;
					$j++;
				}
			}
		}
	}
	mysql_close();
	
	setcookie('authInfo', makeMD5Encode($userData[0],$userData[1]),0,"/");
	if (!$result) {
		header('Location:'.siteName().'/auth/?error=\"true\"');
	}
	else {
		if (!strpos($_POST["from"],"/auth/")) {
			header('Location:'.$_POST["from"]);
		}
		else {
			header('Location:'.siteName());
		}
	}
?>