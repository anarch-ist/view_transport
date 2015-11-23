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
							<th>ID заявки</th>
							<th>ID внутренней заявки</th>
							<th>ID накладной</th>
							<th>Клиент, ИНН</th>
							<th>Пункт доставки</th>
							<th>Склад отправки</th>
							<th>Торговый представитель</th>
							<th>Статус</th>
							<th>Количество коробок</th>
							<th>Водитель</th>
							<th>Номер ТС</th>
							<th>Количество паллет</th>
							<th>Маршрутный лист</th>
							<th>Направление</th>
                            <th>Текущее подразделение</th>
                            <th>Следующий пункт маршрута</th>
							<th>Плановое время прибытия в следующий пункт</th>
						</tr>
					</thead>
			</table>
		</div>
	</body>
</html>
