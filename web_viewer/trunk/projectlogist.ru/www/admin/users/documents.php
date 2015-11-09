<?php
	include_once "../../common_files/functions.php";
	function main() {
		if ($_POST['Status']==='removeUser') {
			echo 'removeUser '.$_COOKIE['UserID'].", ".$_POST['UserID'];
			removeUser();
			echo ' removeUser';
		}else if ($_POST['Status']==='getAllUsers') {
			getAllUsers();
		}else if ($_POST['Status']==='getMyUsers') {
			getAdminUsers();
		}
		else {
			echo $_POST;
			echo ' ... и идите к админу';
		}
	}
	class User {
		public $UserID;
		public $CreatedBy;
		public $UserName;
		public $UserTypeText;
		function __construct($str) {
			$this->UserID = $str['UserID'];
			$this->CreatedBy = $str['CreatedBy'];
			$this->UserName = $str['UserName'];
			$this->UserTypeText = $str['UserTypeText'];
		}
	}
	function getAllUsers() {
		$query="SELECT `users`.`UserID`, `UserTypeText`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`, `users`.`Patronymic`) as `UserName`, concat_ws(' ', T.`LastName`, T.`FirstName`, T.`Patronymic`) as `CreatedBy` FROM `users`, `users` as T, `usertype` WHERE `users`.`IsEnabled` = 1 AND `usertype`.`UserTypeID` = `users`.`UserTypeID` AND `users`.`LastModBy` <> '' AND T.`UserID` = `users`.`LastModBy`";
		$result=mysql_query($query);
		$routes = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$routes[$n] = new User($row);
			$n++;
		}
		echo json_encode($routes);
	}
	function getAdminUsers() {
		$query="SELECT `users`.`UserID`, `UserTypeText`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`, `users`.`Patronymic`) as `UserName`, concat_ws(' ', T.`LastName`, T.`FirstName`, T.`Patronymic`) as `CreatedBy` FROM `users`, `users` as T, `usertype` WHERE `users`.`IsEnabled` = 1 AND `usertype`.`UserTypeID` = `users`.`UserTypeID` AND T.`UserID` = `users`.`LastModBy` AND `users`.`LastModBy` = '".$_COOKIE['UserID']."'";
		$result=mysql_query($query);
		$routes = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$routes[$n] = new User($row);
			$n++;
		}
		echo json_encode($routes);
	}
	function removeUser() {
		$query="UPDATE `users` SET `LastModBy` = '".$_COOKIE['UserID']."', `IsEnabled` = 0 WHERE `UserID` = '".$_POST['UserID']."';";
		$res=mysql_query($query);
		echo $res;
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>