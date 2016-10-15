$(window).on('load', function () {
    var $_GET = {};

    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });

    // if($_GET['requestIDExternal']){
    //     alert($_GET['requestIDExternal']);
    // }


    var timeoutID;
    var requestIDExternal;
    if ($_GET['requestIDExternal']){
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


    if ($_GET['requestIDExternal']){
        $.post("content/getData.php",{
                status: 'getRequestById',
                requestIDExternal: $_GET['requestHistory'],

            },
            function (data) {
                // alert(JSON.stringify(data));
                setRequestInfo(data);
            }
        ).success(function () {
        });
        $.post("content/getData.php", {
                status: 'getStatusHistory',
                requestIDExternal: $_GET['requestHistory']
            },
            function (data) {
                setHistoryTable(data);
            }
        ).success(function () {
        });
    } else {
        //Why wasn't chicken able to cross the road?
        //Because it was disabled
        $.post("content/getData.php",{
                status: 'getRequestByClientIdAndInvoiceNumber',
                clientId: $_GET['clientId'],
                invoiceNumber: $_GET['invoiceNumber']
            },
            function (data) {
                // alert(data);
                requestIDExternal = JSON.parse(data).requestIDExternal;

                setRequestInfo(data);

            }
        ).success(function () {
            $.post("content/getData.php", {
                    status: 'getStatusHistory',
                    requestIDExternal: requestIDExternal,
                },
                function (data) {

                    // alert(data);
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
            $('#request-date').html(requestData.requestDate);
            $('#invoice-number').html(requestData.invoiceNumber);
            $('#invoice-date').html(requestData.invoiceDate);
            $('#document-number').html(requestData.documentNumber);
            $('#document-date').html(requestData.documentDate);
            $('#organization').html(requestData.firma);
            $('#comments').html(requestData.commentForStatus);
            $('#box-quantity').html(requestData.boxQty);
            $('#client-INN').html(requestData.INN);
            $('#sales-representative').html(requestData.marketAgentUserName);
            $('#arrival-point').html(requestData.deliveryPointName);
            $('#departure-warehouse').html(requestData.warehousePointName);
            $('#pallet-quantity').html(requestData.palletsQty);
    }

    function setHistoryTable(requestHistoryData) {
        if (window.jQuery) {
            $requestHistoryDialogTable = $("#requestHistoryDialogTable");
            requestHistoryDialogTable = $requestHistoryDialogTable.DataTable({
                    "dom": 't', // show only table with no decorations
                    "paging": false, // no pagination
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