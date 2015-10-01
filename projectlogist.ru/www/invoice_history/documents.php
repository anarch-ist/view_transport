<?php
	class Invoice {
		public $name;
		public $requestNumber;
		public $insideRequestNumber;
		public $invoiceNumber;
		public $datetime;
		function __construct($row) {
			$this->name=$row['Name'];
			$this->requestNumber=$row['RequestNumber'];
			$this->insideRequestNumber=$row['InsideRequestNumber'];
			$this->invoiceNumber=$row['InvoiceNumber'];
			$this->datetime=$row['Date'];
		}
	}
	class Client {
		public $name;
		public $userID;
		function __construct($row) {
			$this->name=$row['Name'];
			$this->userID=$row['UserID'];
		}
	}
	function main() {
		include_once "../common_files/functions.php";
		if ($_POST['Status']==='getInvoices') {
			return getInvoices($_COOKIE['UserID']);
		} elseif ($_POST['Status']==='getInvoicesForUser') {
			return getInvoices($_POST['UserID']);
		} elseif ($_POST['Status']==='getUsers') {
			return getUsers($_COOKIE['UserID']);
		} else {
			echo $_POST;
			echo 'something went wrong...';
		}
	}
	function getUsers($MarketAgent) {
		$query = "SELECT DISTINCT `users`.`UserID`, concat_ws(' ',`LastName`,`FirstName`,`Patronymic`) as `Name` FROM `users`, `request` WHERE `request`.`MarketAgent` = '".$MarketAgent."' and `request`.`Client` = `users`.`UserID` ORDER BY `Name`";
		$result = mysql_query($query);
		$objs = array();
		$i=0;
		while($row = mysql_fetch_array($result)) {
			$objs[$i] = new Client($row);
			$i++;
		}
		echo json_encode($objs);
	}
	function getInvoices($UserID) {
		$query = "SELECT `invoice`.`InvoiceNumber`, concat_ws(' ',`LastName`,`FirstName`,`Patronymic`) as `Name`, `invoice`.`Date`, `request`.`RequestNumber`, `insiderequest`.`InsideRequestNumber` FROM `invoice`, `users`, `request`, `invoicesforinsiderequest`, `requestandinsiderequest`, `insiderequest` WHERE `invoice`.`IsEnabled` = 1 and `requestandinsiderequest`.`requestID` = `request`.`requestID` and `requestandinsiderequest`.`InsideRequestID` = `invoicesforinsiderequest`.`InsideRequestID` and `invoicesforinsiderequest`.`InvoiceID` = `invoice`.`InvoiceID` and `users`.`UserID` = `request`.`Client` and `insiderequest`.`InsideRequestID` = `invoicesforinsiderequest`.`InsideRequestID` and `users`.`UserID` = '".$UserID."'";
		$result = mysql_query($query);
		$objs = array();
		$i=0;
		while($row = mysql_fetch_array($result)) {
			$objs[$i] = new Invoice($row);
			$i++;
		}
		echo json_encode($objs);
	}
	include_once "../common_files/functions.php";
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>