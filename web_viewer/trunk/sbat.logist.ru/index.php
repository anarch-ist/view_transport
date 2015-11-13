<!DOCTYPE html>
<html>
	<title>Datatable Demo1 | CoderExample</title>
	<head>
		<link rel="stylesheet" type="text/css" href="common_files/media/css/jquery.dataTables.min.css">
		<script type="text/javascript" language="javascript" src="common_files/media/js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="common_files/media/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" language="javascript" >
			$(document).ready(function() {
				var dataTable = $('#user-grid').DataTable( {
					"processing": true,
					"serverSide": true,
					"ajax":{
//						url :"employee-grid-data.php", // json datasource
						url :"AJAX.php?login=gfds@&md5=54321", // json datasource
						type: "post",  // method  , by default get
						error: function(){  // error handling
							$(".user-grid-error").html("");
							$("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
							$("#user-grid_processing").css("display","none");
							
						}
					}
				} );
			} );
		</script>
		<style>
			div.container {
			    margin: 0 auto;
			    max-width:760px;
			}
			div.header {
			    margin: 100px auto;
			    line-height:30px;
			    max-width:760px;
			}
			body {
			    background: #f7f7f7;
			    color: #333;
			    font: 90%/1.45em "Helvetica Neue",HelveticaNeue,Verdana,Arial,Helvetica,sans-serif;
			}
		</style>
	</head>
	<body>
		<div class="header"><h1>DataTable demo (Server side) in Php,Mysql and Ajax </h1></div>
		<div class="container">
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
							<th>лицензия?</th>
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
