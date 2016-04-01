CALL selectData(1, 0, 100, '', TRUE , '');

INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, email, pointTypeID, responsiblePersonId)
VALUES ('1', 'LOGIST_1C', 'point1', 'vrtbty111111', 'ff@ff1111', 'AGENCY', 'some_person11111')
ON DUPLICATE KEY UPDATE
  pointName           = VALUES(pointName),
  address             = VALUES(address),
  email               = VALUES(email),
  pointTypeID         = VALUES(pointTypeID),
  responsiblePersonId = VALUES(responsiblePersonId);

INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE ('111', 'LOGIST_1C', 'wewed')
ON DUPLICATE KEY UPDATE
  routeName = VALUES(routeName);

INSERT INTO users (userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)
  SELECT
    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, clients.clientID
  FROM DUAL LEFT JOIN clients ON
  clientIDExternal = ? AND dataSourceID = ?
ON DUPLICATE KEY UPDATE
  login       = VALUES(login),
  salt        = VALUES(salt),
  passAndSalt = VALUES(passAndSalt),
  userRoleID  = VALUES(userRoleID),
  userName    = VALUES(userName),
  phoneNumber = VALUES(phoneNumber),
  email       = VALUES(email),
  position    = VALUES(position),
  pointID     = VALUES(pointID),
  clientID    = VALUES(clientID);

SELECT
  1, clients.clientID
FROM DUAL LEFT JOIN clients ON(clientIDExternal = 'dd' AND dataSourceID = 'LOGIST_1C');

SELECT 1 as n1 FROM DUAL JOIN clients ON 1=1;

SELECT
  1, clients.clientID
FROM DUAL LEFT JOIN clients ON(clientIDExternal = 'dd' AND dataSourceID = 'LOGIST_1C');

SELECT 1, 2 FROM dual LEFT JOIN clients ON (TRUE);

INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)
  VALUE ('ewd', 'LOGIST_1C', 'e', '123456')
ON DUPLICATE KEY UPDATE
  clientName = VALUES(clientName),
  INN        = VALUES(INN);

INSERT INTO requests
  VALUE
  (NULL,
    ?, ?, ?, ?,
    (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),
    (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?),
    (SELECT users.userID FROM users WHERE users.login = ?),
    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
   NOW(),
   getUserIDByLogin('parser'),
   'CREATED',
   ?,
   (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?),
   NULL,
   NULL
  )
ON DUPLICATE KEY UPDATE
  requestNumber           = VALUES(requestNumber),
  requestDate             = VALUES(requestDate),
  clientID                = VALUES(clientID),
  destinationPointID      = VALUES(destinationPointID),
  marketAgentUserID       = VALUES(marketAgentUserID),
  invoiceNumber           = VALUES(invoiceNumber),
  invoiceDate             = VALUES(invoiceDate),
  documentNumber          = VALUES(documentNumber),
  documentDate            = VALUES(documentDate),
  firma                   = VALUES(firma),
  storage                 = VALUES(storage),
  contactName             = VALUES(contactName),
  contactPhone            = VALUES(contactPhone),
  deliveryOption          = VALUES(deliveryOption),
  deliveryDate            = VALUES(deliveryDate),
  boxQty                  = VALUES(boxQty),
  weight                  = VALUES(weight),
  volume                  = VALUES(volume),
  goodsCost               = VALUES(goodsCost),
  lastStatusUpdated       = VALUES(lastStatusUpdated),
  lastModifiedBy          = VALUES(lastModifiedBy),
  requestStatusID         = VALUES(requestStatusID),
  commentForStatus        = VALUES(commentForStatus),
  warehousePointID        = VALUES(warehousePointID),
  routeListID             = VALUES(routeListID),
  lastVisitedRoutePointID = VALUES(lastVisitedRoutePointID);

INSERT INTO route_lists
  VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, getRouteIDByDirectionIDExternal(?, ?))
ON DUPLICATE KEY UPDATE
  routeListNumber   = VALUES(routeListNumber),
  creationDate      = VALUES(creationDate),
  departureDate     = VALUES(departureDate),
  palletsQty        = VALUES(palletsQty),
  forwarderId       = VALUES(forwarderId),
  driverId          = VALUES(driverId),
  driverPhoneNumber = VALUES(driverPhoneNumber),
  licensePlate      = VALUES(licensePlate),
  status            = VALUES(status),
  routeID           = VALUES(routeID);






