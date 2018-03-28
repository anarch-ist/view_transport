// var routeListIDExternal;
$(document).ready(function () {

    var timeoutID;

    const $_GET = {};
    // var myMap;


    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });

    if ($_GET['routeListHistory']) {
        routeListIDExternal = $_GET['routeListHistory'];

        $.post("content/getData.php", {
                status: 'getRouteListByRouteListIDExternal',
                routeListIDExternal: routeListIDExternal
            },
            function (data) {
                // console.log("routeListData:\n");
                // console.log(data);
                // console.log("\n\n");
                data = JSON.parse(data);
                // setRouteListsInfo(data);
                $('#routeList_Number').html(data.routeListNumber);
                $('#routeListsNumber').html(data.routeListNumber);
                $('#creationDate').html(data.creationDate);
                $('#departureDate').html(data.departureDate);
                $('#palletsQty').html(data.palletsQty);
                $('#driversNumber').html(data.driverPhoneNumber);
                $('#licensePlate').html(data.licensePlate);


            }
        ).success(function () {
            $.post("content/getData.php", {
                    status: 'getRouteListHistory',
                    routeListIDExternal : routeListIDExternal
                },
                function (data) {
                    console.log("History:\n");
                    console.log(data);
                    console.log("\n\n");
                    setHistoryTable(data);

                }
            )
        });

    }

    function setHistoryTable(routeListHistoryData) {
        if (window.jQuery) {
            $routeListHistoryDialogTable = $("#routeListHistoryDialogTable");
            routeListHistoryDialogTable = $routeListHistoryDialogTable.DataTable({
                "dom": 't', // show only table with no decorations
                "paging": false, // no pagination
                "order": [[0, "desc"]],
                "columnDefs": [
                    {"name": "timeMark", "data": "timeMark", "targets": 0},
                    {"name": "routeListNumber", "data": "routeListNumber", "targets": 1},
                    {"name": "creationDate", "data": "creationDate", "targets": 2},
                    {"name": "forwarderId", "data": "forwarderId", "targets": 3},
                    {"name": "driverPhoneNumber", "data": "driverPhoneNumber", "targets": 4},
                    {"name": "status", "data": "status", "targets": 5},
                    // {"name": "routeListNumber", "data": "routeListNumber", "targets": 4, visible: false},
                    // {"name": "boxQty", "data": "boxQty", "targets": 5, visible: false}
                ]
            });
            (function showRouteListHistory(data) {
                data = JSON.parse(data);
                for (var i = 0; i < data.length; i++) {
                    data[i].fullName = data[i].userName;
                    delete data[i].userName;
                }
                routeListHistoryDialogTable.rows().remove();
                routeListHistoryDialogTable.rows.add(data).draw(false);
            })(routeListHistoryData);


        } else {
            timeoutID = window.setTimeout(function () {
                setHistoryTable();
            }, 1000)
        }
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
});