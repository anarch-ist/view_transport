<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta charset="UTF-8">
<title>Отчет</title>
    <script src="<c:url value="/media/pdfmake.min.js"/>"></script>
    <script src="<c:url value="/media/vfs_fonts.js"/>"></script>
        <script>
        var docDefinition= {
        content:[
        {text: 'Отчет по складу: <c:out value="${requestScope.warehouseName}"/> c <c:out value="${requestScope.periodBegin}"/> по <c:out value="${requestScope.periodEnd}"/>', fontSize: 14, bold: true, margin: [0, 20, 0, 8]},
            {
            table: {
            headerRows: 1,
            body: [
            [{text: 'Поставщик', style: 'tableHeader'}, {text: 'Дата прибытия', style: 'tableHeader'}, {text: 'Док', style: 'tableHeader'}, {text: 'Номер машины', style: 'tableHeader'}, {text: 'Период разгрузки', style: 'tableHeader'}, {text: 'Пункт назначения', style: 'tableHeader'}, {text: 'Кол-во паллет', style: 'tableHeader'}, {text: 'Кол-во мест (россыпь)', style: 'tableHeader'} ],
            <c:forEach var="report" items="${requestScope.docReports}">
            ['${report.inn}', '${report.date}', '${report.docname}','${report.licenseplate}','${report.period}','${report.finaldestinations}','${report.orderpalletsqty}','${report.boxqty}'],
            </c:forEach>
            ]

            },
            layout: 'lightHorizontalLines'
            }
        ],
            styles: {
            header: {
            fontSize: 24,
            bold: true,
            margin: [0, 0, 0, 10]
            },
            subheader: {
            fontSize: 16,
            bold: true,
            margin: [0, 10, 0, 5]
            },
            tableHeader: {
            bold: true,
            fontSize: 13,
            color: 'black'
            }
            },
            pageOrientation: 'landscape'

        };
        pdfMake.createPdf(docDefinition).getDataUrl(function(encodedString) {
            window.open(encodedString, '_self');
        });
        </script>

        </head>
        <body>
        </body>
        </html>
