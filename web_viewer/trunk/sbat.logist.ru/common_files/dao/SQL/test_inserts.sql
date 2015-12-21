USE transmaster_transport_db;
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
TRUNCATE invoice_history;
TRUNCATE rout_list_history;
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


INSERT INTO users (firstName, lastName, patronymic, position, salt, passAndSalt, phoneNumber, email, userRoleID, pointID)
VALUES
  ('ivan', 'ivanov', 'ivanovich', 'erwgewg', SUBSTRING(MD5(1) FROM 1 FOR 16),
   md5(CONCAT(md5('test'), SUBSTRING(MD5(1) FROM 1 FOR 16))), '904534356', 'test@test.ru', 'ADMIN',
   getPointIDByName('point1')),
  ('ivan', 'ivanov', 'ivanovich', 'erwgewg', SUBSTRING(MD5(2) FROM 1 FOR 16),
   md5(CONCAT(md5('esrhgruht'), SUBSTRING(MD5(2) FROM 1 FOR 16))), '904534356', 'egrt@irtj.ru', 'DISPATCHER',
   getPointIDByName('point2')),
  ('erir', 'dddddd', 'ewreruiii', 'erfergg', SUBSTRING(MD5(3) FROM 1 FOR 16),
   md5(CONCAT(md5('wefwgrege'), SUBSTRING(MD5(3) FROM 1 FOR 16))), '904534356', 'ey@irtj.ru', 'DISPATCHER',
   getPointIDByName('point4')),
  ('degg', 'rtgrgg', 'rtrtbtybv', 'ergrtgr', SUBSTRING(MD5(4) FROM 1 FOR 16),
   md5(CONCAT(md5('wertgrege'), SUBSTRING(MD5(4) FROM 1 FOR 16))), '904554356', 'ey@i45j.ru', 'MARKET_AGENT',
   getPointIDByName('point3'));


INSERT INTO clients (INN, KPP, corAccount, curAccount, BIK, bankName, contractNumber, dateOfSigning, startContractDate, endContractDate)
VALUES
  ('1234567890', '23674529375734562', 'corAcccc', 'curAxccccc', '34896208375', 'moscowBank', 'erguheru', now(), now(),
   now()),
  ('8947537893', '37549783469587934', 'corAcccc2', 'curAxccccc2', '3324234375', 'moscowBank1', '34guheru', now(), now(),
   now());


INSERT INTO requests (requestNumber, date, marketAgentUserID, clientID, destinationPointID)
VALUES
  ('123356', now(), getUserIDByEmail('ey@i45j.ru'), getClientIDByINN('1234567890'), getPointIDByName('point2')),
  ('859458', now(), getUserIDByEmail('ey@i45j.ru'), getClientIDByINN('8947537893'), getPointIDByName('point1')),
  ('er9458', now(), getUserIDByEmail('ey@i45j.ru'), getClientIDByINN('8947537893'), getPointIDByName('point4'));


INSERT INTO tariffs (cost, capacity, carrier) VALUES (3400.00, 12.5, 'some_carrier');


INSERT INTO routes (routeName, directionName, tariffID)
VALUES
  ('rout1', 'direction1', 1),
  ('rout2', 'direction2', 1);


INSERT INTO route_points (sortOrder, tLoading, timeToNextPoint, distanceToNextPoint, arrivalTime, pointID, routeID)
VALUES
  (1, 20, 40, 200, '12:00:00', getPointIDByName('point1'), getRoutIDByRoutName('rout1')),
  (2, 20, 40, 200, '17:00:00', getPointIDByName('point2'), getRoutIDByRoutName('rout1')),
  (3, 20, 40, 200, '01:00:00', getPointIDByName('point3'), getRoutIDByRoutName('rout1'));


INSERT INTO route_lists (routListNumber, startDate, palletsQty, driver, driverPhoneNumber, licensePlate, routeID)
VALUES
  ('1455668', '2015-11-11', 3, 'Dmitriy', '8905347890', 'екх123', getRoutIDByRoutName('rout1'));


INSERT INTO invoices (
  insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume, goodsCost,
  lastStatusUpdated, lastModifiedBy, invoiceStatusID, requestID, warehousePointID, routeListID, lastVisitedUserPointID
)
VALUES
  ('ogeghei2243', 'qwd22345', now(), now(), 4, 20, 3000, 21000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point1')),
  ('ogeghei2244', 'qwd22334', now(), now(), 2, 20, 3000, 26000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point3')),
  ('ogeghei2245', 'qwd22346', now(), now(), 10, 20, 3000, 11000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   getRequestIDByNumber('123356'),
   getPointIDByName('point1'), NULL, NULL);








