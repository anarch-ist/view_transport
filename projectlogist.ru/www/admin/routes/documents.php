<?php
	include_once "../../common_files/functions.php";
	function main() {
		if ($_POST['Status']==='removeRoute') {
			echo 'removeRoute '.$_COOKIE['UserID'].", ".$_POST['RouteID'];
			removeRoute();
			echo ' removeRoute';
		}else if ($_POST['Status']==='getAllRoutes') {
			getAllRoutes();
		}else if ($_POST['Status']==='getRoutesOfUser') {
			getRoutesOfUser();
		}
		else {
			echo $_POST;
			echo ' ... и идите к админу';
		}
	}
	class Route {
		public $RouteID;
		public $RouteName;
		public $UserName;
		public $Points;
		function __construct($str) {
			$this->RouteID = $str['RouteID'];
			$this->RouteName = $str['RouteName'];
			$this->UserName = $str['UserName'];
			$this->Points = $str['Points'];
		}
	}
	function getAllRoutes() {
		$query="SELECT `route`.`RouteID`, `route`.`RouteName`, `route`.`LastModBy`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`) as `UserName`, `pointNames`.`Points` from (SELECT `routeandroutepoints`.`RouteID`, GROUP_CONCAT(`point`.`PointName` order by `routepoint`.`RoutePointID` separator ' -> ') as `Points` from `point`, `routepoint`, `routeandroutepoints`, `route` where `point`.`PointID` = `routepoint`.`PointID` and `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `route`.`RouteID` = `routeandroutepoints`.`RouteID` and `route`.`IsEnabled` = 1 group by `RouteID`) as `PointNames`, `users`, `route` where `route`.`IsEnabled` = 1 AND `route`.`RouteID` = `PointNames`.`RouteID` and `users`.`UserID` = `route`.`LastModBy`";
		$result=mysql_query($query);
		$routes = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$routes[$n] = new Route($row);
			$n++;
		}
		echo json_encode($routes);
	}
	function getRoutesOfUser() {
		$query="SELECT `route`.`RouteID`, `route`.`RouteName`, `route`.`LastModBy`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`) as `UserName`, `pointNames`.`Points` from (SELECT `routeandroutepoints`.`RouteID`, GROUP_CONCAT(`point`.`PointName` order by `routepoint`.`RoutePointID` separator ' -> ') as `Points` from `point`, `routepoint`, `routeandroutepoints`, `route` where `point`.`PointID` = `routepoint`.`PointID` and `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `route`.`RouteID` = `routeandroutepoints`.`RouteID` and `route`.`IsEnabled` = 1 group by `RouteID`) as `PointNames`, `users`, `route` where `route`.`IsEnabled` = 1 AND `route`.`RouteID` = `PointNames`.`RouteID` and `users`.`UserID` = `route`.`LastModBy` and `route`.`LastModBy` = '".$_COOKIE['UserID']."';";
		$result=mysql_query($query);
		$routes = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$routes[$n] = new Route($row);
			$n++;
		}
		echo json_encode($routes);
	}
	function removeRoute() {
		$query="UPDATE `route` SET `LastModBy` = '".$_COOKIE['UserID']."', `IsEnabled` = 0 WHERE `RouteID` = '".$_POST['RouteID']."';";
		$res=mysql_query($query);
		echo $res;
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>