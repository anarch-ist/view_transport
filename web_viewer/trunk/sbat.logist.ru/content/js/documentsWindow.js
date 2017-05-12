
//Пример
var fgsfds = [{
    title: "Официальные документы",
    documents: [{name: "Торг-12", file: "6NG4KLOG_201.pdf"}, {name: "Счет-фактура", file: "6NG4KLOG_202.pdf"}, {name: "Сертификаты", file: "6NG4KLOG_203.xls"}]
},
    {
        title: "Правовые документы",
        documents: [{name: "Раз", file: "1.pdf"}, {name: "Два", file: "2.pdf"}]
    }];


$(window).on('load', function () {
    var $_GET = {};


    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });

    $.post("content/getData.php", {
            status: 'getRequestById',
            requestIDExternal: $_GET['reqIdExt']
        },
        function (data) {
            setRequestInfo(data);
        }
    ).success(function () {
        $.post("content/getData.php", {
                status: 'getDocuments',
                requestIDExternal: $_GET['reqIdExt']
            },
            function (data) {
                //Просто раскомментируй строку ниже и строку под ней, когда запилишь скрипт
                setDocuments(JSON.parse(data));
                // console.log(data);
                // setDocuments(fgsfds);
            })
    })



});

function setRequestInfo(requestData) {
    requestData = JSON.parse(requestData);

    for (var i = 0; i < requestData.length; i++) {
        requestData[i].fullName = requestData[i].userName;
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
}

function setDocuments(data) {
    document.getElementById('documents-container').insertAdjacentHTML('beforeend',getGroupTemplate(data));

}

function getGroupTemplate(groups) {
    html = '';
    for (var group of groups) {
        html += '<div class="list-group list-group-root">' +
            '<div class="list-group-item">'+ group.title +'</div>' +
            '<div class="list-group">';
        for (var document of group.documents) {
            html += '<a href=" '+document.file +'" class="list-group-item">' + document.name +'</a>'
        }
        html += '</div>' +
            '</div>'
    }
    return html;
}




