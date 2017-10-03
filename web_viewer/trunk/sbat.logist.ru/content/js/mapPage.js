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
        {status: "getAllPointsCoordinates", format: "json"},
        function (json) {
            console.log(json);
            ymaps.modules.require('PieChartClustererLayout')
                .spread(
                    function (PieChartClustererLayout) {
                        objectManager.clusters.options.set('clusterIconLayout', PieChartClustererLayout)
                    }, this);
            // ymaps.modules.require(['PieChartClustererLayout'], );
            // objectManager.objects.options.set('preset', 'islands#greenDotIcon');
            // objectManager.clusters.options.set('preset', 'islands#greenClusterIcons');
            objectManager.add(JSON.parse(json));
            // var geoObjects = ymaps.geoQuery(json)
            //     .addToMap(myMap)
            //     .applyBoundsToMap(myMap, {
            //         checkZoomRange: true
            //     });

        }
    );
}


});