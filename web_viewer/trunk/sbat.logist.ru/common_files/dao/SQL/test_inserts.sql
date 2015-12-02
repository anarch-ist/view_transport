USE project_database;

SET FOREIGN_KEY_CHECKS = 0;
-- truncate drop and create table instead of deleting rows one by one
TRUNCATE points;
TRUNCATE users;
TRUNCATE clients;
TRUNCATE requests;
TRUNCATE tariffs;
TRUNCATE routes;
TRUNCATE route_points;
TRUNCATE route_lists;
TRUNCATE invoices;
SET FOREIGN_KEY_CHECKS = 1;

# insert some test data
INSERT INTO points (pointName, region, timeZone, docs, comments, openTime, closeTime, district, locality, mailIndex, address, email, phoneNumber, pointTypeID)
VALUES
  ('point1', 'moscow', 3, 1, 'some_comment1', '9:00:00', '17:00:00', 'some_district', 'efregrthr', '123456',
   'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'WAREHOUSE'),
  ('point2', 'ekb', 5, 1, 'some_comment2', '9:00:00', '17:00:00', 'erhjeerghig', 'erhefregrthr', '123456',
   'ergherersghrth', 'sf@ewuf.ru', '89032343556', 'WAREHOUSE'),
  ('point3', 'chel', 5, 1, 'some_comment3', '9:00:00', '17:00:00', 'erhjeig', 'efregrthr', '123456', 'ergersghrth',
   'srgf@ewuf.ru', '89032343556', 'AGENCY'),
  ('point4', 'chel', 5, 1, 'some_comment3', '9:00:00', '17:00:00', 'erhjeig', 'efregrthr', '123456', 'ergersghrth',
   'srgf@ewuf.ru', '89032343556', 'AGENCY');



INSERT INTO users (firstName, lastName, patronymic, position, login, passMD5, phoneNumber, email, userRoleID, pointID)
VALUES
  ('ivan', 'ivanov', 'ivanovich', 'erwgewg', 'login1', md5('test'), '904534356', 'test@test.ru', 'MARKET_AGENT',
   getPointIDByName('point1')),
  ('ivan', 'ivanov', 'ivanovich', 'erwgewg', 'login2', md5('esrhgruht'), '904534356', 'egrt@irtj.ru', 'MARKET_AGENT',
   getPointIDByName('point2')),
  ('erir', 'dddddd', 'ewreruiii', 'erfergg', 'login3', md5('wefwgrege'), '904534356', 'ey@irtj.ru', 'MARKET_AGENT',
   getPointIDByName('point4'));


INSERT INTO clients (INN, KPP, corAccount, curAccount, BIK, bankName, contractNumber, dateOfSigning, startContractDate, endContractDate)
VALUES
  ('1234567890', '23674529375734562', 'corAcccc', 'curAxccccc', '34896208375', 'moscowBank', 'erguheru', now(), now(),
   now()),
  ('8947537893', '37549783469587934', 'corAcccc2', 'curAxccccc2', '3324234375', 'moscowBank1', '34guheru', now(), now(),
   now());

INSERT INTO requests (requestNumber, date, marketAgentUserID, clientID, destinationPointID)
VALUES ('123356', now(), getUserIDByLogin('login1'), getClientIDByINN('1234567890'), getPointIDByName('point2'));

INSERT INTO tariffs (tariffID, cost, capacity, carrier)
VALUES (1, 3400.00, 12.5, 'some_carrier');

INSERT INTO routes (routeName, directionName, tariffID)
VALUES
  ('rout1', 'direction1', 1),
  ('rout2', 'direction2', 1);

INSERT INTO route_points (sortOrder, tLoading, timeToNextPoint, distanceToNextPoint, arrivalTime, pointID, routeID)
VALUES
  (1, 20, 40, 200, '12:00:00', getPointIDByName('point1'), getRoutIDByRoutName('rout1')),
  (2, 20, 40, 200, '17:00:00', getPointIDByName('point2'), getRoutIDByRoutName('rout1')),
  (3, 20, 40, 200, '01:00:00', getPointIDByName('point3'), getRoutIDByRoutName('rout1'));

INSERT INTO route_lists (routListNumber, palletsQty, driver, driverPhoneNumber, licensePlate, routeID)
VALUES
  ('1455668', 3, 'Dmitriy', '8905347890', 'екх123', getRoutIDByRoutName('rout1'));

INSERT INTO invoices (
  insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume, goodsCost, sales_invoice,
  invoiceStatusID, requestID, warehousePointID, routeListID, lastVisitedUserPointID
)
VALUES
  ('ogeghei2243', 'qwd22345', now(), now(), 4, 20, 3000, 21000.00,  'oweieih213423', 'CREATED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point1')),
  ('ogeghei2244', 'qwd22334', now(), now(), 2, 20, 3000, 26000.00, 'oweieih2ewf23', 'CREATED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point3')),
  ('ogeghei2245', 'qwd22346', now(), now(), 10, 20, 3000, 11000.00, 'oweiretg213423', 'CREATED', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), NULL, NULL);








