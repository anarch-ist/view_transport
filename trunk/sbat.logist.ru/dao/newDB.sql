SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';
DROP DATABASE IF EXISTS project_database;
CREATE DATABASE `project_database`
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE `project_database`;


#######################################################################################################################
#                                        USERS ROLES PERMISSIONS AND POINTS                                           #
#######################################################################################################################


CREATE TABLE user_roles (
  userRoleID VARCHAR(32),
  PRIMARY KEY (userRoleID)
);

INSERT INTO user_roles (userRoleID)
VALUES
  # администратор, ему доступен полный графический интерфейс сайта и самые высокие права на изменение в БД:
  # имеет право изменить роль пользователя
  ('ADMIN'),
  # диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
  ('W_DISPATCHER'),
  # диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
  ('DISPATCHER'),
  # пользователь клиента, доступен GUI для просмотра данных заявок клиента, которые проходят через пункт к которому привязан пользователь с ролью CLIENT_USER, возможность проставлять статус "доставлен"
  ('CLIENT_USER'),
  # пользователь клиента, доступен GUI для для просмотра всех заявок данного клиента, а не только тех, которые проходят через его пункт.
  ('CLIENT_MANAGER'),
  # торговый представитель, доступ только на чтение тех заявок, в которых он числится торговым
  ('MARKET_AGENT'),
  # временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
  # что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
  ('TEMP_REMOVED');

CREATE TABLE point_types (
  pointTypeID      VARCHAR(32),
  pointTypeRusName VARCHAR(32) NOT NULL,
  PRIMARY KEY (pointTypeID)
);

INSERT INTO point_types
VALUES
  ('WAREHOUSE', 'Склад'),
  ('AGENCY', 'Представительство');

CREATE FUNCTION getPointIDByName(name VARCHAR(128))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT pointID
                  FROM points
                  WHERE name = pointName);
    RETURN result;
  END;

CREATE TABLE points (
  pointID     INTEGER AUTO_INCREMENT,
  pointName   VARCHAR(128) NOT NULL,
  region      VARCHAR(128) NULL,
  district    VARCHAR(64)  NULL,
  locality    VARCHAR(64)  NULL,
  mailIndex   VARCHAR(6)   NULL,
  address     VARCHAR(256) NOT NULL,
  email       VARCHAR(64)  NULL,
  phoneNumber VARCHAR(16)  NULL,
  pointTypeID VARCHAR(32)  NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE (pointName)
);

CREATE FUNCTION getUserIDByLogin(_login VARCHAR(128))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT userID
                  FROM users
                  WHERE login = _login);
    RETURN result;
  END;

CREATE TABLE users (
  userID      INTEGER AUTO_INCREMENT,
  firstName   VARCHAR(64)  NULL,
  lastName    VARCHAR(64)  NULL,
  patronymic  VARCHAR(64)  NULL,
  login       VARCHAR(128) NOT NULL,
  passMD5     VARCHAR(64)  NOT NULL,
  phoneNumber VARCHAR(16)  NULL,
  email       VARCHAR(64)  NULL,
  userRoleID  VARCHAR(32)  NOT NULL,
  pointID     INTEGER      NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE (login)
);

CREATE TABLE permissions (
  permissionID VARCHAR(32),
  PRIMARY KEY (permissionID)
);

INSERT INTO permissions (permissionID)
VALUES
  ('updateUserRole'),
  ('updateUserAttributes'),
  ('insertUser'),
  ('deleteUser'),
  ('selectUser'),
  ('insertPoint'),
  ('updatePoint'),
  ('deletePoint'),
  ('selectPoint'),
  ('insertRoute'),
  ('updateRoute'),
  ('deleteRoute'),
  ('selectRoute'),
  ('updateInvoiceStatus'),
  ('updateRouteListStatus'),
  ('selectOwnHistory');

CREATE TABLE permissions_for_roles (
  userRoleID   VARCHAR(32),
  permissionID VARCHAR(32),
  PRIMARY KEY (userRoleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES permissions (permissionID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE PROCEDURE insert_permission_for_role(IN user_role_name VARCHAR(32), IN permission_name VARCHAR(32))
  BEGIN
    INSERT INTO permissions_for_roles (userRoleID, permissionID) SELECT
                                                                   user_roles.userRoleID,
                                                                   permissions.permissionID
                                                                 FROM user_roles, permissions
                                                                 WHERE user_roles.userRoleID = user_role_name AND
                                                                       permissions.permissionID = permission_name;
  END;

# add all permissions to 'ADMIN'
INSERT INTO permissions_for_roles (userRoleID, permissionID)
  SELECT *
  FROM (SELECT userRoleID
        FROM user_roles
        WHERE userRoleID = 'ADMIN') AS qwe1, (SELECT permissionID
                                              FROM permissions) AS qwe;
# TODO add permissions to 'WAREHOUSE_MANAGER'
# TODO add permissions to 'MANAGER'
# TODO add permissions to 'TEMP_REMOVED'
CALL insert_permission_for_role('CLIENT', 'selectUser');
CALL insert_permission_for_role('CLIENT', 'selectPoint');
CALL insert_permission_for_role('CLIENT', 'selectRoute');


#######################################################################################################################
#                                                 CLIENTS AND REQUESTS                                                #
#######################################################################################################################


CREATE FUNCTION getClientIDByINN(_INN VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT clientID
                  FROM clients
                  WHERE INN = _INN);
    RETURN result;
  END;

CREATE TABLE clients (
  clientID          INTEGER AUTO_INCREMENT,
  INN               VARCHAR(32)  NOT NULL,
  KPP               VARCHAR(64)  NULL,
  corAccount        VARCHAR(64)  NULL,
  curAccount        VARCHAR(64)  NULL,
  BIK               VARCHAR(64)  NULL,
  bankName          VARCHAR(128) NULL,
  contractNumber    VARCHAR(64)  NULL,
  dateOfSigning     DATE         NULL,
  startContractDate DATE         NULL,
  endContractDate   DATE         NULL,
  PRIMARY KEY (clientID),
  UNIQUE (INN)
);

CREATE FUNCTION getRequestIDByNumber(_requestNumber VARCHAR(16))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT requestID
                  FROM requests
                  WHERE requestNumber = _requestNumber);
    RETURN result;
  END;

# insert only manager users
CREATE TABLE requests (
  requestID          INTEGER AUTO_INCREMENT,
  requestNumber      VARCHAR(16) NOT NULL,
  date               DATETIME    NOT NULL,
  marketAgentUserID  INTEGER     NULL,
  clientID           INTEGER     NULL,
  destinationPointID INTEGER     NULL, # адрес, куда должны быть доставлены все товары
  PRIMARY KEY (requestID),
  FOREIGN KEY (marketAgentUserID) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

CREATE FUNCTION is_market_agent(_userID INTEGER)
  RETURNS BOOLEAN
  BEGIN
    DECLARE userRole VARCHAR(32);
    SET userRole = (SELECT userRoleID
                    FROM users
                    WHERE userID = _userID);
    RETURN (userRole = 'MARKET_AGENT');
  END;

CREATE TRIGGER before_request_insert
BEFORE INSERT ON requests FOR EACH ROW
  BEGIN
    IF NOT (is_market_agent(NEW.marketAgentUserID))
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot insert row: only manager users allowed';
    END IF;
  END;


#######################################################################################################################
#                                          ROUTE AND ROUTE LISTS                                                      #
#######################################################################################################################

CREATE FUNCTION getRoutIDByRoutName(_routName VARCHAR(64))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeID
                  FROM routes
                  WHERE routeName = _routName);
    RETURN result;
  END;

CREATE TABLE routes (
  routeID       INTEGER AUTO_INCREMENT,
  routeName     VARCHAR(64)  NOT NULL,
  directionName VARCHAR(255) NULL, # имя направления из предметной области 1С, каждому направлению соответствует один маршрут
  PRIMARY KEY (routeID)
);

CREATE TABLE route_list_statuses (
  routeListStatusID VARCHAR(32),
  PRIMARY KEY (routeListStatusID)
);

INSERT INTO route_list_statuses
VALUES
  ('CREATED'),
  ('UPDATED'),
  ('DELETED');

CREATE TABLE route_points (
  routePointID        INTEGER AUTO_INCREMENT,
  sortOrder           INTEGER NOT NULL,
  tLoading            INTEGER NOT NULL, # в минутах
  timeToNextPoint     INTEGER NOT NULL, # в минутах
  distanceToNextPoint INTEGER NOT NULL, # в километрах
  arrivalTime         TIME    NOT NULL,
  pointID             INTEGER NOT NULL,
  routeID             INTEGER NULL, #TODO почему тут NULL? как точка маршрута может существовать без ссылки на маршрут?
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE FUNCTION getRouteListIDByNumber(_routListNumber VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeListID
                  FROM route_lists
                  WHERE routListNumber = _routListNumber);
    RETURN result;
  END;

CREATE FUNCTION getRoutPointIDByRoutNameAndSortOrder(_routName VARCHAR(64), _sortOrder INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routePointID
                  FROM route_points
                  WHERE routeID = getRoutIDByRoutName(_routName) AND sortOrder = _sortOrder);
    RETURN result;
  END;

CREATE TABLE route_lists (
  routeListID             INTEGER AUTO_INCREMENT,
  routListNumber          VARCHAR(32)  NOT NULL,
  palletsQty              INTEGER      NULL,
  driver                  VARCHAR(255) NULL,
  licensePlate            VARCHAR(9)   NULL, # государственный номер автомобиля
  #TODO обсудить
  #TODO как мы остлеживаем изменение статуса маршрутного листа(должно быть поле - последний посещенный пункт)?
  lastVisitedRoutePointID INTEGER      NOT NULL, # значение в этом поле выставляет диспетчер вручную
  PRIMARY KEY (routeListID),
  FOREIGN KEY (lastVisitedRoutePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (routListNumber)
);

CREATE PROCEDURE insert_into_rout_list_history(
  routeListID             INTEGER,
  routListNumber          VARCHAR(32),
  palletsQty              INTEGER,
  driver                  VARCHAR(255),
  licensePlate            VARCHAR(9),
  lastVisitedRoutePointID INTEGER,
  routeListStatusID       VARCHAR(32)
)
  BEGIN
    INSERT INTO rout_list_history (timeMark, routeListID, routListNumber, palletsQty, driver, licensePlate, lastVisitedRoutePointID, routeListStatusID)
    VALUES
      (NOW(), routeListID, routListNumber, palletsQty, driver, licensePlate, lastVisitedRoutePointID,
       routeListStatusID);
  END;


CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.licensePlate, NEW.lastVisitedRoutePointID, 'CREATED');
  END;

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.licensePlate, NEW.lastVisitedRoutePointID, 'UPDATED');
  END;

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(OLD.routeListID, OLD.routListNumber, OLD.palletsQty, OLD.driver,
                                       OLD.licensePlate, OLD.lastVisitedRoutePointID, 'DELETED');
  END;

CREATE TABLE rout_list_history (
  routeListHistoryID      BIGINT AUTO_INCREMENT,
  timeMark                DATETIME,
  routeListID             INTEGER,
  routListNumber          VARCHAR(32)  NOT NULL,
  palletsQty              INTEGER      NULL,
  driver                  VARCHAR(255) NULL,
  licensePlate            VARCHAR(9)   NULL, # государственный номер автомобиля
  lastVisitedRoutePointID INTEGER      NOT NULL,
  routeListStatusID       VARCHAR(32)  NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (routeListStatusID) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


#######################################################################################################################
#                                                INVOICES                                                             #
#######################################################################################################################


CREATE TABLE invoice_statuses (
  invoiceStatusID      VARCHAR(32),
  invoiceStatusRusName VARCHAR(128) NOT NULL,
  PRIMARY KEY (invoiceStatusID)
);

INSERT INTO invoice_statuses
VALUES
  # duty statuses
  ('CREATED', 'Внутренняя заявка добавлена в БД'),
  ('DELETED', 'Внутренняя заявка удалена из БД'),
  # insider request statuses
  ('APPROVING', 'Выгружена на утверждение торговому представителю'),
  ('RESERVED', 'Резерв'),
  ('APPROVED', 'Утверждена к сборке'),
  ('STOP_LIST', 'Стоп-лист'),
  ('CREDIT_LIMIT', 'Кредитный лимит'),
  ('RASH_CREATED', 'Создана расходная накладная'),
  ('COLLECTING', 'Выдана на сборку'),
  ('CHECK', 'На контроле'),
  ('CHECK_PASSED', 'Контроль пройден'),
  ('PACKAGING', 'Упаковано'),
  ('READY', 'Проверка в зоне отгрузки/Готова к отправке'),
  # invoice statuses
  ('DEPARTURE', 'Накладная убыла'),
  ('ARRIVED', 'Накладная прибыла в пункт'),
  ('ERROR', 'Ошибка. Возвращение в пункт'),
  ('DELIVERED', 'Доставлено');


# invoice объеденяет в себе внутреннюю заявку и накладную,
# при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
CREATE TABLE invoices (
  invoiceID            INTEGER AUTO_INCREMENT,
  insiderRequestNumber VARCHAR(16) NOT NULL,
  invoiceNumber        VARCHAR(16) NOT NULL,
  creationDate         DATETIME    NULL,
  deliveryDate         DATETIME    NULL,
  boxQty               INTEGER     NULL,
  sales_invoice        VARCHAR(16) NULL, # расходная накладная
  invoiceStatusID      VARCHAR(32) NOT NULL,
  requestID            INTEGER     NOT NULL,
  warehousePointID     INTEGER     NOT NULL,
  routeListID          INTEGER     NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  FOREIGN KEY (warehousePointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (routeListID) REFERENCES route_lists (routeListID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  UNIQUE (insiderRequestNumber),
  UNIQUE (invoiceNumber)
);

CREATE FUNCTION is_warehouse(_pointID INTEGER)
  RETURNS BOOLEAN
  BEGIN
    DECLARE pointType VARCHAR(32);
    SET pointType = (SELECT pointTypeID
                     FROM points
                     WHERE pointID = _pointID);
    RETURN (pointType = 'WAREHOUSE');
  END;

CREATE TRIGGER before_invoice_insert
BEFORE INSERT ON invoices FOR EACH ROW
  BEGIN
    IF NOT (is_warehouse(NEW.warehousePointID))
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot insert row: only warehouse points allowed';
    END IF;
  END;


CREATE PROCEDURE insert_into_invoice_history(
  invoiceID            INTEGER,
  insiderRequestNumber VARCHAR(16),
  invoiceNumber        VARCHAR(16),
  creationDate         DATETIME,
  deliveryDate         DATETIME,
  boxQty               INTEGER,
  sales_invoice        VARCHAR(16),
  invoiceStatusID      VARCHAR(32),
  requestID            INTEGER,
  warehousePointID     INTEGER,
  routeListID          INTEGER
)
  BEGIN
    INSERT INTO invoice_history (timeMark, invoiceID, insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, sales_invoice, invoiceStatusID, requestID, warehousePointID, routeListID)
    VALUES
      (NOW(), invoiceID, insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, sales_invoice,
       invoiceStatusID, requestID,
       warehousePointID, routeListID);
  END;

CREATE TRIGGER after_invoice_insert AFTER INSERT ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      NEW.invoiceID, NEW.insiderRequestNumber, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.sales_invoice,
      'CREATED', NEW.requestID, NEW.warehousePointID, NEW.routeListID);

CREATE TRIGGER after_invoice_update AFTER UPDATE ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      NEW.invoiceID, NEW.insiderRequestNumber, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.sales_invoice,
      NEW.invoiceStatusID, NEW.requestID, NEW.warehousePointID, NEW.routeListID);

CREATE TRIGGER after_invoice_delete AFTER DELETE ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      OLD.invoiceID, OLD.insiderRequestNumber, OLD.invoiceNumber, OLD.creationDate, OLD.deliveryDate, OLD.boxQty, OLD.sales_invoice,
      'DELETED', OLD.requestID, OLD.warehousePointID, OLD.routeListID);

CREATE TABLE invoice_history (
  invoiceHistoryID     BIGINT AUTO_INCREMENT,
  timeMark             DATETIME,
  invoiceID            INTEGER,
  insiderRequestNumber VARCHAR(16) NOT NULL,
  invoiceNumber        VARCHAR(16) NOT NULL,
  creationDate         DATETIME    NULL,
  deliveryDate         DATETIME    NULL,
  boxQty               INTEGER     NULL,
  sales_invoice        VARCHAR(16) NULL, # расходная накладная
  invoiceStatusID      VARCHAR(32) NOT NULL,
  requestID            INTEGER     NOT NULL,
  warehousePointID     INTEGER     NOT NULL,
  routeListID          INTEGER     NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


#######################################################################################################################
#                                               USER ACTION HISTORY                                                   #
#######################################################################################################################


CREATE TABLE tables (
  tableID   INTEGER AUTO_INCREMENT,
  tableName VARCHAR(64) NOT NULL,
  PRIMARY KEY (tableID)
);

INSERT INTO tables (tableName) SELECT information_schema.TABLES.TABLE_NAME
                               FROM information_schema.TABLES
                               WHERE TABLE_SCHEMA = 'project_database';

#TODO определить, какие таблицы должны хранить информацию об изменении пользователями
CREATE TABLE user_action_history (
  userActionHistoryID BIGINT AUTO_INCREMENT,
  timeMark            DATETIME,
  userID              INTEGER,
  tableID             INTEGER,
  PRIMARY KEY (userActionHistoryID),
  FOREIGN KEY (tableID) REFERENCES tables (tableID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


