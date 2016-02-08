
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

# requestID          INTEGER AUTO_INCREMENT,
# requestIDExternal  VARCHAR(128) NOT NULL,
# requestNumber      VARCHAR(16)  NOT NULL,
# creationDate       DATETIME     NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
# marketAgentUserID  INTEGER      NULL,
# clientID           INTEGER      NULL,
# destinationPointID INTEGER      NULL, -- адрес, куда должны быть доставлены все товары

INSERT INTO requests
  SELECT
    NULL,
    '23yug2'         AS _requestIDExternal,
    'LOGIST_1C'      AS _dataSourceID,
    'wefbwfu'        AS _requestNumber,
    '2015-03-12'     AS _creationDate,
    users.userID     AS _marketAgentUserID,
    clients.clientID AS _clientID,
    points.pointID   AS _destinationPointID
  FROM users
    INNER JOIN (clients, points)
  WHERE
    users.login = 'Вла024' AND
    clients.clientIDExternal = '124598-01' AND
    points.pointIDExternal = '124598';
#   LIMIT 1
# ON DUPLICATE KEY UPDATE requests.requestIDExternal = _requestIDExternal, requests.requestNumber = _requestNumber, ;

SELECT * FROM points WHERE pointIDExternal = '124598-01';

INSERT INTO requests
SELECT
  NULL,
  '23yug2',
  'LOGIST_1C',
  'wefbwfu',
  '2015-03-12',
  (SELECT users.userID FROM users WHERE users.login = 'Брн006'),
  (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = '120770' AND clients.dataSourceID = 'LOGIST_1C'),
  (SELECT points.pointID FROM points WHERE points.pointIDExternal = '120770-01' AND points.dataSourceID = 'LOGIST_1C')

#   clients.clientID AS _clientID,

SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = '120770' AND clients.dataSourceID = 'LOGIST_1C';
# users.pointID = points.pointID AND
# users.userRoleID = user_roles.userRoleID

INSERT INTO invoices
  VALUE
  (NULL, 'ogeghei2243', 'LOGIST_1C', 'somedocNumber', '2015-01-01', 'some_firma', 'contactName', 'contactPhone',
         '2015-02-02', '2015-02-03', 4, 12, 30, 12000.00, '2015-03-02', getUserIDByLogin('parser'), 'CREATED',
                                        'some_comment123', getRequestIDByNumber('123356'), 1, NULL, NULL);


UPDATE invoices SET routeListID = getRouteIDByDirectionIDExternal('7002','LOGIST_1C') WHERE invoices.invoiceIDExternal = ? AND invoices.dataSourceID = 'LOGIST_1C';

