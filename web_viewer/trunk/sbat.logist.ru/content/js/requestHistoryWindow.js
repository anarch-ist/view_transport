function showPretension(pretensionID, reqIdExt, cathegory, pretensionSum, pretensionComment, positionNumber) {
    // alert('showPretension begin');
    $('#pretensionSum').val(pretensionSum);
    $('#pretensionComment').val(decodeURI(pretensionComment));
    $('#pretensionPositionNumber').val(decodeURI(positionNumber));
    $('#pretensionCathegory').val(decodeURI(cathegory)).trigger('change');
    $('#submitPretension').hide();
    $('#updatePretension').off('click').show().on('click', function () {
        updatePretension(pretensionID, reqIdExt, cathegory, pretensionSum, decodeURI(pretensionComment), decodeURI(positionNumber))
    });
    $('#deletePretension').off('click').show().on('click', function () {
        deletePretension(pretensionID,reqIdExt);
    });

    // alert('showPretension end');
}

function deletePretension(pretensionID, reqIdExt) {
    // alert('Removing pretension №'+pretensionID+', '+reqIdExt);
    if(confirm('Вы действительно хотите удалить претензию?')){
        $.post("content/getData.php", {
            status: 'deletePretension',
            pretensionID: pretensionID,
            requestIDExternal: reqIdExt
        }, function (data) {
            if(data=='true'){
                alert("Претензия успешно удалена");
                location.reload();
            }
        })
    }
}

function updatePretension(pretensionID, reqIdExt) {
    $.post("content/getData.php", {
        status: 'updatePretension',
        commentRequired: $('#pretensionComment').prop('required'),
        pretensionID: pretensionID,
        requestIDExternal: reqIdExt,
        pretensionCathegory: $('#pretensionCathegory').val(),
        pretensionSum: $('#pretensionSum').val(),
        pretensionComment: $('#pretensionComment').val(),
        pretensionPositionNumber: $('#pretensionPositionNumber').val()
    }, function (data) {
        if(data=='true'){
            // alert(data);
            alert('Претензия успешно обновлена');
            location.reload();
        }
    });
    // alert('pretensionID:' + pretensionID + '<br>reqIdExt:' + reqIdExt + '<br>cathegory:' + cathegory + '<br>pretensionSum:' + pretensionSum + '<br>pretensionComment:' + pretensionComment + '<br>positionNumber:' + positionNumber)

}

$(document).ready(function(){
    $("#pretensionModal").on('hidden.bs.modal', function () {
        $('#pretensionCathegory').val('');
        $('#pretensionSum').val('');
        $('#pretensionComment').val('');
        $('#pretensionPositionNumber').val('').trigger('change');
    });

    $("#pretensionButton").on('click', function(){
        $('#updatePretension').hide();
        $('#deletePretension').hide();
        $('#submitPretension').show();
    });


});

$(window).on('load', function () {
    var $_GET = {};


    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });

    // $('#deletePretension').hide();
    $('#pretensionSum').mask('0000000000.00', {reverse: true});

    var timeoutID;
    var requestIDExternal;
    if ($_GET['requestIDExternal']) {
        requestIDExternal = $_GET['requestIDExternal'];
    }

    $(document).on('change', '#pretensionCathegory', function () {
        // alert($('#pretensionCathegory').val());
        switch ($('#pretensionCathegory').val()) {
            case 'Пересорт':
            {
                $("#pretensionSum")
                    .prop('disabled', true)
                    .attr('placeholder', '')
                    .val('');

                $("#pretensionComment")
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                $("label[for = pretensionComment]").html('Комментарий');
                break;
            }
            case 'Брак':
            {
                $("#pretensionSum")
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                $("#pretensionComment")
                    .attr('placeholder', 'Пожалуйста, опишите повреждение/неисправность')
                    .prop('required', true);
                // Some browsers don't support "required" fields. This is a workaround
                $("label[for = pretensionComment]").html('Комментарий<span style="color:#c22929;">*</span>');
                break;
            }
            case 'Недовоз':
            {
                $("#pretensionSum")
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                $("#pretensionComment")
                    .prop('required', false)
                    .attr('placeholder', '');
                // Some browsers don't support "required" fields. This is a workaround
                $("label[for = pretensionComment]").html('Комментарий<br><span style="color:#c22929;">Пожалуйста, напишите, что именно не довезли и в каком количестве</span>');
                break;
            }
            case 'Отказ':
            {
                $("#pretensionSum")
                    .prop('disabled', false)
                    .attr('placeholder', '1234.50');
                // alert("Отказ");
                $("#pretensionComment")
                    .prop('required', false)
                    .attr('placeholder', 'Пожалуйста, укажите причину отказа');
                // Some browsers don't support "required" fields. This is a workaround
                $("label[for = pretensionComment]").html('Комментарий');
                break;
            }
        }
    });

    // $('#pretensionCathegory').change(function () {
    //     alert();
    //     switch ($('#pretensionCathegory').val){
    //         case 'Пересорт':{
    //             break;
    //         }
    //         case 'Брак':{
    //             break;
    //         }
    //         case 'Недовоз':{
    //             break;
    //         }
    //         case 'Отказ':{
    //             alert("Отказ");
    //             $("#pretensionCathegory").prop('required',false);
    //             $('#pretensionComment').attr('placeholder','Пожалуйста, укажите причину отказа');
    //             break;
    //         }
    //     }
    // });

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


    // if ($_GET['requestIDExternal']){
    //     $.post("content/getData.php",{
    //             status: 'getRequestById',
    //             requestIDExternal: $_GET['requestHistory'],
    //
    //         },
    //         function (data) {
    //             // alert(JSON.stringify(data));
    //             setRequestInfo(data);
    //         }
    //     ).success(function () {
    //     });
    //     $.post("content/getData.php", {
    //             status: 'getStatusHistory',
    //             requestIDExternal: $_GET['requestHistory']
    //         },
    //         function (data) {
    //             setHistoryTable(data);
    //         }
    //     ).success(function () {
    //     });
    // } else
    if ($_GET['clientId'] && $_GET['invoiceNumber']) {
        //Why wasn't chicken able to cross the road?
        //Because it was disabled :|
        $.post("content/getData.php", {
                status: 'getRequestByClientIdAndInvoiceNumber',
                clientId: $_GET['clientId'],
                invoiceNumber: $_GET['invoiceNumber']
            },
            function (data) {
                // alert(data);
                // console.log(data);
                requestIDExternal = JSON.parse(data).requestIDExternal;

                setRequestInfo(data);

            }
        ).success(function () {
            $.post("content/getData.php", {
                    status: 'getStatusHistory',
                    requestIDExternal: requestIDExternal
                },
                function (data) {

                    // alert(data);
                    // console.log(data);
                    setHistoryTable(data);
                }
            )
        });

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


        if ($_GET['pretensionModal'] == "1") {
            $('#pretensionModal').modal();
            $('#deletePretension').hide();
            $('#updatePretension').hide();
        }

        $('#submitPretension').click(function () {

            $.post("content/getData.php", {
                commentRequired: $('#pretensionComment').prop('required'),
                status: 'addPretension',
                pretensionCathegory: $('#pretensionCathegory').val(),
                pretensionStatus: 'OPENED',
                pretensionSum: $('#pretensionSum').val(),
                pretensionPositionNumber: $('#pretensionPositionNumber').val(),
                requestIDExternal: requestData.requestIDExternal,
                pretensionComment: $('#pretensionComment').val()
            }, function (data) {
                if (data == 'true') {
                    $('#pretensionModal').modal('toggle');
                    alert('Претензия успешно отправлена');
                } else {
                    alert(data);
                }
            })
        });

        $.post("content/getData.php", {
            status: 'getPretensions',
            requestIDExternal: requestIDExternal
        }, function (data) {
            parsedData = JSON.parse(data);
            // alert(parsedData[1].pretensionSum);
            // alert(data,4);
            parsedData.forEach(function (pretension, i, parsedData) {
                if(pretension.pretensionSum==undefined){
                    pretension.pretensionSum=0;
                }

                $('#pretensionLinks').append('<br><button type="button" ' +
                    ' onclick=showPretension("'+
                    pretension.pretensionID + '","'+
                    pretension.requestIDExternal+'","'+
                    pretension.pretensionCathegory+'",'+
                    pretension.sum+',"'+
                    encodeURI(pretension.pretensionComment)+'","'+
                    pretension.positionNumber+
                    '") ' +
                    ' class="btn btn-link" data-toggle="modal" data-target="#pretensionModal">' +
                    'Претензия №' + pretension.pretensionID +
                    '</button>'
                );
            });

        })
    }



    

    function setHistoryTable(requestHistoryData) {
        if (window.jQuery) {
            $requestHistoryDialogTable = $("#requestHistoryDialogTable");
            requestHistoryDialogTable = $requestHistoryDialogTable.DataTable({
                "dom": 't', // show only table with no decorations
                "paging": false, // no pagination
                "order": [[0,"desc"]],
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


}());

