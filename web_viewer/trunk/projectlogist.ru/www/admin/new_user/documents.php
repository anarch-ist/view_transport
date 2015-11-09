<?php
	include_once "../../common_files/functions.php";


function main() {
	if ($_POST['Status']==='addNewUser') {
		addNewUser();
	}
	else {
		echo $_POST;
		echo ' ... и идите к админу';
	}
}

function insertQuery($tableName, $map) {
	$queryString = "INSERT INTO `".$tableName."` (";
	$queryValues = ") VALUES (";
	$isFirst = true;
	foreach($map as $key => $value) {
		if ($isFirst) {
			$isFirst = false;
		}
		else {
			$queryString.=", ";
			$queryValues.=", ";
		}
		$queryString.="`".$key."`";
		$queryValues.="'".$value."'";
	}
	$queryValues.=");";
//	echo $queryString.$queryValues;
	return $queryString.$queryValues;
}

function addNewUser() {

	if (!emailValidation($_POST["Email"])) {
		mysql_close();
		die ('Неправильный e-mail адрес');
	}


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
		mysql_close();
		die ("ошибка БД");
	}
	$count=mysql_num_rows($result);
	$NickName = $_POST["LastName"];
	if ($count!==0) {
		$NickName .= '0'.$count;
	}
	// echo $NickName;


	$map = array(
		'UserID'		=> $currID,
		'InnerUserID'	=> '0',
		'UserTypeID'	=> $_POST["UserTypeID"],
		'FirstName'		=> $_POST["FirstName"],
		'LastName'		=> $_POST["LastName"],
		'Patronymic'	=> $_POST["Patronymic"],
		'NickName'		=> $NickName,
		'PassMD5'		=> ($_POST["Pass"]!=='') ? md5($_POST["Pass"]) : '',
		'Telephone'		=> $_POST["Telephone"],
		'Email'			=> $_POST["Email"],
		'LastModBy'		=> $_COOKIE["UserID"],
		'UserStatus'	=> 'ACTIVE'
	);

//	$query="INSERT INTO `users` (`UserID`, `InnerUserID`, `UserTypeID`, `FirstName`, `LastName`, `Patronymic`,`NickName`, `PassMD5`, `Telephone`, `Email`, `LastModBy`,`UserStatus`) VALUES ('";
//	$query.=$currID."', '0', '".$_POST["UserTypeID"]."', '".$_POST["FirstName"]."', '".$_POST["LastName"]."', '".$_POST["Patronymic"]."', '".$NickName."', '".(($_POST["Pass"]!=='') ? md5($_POST["Pass"]) : '')."', '".$_POST["Telephone"]."', '".$_POST["Email"]."', '".$_COOKIE["UserID"]."','ACTIVE');";	//TODO: make good query!

	$query = insertQuery('users', $map);

	$result=mysql_query($query);
	if (!$result) {
		mysql_close();
		die ("ошибка добавления данных в БД");
	}
}
mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
mysql_select_db($mysqlparams['database']);
main();
mysql_close();

	//echo ("<script type=\"text/javascript\">location.replace('".siteName()."');</script>\n");
?>