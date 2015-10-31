USE project_database;

SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM points;
DELETE FROM users;
DELETE FROM clients;
DELETE FROM requests;
DELETE FROM routes;
DELETE FROM route_points;
DELETE FROM route_lists;
DELETE FROM invoices;
SET FOREIGN_KEY_CHECKS = 1;

# insert some test data
INSERT INTO points (pointName, region, district, locality, mailIndex, address, email, phoneNumber, pointTypeID)
VALUES
  ('point1', 'wefewgf', 'erhjeig', 'efregrthr', '123456', 'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'WAREHOUSE'),
  ('point2', 'drghgf', 'erhjeerghig', 'erhefregrthr', '123456', 'ergherersghrth', 'sf@ewuf.ru', '89032343556', 'WAREHOUSE'),
  ('point3', 'wefewgf', 'erhjeig', 'efregrthr', '123456', 'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'AGENCY');


INSERT INTO users (firstName, lastName, patronymic, login, passMD5, phoneNumber, email, userRoleID, pointID)
VALUES
  ('ivan', 'ivanov', 'ivanovich', 'login1', 'esrhgruht', '904534356', 'egrt@irtj.ru', 'MARKET_AGENT', getPointIDByName('point1')),
  ('ivan', 'ivanov', 'ivanovich', 'login2', 'esrhgruht', '904534356', 'egrt@irtj.ru', 'MARKET_AGENT', getPointIDByName('point2'));


INSERT INTO clients (INN, KPP, corAccount, curAccount, BIK, bankName, contractNumber, dateOfSigning, startContractDate, endContractDate)
VALUES
  ('1234567890', '23674529375734562', 'corAcccc', 'curAxccccc', '34896208375', 'moscowBank', 'erguheru', now(), now(), now()),
  ('8947537893', '37549783469587934', 'corAcccc2', 'curAxccccc2', '3324234375', 'moscowBank1', '34guheru', now(), now(), now());

INSERT INTO requests (requestNumber, date, marketAgentUserID, clientID, destinationPointID)
VALUES ('123356', now(), getUserIDByLogin('login1'), getClientIDByINN('1234567890'), getPointIDByName('point2'));

INSERT INTO routes (routeName, directionName)
VALUES
  ('rout1', 'direction1'),
  ('rout2', 'direction2');

INSERT INTO route_points(sortOrder, tLoading, timeToNextPoint, distanceToNextPoint, arrivalTime, pointID, routeID)
VALUES
  (1, 20, 40, 200, '01:02:03', getPointIDByName('point1'), getRoutIDByRoutName('rout1')),
  (2, 20, 40, 200, '01:02:06', getPointIDByName('point3'), getRoutIDByRoutName('rout1'));

INSERT INTO route_lists(routListNumber, palletsQty, driver, licensePlate, routeID)
VALUES
  ('1455668', 3, 'Dmitriy', 'екх123', getRoutIDByRoutName('rout1'));

INSERT INTO invoices(invoiceNumber, creationDate, deliveryDate, boxQty, sales_invoice, invoiceStatusID, requestID, warehousePointID, routeListID)
VALUES
  ('qwd22345', now(), now(), 4, 'oweieih213423', 'CREATED', getRequestIDByNumber('123356'), getPointIDByName('point1'), getRouteListIDByNumber('1455668'));







