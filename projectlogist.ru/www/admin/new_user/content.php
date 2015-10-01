		<div id="content">
			<div id="head-content">
				<h2><?php if(isset($_REQUEST['edit'])) echo "Редактирование пользователя"; else echo "Создание новых пользователей"?></h2>
			</div>
<script type="text/javascript">
	DOMobject = null;
	var requizitsAndFields = new function() {
		this.requizitInformationList = new Array();
		this.requizitDOMobjectsList = new Array();
		var count = 0;
		this.count = function() {
			return count;
		}
		this.add = function() {
			this.requizitInformationList[this.count()];
			this.requizitDOMobjectsList[this.count()] = new RequizitDOMObject();
			count++;
		}
		this.removeAll = function() {
			this.requizitInformationList = new Array();
			for (i=0;i<this.count();i++) {
				this.requizitDOMobjectsList[i].field.remove();
			}
			this.requizitDOMobjectsList = new Array();
			count = 0;
		}
	}
	RequizitDOMObject = function () {
		this.field = DOMobject.form.insertBefore(document.createElement('fieldset'),DOMobject.confirm.parentNode);
		var names = [{'name':'INN', 'type':'input', 'text':'ИНН'}, {'name':'KPP', 'type':'input', 'text':'КПП'}, {'name':'CorAccount', 'type':'input', 'text':'Корреспондентский счет'}, {'name':'CurAccount', 'type':'input', 'text':'Расчетный счет'}, {'name':'BIK', 'type':'input', 'text':'БИК'}, {'name':'BankName', 'type':'input', 'text':'Название банка'}, {'name':'ContractNumber', 'type':'input', 'text':'Номер контракта'}, {'name':'DateOfSigning', 'type':'date', 'text':'Дата подписания'}, {'name':'StartContractDate', 'type':'date', 'text':'Дата начала контракта'}, {'name':'EndContractDate', 'type':'date', 'text':'Дата завершения контракта'}];
		for (var i=0;i<names.length;i++) {
			var div = document.createElement('div');
			div.className = 'formElement';
			this.field.appendChild(div);
			var label = div.appendChild(document.createElement('label'));
			var span = div.firstChild.appendChild(document.createElement('span'));
			span.textContent = names[i].text+':';
			if (names[i].type === 'input') {
				label.appendChild(document.createElement('input')).type='text';
			} else if (names[i].type === 'date') {
				var input = label.appendChild(document.createElement('input'));
				input.className='datepicker';
				input.type='text';
			} else {
				label.appendChild(document.createElement('input')).type='text';
			}
		}
		Protoplasm.use('datepicker').transform('input.datepicker', {'locale':'ru_RU'})
		this.remove = function() {
			this.field.parentNode.removeChild(this.field);
		}
	}
	Protoplasm.use('datepicker').transform('input.datepicker', {'locale':'ru_RU'})
	window.onload=function() {
		DOMobject = new function() {
			var base = this;

			this.form = document.getElementsByTagName('form')[0];
			this.form.getUserValues = function() {
				mas = new Object();
				elements = this.getElementsByTagName('input');
				for(i=0;i<elements.length;i++) {
					console.log(elements[i].name);
					console.log(elements[i].value);
					mas[elements[i].name] = elements[i].value;
				}
				elements = this.getElementsByTagName('select');
				for(i=0;i<elements.length;i++) {
					console.log(elements[i].name);
					console.log(elements[i].value);
					mas[elements[i].name] = elements[i].value;
				}
				return mas;
			}

			this.confirm = this.form.getElementsByTagName('button')[1];
			this.userTypeSelect = this.form.getElementsByTagName('select')[0];
			this.requizitsButton = this.form.getElementsByTagName('button')[0];
			///////methods
			this.userTypeSelect.onchange = function() {
				if (this.value === '0004') {
					base.requizitsButton.parentNode.removeAttribute('hidden');
				}
				else {
					base.requizitsButton.parentNode.hidden = true;
					requizitsAndFields.removeAll();
				}
			}

			this.confirm.onclick = function(obj, event) {
				params = 'Status=addNewUser'
				elements = base.form.getUserValues();
				for(key in elements) {
					value = elements[key];
					params+='&'+key+'='+value;
				}


				if (!(isEmpty(document.getElementsByName('LastName')[0], true) || isEmpty(document.getElementsByName('FirstName')[0], true) || !isEqual(document.getElementsByName('Pass')[0], document.getElementsByName('PassConfirm')[0], true) || !isOneOf([document.getElementsByName('Telephone')[0], document.getElementsByName('Email')[0]]))) {
					var request = new XMLHttpRequest();
					request.open('post', 'documents.php', true);
					request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
					request.send(params);
					request.onreadystatechange = function() {
						if (request.readyState!=4) return;
						var result = JSON.parse(this.responseText);
						console.log(result);
						changeRouteName(result.routeName);
						for (var i=0;i<result.points.length;i++) {
							DOMPointObjects.addNewRoutePoint(result.points[i]);
						}
					}
				}
				cancelMainButtonEvent(event);
			}
			this.requizitsButton.onclick = function(obj, event) {
				requizitsAndFields.add();
				cancelMainButtonEvent(event);
			}
		}
		setFocusFor(document.getElementById('labels').getElementsByTagName('input'),'text');
		setFocusFor(document.getElementById('labels').getElementsByTagName('input'),'password');
	}
</script>
			<div id="head-body">
				<div id="user-form">
					<form action="./documents.php" method="post">
						<fieldset id="labels">
							<div class="formElement">
								<label><span>Тип пользователя:</span><select name="UserTypeID">
									<option value="0004" selected>Клиент
									<option value="0003">Торговый представитель
									<option value="0002">Диспетчер
									<option value="0001">Администратор
								</select></label>
							</div>
							<div class="formElement">
								<label><span>Фамилия:</span><input type="text" name="LastName" placeholder="обязательное поле"></label>
							</div>
							<div class="formElement">
								<label><span>Имя:</span><input type="text" name="FirstName" placeholder="обязательное поле"></label>
							</div>
							<div class="formElement">
								<label><span>Отчество:</span><input type="text" name="Patronymic"></label>
							</div>
							<div class="formElement">
								<label><span>Пароль:</span><input type="password" name="Pass"></label>
							</div>
							<div class="formElement">
								<label><span>Пароль еще раз:</span><input type="password" name="PassConfirm"></label>
							</div>
							<div class="formElement">
								<label><span>Телефон:</span><input type="text" name="Telephone"></label>
							</div>
							<div class="formElement">
								<label><span>E-mail:</span><input type="text" name="Email"></label>
							</div>
							<div class="formElement">
								<button>добавить реквизиты</button>
							</div>							
						</fieldset>
						<div class="formElement">
							<button>создать</button>
						</div>
					</form>
				</div>
				<div id="end-of-content-body"></div>
			</div>
			<div id="end-of-content"></div>
		</div>