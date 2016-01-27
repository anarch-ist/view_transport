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
TRUNCATE route_list_history;
TRUNCATE relations_between_route_points;
TRUNCATE distances_between_points;
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

INSERT INTO distances_between_points (pointIDFirst, pointIDSecond, distance) VALUES
  (getPointIDByName('point1'), getPointIDByName('point2'), 400),
  (getPointIDByName('point2'), getPointIDByName('point3'), 230),
  (getPointIDByName('point3'), getPointIDByName('point4'), 340);

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

-- ROUTE CREATION
INSERT INTO routes (firstPointArrivalTime, daysOfWeek, routeName, directionName, tariffID)
VALUES
  ('18:00:00', 'monday,tuesday,wednesday,thursday,friday', 'route1', 'direction1', 1),
  ('14:30:00', 'monday,tuesday,wednesday,thursday,friday', 'route2', 'direction2', 1);

INSERT INTO route_points (sortOrder, timeForLoadingOperations, pointID, routeID)
VALUES
  (1, 120, getPointIDByName('point1'), getRouteIDByRouteName('route1')),
  (2, 40, getPointIDByName('point2'), getRouteIDByRouteName('route1')),
  (3, 230, getPointIDByName('point3'), getRouteIDByRouteName('route1'));

INSERT INTO relations_between_route_points (routePointIDFirst, routePointIDSecond, timeForDistance)
VALUES
  (getRoutePointIDByRouteNameAndSortOrder('route1', 1), getRoutePointIDByRouteNameAndSortOrder('route1', 2), 120),
  (getRoutePointIDByRouteNameAndSortOrder('route1', 2), getRoutePointIDByRouteNameAndSortOrder('route1', 3), 200);
-- END ROUTE CREATION

INSERT INTO route_lists (routeListNumber, startDate, palletsQty, driver, driverPhoneNumber, licensePlate, routeID)
VALUES
  ('1455668', '2015-11-11', 3, 'Dmitriy', '8905347890', 'екх123', getRouteIDByRouteName('route1'));

INSERT INTO invoices (
  insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume, goodsCost,
  lastStatusUpdated, lastModifiedBy, invoiceStatusID, commentForStatus, requestID, warehousePointID, routeListID, lastVisitedUserPointID
)
VALUES
  ('ogeghei2243', 'qwd22345', now(), now(), 4, 20, 3000, 21000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   'some_comment123', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point1')),
  ('ogeghei2244', 'qwd22334', now(), now(), 2, 20, 3000, 26000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   'some_comment2323', getRequestIDByNumber('123356'),
   getPointIDByName('point1'), getRouteListIDByNumber('1455668'), getPointIDByName('point3')),
  ('ogeghei2245', 'qwd22346', now(), now(), 10, 20, 3000, 11000.00, NULL, getUserIDByEmail('test@test.ru'), 'CREATED',
   'some_comment27349', getRequestIDByNumber('123356'), getPointIDByName('point1'), NULL, NULL);







