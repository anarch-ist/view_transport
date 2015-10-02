		<div id="content">
			<div id="head-content">
				<h2><?php if(isset($_REQUEST['edit'])) echo "Редактирование маршрута"; else echo "Создание маршрута"?></h2>
			</div>
			<div id="content-body">
				<fieldset class="labels one">
					<div id="content-route">
						<h3>Название маршрута</h3><a href="javascript:showTextBox();">изменить</a>
						<div id="TextBox">
							<form>
								<input type="text" name="newName" value=""  placeholder="Название маршрута">
								<input type="button" value="OK" onclick="hideTextBox();">
							</form>
						</div>
						<div id="routeTableDiv">
							<p><i>Нет ни одного узла!</i></p>
						</div>
						<div id="content-route-button">
							<form action="<?php echo siteName();?>/admin/route_creating/additing/" method="POST">
								<input type="button" value="<?php if(isset($_REQUEST['edit'])) echo "Изменить маршрут"; else echo "Создать маршрут"?>" disabled>
							</form>
						</div>
					</div>
				</fieldset>
				<fieldset class="labels two">
					<div id="content-route-point">
						<h3>Выберите пункт из списка:</h3>
						<form action="<?php echo siteName();?>/admin/route_creating/" method="post">
							<div id="point-select">
								<select name="NewPointID">
	<?php
		include_once "../../common_files/functions.php";
		mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
		mysql_select_db($mysqlparams['database']);
		$query="SELECT `PointName`, `PointID` FROM `point` where `point`.`IsEnabled` = 1";
		$result=mysql_query($query);
		if (!$result) {
			echo "								<option selected disabled>Подобных пунктов нет..</option>\n";
		}
		else {
			while($row=mysql_fetch_array($result)) {
				echo "								<option value=\"".$row["PointID"]."\">".$row["PointName"]."</option>\n";
			}
		}
		mysql_close();
	?>
								</select>
							</div>
							<hr>
							<div class="content-route-point-button and-text">
								<p style="margin-top:-8px;">или</p>
								<input type="button" value="добавить пункт" onclick="javascript:window.location='<?php echo siteName();?>/admin/point_creating/?from=<?php echo siteName();?>/admin/route_creating/'">
							</div>
							<br>
							<h3>Распределение времени</h3>
							<div class="content-route-point-time">
								<div>
									<label>
										<span>время на погрузку:</span>
										<input type="text" name="load-time" class="datepicker" value="00:00:00">
									</label>
								</div>
								<div>
									<label>
										<span>время до следующего пункта:</span>
										<input type="text" name="leaving-time" class="datepicker" value="00:00:00">
									</label>
								</div>
							</div>
							<hr>
							<div class="content-route-point-button">
								<input type="button" value="добавить узел">
							</div>
						</form>
					</div>
				</fieldset>
				<div class="end-of-content"></div>
			</div>
			<div class="end-of-content"></div>
		</div>
<script type="text/javascript">
	var route = null;
	var DOMRouteObjects = null;
	var DOMPointObjects = null;
	var Route = function() {
		this.routeName = 'Название маршрута';
		this.points = new Array();
		this.getCountPoints = function() {
			return this.points.length;
		}
		this.addPoint = function(point) {
			this.points[this.points.length] = point;
		}
		this.getPoint = function(number) {
			return this.points[number];
		}
		this.changePlaces = function(number1,number2) {
			var pointTMP = this.getPoint(number1);
			this.points[number1] = this.getPoint(number2);
			this.points[number2] = pointTMP;
		}
		this.renameRoute = function(newName) {
			this.routeName = newName;
		}
		this.removePoint = function(number) {
			this.points.splice(number,1);
		}
	}
	var Point = function(pointID, pointName, loadTime, leavingTime) {
		this.pointID=pointID;
		this.pointName=pointName;
		this.loadTime=loadTime;
		this.leavingTime=leavingTime;
		this.convertTimeToNumber = function(time) {
			return time.split(':')[0]*24*60+time.split(':')[1]*60+time.split(':')[2];
		}
		this.convertNumberToTime = function(time) {
			return time/24/60+":"+time/60%24+":"+time%60;
		}
	}
	window.onload = function() {
		route = new Route();
		DOMRouteObjects = new function() {
			this.table = null;
			base = this;
			this.createRouteButton = document.getElementById('content-route-button').getElementsByTagName('input')[0];
			this.createTable = function() {
				var baseObj = document.getElementById('routeTableDiv');
				this.removeParagraph();
				if (!baseObj.children[0] || baseObj.children[0].nodeName !== 'TABLE') {
					this.table = baseObj.appendChild(document.createElement('table')).appendChild(document.createElement('tbody'));
					this.table.parentNode.cellSpacing=0;
					var row = this.addRow();
					row.appendChild(document.createElement("TH")).innerHTML = "название пункта";
					row.appendChild(document.createElement("TH")).innerHTML = "погрузка";
					row.appendChild(document.createElement("TH")).innerHTML = "следующий пункт";
					row.appendChild(document.createElement("TH")).innerHTML = "действия";
				}
			}
			this.addRow = function() {
				if (!this.table) {
					this.createTable();
				}
				row = this.table.appendChild(document.createElement('TR'));
				if (this.table.children.length > 2) {
					this.createRouteButton.removeAttribute('disabled');
				}
				return row;
			}
			this.addCells = function(row, data) {
				row.appendChild(document.createElement('TD')).textContent = data.pointName;
				row.appendChild(document.createElement('TD')).textContent = data.loadTime;
				row.appendChild(document.createElement('TD')).textContent = data.leavingTime;
				var button = row.appendChild(document.createElement('TD')).appendChild(document.createElement('button'));
				button.appendChild(document.createElement('img')).src='../../common_files/imgs/del.png';
				button.className = 'actions';
				button.onclick = function() {
					route.removePoint(this.parentNode.parentNode.rowIndex-1);
					DOMRouteObjects.removeRow(this.parentNode.parentNode.rowIndex);
				}
			}
			this.removeParagraph = function() {
				var baseObj = document.getElementById('routeTableDiv');
				if (baseObj.children[0].nodeName === 'P') {
					baseObj.removeChild(baseObj.children[0]);
				}
			}
			this.removeTable = function() {
				var baseObj = document.getElementById('routeTableDiv');
				if (baseObj.children[0].nodeName === 'TABLE') {
					this.table = null;
					baseObj.removeChild(baseObj.children[0]);
					baseObj.appendChild(document.createElement('p')).appendChild(document.createElement('i')).textContent = 'Нет ни одного узла!';
				}
			}
			this.removeRow = function(number) {
				if (number<1 || this.table.children.length < 2) {
					return false;
				}
				this.table.removeChild(this.table.getElementsByTagName('tr')[number]);
				if (this.table.children.length < 3) {
					this.createRouteButton.disabled = 'disabled';
				}
				if (this.table.children.length === 1) {
					this.removeTable();
				}
				return true;
			}
			this.createRouteButton.onclick = function(event) {
				var newQuery = new XMLHttpRequest();
				newQuery.open('post', 'documents.php', true);
				newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				newQuery.send('Status=addNewRoute<? if(isset($_REQUEST['edit'])) echo '&RouteID='.$_REQUEST['RouteID']; ?>&route='+JSON.stringify(route));
				newQuery.onreadystatechange = function() {
					if (newQuery.readyState!=4) return;
					var result = this.responseText;
					console.log(result);
				}				
				base.removeTable();
				cancelMainButtonEvent(event);
			}
		}
		DOMPointObjects = new function() {
			this.select = document.getElementById('point-select').getElementsByTagName('select')[0];
			this.loadTimeObj = document.getElementsByClassName('content-route-point-time')[0].getElementsByTagName('input')[0];
			this.leavingTimeObj = document.getElementsByClassName('content-route-point-time')[0].getElementsByTagName('input')[1];
			this.createRoutePointButton = document.getElementsByClassName('content-route-point-button')[1].getElementsByTagName('input')[0];
			var base = this;
			this.addNewRoutePoint = function(point) {
				route.addPoint(point);
				DOMRouteObjects.addCells(DOMRouteObjects.addRow(), route.getPoint(route.getCountPoints()-1));
			}
			this.addNewRoutePointFromForm = function() {
				this.addNewRoutePoint(new Point(this.select.value, this.select.children[this.select.selectedIndex].textContent,this.loadTimeObj.value,this.leavingTimeObj.value));
			}
			this.createRoutePointButton.onclick = function() {
				base.addNewRoutePointFromForm();
			}
		}
<?php if(isset($_POST['edit'])): ?>
		var request = new XMLHttpRequest();
		request.open('post', 'documents.php', true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.send('Status=getRouteInformation&RouteID='+'<?php echo $_POST['RouteID']; ?>');
		request.onreadystatechange = function() {
			if (request.readyState!=4) return;
			var result = JSON.parse(this.responseText);
			console.log(result);
			changeRouteName(result.routeName);
			for (var i=0;i<result.points.length;i++) {
				DOMPointObjects.addNewRoutePoint(result.points[i]);
			}
		}
<?php endif; ?>
	}
	function showTextBox() {
		document.getElementById('TextBox').style.display='block';
		document.getElementById('content-route').getElementsByTagName('h3')[0].style.display='none';
		document.getElementById('content-route').getElementsByTagName('a')[0].style.display='none';
	}
	function hideTextBox() {
		document.getElementById('TextBox').removeAttribute("style");
		document.getElementById('content-route').getElementsByTagName('h3')[0].removeAttribute("style");
		document.getElementById('content-route').getElementsByTagName('a')[0].removeAttribute("style");
		if (document.getElementById('TextBox').getElementsByTagName('form')[0].children[0].value.length>0) {
			routeName = document.getElementById('TextBox').getElementsByTagName('form')[0].children[0].value;
		}
		changeRouteName(routeName);
	}
	function changeRouteName(name) {
		document.getElementById('content-route').getElementsByTagName('h3')[0].innerHTML=name;
		route.renameRoute(name);
	}
	Protoplasm.use('timepicker')
		.transform('input.datepicker', {'timePicker' : 'true' , 'use24hrs' : 'true'});
</script>