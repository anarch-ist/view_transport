USE project_database;

# запрос на выборку всех записей, которые должны быть в интерфейсе
# нужно уметь ограничивать количество результирующих записей

# Each SELECT statement that does not insert into a table or a variable will produce a result set.



CREATE PROCEDURE selectData()
  BEGIN

    SELECT
      requests.requestNumber,
      invoices.insiderRequestNumber,
      invoices.invoiceNumber,
      clients.INN,
      delivery_points.pointName as `deliveryPoint`,
      w_points.pointName as `warehousePoint`,
      users.lastName,
      invoices.invoiceStatusID,
      invoices.boxQty,

      route_lists.driver,
      route_lists.licensePlate,
      route_lists.palletsQty,
      route_lists.routListNumber,
      routes.directionName,

      last_visited_points.pointName as `currentPoint`,
      next_route_points.pointName as `nextPoint`,
      route_points.arrivalTime


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

  END;


