
-- inserts into request table some test data
INSERT INTO requests (requestID, requestNumber, date, marketAgentUserID, clientID, destinationPointID)
  SELECT
    NULL,
    '23357',
    now(),
    users.userID     AS randomMarketAgentUserID,
    clients.clientID AS randomClientID,
    points.pointID   AS randomAgencyPointID
  FROM users
    INNER JOIN (clients, points)
  WHERE users.userRoleID = 'MARKET_AGENT' AND points.pointTypeID = 'AGENCY'
  ORDER BY RAND()
  LIMIT 1;

INSERT INTO route_lists
  SELECT NULL , '1455667', 3, 'Dmitriy', '8905347890', 'екх123', routeID
  FROM routes
  ORDER BY RAND()
  LIMIT 1;


INSERT INTO invoices
  SELECT NULL, 'ins_req_n', 'inv_n', now(), now(), 10, 20, 3000, 12000.00, 'CREATED', NULL ,requestID, pointID as warehousePointID, NULL , NULL
  FROM requests
    INNER JOIN (points)
  WHERE points.pointTypeID = 'WAREHOUSE'
  ORDER BY RAND()
  LIMIT 1;

