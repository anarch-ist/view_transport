USE project_database;

# запрос на выборку всех записей, которые должны быть в интерфейсе
# нужно уметь ограничивать количество результирующих записей

SELECT
  invoices.insiderRequestNumber   AS 'номер внутренней заявки',
  invoices.invoiceNumber          AS 'номер накладной',
  invoices.boxQty                 AS 'количество коробок',
  invoices.invoiceStatusID        AS 'статус накладной',
  requests.requestNumber          AS 'номер заявки',
  clients.INN                     AS 'ИНН клиента',
  delivery_points.pointName       AS 'delivery_point',
  w_points.pointName              AS 'warehouse_point',
  users.lastName                  AS 'торговый представитель',

  route_lists.driver              AS 'водитель',
  route_lists.licensePlate        AS 'номер ТС',
  route_lists.palletsQty          AS 'количество паллет',
  route_lists.routListNumber      AS 'номер маршрутного листа',
  routes.directionName            AS 'направление',

  last_visited_points.pointName   AS 'последний посещенный пункт',

  next_route_points.pointName     AS 'следующий пункт маршрута',
  route_points.arrivalTime        AS 'время прибытия в следующий пункт'


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
  LEFT JOIN (route_lists, routes)
    ON (
    invoices.routeListID = route_lists.routeListID AND
    route_lists.routeID = routes.routeID
    )
  LEFT JOIN (points AS last_visited_points)
    ON (
    invoices.lastVisitedUserPointID = last_visited_points.pointID
    )
  LEFT JOIN (route_points, points AS next_route_points)
    ON (
    route_lists.routeID = routes.routeID AND
    routes.routeID = route_points.routeID AND
    route_points.routePointID = getNextRoutePointID(routes.routeID, invoices.lastVisitedUserPointID) AND
    next_route_points.pointID = route_points.pointID
    );
;
