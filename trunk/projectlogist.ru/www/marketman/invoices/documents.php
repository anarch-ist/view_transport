<?php
	class RequestClass {
		public $requestID;
		public $requestNumber;
		public $marketAgent;
		public $insideRequests = array();
		function __construct($row){
			$this->requestID = $row[1];
			$this->requestNumber = $row[2];
			$this->marketAgent = $row[0];
			$this->insideRequests[$row[3]] = new InsideRequestClass($row);
		}
		function addInsideRequest($row) {
			if (isset($row[3])&&!isset($this->insideRequests[$row[3]])) {
				$this->insideRequests[$row[3]] = new InsideRequestClass($row);
			}
			else {
				$this->insideRequests[$row[3]]->addInvoice($row);
			}
		}
		function toString() {
			$str = $this->requestID.' '.$this->requestNumber.' '.$this->marketAgent.' ';
			foreach ($this->insideRequests as $key =>$val) {
					$str.=$val->toString();
			}
			return $str;
		}
	}
	class RequestObj {
		public $documentID;
		public $documentNumber;
		public $marketAgent;
		public $dat;
		public $LastModBy;
		function __construct($row) {
			$this->documentID=$row[0];
			$this->documentNumber=$row[1];
			$this->dat=$row[2];
			$this->marketAgent=$row[3];
			$this->LastModBy=$row[5];
		}
	}
	class InvoiceObj {
		public $documentID;
		public $documentNumber;
		public $dat;
		public $LastModBy;
		public $boxQuantity;
		public $startT;
		public $endT;
		function __construct($row) {
			$this->documentID=$row[0];
			$this->documentNumber=$row[1];
			$this->dat=$row[2];
			$this->boxQuantity=$row[3];
			$this->startT=$row[4];
			$this->endT=$row[5];
			$this->LastModBy=$row[6];
		}
	}
	class InsideRequestObj {
		public $documentID;
		public $documentNumber;
		public $dat;
		public $warehouse;
		public $LastModBy;
		function __construct($row) {
			$this->documentID=$row[0];
			$this->documentNumber=$row[1];
			$this->warehouse=$row[2];
			$this->dat=$row[3];
			$this->LastModBy=$row[4];
		}
	}
	class InsideRequestClass {
		public $insideRequestID;
		public $insideRequestNumber;
		public $invoices = array();
		function __construct($row){
			$this->insideRequestID = $row[3];
			$this->insideRequestNumber = $row[4];
			$this->invoices[$row[5]] = new InvoiceClass($row);
		}
		function addInvoice($row) {
			if (isset($row[5]))
				$this->invoices[$row[5]] = new InvoiceClass($row);
		}
		function toString() {
			$str = $this->requestID.' '.$this->requestNumber.' '.$this->marketAgent.' ';
			foreach ($this->invoices as $key =>$val) {
					$str.=$val->toString();
			}
			return $str;
		}
	} 
	class InvoiceClass {
		public $invoiceID;
		public $invoiceNumber;
		function __construct($row){
			$this->invoiceID = $row[5];
			$this->invoiceNumber = $row[6];
		}
		function toString() {
			return $this->invoiceID.' '.$this->invoiceNumber.' ';
		}
	}
	function main() {
		include_once "../../common_files/functions.php";
		if ($_POST['Status']==='getDocs') {
			return getDocs();
		}
		elseif ($_POST['Status']==='getRequest') {
			return getRequest($_POST['DocumentID']);
		}
		elseif ($_POST['Status']==='getInvoice') {
			return getInvoice($_POST['DocumentID']);
		}
		elseif ($_POST['Status']==='addNewRequest') {
			return addNewRequest();
		}
		elseif ($_POST['Status']==='addNewInvoice') {
			return addNewInvoice();
		}
		elseif ($_POST['Status']==='addNewInsideRequest') {
			return addNewInsideRequest();
		}
		elseif ($_POST['Status']==='getInsideRequest') {
			return getInsideRequest($_POST['DocumentID']);
		}
		else {
			echo $_POST;
			echo 'something went wrong...';
		}
	}
	function getRequest($currID) {
		$query="SELECT * FROM `request` WHERE `RequestID` = '".$currID."' AND `IsEnabled` = '1';";// query for getting request
		$request = new RequestObj(mysql_fetch_array(mysql_query($query)));
		echo json_encode($request);
	}
	function getInvoice($currID) {
		$query="SELECT * FROM `invoice` WHERE `InvoiceID` = '".$currID."' AND `IsEnabled` = '1';";// query for getting request
		$invoice = new InvoiceObj(mysql_fetch_array(mysql_query($query)));
		echo json_encode($invoice);
	}
	function getInsideRequest($currID) {
		$query="SELECT * FROM `insiderequest` WHERE `InsideRequestID` = '".$currID."' AND `IsEnabled` = '1';";// query for getting request
		$insideRequest = new InsideRequestObj(mysql_fetch_array(mysql_query($query)));
		echo json_encode($insideRequest);
	}
	function addNewRequest() {
		$query="SELECT MAX(`RequestID`) FROM `request`;";
		$row=mysql_fetch_array(mysql_query($query));
		$currID=1+$row["MAX(`RequestID`)"];
		if ($currID==1) {
			$currID=getMinID('RequestID');
		}
		$date = new DateTime(convertDateTime($_POST['datetime']));
		$query="INSERT INTO `request` (`RequestID`, `RequestNumber`, `Date`, `MarketAgent`, `Client`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST['documentNumber']."', '".$date->format('Y-m-d H:i:s')."', '".$_COOKIE['UserID']."', '".$_POST['ClientID']."', '".$_COOKIE['UserID']."', '1');";// query for additing new request
		$result=mysql_query($query);
		getRequest($currID);
	}
	function getDocs() {
		$query='SELECT T0.*, T1.*, T2.* FROM (((((SELECT `MarketAgent`, `RequestID`, `RequestNumber` FROM `request` WHERE `IsEnabled`=\'1\' AND `request`.`MarketAgent` = \''.$_COOKIE['UserID'].'\' AND `request`.`Client`=\''.$_POST['ClientID'].'\')as T0 LEFT OUTER JOIN `requestandinsiderequest` ON T0.`RequestID`=`requestandinsiderequest`.`RequestID`) LEFT OUTER JOIN (SELECT `InsideRequestID`, `InsideRequestNumber` FROM `insiderequest` WHERE `insiderequest`.`IsEnabled`=\'1\') as T1 ON `requestandinsiderequest`.`InsideRequestID`=T1.`InsideRequestID`) LEFT OUTER JOIN `invoicesforinsiderequest` ON `invoicesforinsiderequest`.`InsideRequestID`=T1.`InsideRequestID`) LEFT OUTER JOIN (SELECT `InvoiceID`, `InvoiceNumber` FROM `invoice` WHERE `invoice`.`IsEnabled`=\'1\') as T2 ON `invoicesforinsiderequest`.`InvoiceID`=T2.`InvoiceID`);';// query for determining clients of current market agent

		$result=mysql_query($query);
		$reqs = array();
		while ($row=mysql_fetch_array($result)) {
			if (!isset($reqs[$row[1]])) {
				$reqs[$row[1]] = new RequestClass($row);
			}
			else {
				$reqs[$row[1]]->addInsideRequest($row);
			}
		}
		echo '['.json_encode($reqs).']';
	}
	function addNewInvoice() {
		$query="SELECT MAX(`InvoiceID`) FROM `invoice`;";
		$row=mysql_fetch_array(mysql_query($query));
		$currID=1+$row["MAX(`InvoiceID`)"];
		if ($currID==1) {
			$currID=getMinID('InvoiceID');
		}
		$date = new DateTime(convertDateTime($_POST['datetime']));
		$query="INSERT INTO `invoice` (`InvoiceID`, `InvoiceNumber`, `Date`, `BoxQuantity`, `T_start`, `T_end`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST["documentNumber"]."', '".$date->format('Y-m-d H:i:s')."', '".'0'."', '".$date->format('Y-m-d H:i:s')."', '".$date->format('Y-m-d H:i:s')."', '".$_COOKIE["UserID"]."', '1');";// query for additing new invoice 
		$result=mysql_query($query);
		$query="INSERT INTO `invoicesforinsiderequest` (`InsideRequestID`, `InvoiceID`) VALUES ('".$_POST["insideRequest"]."', '".$currID."');";// query for link request and inside request
		$result=mysql_query($query);
		addRouteForInvoice($currID);
		setStatusForInvoice($currID);
		getInvoice($currID);
	}
	function addRouteForInvoice($currID) {
		$query="INSERT INTO `routeforinvoice` (`InvoiceID`, `RouteID`) VALUES ('".$currID."', '".$_POST['RouteID']."');";// query for link request and inside request
		$result=mysql_query($query);
	}
	function addNewInsideRequest() {
		$query="SELECT MAX(`InsideRequestID`) FROM `insiderequest`;";
		$row=mysql_fetch_array(mysql_query($query));
		$currID=1+$row["MAX(`InsideRequestID`)"];
		if ($currID==1) {
			$currID=getMinID('InsideRequestID');
		}
		$date = new DateTime(convertDateTime($_POST['datetime']));
		$query="INSERT INTO `insiderequest` (`InsideRequestID`, `InsideRequestNumber`, `Warehouse`, `Date`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST["documentNumber"]."', '".$_POST['PointID']."', '".$date->format('Y-m-d H:i:s')."', '".$_COOKIE["UserID"]."', '1');";// query for additing new invoice 
		$result=mysql_query($query);
		$query="INSERT INTO `requestandinsiderequest` (`RequestID`, `InsideRequestID`) VALUES ('".$_POST["request"]."', '".$currID."');";// query for link request and inside request
		$result=mysql_query($query);
		// $query="INSERT INTO `invoice` (`InvoiceID`, `InvoiceNumber`, `Date`, `BoxQuantity`, `T_start`, `T_end`, `LastModBy`, `IsEnabled`) VALUES ('".$currID."', '".$_POST["documentNumber"]."', '".$_POST['datetime']."', '".'0'."', '".date("Y-m-d H:i:s")."', '".date("Y-m-d H:i:s")."', '".$_COOKIE["UserID"]."', '1');";// query for additing new invoice 
		// $result=mysql_query($query);
		getInsideRequest($currID);
	}
	function setStatusForInvoice($currID) {
		$query="SELECT MAX(`StatusID`) FROM `statushistory`;";
		$row=mysql_fetch_array(mysql_query($query));
		$statushistoryID=1+$row["MAX(`StatusID`)"];
		if ($statushistoryID==1) {
			$statushistoryID=getMinID('StatusID');
		}
		$query="SELECT `routepoint`.`RoutePointID` FROM `routeandroutepoints`, `routepoint` WHERE `routepoint`.`PreviousRoutePointID` IS NULL AND `routeandroutepoints`.`RouteID`='".$_POST['RouteID']."' AND `routeandroutepoints`.`RoutePointID` = `routepoint`.`RoutePointID`;";// query for getting RoutePointID for first status
		$result=mysql_fetch_array(mysql_query($query));
		$datetime = new DateTime(convertDateTime($_POST['datetime']));
		$query="INSERT INTO `requestdb`.`statushistory` (`StatusID`, `StatusTypeID`, `ObjectID`, `StatusStartDate`, `ExpectedStatusStartDate`, `ExpectedStatusEndDate`, `PointLeavingID`, `PointDestinationID`, `ModBy`, `PreviousStatusID`, `IsEnabled`) VALUES ('".$statushistoryID."', '0301', '".$currID."', '".$datetime->format('Y-m-d H:i:s')."', '".$datetime->format('Y-m-d H:i:s')."', '".$datetime->format('Y-m-d H:i:s')."', '".$result[0]."', '".$result[0]."', '".$_COOKIE['UserID']."', NULL, '1');";// query for link request and inside request
		mysql_query($query);
	}
	include_once "../../common_files/functions.php";
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
	function convertDateTime($datetime) {
		return preg_replace("!(\d{1,2})-(\d{1,2})-(\d{4}) (.+)!","\$3-\$1-\$2 \$4",$datetime);
	}
?>