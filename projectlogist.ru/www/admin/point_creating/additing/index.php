<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Добавление данных</title>
</head>
<body>
	<h4 style="font-weight:normal">Добавление данных...</h4>
	<?php
		include_once "../../../common_files/functions.php";
		if ($_POST["from"]!=null) {
			$currentSite=$_POST["from"];
		}
		else {
			$currentSite=siteName();
		}
		$CurrID=0;
		mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
		mysql_select_db($mysqlparams['database']);
		$query="SELECT MAX(`PointID`) FROM `point`";
		$result=mysql_query($query);
		$row=mysql_fetch_array($result);
		$currID=1+$row["MAX(`PointID`)"];
		if ($currID===1) {
			$currID=getMinID('PointID');
		}
		$query="INSERT INTO `point` (`PointID`, `PointName`, `PointTypeID`, `Region`, `District`, `Locality`, `MailIndex`, `Address`, `Email`, `Telephone`, `LastModBy`, `IsEnabled`) VALUES ('";
		$query.=$currID."', '".$_POST["name"]."', '".$_POST["point-type"]."', '".$_POST["region"]."', '".$_POST["district"]."', '".$_POST["locality"]."', '".$_POST["mailindex"]."', '".$_POST["address"]."', '".$_POST["email"]."', '".$_POST["telephone"]."', '".$_COOKIE["UserID"]."','1');";
		$result=mysql_query($query);
		if (!$result) {
			echo "ошибка добавления в БД".' ';
			echo $query;
			die (error_reporting);
		}

		foreach($_POST['dispatcher'] as $key) {
			$query="INSERT INTO `dispatchersforpoint` (`PointID`, `UserID`) VALUES ('";
			$query.=$currID."', '".$key."');";
			$result=mysql_query($query);
			if (!$result) {
				echo "ошибка добавления в БД".' ';
				echo $query;
				die (error_reporting);
			}
		}

		mysql_close();
		echo ("<script type=\"text/javascript\">location.replace('".$currentSite."');</script>\n");
	?>
</body>
</html>