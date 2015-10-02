		<script type="text/javascript">
			var PointID = <?php
				include_once "../../common_files/functions.php";
				mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
				mysql_select_db($mysqlparams['database']);
				$query="SELECT `pointID` FROM `dispatchersforpoint` WHERE  `UserID` = '".$_COOKIE['UserID']."';";// query for
				$result=mysql_query($query);
				$row = mysql_fetch_array($result);
				if (!isset($row[0]) || $row[0]==='') {
					
					$row[0]=-1;
				}
				echo $row[0];
				$PointID = $row[0];
			?>;
			var Field = function(information) {
				//information object
				this.information = information;
				//creating objects
				this.DOMObj=document.createElement('div');
				this.invoiceNameField = document.createElement('p');
				this.facticalDateCreation = document.createElement('p');
				this.expectedDateCreation = document.createElement('p');
				this.expectedDateChanging = document.createElement('p');
				//setting properties
				this.DOMObj.className = 'car';
				//filling data from information
				this.invoiceNameField.textContent = this.information.invoiceName;
				this.facticalDateCreation.textContent = this.information.statusStartTime;
				this.expectedDateCreation.textContent = this.information.expectedStatusStartTime;
				this.expectedDateChanging.textContent = this.information.expectedEndStatusTime;
				//attaching objs
				this.DOMObj.appendChild(document.createElement('div'));
				this.DOMObj.children[0].appendChild(document.createElement('p'));
				this.DOMObj.children[0].children[0].textContent = "Номер накладной:";
				this.DOMObj.children[0].appendChild(this.invoiceNameField);
				this.DOMObj.appendChild(document.createElement('div'));
				this.DOMObj.children[1].appendChild(document.createElement('p'));
				this.DOMObj.children[1].children[0].textContent = "Ожидаемое время создания статуса:";
				this.DOMObj.children[1].appendChild(this.expectedDateCreation);
				this.DOMObj.appendChild(document.createElement('div'));
				this.DOMObj.children[2].appendChild(document.createElement('p'));
				this.DOMObj.children[2].children[0].textContent = "Фактическое время создания статуса:";
				this.DOMObj.children[2].appendChild(this.facticalDateCreation);
				this.DOMObj.appendChild(document.createElement('div'));
				this.DOMObj.children[3].appendChild(document.createElement('p'));
				this.DOMObj.children[3].children[0].textContent = "Ожидаемое время смены статуса:";
				this.DOMObj.children[3].appendChild(this.expectedDateChanging);
				this.buttons;
				switch (information.type) {
					case 'COME_IN': {
						document.getElementById('status-coming').appendChild(this.DOMObj);
						this.buttons = [new ButtonObject(new Type('COME_IN'),this), new ButtonInfo(this)];
						break;
					}
					case 'CONFIRM': {
						document.getElementById('status-loading').appendChild(this.DOMObj);
						if (information.hasPreviousRoutePoint) {
							this.buttons = [new ButtonObject(new Type('CONFIRM'),this),new ButtonObject(new Type('ERROR'),this), new ButtonInfo(this)];
						} else {
							this.buttons = [new ButtonObject(new Type('CONFIRM'),this), new ButtonInfo(this)];
						}
						break;
					}
				}
				//functions
				this.remove = function() {
					this.DOMObj.parentNode.removeChild(this.DOMObj);
				}
			}
			ButtonInfo = function(baseObj) {
				//information object
				var base = baseObj;
				var baseButton = this;
				// panel for viewing
				this.pointViewPanel = new function() {
					this.root = document.getElementsByClassName('pointsList')[0];
					this.text = this.root.getElementsByTagName('p')[0];
					this.setNewInformation = function(text) {
						this.text.textContent = text;
					}
					this.clearInformation = function() {
						this.setNewInformation('');
					}
					this.turnOn = function() {
						this.root.hidden = false;
						this.clearInformation();
					}
				}
				//creating objects
				this.button = document.createElement('button');
				//setting properties
				this.button.textContent = "подробней";
				//attaching objs
				base.DOMObj.appendChild(this.button);
				//functions
				this.button.onclick = function() {
					baseButton.pointViewPanel.turnOn();
					var newQuery = new XMLHttpRequest();
					newQuery.open('post', 'documents.php', true);
					newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
					newQuery.send('PointID='+PointID+'&status=traceRoute&InvoiceID='+base.information.invoiceID);
					newQuery.onreadystatechange = function() {
						if (newQuery.readyState!=4) return;
						var result = JSON.parse(this.responseText);
						result.outPut = function() {
							str = result;
							return str;
						}
						//console.log(result.outPut());
						console.log(result);
						baseButton.pointViewPanel.setNewInformation(result);
					}
				}
			}
			ButtonObject = function(type, baseObj) {
				//information object
				this.type = type;
				var base = baseObj;
				//creating objects
				this.button = document.createElement('button');
				//setting properties
				this.button.textContent = type.getButtonString();
				//attaching objs
				base.DOMObj.appendChild(this.button);
				//functions
				this.button.onclick = function() {
					confirm.sendingData=type.getAJAXString();
					confirm.sendingData+='&StatusID='+base.information.statusID;
					confirm.sendingData+='&InvoiceID='+base.information.invoiceID;
					confirm.sendingData+='&routePointID='+base.information.routePointID;
					confirm.sendingData+='&expectedEndStatusTime='+base.information.expectedEndStatusTime;
					confirm.activate();
				}
			}			
			var Confirm = function() {
				var base = this;
				this.sendingData='';
				this.main = document.getElementsByClassName('window-phone')[0];
				this.dateObj = this.main.getElementsByTagName('input')[2];
				this.OK = this.main.getElementsByTagName('button')[0];
				this.cancel = this.main.getElementsByTagName('button')[1];
				this.activate = function() {
					var dateQuery = new Date();
					setMethodGetQueryDataStringForDateObj(dateQuery);
					this.dateObj.value = dateQuery.getQueryDataString();
					this.main.className+=' selected';
					suspendRefreshing();
				}
				this.deactivate = function() {
					this.main.className='window-phone';
					resumeRefreshing();
				}
				this.OK.onclick = function() {
					var newQuery = new XMLHttpRequest();
					newQuery.open('post', 'documents.php', true);
					newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
					base.sendingData+='&datetime='+base.dateObj.value;//.substring(6,10)+'-'+base.dateObj.value.substring(0,5)+' '+base.dateObj.value.substring(11,base.dateObj.value.lenght);
					newQuery.send('PointID='+PointID+'&status='+base.sendingData);
					newQuery.onreadystatechange = function() {
						if (newQuery.readyState!=4) return;
						var result = JSON.parse(this.responseText);
						console.log(result);
					}
					base.deactivate();
				}
				this.cancel.onclick = function() {
					base.deactivate();
				}
			}
			
			Type = function(name) {
				var typeArray = {ERROR:'ERROR',COME_IN:'COME_IN',CONFIRM:'CONFIRM'};
				var typeAJAXString = {ERROR:'setMistakeStatusForInvoice',COME_IN:'setLoadingStatusForInvoice',CONFIRM:'setConfirmStatusForInvoice'};
				var typeButtonString = {ERROR:'ошибка',COME_IN:'прибытие',CONFIRM:'подтверждение'};
				if (!typeArray[name]) {
					throw new Error('wrong value for enumerate. ERROR, COME_IN or CONFIRM expected');
				}
				this.type = typeArray[name];
				this.ERROR = function() {
					this.type = typeArray.ERROR;
				}
				this.CONFIRM = function() {
					this.type = typeArray.CONFIRM;
				}
				this.COME_IN = function() {
					this.type = typeArray.COME_IN;
				}
				this.getAJAXString = function() {
					return typeAJAXString[this.type];
				}
				this.getButtonString = function() {
					return typeButtonString[this.type];
				}
			}
			setMethodGetQueryDataStringForDateObj = function(dateA) {
				dateA.getQueryDataString = function() {
					var months = (dateA.getMonth() > 8 ? dateA.getMonth()+1 : '0'+(dateA.getMonth()+1));
					var days = (dateA.getDate() > 0 ? dateA.getDate() : '0'+dateA.getDate());
					return months+'-'+days+'-'+dateA.getFullYear()+' '+ dateA.getHours()+':'+dateA.getMinutes()+':'+dateA.getSeconds();
				}
			}
			var confirm;
			var date = new Date();
			setMethodGetQueryDataStringForDateObj(date);
			function suspendRefreshing() {
				canUpdate=false;
			}
			function resumeRefreshing() {
				canUpdate=true;
			}
			var canUpdate = true;
			var invoices = Array();
			Protoplasm.use('datepicker').transform('input.datepicker', {'timePicker' : 'true' , 'use24hrs' : 'true', 'locale':'ru_RU'});
			//main method
			window.onload = function () {
				confirm = new Confirm();
				setInterval(
					function() {
						if (canUpdate) {
							var newQuery = new XMLHttpRequest();
							newQuery.open('post', 'documents.php', true);
							newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
							var status=date.getQueryDataString()+'&status=getAllObjects';
							newQuery.send('PointID='+PointID+'&datetime='+status);
							date = new Date();
							setMethodGetQueryDataStringForDateObj(date);
							newQuery.onreadystatechange = function() {
								if (newQuery.readyState!=4) return;
								var result = JSON.parse(this.responseText);
								for (var i=0;i<invoices.length;i++) {
									invoices[i].remove();
								}
								invoices = Array();
								for (var i=0;i<result.length;i++) {
									invoices[i] = new Field(result[i]);
									console.log(result[i]);
								}
							}
						}
					},
				3000);
			}
		</script>
		<div id="content">
			<div id="head-content">
				<h2>Движение накладных</h2>
			</div>
			<div>
				<div id="status-coming">
					<h3>Накладные в пути</h3>
				</div>
				<div id="status-loading">
					<h3>Накладные на разгрузке</h3>
				</div>
				<div id="end-of-content"></div>
			</div>
		</div>
		<style>
div.pointsList {
	position: fixed;
	width: 100%;
	height: 100%;
	top: 0;
	left: 0;
	background-color: rgba(0, 0, 0, 0.24);
}
div.pointsList span {
	margin-left: 243px;
}
div.pointsList fieldset {
  box-shadow: black 0 0 100px;
  width: 50%;
  position: relative;
  margin: 10% auto;
}
div.pointsList a.closePointsList {
	position: absolute;
	top: 5px;
	right: 15px;
	font-size: 0.8em;
	color: rgb(53, 97, 189);
	cursor: pointer;
}
		</style>
		<div class="pointsList" hidden>
			<fieldset>
				<span>Список  пунктов</span>
				<p></p>
				<a class="closePointsList" onclick="document.getElementsByClassName('pointsList')[0].hidden=true;">закрыть</a>
			</fieldset>
		</div>