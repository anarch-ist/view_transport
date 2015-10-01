<!DOCTYPE html>
<!-- Разработка Андрея Баранова™ -->
<html>
	<head>
	<meta charset="UTF-8">
	<title>Добавление данных</title>
	<script type="text/javascript" src="../../../common_files/sessvars.js"></script>
<script type="text/javascript">
	sessvars.$.clearMem();
</script>
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
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);

	$query="SELECT MAX(`RoutePointID`) FROM `routepoint`";
	$row=mysql_fetch_array(mysql_query($query));
	$currRoutePointID=1+$row["MAX(`RoutePointID`)"];
	if ($currRoutePointID===1) {
		$currRoutePointID=getMinID('RoutePointID');
	}

	$query="SELECT MAX(`RouteID`) FROM `route`";
	$result=mysql_query($query);
	$row=mysql_fetch_array($result);
	$currRouteID=1+$row["MAX(`RouteID`)"];
	if ($currRouteID===1) {
		$currRouteID=getMinID('RouteID');
	}
	$query="INSERT INTO  `route` (`RouteID` ,`RouteName` ,`LastModBy` ,`IsEnabled`)VALUES ('";
	$query.=$currRouteID."',  '".$_POST['routeName']."',  '".$_COOKIE['UserID']."','1')";
	$result=mysql_query($query);
	if (!$result) {
		echo "ошибка добавления в БД маршрута";
		die (error_reporting);
	}
	$array=$_POST['points'];
	$prevRoutepoint = 'NULL';
	for ($i=0;$i<count($array);$i++) {
		$query="INSERT INTO  `routepoint` (`RoutePointID` ,`PointID` ,`PreviousRoutePointID` ,`tLoading` ,`tToNextPoint` ,`LastModBy`)VALUES ('";
		$query.=$currRoutePointID."',  '".$array[$i]['pointID']."', ".$prevRoutepoint." ,  '".$array[$i]['loadTime']."',  '".$array[$i]['leavingTime']."',  '".$_COOKIE['UserID']."')";
		$result=mysql_query($query);
		if (!$result) {
			echo "ошибка добавления в БД точки маршрута в итерации ".($i);
			die (error_reporting);
		}
		$query="INSERT INTO  `routeandroutepoints` (`RouteID` ,`RoutePointID` ,`LastModBy`)VALUES ('";
		$query.=$currRouteID."',  '".$currRoutePointID."',  '".$_COOKIE['UserID']."')";
		$result=mysql_query($query);
		if (!$result) {
			echo "ошибка добавления в БД связи с маршрутом в итерации ".($i);
			die (error_reporting);
		}
		$query="SELECT MAX(`RoutePointID`) FROM `routepoint`";
		$row=mysql_fetch_array(mysql_query($query));
		$prevRoutepoint = $currRoutePointID;
		$currRoutePointID=1+$row["MAX(`RoutePointID`)"];
	}
	mysql_close();
	echo ("<script type=\"text/javascript\">location.replace('".$currentSite."');</script>\n");
?>
	</body>
</html>