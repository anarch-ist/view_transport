<?php
	include_once "../../common_files/functions.php";
	function main() {
		if ($_POST['Status']==='removeInvoice') {
			echo 'removeInvoice '.$_COOKIE['UserID'].", ".$_POST['InvoiceID'];
			removeInvoice();
			echo ' removeInvoice';
		}
		else {
			echo $_POST;
			echo ' ... и идите к админу';
		}
	}
	function removeInvoice() {
		$query="UPDATE `invoice` SET `LastModBy` = '".$_COOKIE['UserID']."', `IsEnabled` = 0 WHERE `InvoiceID` = '".$_POST['InvoiceID']."';";
		$res=mysql_query($query);
		echo $res;
	}
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	main();
	mysql_close();
?>