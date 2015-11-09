		<div id="content">
			<div id="head-content">
				<h2>Создание нового документа</h2>
			</div>
			<div>
				<div id="content-document-menu">
					<ul>
						<li>
							<a>Клиент</a>
						</li>
						<li>
							<a>Заявка</a>
						</li>
						<li>
							<a>Вн. заявка</a>
						</li>
						<li>
							<a>Накладная</a>
						</li>
					</ul>
				</div>
				<div id="clientTable" class="content-document">
					<div id="userInfo">
						<fieldset>
							<form id="tableAndText">
								<label><input type="text" name="query" placeholder="введите ФИО или номер договора" class="search"></label>
								<div id="noTable"><img src="../../common_files/imgs/error.png" alt="ERROR"><p>нет ни одного клиента</p></div>
								<div id="userTable">
									<table cellspacing="0">
										<colgroup colspan="1" width="35%" id="userTable-first"></colgroup>
										<colgroup colspan="1" width="15%" id="userTable-second"></colgroup>
										<colgroup colspan="1" width="20%" id="userTable-third"></colgroup>
										<colgroup colspan="1" width="30%" id="userTable-fouth"></colgroup>
										<tbody>
											<tr><th>ФИО</th><th>Телефон</th><th>E-mail</th><th>Номер договора</th></tr>
<?php
	class ClientClass {
		public $fullName;
		public $ID;
		public $telephone;
		public $email;
		public $isMine;
		function __construct($row){
			$count++;
			$this->ID = $row[0];
			$this->fullName = $row[1].' '.$row[2].' '.$row[3];
			$this->telephone = $row[4];
			$this->email = $row[5];
			$this->marketAgent = 0;
		}
		public function setMarketAgent() {
			$this->isMine = true;
		}
		public function isMine() {
			//return ($this->isMine) ? ' class="mine"' : '';
			return ' class="mine"';
		}
	} 
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	
	$query="SELECT `users`.`UserID`, `users`.`LastName`, `users`.`FirstName`, `users`.`Patronymic`, `users`.`Telephone`, `users`.`Email` FROM `users` WHERE `users`.`UserStatus`='ACTIVE' AND `users`.`UserTypeID`='0004';";	// query for determining all clients
	$result=mysql_query($query);
	$clients = array();
	while ($row=mysql_fetch_array($result)) {
		$clients[$row[0]] = new ClientClass($row);
	}
	$query="SELECT `users`.`UserID`, `request`.`MarketAgent` FROM `users`, `request` WHERE `users`.`UserID` = `request`.`Client` AND `users`.`UserStatus`='ACTIVE' AND `request`.`MarketAgent`='".$_COOKIE['UserID']."';";	// query for determining clients of current market agent
	$result=mysql_query($query);
	while ($row=mysql_fetch_array($result)) {
		$clients[$row[0]]->setMarketAgent();
	}
	foreach ($clients as $key => $value) {
		echo "											<tr><td>$value->fullName<input type=\"hidden\" name=\"valueID\" value=\"$key\"".$value->isMine()."></td><td>$value->telephone</td><td>$value->email</td><td>номер договора</td></tr>\n";
	}
	mysql_close();
?>
										</tbody>
									</table>
								</div>
							</form>
						</fieldset>
					</div>
					<div id="invoices">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="treeQuery" placeholder="введите номер накладной или заявки" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет заявок на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addRequestMain">добавить заявку</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
				</div>
				<div class="content-document" id="request">
					<div id="requestList">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="requestQuery" placeholder="введите номер заявки" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет заявок на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addRequest">добавить заявку</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
					<div id="insideRequestListMain">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="requestQuery" placeholder="введите номер вн. заявки" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет внутренних заявок на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addInsideRequestMain">добавить вн. заявку</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
					<div class="documentInfo selected">
						<fieldset>
							<div class="viewElement">
								<span>номер заявки:</span><span>номер заявки</span>
							</div>
							<div class="viewElement">
								<span>дата заполнения:</span><span>16.04.2015</span>
							</div>
							<div class="viewElement">
								<span>изменен:</span><span>!</span>
							</div>
						</fieldset>
					</div>
					<div class="documentEdit">
						<fieldset>
							<form id="formRequest">
								<div class="formElement">
									<label><span>номер заявки:</span><input type="text" name="documentNumber" placeholder="обязательное поле"></label>
								</div>
								<div class="formElement">
									<label><span>дата заполнения:</span><input type="text" name="datetime" class="datepicker" placeholder="гггг-мм-чч ЧЧ:ММ:СС"></label>
								</div>
								<div class="button-submit">
									<button>Отмена</button>
									<input type="submit" value="Создать">
								</div>
							</form>
						</fieldset>
					</div>
				</div>
				<div class="content-document" id="insideRequest">
					<div id="insideRequestList">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="requestQuery" placeholder="введите номер вн. заявки" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет внутренних заявок на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addInsideRequest">добавить вн. заявку</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
					<div id="invoiceListMain">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="requestQuery" placeholder="введите номер накладной" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет накладных на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addInvoiceMain">добавить накладную</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
					<div class="documentInfo selected">
						<fieldset>
							<div class="viewElement">
								<span>номер вн. заявки:</span><span>номер вн. заявки</span>
							</div>
							<div class="viewElement">
								<span>склад:</span><span>!</span>
							</div>
							<div class="viewElement">
								<span>дата заполнения:</span><span>16.04.2015</span>
							</div>
							<div class="viewElement">
								<span>изменен:</span><span>!</span>
							</div>
						</fieldset>
					</div>
					<div class="documentEdit">
						<fieldset>
							<form id="formInsideRequest">
								<div class="formElement">
									<label><span>номер вн. заявки:</span><input type="text" name="documentNumber" placeholder="обязательное поле"></label>
								</div>
								<div class="formElement">
									<label><span>склад:</span><select name="PointID">
<?php
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	$query='SELECT `point`.`PointID`, `point`.`PointName` FROM `point` WHERE `point`.`IsEnabled` = 1 AND `point`.`PointTypeID` = \'0101\';';// query for making options of routes
	$result=mysql_query($query);
	if (!$result||$result==='') {
		echo "										<option> маршруты отсутствуют</option>\n";
	}
	else while ($row=mysql_fetch_array($result)) {
		echo '										<option value='.$row['PointID'].'>'.$row['PointName']."</option>\n";
	}
	mysql_close();
?>
									</select></label>
								</div>
								<div class="formElement">
									<label><span>дата заполнения:</span><input type="text" name="datetime" class="datepicker" placeholder="гггг-мм-чч ЧЧ:ММ:СС"></label>
								</div>
								<div class="button-submit">
									<button>Отмена</button>
									<input type="submit" value="Создать">
								</div>
							</form>
						</fieldset>
					</div>
				</div>
				<div class="content-document" id="invoice">
					<div id="invoiceList">
						<fieldset>
							<form class="formClass">
								<label><input type="text" name="requestQuery" placeholder="введите номер накладной" class="search"></label>
								<div class="invoiceArea">
									<div class="noDocument"><p><img src="../../common_files/imgs/error.png" alt="ERROR">нет накладных на данного клиента</p></div>
									<div class="documentData">
										<div class="tree">
											<ul>
											</ul>
										</div>
										<div class="buttonAddRequest"><button name="addInvoice">добавить накладную</button></div>
									</div>
								</div>
							</form>
						</fieldset>
					</div>
					<div class="documentInfo selected">
						<fieldset>
							<div class="viewElement">
								<span>номер накладной:</span><span>номер вн. заявки</span>
							</div>
							<div class="viewElement">
								<span>дата заполнения:</span><span>16.04.2015</span>
							</div>
							<div class="viewElement">
								<span>дата открытия:</span><span>16.04.2015</span>
							</div>
							<div class="viewElement">
								<span>дата плановой доставки:</span><span>16.04.2015</span>
							</div>
							<div class="viewElement">
								<span>изменен:</span><span>16.04.2015</span>
							</div>
						</fieldset>
					</div>
					<div class="documentEdit">
						<fieldset>
							<form id="formInvoice">
								<div class="formElement">
									<label><span>номер накладной:</span><input type="text" name="documentNumber" placeholder="обязательное поле"></label>
								</div>
								<div class="formElement">
									<label><span>маршрут:</span><select name="RouteID">
<?php
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	$query='SELECT `route`.`RouteID`, `route`.`RouteName` FROM `route` WHERE `route`.`IsEnabled` = 1;';// query for making options of routes
	$result=mysql_query($query);
	if (!$result||$result==='') {
		echo "										<option> маршруты отсутствуют</option>\n";
	}
	else while ($row=mysql_fetch_array($result)) {
		echo '										<option value='.$row['RouteID'].'>'.$row['RouteName']."</option>\n";
	}
	mysql_close();
?>
									</select></label>
								</div>
								<div class="formElement">
									<label><span>отправка:</span><input type="text" name="datetime" class="datepicker" placeholder="гггг-мм-чч ЧЧ:ММ:СС"></label>
								</div>
								<div class="button-submit">
									<button>Отмена</button>
									<input type="submit" value="Создать">
								</div>
							</form>
						</fieldset>
					</div>
				</div>
				<div id="end-of-content"></div>
			</div>
		</div>