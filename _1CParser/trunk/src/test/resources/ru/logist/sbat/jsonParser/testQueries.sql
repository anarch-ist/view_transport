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

INSERT INTO users (login, userName, email, phoneNumber, salt, passAndSalt, userRoleID)
  VALUE ('11', 'w', 'w', 'w', 'w', 'w', 'ADMIN')
ON DUPLICATE KEY UPDATE
  userName    = VALUES(userName),
  email       = VALUES(email),
  phoneNumber = VALUES(phoneNumber),
  salt        = VALUES(salt),
  passAndSalt = VALUES(passAndSalt),
  userRoleID  = VALUES(userRoleID);

INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)
  VALUE ('ewd', 'LOGIST_1C', 'e', '123456')
ON DUPLICATE KEY UPDATE
  clientName = VALUES(clientName),
  INN        = VALUES(INN);

INSERT INTO requests
  SELECT
    NULL,
    ?,
    ?,
    ?,
    ?,
    (SELECT users.userID FROM users WHERE users.login = ?),
    (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),
    (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)
  ON DUPLICATE KEY UPDATE
    requestNumber      = VALUES(requestNumber),
    creationDate       = VALUES(creationDate),
    marketAgentUserID  = VALUES(marketAgentUserID),
    clientID           = VALUES(clientID),
    destinationPointID = VALUES(destinationPointID);

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






