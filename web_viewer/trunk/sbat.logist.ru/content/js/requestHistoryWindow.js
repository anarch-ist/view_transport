function showPretension(pretensionID, reqIdExt, cathegory, pretensionSum, pretensionComment, positionNumber) {

    $('#editPretensionSum').val(pretensionSum);
    $('#editPretensionCathegory').val(decodeURI(cathegory)).trigger('change');
    $('#editPretensionComment').val(decodeURI(pretensionComment));
    $('#editPretensionPositionNumber').val(decodeURI(positionNumber));
    $('#updatePretension').off('click').show().on('click', function () {
        updatePretension(pretensionID, reqIdExt, cathegory, pretensionSum, decodeURI(pretensionComment), decodeURI(positionNumber))
    });
    $('#deletePretension').off('click').show().on('click', function () {
        deletePretension(pretensionID, reqIdExt);
    });
}

function loadPretensions() {
    $.post("content/getData.php", {
        status: 'getPretensions',
        requestIDExternal: requestIDExternal
    }, function (data) {
        parsedData = JSON.parse(data);

        $(".pretensionLink").remove();

        parsedData.forEach(function (pretension, i, parsedData) {
            if (pretension.sum === undefined) {
                pretension.sum = 0;
            }

            $('#pretensionLinks').append(
                // "<br>",
                $('<button>', {
                    text: 'Претензия №' + pretension.pretensionID,
                    click: function () {
                        showPretension(
                            pretension.pretensionID,
                            pretension.requestIDExternal,
                            pretension.pretensionCathegory,
                            parseFloat(pretension.sum),
                            encodeURI(pretension.pretensionComment),
                            pretension.positionNumber)
                    }
                }).addClass("pretensionLink btn-link")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#editPretensionModal")
            );
        });

    })
}

function deletePretension(pretensionID, reqIdExt) {
    if (confirm('Вы действительно хотите удалить претензию?')) {
        $.post("content/getData.php", {
            status: 'deletePretension',
            pretensionID: pretensionID,
            requestIDExternal: reqIdExt
        }, function (data) {
            if (data == 'true') {
                alert("Претензия успешно удалена");
                $('#editPretensionModal').modal('toggle');
                loadPretensions();
            }
        })
    }
}

function updatePretension(pretensionID, reqIdExt) {
    $.post("content/getData.php", {
        status: 'updatePretension',
        commentRequired: $('#editPretensionComment').prop('required'),
        pretensionID: pretensionID,
        requestIDExternal: reqIdExt,
        pretensionCathegory: $('#editPretensionCathegory').val(),
        pretensionSum: $('#editPretensionSum').val(),
        pretensionComment: $('#editPretensionComment').val(),
        pretensionPositionNumber: $('#editPretensionPositionNumber').val()
    }, function (data) {
        if (data === 'true') {
            alert('Претензия успешно обновлена');
            $('#editPretensionModal').modal('toggle');
            loadPretensions();
        }
    });

}


$(document).ready(function () {


    $("#editPretensionModal").on('hidden.bs.modal', function () {
        $('#editPretensionCathegory').val('');
        $('#editPretensionSum').val('');
        $('#editPretensionComment').val('');
        $('#editPretensionPositionNumber').val('').trigger('change');
    });


    $(document).on('change', '.pretensionCathegory', function () {
        let pretensionSum = $(this).closest('.modal-body').find('.pretensionSum');
        let pretensionComment = $(this).closest('.modal-body').find('.pretensionComment');
        let pretensionCommentLabel = $(this).closest('.modal-body').find("label[for = pretensionComment]");
        switch ($(this).val()) {
            case 'Пересорт': {
                pretensionSum
                    .prop('disabled', true)
                    .attr('placeholder', '')
                    .val('');

                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий');
                break;
            }
            case 'Брак': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .attr('placeholder', 'Пожалуйста, опишите повреждение/неисправность')
                    .prop('required', true);
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий<span style="color:#c22929;">*</span>');
                break;
            }
            case 'Недовоз': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий<br><span style="color:#c22929;">Пожалуйста, напишите, что именно не довезли и в каком количестве</span>');
                break;
            }
            case 'Отказ': {
                pretensionSum
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                pretensionComment
                    .prop('required', false)
                    .attr('placeholder', 'Пожалуйста, укажите причину отказа');
                // Some browsers don't support "required" fields. This is a workaround
                pretensionCommentLabel.html('Комментарий');
                break;
            }
        }
    });



});

// var requestIDExternal = '0';
$(window).on('load', function () {
    var $_GET = {};


    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });

    // $('#deletePretension').hide();
    $('.pretensionSum').mask('0000000000.00', {reverse: true});
    // $('#editPretensionSum').mask('0000000000.00', {reverse: true});

    var timeoutID;

    if ($_GET['requestIDExternal']) {
        requestIDExternal = $_GET['requestIDExternal'];
    }


    function setHtml(html, callback) {
        document.write(html);
        if (window.jQuery) {
            callback();
        } else if (!(window.jQuery)) {

            timeoutID = window.setTimeout(function () {
                    callback()
                }
                , 2000
            );
        }
    }


    if ($_GET['clientId'] && $_GET['invoiceNumber']) {
        //Why wasn't chicken able to cross the road?
        //Because it was disabled :|
        // console.log('fgs');
        $.post("content/getData.php", {
                status: 'getRequestByClientIdAndInvoiceNumber',
                clientId: $_GET['clientId'],
                invoiceNumber: $_GET['invoiceNumber']
            },
            function (data) {
                requestIDExternal = JSON.parse(data).requestIDExternal;
                setRequestInfo(data);
                loadPretensions();

            }
        ).success(function () {
            $.post("content/getData.php", {
                    status: 'getStatusHistory',
                    requestIDExternal: requestIDExternal
                },
                function (data) {

                    setHistoryTable(data);
                    removeLoadingScreen();

                    if ($_GET['pretensionModal'] === "1") {
                        $('#pretensionModal').modal();
                    }
                }
            )
        });

    } else {
        // setNoData();
        // document.getElementsByClassName('loading')[0].innerHTML="У заявки отсутствует номер накладной";
        removeLoadingScreen("noData");
        $(".loading").html("У заявки отсутствует номер накладной");
    }


    var $requestHistoryDialogTable;
    var requestHistoryDialogTable;

    function setRequestInfo(requestData) {
        requestData = JSON.parse(requestData);

        for (var i = 0; i < requestData.length; i++) {
            requestData[i].fullName = data[i].userName;
            delete requestData[i].userName;
        }
        $('#request-date').html(requestData.requestDate.split(' ')[0]);
        $('#invoice-number').html(requestData.invoiceNumber);
        $('#invoice-date').html(requestData.invoiceDate.split(' ')[0]);
        $('#document-number').html(requestData.documentNumber);
        $('#document-date').html(requestData.documentDate.split(' ')[0]);
        $('#organization').html(requestData.firma);
        $('#comments').html(requestData.commentForStatus);
        $('#box-quantity').html(requestData.boxQty);
        $('#client-INN').html(requestData.clientName + ' ' + requestData.INN);
        $('#sales-representative').html(requestData.marketAgentUserName);
        $('#arrival-point').html(requestData.deliveryPointName);
        $('#departure-warehouse').html(requestData.warehousePointName);
        $('#pallet-quantity').html(requestData.palletsQty);


        $('#submitPretension').click(function () {

            $.post("content/getData.php", {
                commentRequired: $('#pretensionComment').prop('required'),
                status: 'addPretension',
                pretensionCathegory: $('#pretensionCathegory').val(),
                pretensionStatus: 'OPENED',
                pretensionSum: $('#pretensionSum').val(),
                pretensionPositionNumber: $('#pretensionPositionNumber').val(),
                requestIDExternal: requestData.requestIDExternal,
                pretensionComment: $('#pretensionComment').val(),
                linkToRequest: window.location.href.replace('&pretensionModal=1', '').replace(/(md5=.{32}&)/, '').replace(/(&md5=.{32})/, ''),
                invoiceNumber: $_GET['invoiceNumber']
            }, function (data) {
                if (data == 'true') {
                    $('#pretensionModal').modal('toggle');
                    alert('Претензия успешно отправлена');
                    loadPretensions();
                } else {
                    alert(data);
                }
            })
        });

    }


    function setHistoryTable(requestHistoryData) {
        if (window.jQuery) {
            $requestHistoryDialogTable = $("#requestHistoryDialogTable");
            requestHistoryDialogTable = $requestHistoryDialogTable.DataTable({
                "dom": 't', // show only table with no decorations
                "paging": false, // no pagination
                "order": [[0, "desc"]],
                "columnDefs": [
                    {"name": "timeMarkWhenRequestWasChanged", "data": "timeMarkWhenRequestWasChanged", "targets": 0},
                    {"name": "pointWhereStatusWasChanged", "data": "pointWhereStatusWasChanged", "targets": 1},
                    // {"name": "userNameThatChangedStatus", "data": "userNameThatChangedStatus", "targets": 2, visible: false},
                    {"name": "requestStatusRusName", "data": "requestStatusRusName", "targets": 2},
                    // {"name": "routeListNumber", "data": "routeListNumber", "targets": 4, visible: false},
                    // {"name": "boxQty", "data": "boxQty", "targets": 5, visible: false}
                ]
            });
            (function showRequestHistory(data) {
                data = JSON.parse(data);
                for (var i = 0; i < data.length; i++) {
                    data[i].fullName = data[i].userName;
                    delete data[i].userName;
                }
                requestHistoryDialogTable.rows().remove();
                requestHistoryDialogTable.rows.add(data).draw(false);
            })(requestHistoryData);


        } else {
            timeoutID = window.setTimeout(function () {
                setHistoryTable();
            }, 1000)
        }
    }

    function removeLoadingScreen(noData) {
        if (!noData) {
            $(".loading").remove();
            $(".left-table").show();
            $(".right-table").show();
        } else {
            $(".loading").html("У заявки отсутствует номер накладной");
        }


    }


}());




