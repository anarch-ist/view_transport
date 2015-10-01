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
		public $route;	//TODO: set route
		function __construct($res) {
			$this->statusID = $res['StatusID'];
			$this->invoiceID = $res['ObjectID'];
			$this->invoiceName = $res['InvoiceNumber'];
			$this->statusStartTime = $res['StatusStartDate'];
			$this->expectedStatusStartTime = $res['ExpectedStatusStartDate'];
			$this->expectedEndStatusTime = $res['ExpectedStatusEndDate'];
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
		$query="SELECT `statushistory`.*, `invoice`.`InvoiceNumber` FROM `statushistory`, `invoice` WHERE `invoice`.`InvoiceID` = `statushistory`.`ObjectID` AND `statushistory`.`PointDestinationID` = '".$_POST['PointID']."' AND `statushistory`.`IsEnabled`='1';";// query for getting all objects
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
		$PointID = $_POST['PointID'];
		$date = new DateTime($_POST['expectedEndStatusTime']);
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$timeForAction = getTimeForAction(getRouteID($currID),$_POST['PointID']);
		if (!($timeForAction)) {
			echo $currID.' ';
			$aaa=getRouteID($currID);
			echo $aaa.' ';
			echo $_POST['PointID'].' ';
			return null;
		}
		$result = changeStatus('0303',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tLoading'].' minutes')->format('Y-m-d H:i:s'),$PointID,$PointID,1);
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
		$timeForAction = getTimeForAction(getRouteID($currID),$PointID);
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
		$result = changeStatus('0305',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tToNextPoint'].' minutes')->format('Y-m-d H:i:s'),$PointID,$PointID,0);
		if ($result) {
			disablePrevStatus();
			return getIndex();
		}
		else {
			return null;
		}
	}
	function setMistakeStatusForInvoice($currID) {		// set 'error' status for $currID object. TODO: remake this method for ise RoutePointID instead PointID
		$RouteID = $_POST['RouteID'];
		$PointID = $_POST['PointID'];
		$date = new DateTime($_POST['expectedEndStatusTime']);
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$timeForAction = getTimeForAction(getRouteID($currID),getPrevPoint($PointID));
		if (!isset($_POST['RouteID'])) {
			$RouteID=getRouteID($currID);
		}
		$result = getPrevPoint($_POST['PointID']);
		if ($result===false) {
			$result=$_POST['PointID'];
		}
		else {
			$result = changeStatus('0304',$currID,$datetime->format('Y-m-d H:i:s'),$_POST['expectedEndStatusTime'],$date->modify('+'.$timeForAction['tToNextPoint'].' minutes')->format('Y-m-d H:i:s'),$PointID,getPrevPoint($PointID),1);
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
		$query="SELECT `statushistory`.*, `invoice`.`InvoiceNumber` FROM `statushistory`, `invoice` WHERE `statushistory`.`StatusID`='".$StatusID."';";
		echo json_encode(mysql_fetch_array(mysql_query($query)));
	}
	function traceRoute($currID, $PointID) {
		$RouteID = $_POST['RouteID'];
		if (!isset($_POST['RouteID'])) {
			$RouteID=getRouteID($currID);
		}
		$query="SELECT `routepoint`.`PointID`, `point`.`PointName`, `routepoint`.`PreviousRoutePointID`, `routepoint`.`RoutePointID` FROM `routepoint`, `routeandroutepoints`, `point` WHERE `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `point`.`PointID` = `routepoint`.`PointID` and `routeandroutepoints`.`RouteID` = '".$RouteID."';";// query for 
		$result=mysql_query($query);
		if ($result){
			$rows = array();
			$n=0;
			while ($rows[$n]=mysql_fetch_array($result)) {$n++;}
			$currentPointElement="";
			$pointList = array();
			$j=0;
			for($i=0;$i<$n;$i++) {
				if (!isset($rows[$i]['PreviousRoutePointID']) || $currentPointElement === $rows[$i]['PreviousRoutePointID']) {
					$currentPointElement = $rows[$i]['RoutePointID'];
					$pointList[$j]=$rows[$i]['PointName'];
					$j++;
					$rows[$i]['PreviousRoutePointID']="0";
					if ($rows[$i]['PointID'] == $PointID) {
						break;
					}
					$i=-1;
				}
			}		
			return json_encode($pointList);
		}
		else {
			return '[null]';
		}
	}
	function changeStatus($statusType, $invoiceID, $StatusStartDate, $ExpectedStatusStartDate,$ExpectedStatusEndDate, $RoutePointID, $NextRoutePointID,$isEnabled) {
		$query="INSERT INTO `requestdb`.`statushistory` (`StatusID`, `StatusTypeID`, `ObjectID`, `StatusStartDate`, `ExpectedStatusStartDate`, `ExpectedStatusEndDate`, `PointLeavingID`, `PointDestinationID`, `ModBy`, `PreviousStatusID`, `IsEnabled`) VALUES ('".getIndex()."', '".$statusType."', '".$invoiceID."', '".$StatusStartDate."', '".$ExpectedStatusStartDate."', '".$ExpectedStatusEndDate."', '".$RoutePointID."', '".$NextRoutePointID."', '".$_COOKIE['UserID']."',".$_POST['StatusID'].", '".$isEnabled."');";
		return mysql_query($query);
	}
	function getTimeForAction($RouteID, $PointID) {
		$query = "SELECT `routepoint`.`tLoading`, `routepoint`.`tToNextPoint` FROM `routeandroutepoints`, `routepoint` where `routepoint`.`RoutePointID` = `routeandroutepoints`.`RoutePointID` and `routepoint`.`PointID` = '".$PointID."' and `routeandroutepoints`.`RouteID` = '".$RouteID."'";
		return mysql_fetch_array(mysql_query($query));
	}
	function getPrevPoint($PointID) {	//TODO: remake with RoutePointID
		$query="SELECT T1.`PointID` FROM `routepoint` as T0, `routepoint` as T1 where T0.`PointID` = '".$PointID."' and T0.`PreviousRoutePointID` = T1.`RoutePointID`;";// 
		$result=mysql_fetch_array(mysql_query($query));
		return $result[0];
	}
	function getNextPoint($PointID) {	//TODO: remake with RoutePointID
		$query="SELECT T1.`PointID` FROM `routepoint` as T0, `routepoint` as T1 where T0.`PointID` = '".$PointID."' and T1.`PreviousRoutePointID` = T0.`RoutePointID`;";// 
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