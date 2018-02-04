$(document).ready(function () {
    var myMap;

// Дождёмся загрузки API и готовности DOM.
    ymaps.ready(init);
    // .done(function (ym) {
    //     var myMap = new ym.Map('YMapsID', {
    //         center: [55.751574, 37.573856],
    //         zoom: 10
    //     }, {
    //         searchControlProvider: 'yandex#search'
    //     });
    //
    //
    //     // jQuery.getJSON('data.json', function (json) {
    //     //     /** Сохраним ссылку на геообъекты на случай, если понадобится какая-либо постобработка.
    //     //      * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/GeoQueryResult.xml
    //     //      */
    //     //
    //     // });
    // });





    function init () {

        myMap = new ymaps.Map('map', {
            center: [55.76, 37.64], // Москва
            zoom: 10
        }, {
            searchControlProvider: 'yandex#search'
        });
        objectManager = new ymaps.ObjectManager({
            // Чтобы метки начали кластеризоваться, выставляем опцию.
            clusterize: true,
            // ObjectManager принимает те же опции, что и кластеризатор.
            gridSize: 32,
            clusterDisableClickZoom: true
        });

        myMap.geoObjects.add(objectManager);

        $.post("content/getData.php",
            {status: "getAllWialonResources", format: "json"},
            function (json) {
                // console.log(json);
                json = JSON.parse(json);

                json.forEach(function (placemark) {
                    myMap.geoObjects.add(new ymaps.Placemark(placemark.geometry,placemark.properties, placemark.options))
                });
            }
        )
            .done(function () {
                var formHtml='<div class="openAccordion ">Добавить в базу данных</div>\n' +
                    '        <div class="accordion">\n' +
                    '            <form class="form-group" style="display: none">\n' +
                    '                <label class="form-group">Номер лицензии\n' +
                    '                    <input type="text" class="inputLicense form-control"\n' +
                    '                           placeholder="Номер лицензии ТС"></label>\n' +
                    '                <label class="form-group">Модель ТС\n' +
                    '                    <input type="text" class="form-control"\n' +
                    '                           placeholder="Модель ТС"></label>\n' +
                    '                <label class="form-group">Тип ТС\n' +
                    '                        <select class="form-control" name="type">\n' +
                    '                            <option>Тент</option>\n' +
                    '                            <option>Термос</option>\n' +
                    '                            <option>Рефрижератор</option>\n' +
                    '                        </select>\n' +
                    '                </label>\n' +
                    '                <label class="form-group">Тип погрузки\n' +
                    '                    <select class="form-control" name="type">\n' +
                    '                        <option>Задняя</option>\n' +
                    '                        <option>Боковая</option>\n' +
                    '                        <option>Верхняя</option>\n' +
                    '                    </select>\n' +
                    '                </label>\n' +
                    '                <label class="form-group"> Объем M<sup>3</sup>\n' +
                    '\n' +
                    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"\n' +
                    '                           name="volume">\n' +
                    '                </label>\n' +
                    '                <label class="form-group">Кол-во паллет\n' +
                    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"\n' +
                    '                           name="pallets_quantity">\n' +
                    '                </label>\n' +
                    '                <label class="form-group">Лимит загрузки КГ\n' +
                    '                    <input type="text" class="form-control" placeholder="123" onkeypress="return isNumberKey(event)"\n' +
                    '                           name="carrying_capacity">\n' +
                    '                </label>\n' +
                    '                <label class="form-group">WialonID\n' +
                    '                    <input type="text" disabled="disabled" class="form-control"\n' +
                    '                           placeholder="" value="fgsfds">\n' +
                    '                </label>\n' +
                    '                <div class="form-group">\n' +
                    '                    <input type="submit" class="btn btn-default" value="Submit Button">\n' +
                    '                </div>\n' +
                    '            </form>\n' +
                    '        </div>';

                $('.ymaps-2-1-60-balloon__content').css('height','auto');
                $('.form-placement').html(formHtml);

                $('.openAccordion').click(function () {

                    var form = $(this).parent().find('form')[0];
                    $(form).slideToggle("slow");
                    // if ($(form).is(':hidden')){
                    //     $(form).slideToggle("slow");
                    // } else {
                    //     $(form).slideToggle("slow");
                    // }
                });
            });


        function addVehicle(data){

        }
    }


});

function openAccordion(element) {

    $('.accordion').slideToggle("fast");
    $('.ymaps-2-1-60-balloon__content').find('ymaps').css('height','auto');
    $( ".accordion").find('form').submit(function( event ) {

        var unindexed_array = $(this).serializeArray();
        var indexed_array = {};

        $.map(unindexed_array, function(n, i){
            indexed_array[n['name']] = n['value'];
        });
        event.preventDefault();
        $.post("content/getData.php",
            {status: "addVehicleFromVMap", format: "json", data: indexed_array},
            function (json) {
                if (json!=true){
                    alert(json);
                } else {
                    location.reload();
                }

            }
        )
    });
    // $(form).slideToggle("slow");
}

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}