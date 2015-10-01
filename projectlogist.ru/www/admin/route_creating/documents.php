<?php
	include_once "../../common_files/functions.php";
	class Route {
		public $routeName;
		public $points;
		public function __construct($result) {
			$this->points = array();
			$i=0;
			while($row=mysql_fetch_array($result)) {
				
				$this->points[$i] = new Point($row);
				if (!$i) {
					$this->routeName = $row['RouteName'];
				}
				$i++;
			}
		}
	}
	class Point{
		public $loadTime;
		public $leavingTime;
		public $pointName;
		public function __construct($data) {
			$this->loadTime = $data['tLoading'];
			$this->leavingTime = $data['tToNextPoint'];
			$this->pointName = $data['PointName'];
			$this->pointID = $data['PointID'];
		}
	}
	function getMaxRoutePointID() {
		$query="SELECT MAX(`RoutePointID`) FROM `routepoint`";
		$row=mysql_fetch_array(mysql_query($query));
		$currRoutePointID=1+$row["MAX(`RoutePointID`)"];
		if ($currRoutePointID===1) {
			$currRoutePointID=getMinID('RoutePointID');
		}
		return $currRoutePointID;
	}
	function getMaxRouteID() {
		$query="SELECT MAX(`RouteID`) FROM `route`";
		$result=mysql_query($query);
		$row=mysql_fetch_array($result);
		$currRouteID=1+$row["MAX(`RouteID`)"];
		if ($currRouteID===1) {
			$currRouteID=getMinID('RouteID');
		}
		return $currRouteID;
	}
	function addNewRoute($route) {
		$route = json_decode($route);
		$currRouteID=getMaxRouteID();
		$query="INSERT INTO  `route` (`RouteID` ,`RouteName` ,`LastModBy` ,`IsEnabled`)VALUES ('";
		$query.=$currRouteID."',  '".$route->routeName."',  '".$_COOKIE['UserID']."','1')";
		$result=mysql_query($query);
		if (!$result) {
			die ("ошибка добавления в БД маршрута");
		}
		$array=$route->points;
		$prevRoutePointID = 'NULL';
		$currRoutePointID = getMaxRoutePointID();
		for ($i=0;$i<count($array);$i++) {
			$query="INSERT INTO  `routepoint` (`RoutePointID` ,`PointID` ,`PreviousRoutePointID` ,`tLoading` ,`tToNextPoint` ,`LastModBy`)VALUES ('";
			$query.=$currRoutePointID."',  '".$array[$i]->pointID."', ".$prevRoutePointID." ,  '".$array[$i]->loadTime."',  '".$array[$i]->leavingTime."',  '".$_COOKIE['UserID']."')";
			$result=mysql_query($query);
			if (!$result) {
				die ("ошибка добавления в БД точки маршрута в итерации ".($i));
			}
			$query="INSERT INTO  `routeandroutepoints` (`RouteID` ,`RoutePointID` ,`LastModBy`)VALUES ('";
			$query.=$currRouteID."',  '".$currRoutePointID."',  '".$_COOKIE['UserID']."')";
			$result=mysql_query($query);
			if (!$result) {
				die ("ошибка добавления в БД связи с маршрутом в итерации ".($i));
			}
			$prevRoutePointID = $currRoutePointID;
			$currRoutePointID=getMaxRoutePointID();
		}
	}
	function getRouteInformation($RouteID) {
		$query = "SELECT `route`.`RouteName`, `point`.`PointName`, `routepoint`.`tLoading`,`routepoint`.`tToNextPoint` FROM `routeandroutepoints`, `route`, `point`,`routepoint` WHERE `route`.`RouteID` = '".$RouteID."' and `route`.`RouteID` = `routeandroutepoints`.`RouteID` and `routeandroutepoints`.`RoutePointID` = `routepoint`.`RoutePointID` and `routepoint`.`PointID` = `point`.`PointID` order by `routeandroutepoints`.`RoutePointID` ASC";
		$result = mysql_query($query);
		return json_encode(new Route($result));
	}
	function disableOldRoute($RouteID) {
		$query = "update `route` set `IsEnabled` = 0 where `RouteID`='".$RouteID."'";
		return mysql_query($query);
	}
	function main() {
		if ($_POST['Status'] === 'addNewRoute') {
			addNewRoute($_POST['route']);
			if (isset($_POST['RouteID'])) {
				disableOldRoute($_POST['RouteID']);
			}
		} else if ($_POST['Status'] === 'getRouteInformation') {
			echo getRouteInformation($_POST['RouteID']);
		}
		else {
			die('Не передано никаких параметров!');
		}
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>