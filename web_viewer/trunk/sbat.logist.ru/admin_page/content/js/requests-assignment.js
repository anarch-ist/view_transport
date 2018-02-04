$(document).ready(function () {
    let warehouseSelect;
    $.post(
        "content/getData.php",
        {
            status: "getWarehouses", format: "json"
        },
        function (data) {
            let options = [];
            // console.log(data);
            data = JSON.parse(data);
            data.forEach(function (entry) {
                var selectizeOption = {text: entry.pointName, value: entry.pointID};
                options.push(selectizeOption);
            });
            warehouseSelect = $("#warehouseId").selectize(
                {
                    sortField: "text",
                    diacritics: true,
                    maxItems: 1,
                    options: options,
                    dropdownParent: 'body',
                    onChange: function (values) {
                        if (values) {
                            $("#assignRequests").removeAttr("disabled");
                            // console.log(JSON.stringify(values));
                        } else {
                            $("#assignRequests").attr("disabled", "disabled");
                        }
                    }
                }

            );
        }
    );



// function assignRequests() {
//     console.log(JSON.stringify(warehouseSelect[0].selectize.getValue()));
//     $("#assignRequests").attr("disabled", "disabled");
//     $("#assignRequests").val("Заявки распределяются, подождите");
//     $.post(
//         "content/getData.php",
//         {
//             status: "assignRequests", format: "json", warehouseId: warehouseSelect[0].selectize.getValue()
//         },
//         function (data) {
//             // console.log(JSON.stringify(data));
//             $("#assignRequests").removeAttr("disabled");
//             $("#assignRequests").setValue("Распределить");
//         }
//     );
// }

    $("#assignRequests").on("click", function () {
        $("#assignRequests").attr("disabled", "disabled").val("Заявки распределяются, подождите");
        $.post(
            "content/getData.php",
            {
                status: "assignRequests", format: "json", warehouseId: warehouseSelect[0].selectize.getValue()
            },
            function (data) {
                // console.log(data);
                if (data!='0'){
                    localStorage.setItem("generatedRouteLists",data);
                }
                generateLinks();
                $("#assignRequests").removeAttr("disabled").val("Распределить");

            }
        );
    });


    function generateLinks() {
        routeListsData = localStorage.getItem("generatedRouteLists");
        if(routeListsData!=0){
            routeListsData = JSON.parse(routeListsData);
            // console.log(JSON.stringify(routeListsData));
            // console.log(routeListsData[0].routeListId);
            if (routeListsData!==null && routeListsData!==''){

                $("#routeListsLinks").show();
                $('#autoInsertedRouteListLinks').html('');
                $html='';
                if(routeListsData==0){
                    $html+='<li>Не удалось подобрать маршрутные листы</li>';
                } else {
                    routeListsData.forEach(function (item) {
                        $html+=
                            "<li><a href=/?routeListId="+item.routeListId+" >"+item.routeListNumber +"</a>"+((item.maxCapacity<=item.currentCapacity) ? " МЛ загружен на 100% возможно переполнение" : "")+"</li>"
                    });
                }

                $('#autoInsertedRouteListLinks').html($html);
            }
        } else {
            var $html='<li>Не удалось подобрать маршрутные листы</li>';
            $('#autoInsertedRouteListLinks').html($html);
        }
    }

    generateLinks();
});


