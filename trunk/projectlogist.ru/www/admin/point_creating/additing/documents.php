<?php
	include_once "../../../common_files/functions.php";
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	$query="SELECT MAX(`PointID`) FROM `point`";
	$result=mysql_query($query);
	$row=mysql_fetch_array($result);
	$currID=1+$row["MAX(`PointID`)"];
	if ($currID===1) {
		$currID=getMinID('PointID');
	}
	mysql_query('Start transaction');
	$query="INSERT INTO `point` (`PointID`, `PointName`, `PointTypeID`, `Region`, `District`, `Locality`, `MailIndex`, `Address`, `Email`, `Telephone`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST["name"]."', '".$_POST["point-type"]."', '".$_POST["region"]."', '".$_POST["district"]."', '".$_POST["locality"]."', '".$_POST["mailindex"]."', '".$_POST["address"]."', '".$_POST["email"]."', '".$_POST["telephone"]."', '".$_COOKIE["UserID"]."','1');";
	$result=mysql_query($query);
	if (!$result) {
		echo "ошибка добавления в БД".' '.$query;
		mysql_query('Rollback');
		foreach($_POST as $key => $value) {
			echo "\n - ".$key.' => '.$value;
		}
		die ();
	}

	foreach($_POST['dispatcher'] as $key) {
		$query="INSERT INTO `dispatchersforpoint` (`PointID`, `UserID`) VALUES ('";
		$query.=$currID."', '".$key."');";
		$result=mysql_query($query);
		if (!$result) {
			echo "ошибка добавления в БД".' ';
			echo $query;
			mysql_query('Rollback');
			die (error_reporting);
		}
	}
	mysql_query('Commit');
	mysql_close();
	echo ("<scdrsdfipt type=\"text/jasdfvascript\">location.replace('".$startPage."');</scsdfsdript>\n");
?>