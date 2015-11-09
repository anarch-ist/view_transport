<?php
	if (!isset($_POST["from"])||!isset($_POST["FIO"])) die (error_reporting);
	include_once "../common_files/functions.php";
	$connection = getConnection();
	$pass = ($_POST['Password']!='') ? md5($_POST['Password']) : '';
	$userData = array();
	$query="SELECT `UserID`, `UserTypeID` FROM `users` WHERE concat(`LastName`,' ',`FirstName`,' ',`Patronymic`)='".$_POST["FIO"]."' AND `PassMD5`='".$pass."' AND `UserStatus` = 'ACTIVE'";	//Checking of user existing - auth with FIO
	$result=mysqli_query($connection, $query);
	$i=0;
	if ($result) {
		while($row=mysqli_fetch_array($result)) {
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
	if (!$result||$i!==1) {
		$query="SELECT `UserID`, `UserTypeID`, `FirstName`, `LastName`, `Patronymic` FROM `users` WHERE `NickName`='".$_POST["FIO"]."' AND `PassMD5`='".$pass."' AND `UserStatus` = 'ACTIVE'";	//Checking of user existing - auth with nickname
		//$query="insert into users values('100000001', '0', '0001', 'ROOT', 'ROOT', 'ROOT', 'ROOTUSER', '', '+79151186753', NULL, '', 'ACTIVE');";	//Checking of user existing - auth with nickname
		echo $query;
		$result=mysqli_query($connection, $query);
		$i=0;
		while($row=mysqli_fetch_array($result)) {
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
	mysqli_close();
	
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