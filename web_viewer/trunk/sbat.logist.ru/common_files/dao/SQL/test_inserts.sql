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
TRUNCATE route_list_history;
TRUNCATE relations_between_route_points;
TRUNCATE distances_between_points;
TRUNCATE exchange;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO points (pointIDExternal, dataSourceID, pointName, region, timeZone, docs, comments, openTime, closeTime,
                    district, locality, mailIndex, address, email, phoneNumber, responsiblePersonId, pointTypeID)
VALUES ('wle', 'LOGIST_1C', 'point1', 'moscow', 3, 1, 'some_comment1', '9:00:00', '17:00:00', 'some_district', 'efregrthr', '123456',
        'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'resp_personID1', 'WAREHOUSE'),
       ('wla', 'LOGIST_1C', 'point2', 'ekb', 5, 1, 'some_comment2', '9:00:00', '17:00:00', 'erhjeerghig', 'erhefregrthr', '123456',
        'ergherersghrth', 'sf@ewuf.ru', '89032343556', 'resp_personID2', 'WAREHOUSE'),
       ('wlq', 'LOGIST_1C', 'point3', 'chel', 5, 1, 'some_comment3', '9:00:00', '17:00:00', 'erhjeig', 'efregrthr', '123456', 'ergersghrth',
        'srgf@ewuf.ru', '89032343556', 'resp_personID3', 'AGENCY'),
       ('wlc', 'LOGIST_1C', 'point4', 'chel', 5, 1, 'some_comment3', '9:00:00', '17:00:00', 'erhjeig', 'efregrthr', '123456', 'ergersghrth',
         'srgf@ewuf.ru', '89032343556', 'resp_personID4', 'AGENCY');

INSERT INTO distances_between_points (pointIDFirst, pointIDSecond, distance) VALUES
  (getPointIDByName('point1'), getPointIDByName('point2'), 400),
  (getPointIDByName('point2'), getPointIDByName('point3'), 230),
  (getPointIDByName('point3'), getPointIDByName('point4'), 340);

INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN, KPP, corAccount, curAccount, BIK, bankName, contractNumber, dateOfSigning, startContractDate, endContractDate)
VALUES
  ('extClId1', 'LOGIST_1C', 'ИП Иванов', '1234567890', '23674529375734562', 'corAcccc', 'curAxccccc', '34896208375', 'moscowBank', 'erguheru', now(), now(),
   now()),
  ('extClId2', 'LOGIST_1C', 'ИП Петров', '8947537893', '37549783469587934', 'corAcccc2', 'curAxccccc2', '3324234375', 'moscowBank1', '34guheru', now(), now(),
   now());

INSERT INTO users (login, userIDExternal, dataSourceID, userName, position, salt, passAndSalt, phoneNumber, email, userRoleID, pointID, clientID)
VALUES
  ('test', '123', 'ADMIN_PAGE','ivanov i.i.', 'position', 'nvuritneg4785231',
   md5(CONCAT(md5('test'), 'nvuritneg4785231')),  '904534356', 'test@test.ru', 'ADMIN', NULL, NULL),
  ('user2', '124', 'ADMIN_PAGE', 'ivanov i.A.', 'erwgewg', SUBSTRING(MD5(2) FROM 1 FOR 16),
   md5(CONCAT(md5('esrhgruht'), SUBSTRING(MD5(2) FROM 1 FOR 16))), '904534356', 'egrt@irtj.ru', 'DISPATCHER',
   getPointIDByName('point2'), NULL),
  ('user3', '125', 'ADMIN_PAGE', 'petrov i.A.', 'erfergg', SUBSTRING(MD5(3) FROM 1 FOR 16),
   md5(CONCAT(md5('wefwgrege'), SUBSTRING(MD5(3) FROM 1 FOR 16))), '904534356', 'ey@irtj.ru', 'DISPATCHER',
   getPointIDByName('point4'), NULL),
  ('user4', '126', 'ADMIN_PAGE', 'sidorov i.A.', 'ergrtgr', SUBSTRING(MD5(4) FROM 1 FOR 16),
   md5(CONCAT(md5('wertgrege'), SUBSTRING(MD5(4) FROM 1 FOR 16))), '904554356', 'ey@i45j.ru', 'W_DISPATCHER',
   getPointIDByName('point3'), NULL),
  ('clientUser', '127', 'ADMIN_PAGE', 'wlrfekj.', 'ergrtgr', SUBSTRING(MD5(4) FROM 1 FOR 16),
   md5(CONCAT(md5('1111'), SUBSTRING(MD5(4) FROM 1 FOR 16))), '904554356', 'cl@cl.ru', 'CLIENT_MANAGER',
   NULL , getClientIDByINN('1234567890'));

INSERT INTO tariffs (cost, capacity, carrier) VALUES (3400.00, 12.5, 'some_carrier');

-- ROUTE CREATION
INSERT INTO routes (directionIDExternal, dataSourceID, routeName, firstPointArrivalTime, daysOfWeek, tariffID)
VALUES
  ('dirIdExt1', 'LOGIST_1C', 'route1', '18:00:00', 'monday,tuesday,wednesday,thursday,friday', 1),
  ('dirIdExt2', 'LOGIST_1C', 'route2', '14:30:00', 'monday,tuesday,wednesday,thursday,friday', 1);

INSERT INTO route_points (sortOrder, timeForLoadingOperations, pointID, routeID)
VALUES
  (1, 120, getPointIDByName('point1'), getRouteIDByRouteName('route1')),
  (2, 40, getPointIDByName('point2'), getRouteIDByRouteName('route1')),
  (5, 230, getPointIDByName('point3'), getRouteIDByRouteName('route1')),
  (4, 230, getPointIDByName('point1'), getRouteIDByRouteName('route1')),
  (8, 120, getPointIDByName('point4'), getRouteIDByRouteName('route1'));
-- END ROUTE CREATION

INSERT INTO route_lists (routeListIDExternal, dataSourceID, routeListNumber, creationDate, departureDate, palletsQty,
                         forwarderId, driverId, driverPhoneNumber, licensePlate, status, routeID)
VALUES
  ('routeListIdExt1', 'LOGIST_1C', '1455668', '2015-11-11', '2015-11-11', 3, 'Дмитрий Лже Первый', 'водила1',
                      '8905347890', 'екх123', 'APPROVED', getRouteIDByRouteName('route1'));


