

INSERT INTO route_lists (routListNumber, palletsQty, driver, driverPhoneNumber, licensePlate, routeID)
VALUES
  ( '1455668', 3, 'Dmitriy', '8905347890', 'екх123', getRoutIDByRoutName('rout1'));


INSERT INTO invoices (
  insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume, goodsCost, sales_invoice,
  invoiceStatusID, requestID, warehousePointID, routeListID, lastVisitedUserPointID
)
VALUES
  ('ogeghei2243', 'qwd22345', now(), now(), 4, 20, 3000, 21000.00,  'oweieih213423', 'ARRIVED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point1')),
  ('ogeghei2244', 'qwd22334', now(), now(), 2, 20, 3000, 26000.00, 'oweieih2ewf23', 'CREATED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point3')),
  ('ogeghei2245', 'qwd22346', now(), now(), 10, 20, 3000, 11000.00, 'oweiretg213423', 'CREATED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), NULL, NULL);

