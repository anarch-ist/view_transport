SHOW VARIABLES;
SHOW VARIABLES LIKE '%innodb_log_waits%';



CREATE PROCEDURE examplePrStatement(id VARCHAR(255), _orderBy VARCHAR(255))
  BEGIN
    SET @s = CONCAT('SELECT * FROM points WHERE pointID <> ? ORDER BY ', _orderBy);
    PREPARE statement FROM @s;
    SET @_id = id;
    EXECUTE statement USING @_id;
    DEALLOCATE PREPARE statement;
  END;


DROP PROCEDURE selectData;

-- ----------------------------

USE transmaster_transport_db;

SELECT * FROM big_select LIMIT 10000; -- должен работать мгновенно

CALL refreshMaterializedView();

SELECT COUNT(*) FROM big_select_materialized; -- 14ms

CALL selectData(1, 0, 10000, '', TRUE, 'requestIDExternal,12;');


CREATE FULLTEXT INDEX ind1 ON big_select_materialized (requestIDExternal(10));

CREATE INDEX ind3 ON big_select_materialized (requestDate);
CREATE FULLTEXT INDEX ind4 ON big_select_materialized (invoiceNumber(10));
CREATE INDEX ind5 ON big_select_materialized (invoiceDate);
CREATE FULLTEXT INDEX ind6 ON big_select_materialized (documentNumber(10));

CALL selectDataTest(1, 0, 10000, 'derfreg', TRUE, '12');
DROP INDEX reqStatusRusName ON request_statuses;
CREATE INDEX reqStatusRusName ON request_statuses (requestStatusID, requestStatusRusName(10));


DROP INDEX ind2 ON big_select_materialized;
CREATE INDEX ind2 ON big_select_materialized (requestNumber);

SELECT SQL_CALC_FOUND_ROWS
  big_select_materialized.requestIDExternal,
  big_select_materialized.requestNumber,
  big_select_materialized.requestDate,
  big_select_materialized.invoiceNumber,
  big_select_materialized.invoiceDate,
  big_select_materialized.documentNumber,
  big_select_materialized.documentDate,
  big_select_materialized.firma,
  big_select_materialized.storage,
  requests.commentForStatus,
  big_select_materialized.boxQty,
  request_statuses.requestStatusRusName,
  big_select_materialized.clientIDExternal,
  big_select_materialized.INN,
  big_select_materialized.clientName,
  big_select_materialized.userName,
  big_select_materialized.deliveryPointName,
  big_select_materialized.warehousePointName,
  currentPoints.pointName AS currentPointName,
  next_points.nextPointName AS nextPointName,
  big_select_materialized.routeName,
  big_select_materialized.driverId,
  route_lists.licensePlate,
  route_lists.palletsQty,
  big_select_materialized.routeListNumber,
  TIMESTAMPADD(MINUTE, time_to_next.timeToNextPoint, departure_date.departureTimeFromLastRoutePoint) AS arrivalTime,
  requests.requestStatusID,
  big_select_materialized.routeListID
FROM big_select_materialized
  INNER JOIN (requests) ON (
     requests.requestID = big_select_materialized.requestID
    )
  INNER JOIN (request_statuses) ON (
    request_statuses.requestStatusID = requests.requestStatusID
    )
  LEFT JOIN (route_points) ON (
    route_points.routePointID = requests.lastVisitedRoutePointID
    )
  LEFT JOIN (points AS currentPoints) ON (
    currentPoints.pointID = route_points.pointID
    )
  LEFT JOIN (routes) ON (
    routes.routeID = big_select_materialized.routeID
    )
  LEFT JOIN (route_lists) ON (
    route_lists.routeListID = big_select_materialized.routeListID
    )
#   -- TODO из этого селекта можно сделать постоянно обновляемый materialized view
  LEFT JOIN (SELECT
               mainRP.routePointID,
               mainRP.routeID,
               innerRP.routePointID AS nextRoutePoint,
               innerRP.pointID      AS nextPointID,
               points.pointName     AS nextPointName
             FROM route_points mainRP
               LEFT JOIN route_points innerRP ON (
                 innerRP.routeID = mainRP.routeID AND
                 innerRP.sortOrder = (SELECT innerRP2.sortOrder
                                      FROM route_points innerRP2
                                      WHERE (innerRP2.routeID = mainRP.routeID AND innerRP2.sortOrder > mainRP.sortOrder)
                                      ORDER BY innerRP.sortOrder
                                      LIMIT 1)
                 )
               LEFT JOIN points ON points.pointID = innerRP.pointID) AS next_points ON (
    next_points.routePointID = requests.lastVisitedRoutePointID
    )
  -- TODO из этого селекта можно сделать постоянно обновляемый materialized view
  LEFT JOIN (SELECT
               mainRP.routePointID,
               relations_between_route_points.timeForDistance AS timeToNextPoint
             FROM route_points mainRP
               INNER JOIN route_points innerRP ON (
                 innerRP.routeID = mainRP.routeID AND
                 innerRP.sortOrder = (SELECT innerRP2.sortOrder
                                      FROM route_points innerRP2
                                      WHERE
                                        (innerRP2.routeID = mainRP.routeID AND innerRP2.sortOrder > mainRP.sortOrder)
                                      ORDER BY innerRP.sortOrder
                                      LIMIT 1)
                 )
               INNER JOIN relations_between_route_points ON (
                 mainRP.routePointID = relations_between_route_points.routePointIDFirst AND
                 innerRP.routePointID = relations_between_route_points.routePointIDSecond
                 )) AS time_to_next ON (
    time_to_next.routePointID = requests.lastVisitedRoutePointID
    )
  -- TODO из этого селекта можно сделать постоянно обновляемый materialized view
  LEFT JOIN (SELECT
               requests_history.requestID,
               requests_history.lastStatusUpdated,
               MAX(requests_history.lastStatusUpdated) AS departureTimeFromLastRoutePoint
             FROM requests_history
             WHERE requests_history.requestStatusID = 'DEPARTURE'
             GROUP BY requestID
             ORDER BY NULL
    ) AS departure_date ON (
    departure_date.requestID = big_select_materialized.requestID
    )
  -- WHERE (big_select_materialized.marketAgentUserID = 1 AND big_select_materialized.requestNumber LIKE '%12%')
;
SELECT * FROM big_select_materialized ORDER BY requestNumber;

DROP INDEX ind2 ON big_select_materialized;
CREATE INDEX ind2 ON big_select_materialized (requestNumber);