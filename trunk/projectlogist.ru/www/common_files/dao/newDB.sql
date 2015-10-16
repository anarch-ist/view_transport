DROP DATABASE IF EXISTS project_database;
CREATE DATABASE `project_database` CHARACTER SET utf8 COLLATE utf8_bin;
USE `project_database`;

CREATE TABLE user_roles (
  userRoleID INTEGER AUTO_INCREMENT,
  userRoleName VARCHAR(64) NOT NULL,
  PRIMARY KEY (userRoleID)
);
# администратор, ему доступен полный графический интерфейс сайта, и самые высокие права на изменение в БД:
# имеет право изменить роль пользователя
#
INSERT INTO user_roles (userRoleName) VALUES ('ADMIN');
# диспетчер склада, доступна часть GUI для выбора маршрута и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('W_DISPATCHER');
# диспетчер, доступен GUI для установки статуса накладных и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('DISPATCHER');
# клиент, доступен GUI для для просмотра данных своих ЗН(описание_проекта.odt) и права только на SELECT с его заявкой
INSERT INTO user_roles (userRoleName) VALUES ('CLIENT');
INSERT INTO user_roles (userRoleName) VALUES ('MANAGER'); # торговый представитель, доступ только на чтение
# временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
# что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
INSERT INTO user_roles (userRoleName) VALUES ('TEMP_REMOVED');


CREATE TABLE users (
  userID INTEGER AUTO_INCREMENT,
  firstName VARCHAR(64) NOT NULL,
  lastName VARCHAR(64) NOT NULL,
  patronymic VARCHAR(64),
  login VARCHAR(128) NOT NULL,
  passMD5 VARCHAR(64) NOT NULL,
  phoneNumber VARCHAR(16) NOT NULL,
  email VARCHAR(64),
  userRoleID INTEGER NOT NULL,
  pointID INTEGER NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE permissions (
  permissionID INTEGER AUTO_INCREMENT,
  permissionName VARCHAR(64),
  PRIMARY KEY (permissionID)
);
INSERT INTO permissions (permissionName) VALUES ('updateUserRole');
INSERT INTO permissions (permissionName) VALUES ('updateUserAttributes');
INSERT INTO permissions (permissionName) VALUES ('insertUser');
INSERT INTO permissions (permissionName) VALUES ('deleteUser');
INSERT INTO permissions (permissionName) VALUES ('selectUser');

INSERT INTO permissions (permissionName) VALUES ('insertPoint');
INSERT INTO permissions (permissionName) VALUES ('updatePoint');
INSERT INTO permissions (permissionName) VALUES ('deletePoint');
INSERT INTO permissions (permissionName) VALUES ('selectPoint');

INSERT INTO permissions (permissionName) VALUES ('insertRoute');
INSERT INTO permissions (permissionName) VALUES ('updateRoute');
INSERT INTO permissions (permissionName) VALUES ('deleteRoute');
INSERT INTO permissions (permissionName) VALUES ('selectRoute');

INSERT INTO permissions (permissionName) VALUES ('updateInvoiceStatus');
INSERT INTO permissions (permissionName) VALUES ('updateRouteListStatus');
INSERT INTO permissions (permissionName) VALUES ('selectOwnHistory');



CREATE TABLE permissions_for_roles (
  roleID INTEGER,
  permissionID INTEGER,
  PRIMARY KEY (roleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES permissions (permissionID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (roleID) REFERENCES user_roles (userRoleID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE PROCEDURE inset_permission_for_role(IN user_role_name VARCHAR(64), IN permission_name VARCHAR(64))
  BEGIN
    INSERT INTO permissions_for_roles (roleID, permissionID) SELECT
                                                               user_roles.userRoleID,
                                                               permissions.permissionID
                                                             FROM user_roles, permissions
                                                             WHERE user_roles.userRoleName = user_role_name AND
                                                                   permissions.permissionName = permission_name;
  END;

# add all permissions to 'ADMIN'
INSERT INTO permissions_for_roles (roleID, permissionID)
  SELECT * FROM (SELECT userRoleID FROM user_roles WHERE userRoleName='ADMIN') as qwe1, (SELECT permissionID FROM permissions) AS qwe;

# add permissions to 'WAREHOUSE_MANAGER'
# TODO
# add permissions to 'MANAGER'
# TODO
# add permissions to 'CLIENT'
CALL inset_permission_for_role('CLIENT', 'selectUser');
CALL inset_permission_for_role('CLIENT', 'selectPoint');
CALL inset_permission_for_role('CLIENT', 'selectRoute');
# TODO
# add permissions to 'TEMP_REMOVED'


CREATE TABLE requizit (
  requizitID VARCHAR(16) PRIMARY KEY NOT NULL,
  INN VARCHAR(32) NOT NULL,
  KPP VARCHAR(64) NOT NULL,
  corAccount VARCHAR(64) NOT NULL,
  curAccount VARCHAR(64) NOT NULL,
  BIK VARCHAR(64) NOT NULL,
  bankName VARCHAR(128) NOT NULL,
  contractNumber VARCHAR(64),
  dateOfSigning DATE,
  startContractDate DATE,
  endContractDate DATE,
  isEnabled TINYINT DEFAULT 1 NOT NULL
);
CREATE UNIQUE INDEX RequizitID ON requizit (requizitID);

CREATE TABLE requizitsforuser (
  userID VARCHAR(16) NOT NULL,
  requizitID VARCHAR(16) NOT NULL,
  PRIMARY KEY (userID, requizitID),
  FOREIGN KEY (userID) REFERENCES users (userID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (requizitID) REFERENCES requizit (requizitID) ON DELETE CASCADE ON UPDATE CASCADE
);




CREATE TABLE pointTypes (
  pointTypeID INTEGER AUTO_INCREMENT,
  pointTypeText VARCHAR(64) NOT NULL,
  PRIMARY KEY (pointTypeID)
);
INSERT INTO pointTypes (pointTypeText) VALUES ('Склад');
INSERT INTO pointTypes (pointTypeText) VALUES ('Представительство');

CREATE TABLE points (
  pointID INTEGER,
  pointName VARCHAR(128) DEFAULT '' NOT NULL,
  pointTypeID VARCHAR(16) NOT NULL,
  region VARCHAR(128),
  district VARCHAR(64),
  locality VARCHAR(64) NOT NULL,
  mailIndex VARCHAR(6) NOT NULL,
  address VARCHAR(256) NOT NULL,
  email VARCHAR(64),
  phoneNumber VARCHAR(16) NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (pointTypeID) REFERENCES pointtype (pointTypeID) ON DELETE CASCADE ON UPDATE CASCADE
  );
CREATE UNIQUE INDEX PointID ON point (PointID);
CREATE UNIQUE INDEX PointID_2 ON point (PointID, PointName);


# goodsForInvoice - не нужная таблица
# количесвто коробок - атрибут накладной, количесвто паллет атрибут маршрутного листа
# dispatchersforpoint - не нужная таблица
# marketAgent == MANAGER должен присутвовать в каждой заявке пользователя, он её и утверждает
# создать отдельную таблицу клиенты это фактически список ИННов. Клинетом может быть например пятерочка.

CREATE TABLE clients (
  clientID INTEGER AUTO_INCREMENT,
  INN BIGINT,
  PRIMARY KEY (clientID)
);


CREATE VIEW manager_users AS SELECT users.userID FROM users WHERE users.userRoleID = 5; #TODO manager select

CREATE TABLE requests (
  requestID INTEGER NOT NULL,
  requestNumber VARCHAR(16) NOT NULL,
  date DATETIME NOT NULL,
  pointID INTEGER,
  managerUser INTEGER NOT NULL,
  clientID INTEGER NOT NULL,
  PRIMARY KEY (requestID),
  FOREIGN KEY (managerUser) REFERENCES manager_users (userID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)  ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE VIEW warehouse_points AS SELECT points.pointID FROM points WHERE points.pointTypeID = 1; #TODO select where 'Склад'
# invoice объеденяет в себе внутреннюю заявку и накладную,
# при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
CREATE TABLE invoices (
  invoiceID INTEGER,
  invoiceNumber VARCHAR(16) NOT NULL,
  date DATETIME NOT NULL,
  requestID INTEGER,
  warehousePoint INTEGER,
  sales_invoice VARCHAR(16), # расходная накладная
  PRIMARY KEY (invoiceID),
  FOREIGN KEY (requestID) REFERENCES requests (requestID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (warehousePoint) REFERENCES warehouse_points (pointID) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE(invoiceNumber)
);


CREATE TABLE status_list (
  invoiceStatusID INTEGER AUTO_INCREMENT,
  invoiceStatusName VARCHAR(32) NOT NULL,
  invoiceStatusRusName VARCHAR(128) NOT NULL,
  PRIMARY KEY (invoiceStatusID)
);
# insider request statuses
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('APPROVING', 'Выгружена на утверждение торговому представителю');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('RESERVED', 'Резерв');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('APPROVED', 'Утверждена к сборке');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('STOP_LIST', 'Стоп-лист');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('CREDIT_LIMIT', 'Кредитный лимит');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('RASH_CREATED', 'Создана расходная накладная');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('COLLECTING', 'Выдана на сборку');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('CHECK', 'На контроле');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('CHECK_PASSED', 'Контроль пройден');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('PACKAGING', 'Упаковано');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('READY', 'Проверка в зоне отгрузки/Готова к отправке');
# invoice statuses
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('DEPARTURE', 'Накладная убыла');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('ARRIVED', 'Накладная прибыла в пункт');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('ERROR', 'Ошибка. Возвращение в пункт');
INSERT INTO status_list (invoiceStatusName, invoiceStatusRusName) VALUES ('DELIVERED', 'Доставлено');


CREATE TABLE invoice_history (
  invoiceHistoryID BIGINT,
  timeMark DATETIME, # устанавливается пользователем либо из 1с
  invoiceStatusID INTEGER,
  boxQty INTEGER,
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES status_list (invoiceStatusID)
);




CREATE TABLE routes (
  routeID INTEGER NOT NULL,
  routeName VARCHAR(64) NOT NULL,
  PRIMARY KEY (RouteID)
);


CREATE TABLE route_points (
  routePointID INTEGER NOT NULL,
  pointID INTEGER NOT NULL,
  sortOrder INTEGER,
  tLoading INTEGER, # в минутах
  timeToNextPoint INTEGER, # в минутах
  distanceToNextPoint INTEGER, # в километрах
  arrivalTime TIME,
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE routeforinvoice (
  invoiceID VARCHAR(16) NOT NULL,
  routeID VARCHAR(16) NOT NULL,
  PRIMARY KEY (InvoiceID, RouteID),
  FOREIGN KEY (InvoiceID) REFERENCES invoice (InvoiceID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (RouteID) REFERENCES route (RouteID) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE rout_list_history (
  routListHistoryID BIGINT AUTO_INCREMENT,
  routeListID INTEGER,
  timeMark DATETIME,
  routListNumber VARCHAR(32),
  driver VARCHAR(255),
  licensePlate VARCHAR(9), # государственный номер автомобиля
  routeID INTEGER,
  invoiceID INTEGER,
  PRIMARY KEY (routListHistoryID),
  FOREIGN KEY (routeID) REFERENCES routes (routID),
  FOREIGN KEY (invoiceID) REFERENCES invoices (invoiceID)
);












