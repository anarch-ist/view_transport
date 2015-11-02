USE project_database;


# запрос на выборку всех записей, которые должны быть в интерфейсе

# нужно уметь ограничивать количество результирующих записей


SELECT
  invoices.insiderRequestNumber AS 'номер внутренней заявки',
  invoices.invoiceNumber        AS 'номер накладной',
  invoices.boxQty               AS 'количество коробок',
  invoices.invoiceStatusID      AS 'статус накладной',
  requests.requestNumber        AS 'номер заявки',
  clients.INN                   AS 'ИНН клиента',
  points.pointName              AS 'пункт доставки',
  users.lastName                AS 'торговый представитель',
  route_lists.driver            AS 'водитель',
  route_lists.licensePlate      AS 'номер ТС',
  route_lists.palletsQty        AS 'количество паллет',
  route_lists.routListNumber    AS 'номер маршрутного листа',
  routes.directionName          AS 'направление'


FROM invoices
  INNER JOIN (requests, clients, points, users, route_lists, routes)
    ON (
    invoices.requestID = requests.requestID AND
    requests.clientID = clients.clientID AND
    requests.destinationPointID = points.pointID AND
    requests.marketAgentUserID = users.userID AND
    invoices.routeListID = route_lists.routeListID AND
    route_lists.routeID = routes.routeID
    )

# because routeList in invoices table can be null, we use left or right join.

#   INNER JOIN requests ON invoices.requestID = requests.requestID
#   INNER JOIN clients ON requests.clientID = clients.clientID
#   INNER JOIN points ON requests.destinationPointID = points.pointID
;
