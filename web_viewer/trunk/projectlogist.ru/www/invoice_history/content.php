		<div id="content">
			<div id="head-content">
				<h2>История накладных</h2>
			</div>
			<div>
				<div id="content-document-menu">
<?php if($_COOKIE['UserTypeID']==='0003'): ?>
					<!-- <input type="text" placeholder="ФИО клиента"> -->
					<select style="width: 25%;">
					</select>
<script type="text/javascript">
	function getUsers() {
		var req = new XMLHttpRequest();
		req.open('post', 'documents.php', true);
		req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var status = 'getUsers';
		req.send('Status='+status);
		DOMObjectForTable.clear();
		req.onreadystatechange = function() {
			if (this.readyState!=4) return;
			users = JSON.parse(this.responseText);
			console.log(users);
			request.addUsers(users);
			// invoices = JSON.parse(this.responseText);
			// console.log(invoices);
			// for (i=0;i<invoices.length;i++) {
				// DOMObjectForTable.addCells(invoices[i]);
			// }
		}
	}
</script>
<?php endif; ?>
					<table style="" cellspacing="0">
						<tbody>
							<tr>
								<th>номер заявки</th>
								<th>номер внутренней заявки</th>
								<th>номер накладной</th>
								<th>дата</th>
								<th>ФИО</th>
							</tr>
						</tbody>
					</table>
				</div>
				<button onclick="request.getInformation();">обновить</button>
				<div id="end-of-content"></div>
			</div>
		</div>
<script type="text/javascript">
	var invoices = null;
	window.onload = function() {
<?php if ($_COOKIE['UserTypeID']==='0004'):?>
		request.setUser('<?php echo $_COOKIE["UserID"]?>');
		request.getInformation();
<?php endif; ?>
<?php if ($_COOKIE['UserTypeID']==='0003'):?>
		request.requireUsers();
<?php endif; ?>
	}
	request = new function() {
		this.UserID;
		this.setUser = function(userID) {
			this.UserID = userID;
		}
<?php if ($_COOKIE['UserTypeID']==='0003'):?>
		this.users = null;
		this.requireUsers = function() {
			getUsers();
		}
		this.addUsers = function(users) {
			this.users = users;
			DOMObjectForSelect.addUsers(users);
			this.changeUser();
		}
		this.changeUser = function() {
			this.setUser(this.users[DOMObjectForSelect.getCurrentUserNumber()].userID);
			this.getInformation();
		}
<?php endif; ?>
		this.getInformation = function() {
			if (!status) {
				status = 'getInvoices';
			}
<?php if ($_COOKIE['UserTypeID']==='0003'):?>
			status='getInvoicesForUser';
<?php endif; ?>
<?php if ($_COOKIE['UserTypeID']==='0004'):?>
			status='getInvoices';
<?php endif; ?>
			var request = new XMLHttpRequest();
			request.open('post', 'documents.php', true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			status += '&UserID='+this.UserID;
			request.send('Status='+status);
			DOMObjectForTable.clear();
			request.onreadystatechange = function() {
				if (this.readyState!=4) return;
				invoices = JSON.parse(this.responseText);
				console.log(invoices);
				for (i=0;i<invoices.length;i++) {
					DOMObjectForTable.addCells(invoices[i]);
				}
			}
		}
		this.sendRequest = function(params) {
			
		}
		this.getFullInfo = function() {
			
		}
	}
	DOMObjectForTable = new function() {
		this.table = document.getElementsByTagName('tbody')[0];
		this.rowCount = 0;
		this.addRow = function() {
			this.rowCount++;
			return this.table.appendChild(document.createElement('tr'));
		}
		this.clear = function() {
			for (i=0;i<this.rowCount;i++) {
				this.table.removeChild(this.table.getElementsByTagName('tr')[1]);
			}
			this.rowCount=0;
		}
		this.addCells = function(invoice) {
			row = this.addRow();
			row.appendChild(document.createElement('td')).textContent = invoice.requestNumber;
			row.appendChild(document.createElement('td')).textContent = invoice.insideRequestNumber;
			row.appendChild(document.createElement('td')).textContent = invoice.invoiceNumber;
			row.appendChild(document.createElement('td')).textContent = invoice.datetime;
			row.appendChild(document.createElement('td')).textContent = invoice.name;
		}
	}
<?php if ($_COOKIE['UserTypeID']==='0003'):?>
	DOMObjectForSelect = new function() {
		this.select = document.getElementsByTagName('select')[0];
		this.clear = function() {
			while(this.select.children.length) {
				this.select.removeChild(this.select.children[0]);
			}
		}
		this.select.onchange = function() {
			changeUser();
		}
		this.addUsers = function(users) {
			this.clear();
			for (i=0;i<users.length;i++) {
				this.select.appendChild(document.createElement('option')).textContent = users[i].name;
			}
			
		}
		this.getCurrentUserNumber = function() {
			return this.select.selectedIndex;
		}
		this.clear = function() {
			for (i=0;i<this.rowCount;i++) {
				this.table.removeChild(this.table.getElementsByTagName('tr')[1]);
			}
			this.rowCount=0;
		}
	}
	function changeUser() {
		request.changeUser();
	}
<?php endif; ?>
</script>