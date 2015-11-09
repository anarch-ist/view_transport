		<div id="content">
			<div id="head-content">
				<h2><?php if(isset($_REQUEST['edit'])) echo "Редактирование пункта"; else echo "Создание пункта";?></h2>
			</div>
			<div id="content-point">
<?php
	class Point {
		public $name;
		public $pointType;
		public $region;
		public $district;
		public $locality;
		public $address;
		public $mailIndex;
		public $email;
		public $telephone;
		public $dispatchers;
		function __construct() {
			$this->name='';
			$this->pointType='';
			$this->region='';
			$this->district='';
			$this->locality='';
			$this->address='';
			$this->mailIndex='';
			$this->email='';
			$this->telephone='';
			$this->dispatchers = array();
		}
		function setData($data) {
			$this->name=$data['PointName'];
			$this->pointType=$data['PointTypeID'];
			$this->region=$data['Region'];
			$this->district=$data['District'];
			$this->locality=$data['Locality'];
			$this->address=$data['Address'];
			$this->mailIndex=$data['MailIndex'];
			$this->email=$data['Email'];
			$this->telephone=$data['Telephone'];
		}
		function addDispatcher($dispatcher) {
			$i = count($this->dispatchers);
			$this->dispatchers[$i]['UserID']=$dispatcher['UserID'];
			$this->dispatchers[$i]['UserName']=$dispatcher['UserName'];
		}
	}
	$point = new Point();
	if (isset($_REQUEST['PointID'])) {
		mysql_connect($mysqlparams['server'],$mysqlparams['user'],$mysqlparams['password']);
		mysql_select_db($mysqlparams['database']);
		$query="SELECT `point`.* from `point` where `point`.`PointID` = '".$_REQUEST['PointID']."';";
		$result=mysql_fetch_array(mysql_query($query));
		$point->setData($result);
		$query="SELECT `users`.`UserID`, concat_ws(' ', `users`.`LastName`,`users`.`FirstName`,`users`.`Patronymic`) as `UserName` FROM `users` WHERE `users`.`UserID` IN (SELECT `dispatchersforpoint`.`UserID` FROM `dispatchersforpoint` WHERE `dispatchersforpoint`.`PointID`='".$_REQUEST['PointID']."');";
		$result = mysql_query($query);
		while ($row = mysql_fetch_array($result)) {
			$point->addDispatcher($row);
		}
		mysql_close();
	}
?>
<script type="text/javascript">
	button = null;
	Information = function() {
		this.getSendingString = function() {
			var str = '';
			for (i in this) {
				if (typeof this[i] === 'string') {
					if (this[i]==='') {
						continue;
					}
					if (str!=='') {
						str+='&';
					}
					str+=i+'='+this[i];
				} else if (typeof this[i] === 'object') {
					for (j=0;j<this[i].length;j++) {
						if (str!=='') {
							str+='&';
						}
						str+=i+'[]='+this[i][j];
					}
				}
			}
			return str;
		}
		this.setInformation = function() {
			for (i=0;i<10;i++) {
				formElement = document.getElementsByTagName('label')[i].firstChild.nextSibling;
				if (formElement.nodeName === 'INPUT') {
					this[formElement.name] = formElement.value;
				} else if (formElement.nodeName === 'SELECT') {
					if (!formElement['multiple']) {
						this[formElement.name] = formElement.value;
					}
					else {
						selectedElems = new Array();
						opt = formElement.getElementsByTagName('option');
						for (j=0;j<opt.length;j++) {
							if (opt[j].selected) {
								selectedElems[selectedElems.length] = opt[j].value;
							}
						}
						this[formElement.name] = selectedElems;
					}
				}
			}
		}
	}
	DOMmultiple = null;	
	window.onload=function() {
		DOMmultiple = new function() {
			var base = this;
			this.DOMobject = document.getElementsByTagName('select')[1];
			this.eventInitiator = document.getElementsByTagName('select')[0];
			this.eventInitiator.onchange = function() {
				base.changeOptions();
			}
			this.changeOptions = function() {
				request = new XMLHttpRequest();
				this.eventInitiator.disabled="disabled";
				request.open('post', 'documents.php', true);
				request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				request.send('pointType='+base.eventInitiator.value+'&status=getFreeUsers');
				request.onreadystatechange = function() {
					if (this.readyState!=4) return;
					base.eventInitiator.removeAttribute('disabled');
					var incomeInfo = JSON.parse(this.responseText);
					if (incomeInfo) {
						for (i =0;i < incomeInfo.length; i++) {
							elem = document.createElement('option');
							elem.value = incomeInfo[i].value;
							elem.textContent = incomeInfo[i].text;
							base.DOMobject.appendChild(elem);
						}
					}
<?php
	echo "							if (base.eventInitiator.value === '".$point->pointType."') {\n";	
	foreach($point->dispatchers as $key => $value) {
		echo "								elem = document.createElement('option');\n";
		echo "								elem.selected=true;\n";
		echo "								elem.value = '".$value['UserID']."';\n";
		echo "								elem.textContent = '".$value['UserName']."';\n";
		echo "								base.DOMobject.appendChild(elem);\n";
	}
	echo "							}";
?>
				}
				while (base.DOMobject.children.length>0) {
					base.DOMobject.removeChild(base.DOMobject.children[0]);
				}
			}
			this.getRoot = function() {
				return this.DOMobject;
			}
		}
		DOMmultiple.changeOptions();
		button = document.getElementById('button-submit').getElementsByTagName('button')[0];
		button.onclick=function(obj, event) {
			if (!(isEmpty(document.getElementsByName('name')[0], true)||isEmpty(document.getElementsByName('locality')[0], true)||isEmpty(document.getElementsByName('address')[0], true)||!isOneOf([document.getElementsByName('telephone')[0],document.getElementsByName('email')[0]]))) {
				request = new XMLHttpRequest();
				info = new Information();
				request.open('post', 'documents.php', true);
				request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				info.setInformation();
				request.send(info.getSendingString()+'&status=<?php if(isset($_REQUEST['edit'])) echo 'editData&PointID='.$_REQUEST['PointID']; else echo "addNewData";?>');
				request.onreadystatechange = function() {
					if (this.readyState!=4) return;
					DOMmultiple.changeOptions();
					console.log(this.responseText);
					location.replace('<?php if(isset($_REQUEST['edit'])) echo "../points/"; else echo "../../";?>');
				}
			}
			cancelMainButtonEvent(event);
		}
		setFocusFor(document.getElementById('labels').getElementsByTagName('input'),'text');
	}
</script>
				<form action="./documents.php" method="post">
					<fieldset id="labels">
						<div class="formElement">
							<label><span>название:</span><input type="text" name="name" placeholder="обязательное поле" value="<?php echo $point->name; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>тип:</span><select size="1" name="pointType">
								<option value="0101" <?php if ($point->pointType == '0101') echo 'selected'?>>Склад</option>
								<option value="0102" <?php if ($point->pointType == '0102') echo 'selected'?>>Клиент</option>
								<option value="0103" <?php if ($point->pointType == '0103') echo 'selected'?>>Торговое представительство</option>
							</select></label>
						</div>
						<div class="formElement">
							<label><span>субъект РФ:</span><input type="text" name="region"  placeholder="область, край и т.д." value="<?php echo $point->region; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>район:</span><input type="text" name="district" value="<?php echo $point->district; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>населенный пункт:</span><input type="text" name="locality"  placeholder="например, город Москва" value="<?php echo $point->locality; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>адрес:</span><input type="text" name="address" placeholder="обязательное поле" value="<?php echo $point->address; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>индекс:</span><input type="text" maxlength="6" id="mailIndex" name="mailIndex" onkeyup="this.value=parseInt(this.value) | '';" value="<?php echo $point->mailIndex; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>эл. почта:</span><input type="text" name="email" value="<?php echo $point->email; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>телефон:</span><input type="text" name="telephone" value="<?php echo $point->telephone; ?>"></label>
						</div>
						<div class="formElement">
							<label><span>ответственный:</span><select size="3" name="dispatcher" multiple></select>
						</div>
						<div id="button-submit">
							<button><?php if(isset($_REQUEST['edit'])) echo "Изменить"; else echo "Создать";?></button>
						</div>
					</fieldset>
				</form>
			</div>
			<div id="end-of-content">
			</div>
		</div>