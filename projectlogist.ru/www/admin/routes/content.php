		<div id="content">
			<div id="head-content">
				<h2>Работа с маршрутами</h2>
			</div>
			<div id="content-body">
				<fieldset class="labels one">
				<label style="margin: 0;font-size: 0.8em;">
					<input type="checkbox" style="vertical-align: middle;">только свои маршруты
				</label>
					<div id="content-route">
						<table cellspacing="0">
							<colgroup colspan="1" width="25%"></colgroup>
							<colgroup colspan="1" width="40%"></colgroup>
							<colgroup colspan="1" width="20%"></colgroup>
							<colgroup colspan="1" width="15%"></colgroup>
							<tr><th>Маршрут</th><th>Список узлов</th><th>Создатель</th><th>Действия</th></tr>
						</table>
					</div>
				</fieldset>
				<div class="end-of-content"></div>
			</div>
			<div class="end-of-content"></div>
		</div>
<script type="text/javascript">
	function getRoutes(isUserRoutesOnly) {
		var query = new XMLHttpRequest();
		query.open('post', 'documents.php', true);
		query.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var status = (isUserRoutesOnly ? 'getRoutesOfUser' : 'getAllRoutes');
		query.send('Status='+status);
		query.onreadystatechange = function() {
			if (this.readyState!=4) return;
			var result =  JSON.parse(this.responseText);
			console.log(result);
			DOMObjects.table.removeRows();
			DOMObjects.checkbox.removeAttribute('disabled');
			for (i=0;i<result.length;i++) {
				DOMObjects.table.addCells(DOMObjects.table.addRow(), result[i]);
			}
		}
	}
	window.onload = function() {
		DOMObjects = new function() {
			this.checkbox = document.getElementsByTagName('input')[0];
			this.table = document.getElementsByTagName('table')[0].getElementsByTagName('tbody')[0];
			var base = this;
			this.checkbox.onchange = function() {
				getRoutes(this.checked);
				this.disabled = 'disabled';
			}
			this.table.removeRows = function() {
				while (this.children.length > 1) {
					this.removeChild(this.children[this.children.length-1]);
				}
			}
			this.table.addRow = function() {
				return this.appendChild(document.createElement('tr'));
			}
			this.table.addCells = function(row, data) {
				row.appendChild(document.createElement('td')).textContent = data.RouteName;
				row.appendChild(document.createElement('td')).textContent = data.Points;
				row.appendChild(document.createElement('td')).textContent = data.UserName;
				var buttonDel = row.appendChild(document.createElement('td')).appendChild(document.createElement('button'));
				var buttonEdit = buttonDel.parentNode.appendChild(document.createElement('button'));
				buttonEdit.appendChild(document.createElement('img')).src='../../common_files/imgs/edit.png';
				buttonDel.appendChild(document.createElement('img')).src='../../common_files/imgs/del.png';
				buttonEdit.className = "actions";
				buttonDel.className = "actions";
				buttonEdit.value = data.RouteID;
				buttonDel.value = data.RouteID;
				buttonDel.onclick = function() {
					var newQuery = new XMLHttpRequest();
					newQuery.open('post', 'documents.php', true);
					newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
					var status='removeRoute&RouteID='+this.value;
					newQuery.send('Status='+status);
					newQuery.onreadystatechange = function() {
						if (newQuery.readyState!=4) return;
						var result = this.responseText;
						console.log(result);
					}
					this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);
				}
				buttonEdit.onclick = function() {
					var form = document.createElement('form');
					form.method = 'POST';
					form.action = '../route_creating/';
					var input = form.appendChild(document.createElement('input'));
					input.type = 'hidden';
					input.name = 'edit';
					input = form.appendChild(document.createElement('input'));
					input.type = 'hidden';
					input.name = 'RouteID';
					input.value = this.value;
					form.submit();
				}
			}
		}
		getRoutes(false);
	}
</script>