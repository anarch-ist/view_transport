<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>История поставщика: <c:out value="${requestScope.supplierName}"/></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/DataTables-1.10.12/css/jquery.dataTables.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/media/custom/supplierHistoryPage/supplierHistory.css"/>">
    <script src="<c:url value="/media/jQuery-2.1.4/jquery-2.1.4.min.js"/>"></script>
    <script src="<c:url value="/media/DataTables-1.10.12/js/jquery.dataTables.min.js"/>"></script>
    <script src="<c:url value="/media/Moment-2.8.4/moment.js"/>"></script>
    <script src="<c:url value="/media/datetime-moment/datetime-moment.js"/>"></script>


    <script>
        $(document).ready(function () {
            "use strict";
            // BINDING SupplierDonuts.java

            $('#allDonutsForSupplierTable tfoot th').each(function () {
                var title = $(this).text();
                $(this).html('<input type="text" placeholder="Поиск ' + title + '" />');
            });

            $.fn.dataTable.moment( 'DD-MM-YYYY hh:mm' );

            var dataTable = $('#allDonutsForSupplierTable').DataTable({
                dom: "lrtip",
                columnDefs: [
                    { "orderable": false, "targets": 4 },
                    { "orderable": false, "targets": 5 }
                ],
                language: {
                    "select": {
                        "rows": {
                            "0": "Выделите запись",
                            "1": ""
                        }
                    },
                    "processing": "Подождите...",
                    "search": "Поиск:",
                    "lengthMenu": "Показать _MENU_ записей",
                    "info": "Записи с _START_ до _END_ из _TOTAL_ записей",
                    "infoEmpty": "Записи с 0 до 0 из 0 записей",
                    "infoFiltered": "(отфильтровано из _MAX_ записей)",
                    "infoPostFix": "",
                    "loadingRecords": "Загрузка записей...",
                    "zeroRecords": "Записи отсутствуют.",
                    "emptyTable": "В таблице отсутствуют данные",
                    "paginate": {
                        "first": "Первая",
                        "previous": "Предыдущая",
                        "next": "Следующая",
                        "last": "Последняя"
                    },
                    "aria": {
                        "sortAscending": ": активировать для сортировки столбца по возрастанию",
                        "sortDescending": ": активировать для сортировки столбца по убыванию"
                    }
                }
            });

            dataTable.columns().every(function () {
                var that = this;

                $('input', this.footer()).on('keyup change', function () {
                    if (that.search() !== this.value) {
                        that
                                .search(this.value)
                                .draw();
                    }
                });
            });
        });
    </script>
</head>
<body>
<c:set var="allDonutsForSupplier" scope="request" value="${requestScope.allDonutsForSupplier}"/>

<table id="allDonutsForSupplierTable" cellspacing="0" width="100%">
    <caption>Наименование поставщика: <br><span><c:out value="${requestScope.supplierName}"/></span></caption>
    <thead>
    <tr>
        <th>Начало периода</th>
        <th>Конец периода</th>
        <th>Склад</th>
        <th>Док</th>
        <th>Заявки</th>
        <th>Статусы заявок</th>
        <th>Комментарий</th>
        <th>Время последнего обновления</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="donut" items="${requestScope.allDonutsForSupplier}">
        <tr>
            <td>${donut.periodBegin}</td>
            <td>${donut.periodEnd}</td>
            <td>${donut.warehouseName}</td>
            <td>${donut.docName}</td>
            <td>${donut.orderNumbersAsString}</td>
            <td>${donut.orderStatusesAsString}</td>
            <td>${donut.comment}</td>
            <td>${donut.lastModified}</td>
        </tr>
    </c:forEach>
    </tbody>

    <tfoot>
    <tr>
        <th>Начало периода</th>
        <th>Конец периода</th>
        <th>Склад</th>
        <th>Док</th>
        <th>Заявки</th>
        <th>Статусы заявок</th>
        <th>Комментарий</th>
        <th>Время последнего обновления</th>
    </tr>
    </tfoot>
</table>





</body>
</html>
