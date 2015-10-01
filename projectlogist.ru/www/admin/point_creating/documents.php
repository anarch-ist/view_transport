<?php
	include_once "../../common_files/functions.php";
	function addNewData() {
		$query="SELECT MAX(`PointID`) FROM `point`";
		$result=mysql_query($query);
		$row=mysql_fetch_array($result);
		$currID=1+$row["MAX(`PointID`)"];
		if ($currID===1) {
			$currID=getMinID('PointID');
		}
		mysql_query('Start transaction');
		$query="INSERT INTO `point` (`PointID`, `PointName`, `PointTypeID`, `Region`, `District`, `Locality`, `MailIndex`, `Address`, `Email`, `Telephone`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST["name"]."', '".$_POST["pointType"]."', '".$_POST["region"]."', '".$_POST["district"]."', '".$_POST["locality"]."', '".$_POST["mailIndex"]."', '".$_POST["address"]."', '".$_POST["email"]."', '".$_POST["telephone"]."', '".$_COOKIE["UserID"]."','1');";
		$result=mysql_query($query);
		if (!$result) {
			//echo "ошибка добавления в БД".' clear - '.$query;
			mysql_query('Rollback');
			die ("ошибка добавления в БД".' clear - '.$query);
		}

		if (count($_POST['dispatcher'])>0) foreach($_POST['dispatcher'] as $key) {
			$query="INSERT INTO `dispatchersforpoint` (`PointID`, `UserID`) VALUES ('";
			$query.=$currID."', '".$key."');";
			$result=mysql_query($query);
			if (!$result) {
				//echo "ошибка добавления в БД при добавлении \"диспетчера\" c ИД ".$key;
				mysql_query('Rollback');
				die ("ошибка добавления в БД".' clear '.$query);
			}
		}
		mysql_query('Commit');
		echo 'Успешное добавление пункта!';
	}
	function convertPointTypeToUserType($pointType) {
		if ($pointType==='0101') {
			return '0002';
		} else if ($pointType === '0102') {
			return '0004';
		} else if ($pointType === '0103') {
			return '0002';
		} else {
			die('Неправильный тип пункта!');
		}
	}
	function getFreeUsers($userType) {
		$query = "SELECT `UserID`, concat(`LastName`,' ',`FirstName`,' ',`Patronymic`) FROM `users` WHERE `UserTypeID` = '".$userType."' AND `users`.`UserID` NOT IN (SELECT `UserID` FROM `dispatchersforpoint`)";
		$result = mysql_query($query);
		if (!$result) {
			echo ('нет пользователей');
			return;
		}
		$num=0;
		while($row = mysql_fetch_array($result)) {
			$response[$num]['value'] = $row[0];
			$response[$num]['text'] = $row[1];
			$num++;
		}
		echo json_encode($response);
	}
	function editData($PointID) {
		addNewData();
		disableOldPoint($PointID);
	}
	function disableOldPoint($PointID) {
		$query="UPDATE `point` SET `LastModBy` = '".$_COOKIE['UserID']."', `IsEnabled` = 0 WHERE `PointID` = '".$_POST['PointID']."';";
		$res=mysql_query($query);
		echo $res;
	}
	function main() {
		if ($_POST['status']==='addNewData') {
			addNewData();
		} else if ($_POST['status']==='editData') {
			editData($_POST['PointID']);
		} else if ($_POST['status']==='getFreeUsers') {
			getFreeUsers(convertPointTypeToUserType($_POST['pointType']));
		} else {
			die('Не передано никаких параметров!');
		}
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>