<?php
	include_once "../../common_files/functions.php";
	function main() {
		if ($_POST['Status']==='removePoint') {
			echo 'removePoint '.$_COOKIE['UserID'].", ".$_POST['PointID'];
			removePoint();
			echo ' removePoint';
		}else if ($_POST['Status']==='getAllPoints') {
			getAllPoints();
		}else if ($_POST['Status']==='getPointsOfUser') {
			getPointsOfUser();
		}
		else {
			echo $_POST;
			echo ' ... и идите к админу';
		}
	}
	class Point {
		public $PointID;
		public $PointName;
		public $UserName;
		public $Points;
		function __construct($str) {
			$this->PointID = $str['PointID'];
			$this->PointName = $str['PointName'];
			$this->UserName = $str['UserName'];
			$this->Points = $str['Points'];
		}
	}
	function getAllPoints() {
		$query="SELECT `point`.`PointID`, `point`.`PointName`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`, `users`.`FirstName`) as `UserName`, `point`.`LastModBy` from `users`, `point` where `point`.`IsEnabled` = 1 AND `users`.`UserID` = `point`.`LastModBy` order by `point`.`PointName`";
		$result=mysql_query($query);
		$points = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$points[$n] = new Point($row);
			$n++;
		}
		echo json_encode($points);
	}
	function getPointsOfUser() {
		$query="SELECT `point`.`PointID`, `point`.`PointName`, concat_ws(' ', `users`.`LastName`, `users`.`FirstName`, `users`.`FirstName`) as `UserName`, `point`.`LastModBy` from `users`, `point` where `point`.`IsEnabled` = 1 AND `users`.`UserID` = `point`.`LastModBy` and `point`.`LastModBy` = '".$_COOKIE['UserID']."' order by `point`.`PointName`";
		$result=mysql_query($query);
		$points = array();
		$n=0;
		while($row=mysql_fetch_array($result)) {
			$points[$n] = new Point($row);
			$n++;
		}
		echo json_encode($points);
	}
	function removePoint() {
		$query="UPDATE `point` SET `LastModBy` = '".$_COOKIE['UserID']."', `IsEnabled` = 0 WHERE `PointID` = '".$_POST['PointID']."';";
		$res=mysql_query($query);
		echo $res;
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>