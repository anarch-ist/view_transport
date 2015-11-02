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

  delivery_points.pointName     AS 'delivery_point',
  w_points.pointName            AS 'warehouse_point', #TODO
  last_visited_points           AS 'lastVisited', #TODO
  points.pointName              AS 'next_route_point',

  users.lastName                AS 'торговый представитель',
  route_lists.driver            AS 'водитель',
  route_lists.licensePlate      AS 'номер ТС',
  route_lists.palletsQty        AS 'количество паллет',
  route_lists.routListNumber    AS 'номер маршрутного листа',
  routes.directionName          AS 'направление'

FROM invoices

  INNER JOIN (requests, clients, points AS delivery_points, points AS w_points, users)
    ON (
    invoices.requestID = requests.requestID AND
    invoices.warehousePointID = w_points.pointID AND
    requests.clientID = clients.clientID AND
    requests.destinationPointID = delivery_points.pointID AND
    requests.marketAgentUserID = users.userID
    )
  # because routeList in invoices table can be null, we use left join.
  LEFT JOIN (route_lists, route_points, routes)
    ON (
    invoices.routeListID = route_lists.routeListID AND
    route_lists.lastVisitedRoutePointID = route_points.routePointID
    route_lists.routeID = routes.routeID
    );


#   INNER JOIN requests ON invoices.requestID = requests.requestID
#   INNER JOIN clients ON requests.clientID = clients.clientID
#   INNER JOIN points ON requests.destinationPointID = points.pointID
;
