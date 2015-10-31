use project_database;


# запрос на выборку всех записей, которые должны быть в интерфейсе

# нужно уметь ограничивать количество результирующих записей


SELECT
  invoices.insiderRequestNumber AS 'номер внутренней заявки',
  invoices.invoiceNumber AS 'номер накладной',
  invoices.boxQty AS 'количество коробок',
  invoices.invoiceStatusID AS 'статус накладной',
  requests.requestNumber AS 'номер заявки',
  clients.INN AS 'ИНН клиента',
  points.pointName AS 'пункт доставки'

FROM invoices
  INNER JOIN requests ON invoices.requestID = requests.requestID
  INNER JOIN clients ON requests.clientID = clients.clientID
  INNER JOIN points ON requests.destinationPointID = points.pointID
;
