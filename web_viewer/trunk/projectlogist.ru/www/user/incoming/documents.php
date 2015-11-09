<?php
	include_once "../../common_files/functions.php";
	function main() {
		//if (isset($_POST['date'])) {
		if ($_POST['status']==='getAllObjects') {
			return getAllObjects();
		}
		else if ($_POST['status']==='setLoadingStatusForInvoice') {
			$StatusID = setLoadingStatusForInvoice($_POST['InvoiceID']);
			if (isset($StatusID)) {getNewStatus($StatusID);}
			return;
		}
		else if ($_POST['status']==='setConfirmStatusForInvoice') {
			$StatusID = setConfirmStatusForInvoice($_POST['InvoiceID']);
			if (isset($StatusID)) {getNewStatus($StatusID);}
			return;
		}
		else if ($_POST['status']==='setMistakeStatusForInvoice') {
			$StatusID = setMistakeStatusForInvoice($_POST['InvoiceID']);
			if (isset($StatusID)) {getNewStatus($StatusID);}
			return;
		}
		else if ($_POST['status']==='traceRoute') {
			echo traceRoute($_POST['InvoiceID'],$_POST['PointID']);			
			return;
		}
		else {
			echo 'Передан некорректный параметр '.$_POST['status'];
		}
	}
	class InvoicesOfPoint {
		public $statusID;
		public $invoiceID;
		public $invoiceName;
		public $statusStartTime;
		public $expectedStatusStartTime;
		public $expectedEndStatusTime;
		public $type;
		public $routePointID;
		public $hasPreviousRoutePoint;
		function __construct($res) {
			$this->statusID = $res['StatusID'];
			$this->invoiceID = $res['ObjectID'];
			$this->invoiceName = $res['InvoiceNumber'];
			$this->statusStartTime = $res['StatusStartDate'];
			$this->expectedStatusStartTime = $res['ExpectedStatusStartDate'];
			$this->expectedEndStatusTime = $res['ExpectedStatusEndDate'];
			$this->routePointID = $res['PointDestinationID'];
			$this->hasPreviousRoutePoint = getPrevPoint($res['PointDestinationID']);
			switch ($res['StatusTypeID']) {
				case '0302': {
					$this->type = 'COME_IN';
					break;
				}
				case '0303': {
					$this->type = 'CONFIRM';
					break;
				}
				case '0301': {
					$this->type = 'CONFIRM';
					break;
				}
				case '0304': {
					$this->type = 'ERROR';
					break;
				}
			}
			
		}
	}
	function getAllObjects() {
		$query="SELECT `statushistory`.*, `invoice`.`InvoiceNumber` FROM `statushistory`, `invoice`, `routepoint` WHERE `invoice`.`InvoiceID` = `statushistory`.`ObjectID` AND `statushistory`.`PointDestinationID` = `routepoint`.`RoutePointID` AND `routepoint`.`PointID`= '".$_POST['PointID']."' AND `statushistory`.`IsEnabled`='1';";// query for getting all objects
		$result=mysql_query($query);
		$reqs = array();
		$i=0;
		if ($result!==false) while ($row=mysql_fetch_array($result)) {
			$reqs[$i] = new InvoicesOfPoint($row);
			$i++;
		}
		echo json_encode($reqs);
	}
	function getRouteID($currID) {
		$query="SELECT `RouteID` FROM `routeforinvoice` WHERE `routeforinvoice`.`InvoiceID` = '".$currID."'";// query getting RouteID linked with invoice
		$result = mysql_fetch_array(mysql_query($query));
		return $result[0];
	}
	function setLoadingStatusForInvoice($currID) {		// set loading status for $currID object
		$date = new DateTime($_POST['expectedEndStatusTime']);
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$timeForAction = getTimeForAction($_POST['routePointID']);
		if (!($timeForAction)) {
			echo $currID.' - ';
			$aaa=getRouteID($currID);
			echo $aaa.' - ';
			echo $_POST['PointID'].' - ';
			echo $_POST['RoutePointID'].' ';
			return null;
		}
		$result = changeStatus('0303',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tLoading'].' minutes')->format('Y-m-d H:i:s'),$_POST['routePointID'],$_POST['routePointID'],1);
		if ($result) {
			disablePrevStatus();
			return getIndex();
		}
		else {
			return null;
		}
	}
	function setConfirmStatusForInvoice($currID) {		// set 'come in next point' status for $currID object. TODO: remake this method for using RoutePointID instead PointID
		$RouteID = $_POST['RouteID'];
		$PointID = $_POST['PointID'];
		$date = new DateTime($_POST['expectedEndStatusTime']);
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$timeForAction = getTimeForAction($_POST['routePointID']);
		if (!isset($_POST['RouteID'])) {
			$RouteID=getRouteID($currID);
		}
		if (!($timeForAction)) {
			echo $currID.' ';
			$aaa=getRouteID($currID);
			echo $aaa.' ';
			echo $PointID.' ';
			return null;
		}
		$result = changeStatus('0305',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tToNextPoint'].' minutes')->format('Y-m-d H:i:s'),$_POST['routePointID'],$_POST['routePointID'],0);
		if ($result) {
			disablePrevStatus();
			return getIndex();
		}
		else {
			return null;
		}
	}
	function setMistakeStatusForInvoice($currID) {		// set 'error' status for $currID object. 
		$RouteID = $_POST['RouteID'];
		$PointID = $_POST['PointID'];
		$date = new DateTime($_POST['expectedEndStatusTime']);
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$timeForAction = getTimeForAction($_POST['routePointID']);
		if (!isset($_POST['RouteID'])) {
			$RouteID=getRouteID($currID);
		}
		$result = getPrevPoint($_POST['PointID']);
		if ($result===false) {
			$result=$_POST['PointID'];
		}
		else {
			$result = changeStatus('0304',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tToNextPoint'].' minutes')->format('Y-m-d H:i:s'),$_POST['routePointID'],getPrevPoint($_POST['routePointID']),1);
		}
		if ($result) {
			disablePrevStatus();
			return getIndex();
		}
		else {
			return null;
		}
	}
	function disablePrevStatus() {
		$query = "UPDATE  `requestdb`.`statushistory` SET  `IsEnabled` =  '0' WHERE  `statushistory`.`StatusID` =  '".$_POST['StatusID']."';";
		mysql_query($query);
	}
	function getIndex() {
		$query="SELECT MAX(`StatusID`) FROM `statushistory`;";
		$row=mysql_fetch_array(mysql_query($query));
		$statushistoryID=1+$row["MAX(`StatusID`)"];
		if ($statushistoryID==1) {
			$statushistoryID=getMinID('StatusID');
		}
		return $statushistoryID;
	}
	function getNewStatus($StatusID) {
		$query="SELECT `statushistory`.*, `invoice`.`InvoiceNumber` FROM `statushistory`, `invoice` WHERE `invoice`.`InvoiceID` = `statushistory`.`ObjectID` AND `statushistory`.`StatusID`='".$StatusID."';";
		echo json_encode(mysql_fetch_array(mysql_query($query)));
	}
	function traceRoute($currID, $PointID) {
		$RouteID = $_POST['RouteID'];
		if (!isset($_POST['RouteID'])) {
			$RouteID=getRouteID($currID);
		}
		// $query="SELECT `routepoint`.`PointID`, `point`.`PointName`, `routepoint`.`PreviousRoutePointID`, `routepoint`.`RoutePointID` FROM `routepoint`, `routeandroutepoints`, `point` WHERE `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `point`.`PointID` = `routepoint`.`PointID` and `routeandroutepoints`.`RouteID` = '".$RouteID."';";// query for 
		$query="SELECT GROUP_CONCAT(`point`.`PointName` order by `routepoint`.`RoutePointID` separator ' -> ') as `Points` from `point`, `routepoint`, `routeandroutepoints`, `route`, (select max(`routepoint`.`RoutePointID`) as `RoutePointID` from `routepoint`, `routeandroutepoints`, `point` where `point`.`PointID` = '".$PointID."' and `routeandroutepoints`.`RouteID` = '".$RouteID."' and `routeandroutepoints`.`RoutePointID` = `routepoint`.`RoutePointID` and `point`.`POintID` = `routepoint`.`PointID`) as T0 where `point`.`PointID` = `routepoint`.`PointID` and `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `route`.`RouteID` = `routeandroutepoints`.`RouteID` and `route`.`IsEnabled` = 1 and `routepoint`.`RoutePointID` <= T0.`RoutePointID`";// query for 
		$result=mysql_query($query);
		if ($result){
			$pointList=mysql_fetch_array($result);
			return json_encode($pointList[0]);
		}
		else {
			die('Не удалось получить список предыдущих пунктов...');
		}
	}
	function changeStatus($statusType, $invoiceID, $StatusStartDate, $ExpectedStatusStartDate,$ExpectedStatusEndDate, $RoutePointID, $NextRoutePointID,$isEnabled) {
		$query="INSERT INTO `requestdb`.`statushistory` (`StatusID`, `StatusTypeID`, `ObjectID`, `StatusStartDate`, `ExpectedStatusStartDate`, `ExpectedStatusEndDate`, `PointLeavingID`, `PointDestinationID`, `ModBy`, `PreviousStatusID`, `IsEnabled`) VALUES ('".getIndex()."', '".$statusType."', '".$invoiceID."', '".$StatusStartDate."', '".$ExpectedStatusStartDate."', '".$ExpectedStatusEndDate."', '".$RoutePointID."', '".$NextRoutePointID."', '".$_COOKIE['UserID']."',".$_POST['StatusID'].", '".$isEnabled."');";
		return mysql_query($query);
	}
	function getTimeForAction($RoutePointID) {
		$query = "SELECT `tLoading`, `tToNextPoint` FROM `routepoint` where `routepoint`.`RoutePointID` = '".$RoutePointID."';";
		return mysql_fetch_array(mysql_query($query));
	}
	function getPrevPoint($RoutePointID) {
		$query="SELECT `PreviousRoutePointID` FROM `routepoint` where `RoutePointID` = '".$RoutePointID."';";
		$result=mysql_fetch_array(mysql_query($query));
		return $result[0];
	}
	function getNextPoint($RoutePointID) {
		$query="SELECT `RoutePointID` FROM `routepoint` where `PreviousRoutePointID` = '".$RoutePointID."';";
		$result=mysql_fetch_array(mysql_query($query));
		return $result[0];
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
	function convertDateTime($datetime) {
		return preg_replace("!(\d{1,2})-(\d{1,2})-(\d{4}) (.+)!","\$3-\$1-\$2 \$4",$datetime);
	}
?>