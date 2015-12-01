
$(document).ready(function () {

    $("#tableTypeSelect").selectmenu();

    // create selectColumnsControl
    $("#selectColumnsControl").on("click", function () {
        // create dialog here with selection content
    });

    $("#logout").on("click", function() {
        // delete auth cookies
        $.cookie('UserID', null);
        $.cookie('md5', null);
        // make redirect to login page
        window.location.reload();
        //location.reload(true);
    });

    // create div that will be dialog container
    $("body").append(
        '<div id="statusChangeDialog" title="Выбор нового статуса">' +
            '<label for="statusSelect">Новый статус: </label>' +
            '<select id="statusSelect"></select>' +'<br><br><br>'+
            '<label for="dateTimePickerInput">Дата и время: </label>' +
            '<input id="dateTimePicker" type="text">' +
        '</div>'
    );

    createDateTimePickerLocalization();
    var $dateTimePicker = $( "#dateTimePicker" );
    $dateTimePicker.datetimepicker();

    var $statusChangeDialog = $("#statusChangeDialog");
    $statusChangeDialog.dialog({
        autoOpen: false,
        resizable: true,
        height: 300,
        width: 400,
        modal: true,
        buttons: {
            "Сохранить": function () {
                //TODO get request and response for update invoice status
                $(this).dialog("close");
            },
            "Отмена": function () {
                $(this).dialog("close");
            }
        }
    });


    function showSelectInvoiceStatusDialog() {
        $statusChangeDialog.dialog("open");

        var options = [];
        var storedStatuses = JSON.parse(window.sessionStorage["USER_STATUSES"]);
        for (var i = 0; i < storedStatuses.length; i++) {
            options.push("<option value='" + storedStatuses[i] + "'>" + storedStatuses[i] + "</option>");
        }
        //append after populating all options
        var $statusSelect = $("#statusSelect");
        $statusSelect.html(options.join("")).selectmenu();
    }

    function createDateTimePickerLocalization() {
        $.datepicker.regional['ru'] = {
            closeText: 'Закрыть',
            prevText: '<Пред',
            nextText: 'След>',
            currentText: 'Сегодня',
            monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            dayNames: ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
            dayNamesShort: ['вск', 'пнд', 'втр', 'срд', 'чтв', 'птн', 'сбт'],
            dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
            weekHeader: 'Не',
            dateFormat: 'dd.mm.yy',
            firstDay: 1,
            isRTL: false,
            showMonthAfterYear: false,
            yearSuffix: ''
        };
        $.datepicker.setDefaults($.datepicker.regional['ru']);
        $.timepicker.regional['ru'] = {
            timeOnlyTitle: 'Выберите время',
            timeText: 'Время',
            hourText: 'Часы',
            minuteText: 'Минуты',
            secondText: 'Секунды',
            millisecText: 'Миллисекунды',
            timezoneText: 'Часовой пояс',
            currentText: 'Сейчас',
            closeText: 'Закрыть',
            timeFormat: 'HH:mm',
            amNames: ['AM', 'A'],
            pmNames: ['PM', 'P'],
            isRTL: false
        };
        $.timepicker.setDefaults($.timepicker.regional['ru']);
    }

    // --------DATATABLE INIT--------------

    var dataTable = $('#user-grid').DataTable( {
        "processing": true,
        "serverSide": true,
        select: {
            style: 'single'
        },
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'selectedSingle',
                text: 'изменить статус накладной',
                action: function ( e, dt, node, config ) {
                    showSelectInvoiceStatusDialog();
                }
            },
            {
                extend: 'selectedSingle',
                text: 'изменить статус МЛ',
                action: function ( e, dt, node, config ) {
                    alert( 'изм статус маршр листа' );
                }
            }
        ],
        "ajax":{
//						url :"employee-grid-data.php", // json datasource
            url :"content/getData.php", // json datasource
            type: "post",  // method  , by default get
            error: function(){  // error handling
                $(".user-grid-error").html("");
                $("#user-grid").append('<tbody class="user-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
                $("#user-grid_processing").css("display","none");

            }
        },
        "language": {
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
    } );
});