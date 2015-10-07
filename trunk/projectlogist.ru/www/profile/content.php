<div id="content">
	<div id="head-content">
		<h2><?php if ($_GET['edit']==true) echo 'редактирование данных пользователя'; else echo 'просмотр данных пользователя'?></h2>
	</div>
	<?php
	mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
	mysql_select_db($mysqlparams['database']);
	?>
	<script type="text/javascript">
		UserMainData = function() {
			this.firstName;
			this.lastName;
			this.patronymic;
			this.nickName;
			this.telephone;
			this.email;
		}
		ClientRequizitData = function() {
			this.INN;
			this.KPP;
			this.CorAccount;
			this.CurAccount;
			this.BIK;
			this.BankName;
			this.ContractNumber;
			this.DateOfSigning;
			this.StartContractDate;
			this.EndContractDate;
		}
		DataAndTables = function() {
			this.userMainData;
			this.clientRequizitDataList;
			this.getDataFromViewTables = function() {
				this.userMainData = DOMObjects.getDataFromUserMainDataViewTable();
				this.clientRequizitDataList = DOMObjects.getDataFromClientRequizitDataViewTable();
			}
		}
		DOMObjects=null;
		window.onload = function() {
			DOMObjects = new function() {
				this.editProfile = document.getElementById('profileEdit');
				this.viewProfile = document.getElementById('profileView');
				this.getDataFromUserMainDataViewTable = function() {
					var table = this.viewProfile.getElementsByClassName('userMainData View')[0];
					data = new UserMainData();
					var j=0;
					for (var i in data) {
						data[i] = table.getElementsByTagName('td')[2*j+1].textContent;
					}
					return data;
				}
				this.getDataFromClientRequizitDataViewTable = function() {
					var tables = this.viewProfile.getElementsByClassName('userMainData View');
					data = new Array();
					for (k=0;k<tables.length;k++) {
						var j=0;
						data[k] = new ClientRequizitData();
						for (var i in data) {
							data[k][i] = tables[k].getElementsByTagName('td')[2*j+1].textContent;
						}
					}
					return data;
				}
				this.setEditProfileVisibility = function(isEditionEnabled) {
					this.viewProfile.hidden = isEditionEnabled;
					this.editProfile.hidden =!isEditionEnabled;
				}
				this.enableEdition = function() {
					this.setEditProfileVisibility(true);
				}
				this.disableEdition = function() {
					this.setEditProfileVisibility(false);
				}
			}
			navigation= new DOMObjectsForNavigation();
		}

		var DOMObjectsForNavigation = function() {									// class for navigation
			this.allTabs = document.getElementById('content-document-menu').getElementsByTagName('ul')[0].children;			// tabs' array;
			this.currentTabNumber = 0;												// current tab number;
			this.maxNumber = 2;
			this.allTabs[this.currentTabNumber].className = 'selected';
			this.lock = function() {
				for (var i=0;i<this.allTabs.length;i++) {
					this.allTabs[i].className = 'disabled';
				}
				this.goToTab(this.currentTabNumber);
			}
			this.unlock = function() {
				this.switchOnTab(this.maxNumber);
			}
			this.switchOnTab = function(number) {
				this.maxNumber = number;
				for (var i=0;i<this.allTabs.length;i++) {
					if (i>number) {
						this.allTabs[i].className = 'disabled';
					}
					else {
						this.allTabs[i].removeAttribute('class');
					}
				}
				this.goToTab(this.currentTabNumber);
			}
			this.fields = document.getElementsByClassName('field');
			this.field = this.fields[0];
			this.field.className += ' selected';
			this.goToTab = function(number) {
				this.allTabs[this.currentTabNumber].removeAttribute('class');
				this.field.className = 'field';
				this.currentTabNumber = number;
				this.allTabs[this.currentTabNumber].className = 'selected';
				this.field = base.fields[number];
				this.field.className += ' selected';
			}
			this.allTabs.indexOf = function(tab) {
				for (var i in this) {
					if (this[i] === tab)
						return i;
				}
				return -1;
			}
			var base = this;
			for (var i in this.allTabs) {
				this.allTabs[i].onclick = function() {							// switches tabs
					if (this!==base.currentTab && this.className!=='disabled') {
						base.goToTab(base.allTabs.indexOf(this));
					}
				}
			}
			this.removeLastTab = function(isOneMore) {
				if(isOneMore) {
					this.allTabs[this.maxNumber].parentNode.removeChild(this.allTabs[this.maxNumber]);
					this.fields[this.maxNumber].parentNode.removeChild(this.fields[this.maxNumber]);
					this.maxNumber--;
				}
			}
			this.removeLastTab(document.cookie.substr(document.cookie.indexOf('UserTypeID')+14,1)!='4');
		}

		function startEditing() {
			DOMObjects.enableEdition();
			//navigation.switchOnTab(document.cookie.substr(document.cookie.indexOf('UserTypeID')+14,1)=='4'?2:1);
		}
		function discardChanges() {
			DOMObjects.disableEdition();
			navigation.goToTab(0);
		}
		function confirmChanges() {
			DOMObjects.disableEdition();
			navigation.goToTab(0);
		}
	</script>
	<div id="profileView">
		<table class="userMainData View">
			<tr>
				<th colspan=2>
					Общие данные пользователя
				</th>
			</tr>
			<?php
			$query = "SELECT `LastName`,`FirstName`,`Patronymic`,`NickName`,`Telephone`,`Email`,`UserTypeText` FROM `users`, `usertype` WHERE `users`.`UserID` = '".$_COOKIE['UserID']."' and `usertype`.`UserTypeID` = `users`.`UserTypeID`";
			$userInfo = mysql_fetch_array(mysql_query($query));
			$fields = array(1 => 'Имя', 0 => 'Фамилия', 2 => 'Отчество', 3 => 'Логин', 4 => 'Телефон', 5 => 'Почтовый ящик', 6 => 'Роль пользователя');
			for($i=0;$i<count($fields);$i++):
				?>
				<tr>
					<td class="propertyName"><?php echo $fields[$i];?></td>
					<td class="propertyValue"><?php echo $userInfo[$i]?></td>
				</tr>
				<?php
			endfor;
			?>
		</table>
		<?php
		if ($userInfo['UserTypeText']==='Клиент') :
			$query = "SELECT `INN`, `KPP`, `CorAccount`, `CurAccount`, `BIK`, `BankName`, `ContractNumber`, `DateOfSigning`, `StartContractDate`, `EndContractDate` FROM `requizitsforuser`, `requizit` WHERE `requizit`.`RequizitID` = `requizitsforuser`.`RequizitID` and `requizitsforuser`.`UserID` = '".$_COOKIE['UserID']."'";
			$fieldsRequizit = array(0 => 'ИНН', 1 => 'КПП', 2 => 'Корреспондентский счет', 3 => 'Расчетный счет', 4 => 'БИК', 5 => 'Банк', 6 => 'Номер контракта', 7 => 'Дата подписания', 8 => 'Дата начала действия контракта', 9 => 'Дата завершения действия контракта');
			$req=0;
			$result = mysql_query($query);
			$row = array();
			while($row[$req] = mysql_fetch_array($result)):
				?>
				<table class="clientRequizitData View">
					<tr>
						<th colspan=2>
							Реквизиты - <?php echo ($req+1);?>
						</th>
					</tr>
					<?php
					for($i=0;$i<count($fieldsRequizit);$i++):
						?>
						<tr>
							<td class="propertyName"><?php echo $fieldsRequizit[$i];?></td>
							<td class="propertyValue"><?php echo $row[$req][$i]?></td>
						</tr>
						<?php
					endfor;
					?>
				</table>
				<?php
				$req++;
			endwhile;
		endif;
		mysql_close();
		?>
		<button class="edit-view" onclick="startEditing();">редактировать</button>
	</div>
	<div id="profileEdit" hidden>
		<div id="content-document-menu">
			<ul>
				<li class="selected">
					<a>Основные данные</a>
				</li>
				<li>
					<a>Логин и пароль</a>
				</li>
				<li>
					<a>Реквизиты</a>
				</li>
			</ul>
		</div>
		<div class="field">
			<?php
			$fields = array(1 => 'Имя', 0 => 'Фамилия', 2 => 'Отчество',3 => 'Логин', 4 => 'Телефон', 5 => 'Почтовый ящик');
			?>
			<table class="userMainData Edit">
				<tr>
					<th colspan=2>
						Редактирование общих данных
					</th>
				</tr>
				<?php
				for($i=0;$i<count($fields);$i++):
					if ($i===3) continue;
					?>
					<tr>
						<td class="propertyName"><?php echo $fields[$i];?></td>
						<td class="propertyValue"><input type="text" value="<?php echo $userInfo[$i]?>"></td>
					</tr>
					<?php
				endfor;
				?>
			</table>
		</div>
		<div class="field">
			<table class="userLogin Edit">
				<tr>
					<th colspan=2>
						Смена логина
					</th>
				</tr>
				<tr>
					<td class="propertyName"><?php echo $fields[3];?></td>
					<td class="propertyValue"><input type="text" value="<?php echo $userInfo[3]?>"></td>
				</tr>
				<tr>
					<td class="propertyName">пароль</td>
					<td class="propertyValue"><input type="password"></td>
				</tr>
			</table>
			<table class="userPassword Edit">
				<tr>
					<th colspan=2>
						Смена пароля
					</th>
				</tr>
				<tr>
					<td class="propertyName">новый пароль</td>
					<td class="propertyValue"><input type="password"></td>
				</tr>
				<tr>
					<td class="propertyName">подтверждение пароля</td>
					<td class="propertyValue"><input type="password"></td>
				</tr>
			</table>
		</div>
		<div class="field">
			<?php
			for($i=0;$i<$req;$i++):
				?>
				<table class="clientRequizitData Edit">
					<tr>
						<th colspan=2>
							Реквизиты - <?php echo ($i+1);?>
						</th>
					</tr>
					<?php
					for($j=0;$j<count($fieldsRequizit);$j++):
						?>
						<tr>
							<td class="propertyName"><?php echo $fieldsRequizit[$j];?></td>
							<td class="propertyValue"><input type="text" value="<?php echo $row[$i][$j]?>"></td>
						</tr>
						<?php
					endfor;
					?>
				</table>
				<?php
			endfor;
			?>
		</div>
		<button class="edit-view" onclick="confirmChanges()">подтвердить изменения</button>
		<button class="edit-view" onclick="discardChanges()">отмена</button>
	</div>
</div>