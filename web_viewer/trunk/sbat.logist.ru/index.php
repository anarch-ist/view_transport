<!DOCTYPE html>
<html>
	<title>логистика</title>
	<head>
		<link rel="stylesheet" type="text/css" href="common_files/media/css/jquery.dataTables.min.css">
		<link rel="stylesheet" type="text/css" href="common_files/media/css/jquery-ui.min.css">
		<link rel="stylesheet" type="text/css" href="/css/style.css">

		<script type="text/javascript" language="javascript" src="common_files/media/js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="common_files/media/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" language="javascript" src="common_files/media/js/jquery-ui.min.js"></script>

		<script type="text/javascript" language="javascript" src="/js/datatables.init.js"></script>
		<script type="text/javascript" language="javascript" src="/js/index.js"></script>


	</head>
	<body>

		<div class="container">
            <div id="menu">



                <label for="tableTypeSelect">Выберите таблицу:</label>
                <select name="tableType" id="tableTypeSelect">
                    <option selected="selected">Текущие статусы</option>
                    <option>История статусов</option>
                    <option>Маршрутные листы</option>
                </select>

                <div id="buttonsContainer" style="display: inline-block">
                    <button id="selectColumnsControl">
                        выбрать колонки
                    </button>
                    <button>
                        Изменить статус накладной
                    </button>
                    <button>
                        Изменить статус МЛ
                    </button>
                    <button>
                        Сброс фильтров
                    </button>
                </div>





            </div>
			<table id="user-grid"  cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
					<thead>
						<tr>
							<th>номер заявки</th>
							<th>номер вн. заявки</th>
							<th>номер накладной</th>
							<th>инн</th>
							<th>пункт прибытия</th>
							<th>пункт отправки</th>
							<th>фамилия</th>
							<th>статус накладной</th>
							<th>кол-во коробок</th>
							<th>водитель</th>
							<th>гос. номер</th>
							<th>палеты</th>
							<th>номер марш. листа</th>
							<th>направление</th>
                            <th>пункт текущий</th>
                            <th>пункт следующий</th>
							<th>прибытие</th>
						</tr>
					</thead>
			</table>
		</div>
	</body>
</html>
