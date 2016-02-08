SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';
DROP DATABASE IF EXISTS transmaster_transport_db;
CREATE DATABASE `transmaster_transport_db`
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE `transmaster_transport_db`;


-- -------------------------------------------------------------------------------------------------------------------
--                                                  DATA SOURCES
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE data_sources (
  dataSourceID VARCHAR(32) PRIMARY KEY
);
INSERT INTO data_sources VALUES ('LOGIST_1C');


-- -------------------------------------------------------------------------------------------------------------------
--                                        USERS ROLES PERMISSIONS AND POINTS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE user_roles (
  userRoleID      VARCHAR(32),
  userRoleRusName VARCHAR(128),
  PRIMARY KEY (userRoleID)
);

INSERT INTO user_roles (userRoleID, userRoleRusName)
VALUES
-- администратор, ему доступен полный графический интерфейс сайта и самые высокие права на изменение в БД:
-- имеет право изменить роль пользователя
  ('ADMIN', 'Администратор'),
-- диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
  ('W_DISPATCHER', 'Диспетчер_склада'),
-- диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
  ('DISPATCHER', 'Диспетчер'),
-- пользователь клиента, доступен GUI для для просмотра всех заявок данного клиента, а не только тех, которые проходят через его пункт.
  ('CLIENT_MANAGER', 'Пользователь_клиента'),
-- торговый представитель, доступ только на чтение тех заявок, в которых он числится торговым
  ('MARKET_AGENT', 'Торговый_представитель'),
-- временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
-- что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
  ('TEMP_REMOVED', 'Временно_удален'),

  ('VIEW_LAST_TEN', 'Последние_десять');

CREATE TABLE point_types (
  pointTypeID VARCHAR(32),
  PRIMARY KEY (pointTypeID)
);

INSERT INTO point_types
VALUES
  ('WAREHOUSE'),
  ('AGENCY');

CREATE TABLE points (
  pointID             INTEGER AUTO_INCREMENT,
  pointIDExternal     VARCHAR(128)   NOT NULL,
  dataSourceID        VARCHAR(32)    NOT NULL,
  pointName           VARCHAR(128)   NOT NULL,
  region              VARCHAR(128)   NULL,
  timeZone            TINYINT SIGNED NULL, -- сдвиг времени по гринвичу GMT + value
  docs                TINYINT SIGNED NULL, -- количество окон разгрузки
  comments            LONGTEXT       NULL,
  openTime            TIME           NULL, -- например 9:00
  closeTime           TIME           NULL, -- например 17:00
  district            VARCHAR(64)    NULL,
  locality            VARCHAR(64)    NULL,
  mailIndex           VARCHAR(6)     NULL,
  address             TEXT           NOT NULL,
  email               VARCHAR(255)    NULL,
  phoneNumber         VARCHAR(16)    NULL,
  responsiblePersonId VARCHAR(128)   NULL,
  pointTypeID         VARCHAR(32)    NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE(pointIDExternal, dataSourceID)
);

-- служебная запись, этот пункт используется для тех накладных у которых нет склада
INSERT INTO points (pointID, pointIDExternal, dataSourceID, pointName, region, timeZone, docs, comments, openTime, closeTime, district, locality, mailIndex, address, email, phoneNumber, responsiblePersonId, pointTypeID) VALUE
  (1, 'def', 'LOGIST_1C', 'NO_W_POINT', '', 0, 0, '', '00:00:00', '00:00:00', '', '', '', '', '', '', '', 'WAREHOUSE');

-- CONSTRAINT pointIDFirst must not be equal pointIDSecond
CREATE TABLE distances_between_points (
  pointIDFirst  INTEGER,
  pointIDSecond INTEGER,
  distance      SMALLINT, -- distance between routePoints measured in km.
  PRIMARY KEY (pointIDFirst, pointIDSecond),
  FOREIGN KEY (pointIDFirst) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (pointIDSecond) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE users (
  userID      INTEGER     AUTO_INCREMENT,
  login       VARCHAR(255) NOT NULL UNIQUE, -- userIDExternal
  firstName   VARCHAR(64)  NULL,
  lastName    VARCHAR(64)  NULL,
  patronymic  VARCHAR(64)  NULL,
  userName    VARCHAR(255) NULL,
  phoneNumber VARCHAR(255) NULL,
  email       VARCHAR(255) NULL,
  position    VARCHAR(64)  NULL, -- должность
  salt        VARCHAR(16) DEFAULT 'anqh14dajk4sn2j3', -- соль, нужна для защиты паролей
  passAndSalt VARCHAR(64)  NOT NULL,
  userRoleID  VARCHAR(32)  NOT NULL,
  pointID     INTEGER      NULL, -- у пользователя не обязан быть пункт.
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
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

-- add all permissions to 'ADMIN'
INSERT INTO permissions_for_roles (userRoleID, permissionID)
  SELECT *
  FROM (SELECT userRoleID
        FROM user_roles
        WHERE userRoleID = 'ADMIN') AS qwe1, (SELECT permissionID
                                              FROM permissions) AS qwe;
-- TODO add permissions to 'WAREHOUSE_MANAGER'
-- TODO add permissions to 'MANAGER'
-- TODO add permissions to 'TEMP_REMOVED'
CALL insert_permission_for_role('CLIENT', 'selectUser');
CALL insert_permission_for_role('CLIENT', 'selectPoint');
CALL insert_permission_for_role('CLIENT', 'selectRoute');

INSERT INTO users VALUES (NULL, 'parser', '', '', '', '','', 'fff', md5(CONCAT(md5('nolpitf43gwer'), 'fff')), '', '', 'ADMIN', NULL);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 CLIENTS AND REQUESTS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE clients (
  clientID          INTEGER AUTO_INCREMENT,
  clientIDExternal  VARCHAR(255) NOT NULL,
  dataSourceID      VARCHAR(32)  NOT NULL,
  INN               VARCHAR(32)  NOT NULL,
  clientName        VARCHAR(255) NULL,
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
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE(clientIDExternal, dataSourceID)
);

-- insert only manager users
CREATE TABLE requests (
  requestID          INTEGER AUTO_INCREMENT,
  requestIDExternal  VARCHAR(128) NOT NULL,
  dataSourceID       VARCHAR(32)  NOT NULL,
  requestNumber      VARCHAR(16)  NOT NULL,
  creationDate       DATETIME     NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
  marketAgentUserID  INTEGER      NULL,
  clientID           INTEGER      NULL,
  destinationPointID INTEGER      NULL, -- адрес, куда должны быть доставлены все товары
  PRIMARY KEY (requestID),
  FOREIGN KEY (marketAgentUserID) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE(requestIDExternal, dataSourceID)
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
      SET MESSAGE_TEXT = 'SERGEY ERROR: Can\'t insert row: only MARKET_AGENT users allowed';
    END IF;
  END;


-- -------------------------------------------------------------------------------------------------------------------
--                                          ROUTE AND ROUTE LISTS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE tariffs (
  tariffID INTEGER AUTO_INCREMENT,
  cost     DECIMAL(12, 2) NULL, -- цена доставки
  capacity DECIMAL(4, 2)  NULL, -- грузоподъёмность в тоннах
  carrier  VARCHAR(64), -- перевозчик
  PRIMARY KEY (tariffID)
);

-- zero time is 00:00(GMT) of that day, when carrier arrives at first point of route.
CREATE TABLE routes (
  routeID               INTEGER AUTO_INCREMENT,
  directionIDExternal   VARCHAR(255)             NOT NULL,
  dataSourceID          VARCHAR(32)              NOT NULL,
  routeName             VARCHAR(255)             NOT NULL,
  firstPointArrivalTime TIME DEFAULT '00:00:00'  NOT NULL,
  daysOfWeek            SET('monday',
                            'tuesday',
                            'wednesday',
                            'thursday',
                            'friday',
                            'saturday',
                            'sunday') DEFAULT '' NOT NULL,
  tariffID              INTEGER                  NULL,
  PRIMARY KEY (routeID),
  FOREIGN KEY (tariffID) REFERENCES tariffs (tariffID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE (routeName),
  UNIQUE (directionIDExternal, dataSourceID)
);

CREATE TABLE route_list_statuses (
  routeListStatusID      VARCHAR(32),
  routeListStatusRusName VARCHAR(255),
  PRIMARY KEY (routeListStatusID)
);

INSERT INTO route_list_statuses
VALUES
  ('CREATED', 'Маршрутный лист создается'),
  ('APPROVED', 'Формирование маршрутного листа завершено');

CREATE TABLE route_points (
  routePointID             INTEGER AUTO_INCREMENT,
  sortOrder                INTEGER NOT NULL,
  timeForLoadingOperations INTEGER NOT NULL, -- time in minutes
  pointID                  INTEGER NOT NULL,
  routeID                  INTEGER NOT NULL,
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- CONSTRAINT routePointIDFirst must not be equal routePointIDSecond
CREATE TABLE relations_between_route_points (
  routePointIDFirst  INTEGER,
  routePointIDSecond INTEGER,
  timeForDistance    INTEGER, -- time in minutes for carrier drive through distance
  PRIMARY KEY (routePointIDFirst, routePointIDSecond),
  FOREIGN KEY (routePointIDFirst) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routePointIDSecond) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- ВНИМАНИЕ! при изменении в этой таблице нужно согласовать все с триггерами и таблицей route_list_history
CREATE TABLE route_lists (
  routeListID         INTEGER AUTO_INCREMENT,
  routeListIDExternal VARCHAR(32)  NOT NULL,
  dataSourceID        VARCHAR(32)  NOT NULL,
  routeListNumber     VARCHAR(32)  NOT NULL,
  creationDate        DATE         NULL,
  departureDate       DATE         NULL,
  palletsQty          INTEGER      NULL,
  driverId            VARCHAR(255) NULL,
  driverPhoneNumber   VARCHAR(12)  NULL,
  licensePlate        VARCHAR(9)   NULL, -- государственный номер автомобиля
  status              VARCHAR(32)  NOT NULL,
  routeID             INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE (routeListNumber),
  UNIQUE (routeListIDExternal, dataSourceID)
);


CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
           NEW.departureDate, NEW.palletsQty, NEW.driverId,
           NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'CREATED');

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
           NEW.departureDate, NEW.palletsQty, NEW.driverId,
           NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'UPDATED');

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), OLD.routeListID, OLD.routeListIDExternal, OLD.dataSourceID, OLD.routeListNumber, OLD.creationDate,
           OLD.departureDate, OLD.palletsQty, OLD.driverId,
           OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.routeID, 'DELETED');


CREATE TABLE route_list_history (
  routeListHistoryID  BIGINT AUTO_INCREMENT,
  timeMark            DATETIME                              NOT NULL,
  routeListID         INTEGER                               NOT NULL,
  routeListIDExternal VARCHAR(32)                           NOT NULL,
  dataSourceID        VARCHAR(32)                           NOT NULL,
  routeListNumber     VARCHAR(32)                           NOT NULL,
  creationDate        DATE                                  NULL,
  departureDate       DATE                                  NULL,
  palletsQty          INTEGER                               NULL,
  driverId            VARCHAR(255)                          NULL,
  driverPhoneNumber   VARCHAR(12)                           NULL,
  licensePlate        VARCHAR(9)                            NULL, -- государственный номер автомобиля
  status              VARCHAR(32)                           NOT NULL,
  routeID             INTEGER                               NOT NULL,
  dutyStatus          ENUM('CREATED', 'UPDATED', 'DELETED') NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                INVOICES
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE invoice_statuses (
  invoiceStatusID      VARCHAR(32),
  invoiceStatusRusName VARCHAR(128),
  sequence             TINYINT,
  PRIMARY KEY (invoiceStatusID)
);

INSERT INTO invoice_statuses
VALUES
-- duty statuses
  ('CREATED', 'Внутренняя заявка добавлена в БД', 0),
  ('DELETED', 'Внутренняя заявка удалена из БД', -1),
-- insider request statuses
  ('SAVED', 'Заявка в состоянии черновика', 1),
  ('APPROVING', 'Выгружена на утверждение торговому представителю', 2),
  ('RESERVED', 'Резерв', 3),
  ('APPROVED', 'Утверждена к сборке', 4),
  ('STOP_LIST', 'Стоп-лист', -2),
  ('CREDIT_LIMIT', 'Кредитный лимит', -3),
  ('RASH_CREATED', 'Создана расходная накладная', 5),
  ('COLLECTING', 'Выдана на сборку', 6),
  ('CHECK', 'На контроле', 7),
  ('CHECK_PASSED', 'Контроль пройден', 8),
  ('ADJUSTMENTS_MADE', 'Сделаны коректировки\распечатаны документы', 9),
  ('PACKAGING', 'Упаковано', 10),
  ('CHECK_BOXES', 'Проверка коробок в зоне отгрузки', 11),
  ('READY', 'Проверка в зоне отгрузки/Готова к отправке', 12),
  ('TRANSPORTATION', 'Маршрутный лист закрыт, товар передан экспедитору на погрузку', 13),
-- invoice statuses
  ('DEPARTURE', 'В транзите', 14),
  ('ARRIVED', 'Накладная прибыла в пункт', 15),
  ('ERROR', 'Ошибка. Возвращение в пункт', -4),
  ('DELIVERED', 'Доставлено', 16);

CREATE TABLE invoice_statuses_for_user_role (
  userRoleID      VARCHAR(32),
  invoiceStatusID VARCHAR(32),
  PRIMARY KEY (userRoleID, invoiceStatusID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
);

-- TODO fill this list
INSERT INTO invoice_statuses_for_user_role
VALUES
  ('ADMIN', 'APPROVING'),
  ('ADMIN', 'RESERVED'),
  ('ADMIN', 'APPROVED'),
  ('ADMIN', 'STOP_LIST'),
  ('ADMIN', 'READY'),
  ('ADMIN', 'DEPARTURE'),
  ('ADMIN', 'ERROR'),
  ('W_DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'ARRIVED'),
  ('DISPATCHER', 'ERROR'),
  ('DISPATCHER', 'DELIVERED'),
  ('MARKET_AGENT', 'ERROR'),
  ('MARKET_AGENT', 'DELIVERED');


-- invoice объеденяет в себе внутреннюю заявку и накладную,
-- при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
-- ВНИМАНИЕ! при изменении порядка полей их также нужно менять в триггерах и в таблице invoice_history!!!
CREATE TABLE invoices (
  invoiceID              INTEGER AUTO_INCREMENT,
  invoiceIDExternal      VARCHAR(255)   NOT NULL,
  dataSourceID           VARCHAR(32)    NOT NULL,
  documentNumber         VARCHAR(255)   NOT NULL,
  documentDate           DATETIME       NULL,
  firma                  VARCHAR(255)   NULL,
  contactName            VARCHAR(255)   NULL,
  contactPhone           VARCHAR(255)   NULL,
  creationDate           DATETIME       NULL,
  deliveryDate           DATETIME       NULL,
  boxQty                 INTEGER        NULL,
  weight                 INTEGER        NULL, -- масса в граммах
  volume                 INTEGER        NULL, -- в кубических сантиметрах
  goodsCost              DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  lastStatusUpdated      DATETIME       NULL, -- date and time when status was updated by any user
  lastModifiedBy         INTEGER        NULL, -- один из пользователей - это parser.
  invoiceStatusID        VARCHAR(32)    NOT NULL,
  commentForStatus       TEXT           NULL,
  requestID              INTEGER        NOT NULL,
  warehousePointID       INTEGER        NOT NULL,
  routeListID            INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedUserPointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
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
  FOREIGN KEY (lastVisitedUserPointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (invoiceIDExternal, dataSourceID)
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
      SET MESSAGE_TEXT = 'SERGEY ERROR: Can\'t insert row: only warehouse points allowed';
    END IF;
  END;


CREATE TRIGGER after_invoice_insert AFTER INSERT ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
  VALUE
    (NULL, NOW(), NEW.invoiceID, NEW.invoiceIDExternal, NEW.dataSourceID, NEW.documentNumber, NEW.documentDate,
     NEW.firma, NEW.contactName, NEW.contactPhone, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.weight,
     NEW.volume, NEW.goodsCost, NEW.lastStatusUpdated, NEW.lastModifiedBy, 'CREATED', NEW.commentForStatus,
     NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedUserPointID);

CREATE TRIGGER after_invoice_update AFTER UPDATE ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
    VALUE
    (NULL, NOW(), NEW.invoiceID, NEW.invoiceIDExternal, NEW.dataSourceID, NEW.documentNumber, NEW.documentDate,
     NEW.firma, NEW.contactName, NEW.contactPhone, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.weight,
     NEW.volume, NEW.goodsCost, NEW.lastStatusUpdated, NEW.lastModifiedBy, NEW.invoiceStatusID, NEW.commentForStatus,
     NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedUserPointID);


CREATE TRIGGER after_invoice_delete AFTER DELETE ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
    VALUE
    (NULL, NOW(), OLD.invoiceID, OLD.invoiceIDExternal, OLD.dataSourceID, OLD.documentNumber, OLD.documentDate,
     OLD.firma, OLD.contactName, OLD.contactPhone, OLD.creationDate, OLD.deliveryDate, OLD.boxQty, OLD.weight,
     OLD.volume, OLD.goodsCost, OLD.lastStatusUpdated, OLD.lastModifiedBy, 'CREATED', OLD.commentForStatus,
     OLD.requestID, OLD.warehousePointID, OLD.routeListID, OLD.lastVisitedUserPointID);

CREATE TRIGGER before_invoice_update BEFORE UPDATE ON invoices
FOR EACH ROW
  BEGIN
-- берем пользователя, который изменил статус на один из invoice statuses, затем находим его пункт, и этот
-- пункт записываем в таблицу invoices в поле lastVisitedUserPointID
    IF (NEW.invoiceStatusID = 'DEPARTURE' OR NEW.invoiceStatusID = 'ARRIVED' OR NEW.invoiceStatusID = 'ERROR' OR
        NEW.invoiceStatusID = 'DELIVERED')
    THEN
      BEGIN
        SET NEW.lastVisitedUserPointID = (SELECT pointID
                                          FROM users
                                          WHERE userID = NEW.lastModifiedBy);
      END;
    END IF;
  END;

CREATE TABLE invoice_history (
  invoiceHistoryID       BIGINT AUTO_INCREMENT,
  autoTimeMark           DATETIME       NOT NULL,
  invoiceID              INTEGER        NOT NULL,
  invoiceIDExternal      VARCHAR(255)   NOT NULL,
  dataSourceID           VARCHAR(32)    NOT NULL,
  documentNumber         VARCHAR(255)   NOT NULL,
  documentDate           DATETIME       NULL,
  firma                  VARCHAR(255)   NULL,
  contactName            VARCHAR(255)   NULL,
  contactPhone           VARCHAR(255)   NULL,
  creationDate           DATETIME       NULL,
  deliveryDate           DATETIME       NULL,
  boxQty                 INTEGER        NULL,
  weight                 INTEGER        NULL, -- масса в граммах
  volume                 INTEGER        NULL, -- в кубических сантиметрах
  goodsCost              DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  lastStatusUpdated      DATETIME       NULL, -- date and time when status was updated by any user
  lastModifiedBy         INTEGER        NULL, -- один из пользователей - это parser.
  invoiceStatusID        VARCHAR(32)    NOT NULL,
  commentForStatus       TEXT           NULL,
  requestID              INTEGER        NOT NULL,
  warehousePointID       INTEGER        NOT NULL,
  routeListID            INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedUserPointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                   GETTERS
-- -------------------------------------------------------------------------------------------------------------------


CREATE FUNCTION getPointIDByName(name VARCHAR(128))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT pointID
                  FROM points
                  WHERE name = pointName);
    RETURN result;
  END;

CREATE FUNCTION getUserIDByLogin(_login VARCHAR(64))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT userID
                  FROM users
                  WHERE login = _login);
    RETURN result;
  END;

CREATE FUNCTION getClientIDByINN(_INN VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT clientID
                  FROM clients
                  WHERE INN = _INN);
    RETURN result;
  END;

CREATE FUNCTION getRequestIDByNumber(_requestNumber VARCHAR(16))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT requestID
                  FROM requests
                  WHERE requestNumber = _requestNumber);
    RETURN result;
  END;

CREATE FUNCTION getRouteIDByRouteName(_routeName VARCHAR(64))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeID
                  FROM routes
                  WHERE routeName = _routeName);
    RETURN result;
  END;

CREATE FUNCTION getRouteListIDByNumber(_routeListNumber VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeListID
                  FROM route_lists
                  WHERE routeListNumber = _routeListNumber);
    RETURN result;
  END;

CREATE FUNCTION getRoutePointIDByRouteNameAndSortOrder(_routeName VARCHAR(64), _sortOrder INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routePointID
                  FROM route_points
                  WHERE routeID = getRouteIDByRouteName(_routeName) AND sortOrder = _sortOrder);
    RETURN result;
  END;

CREATE FUNCTION getRouteIDByDirectionIDExternal(_directionIDExternal VARCHAR(64), _dataSourceID VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routes.routeID FROM routes WHERE routes.directionIDExternal = _directionIDExternal AND routes.dataSourceID = _dataSourceID);
    RETURN result;
  END;

CREATE FUNCTION getNextSortOrder(_routeID INTEGER, _lastVisitedPointID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    DECLARE currentSortOrder INTEGER;
    DECLARE nextSortOrder INTEGER;

    SET currentSortOrder = (SELECT sortOrder
                            FROM route_points
                            WHERE (routeID = _routeID AND pointID = _lastVisitedPointID));

    IF (currentSortOrder IS NULL)
    THEN
      BEGIN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'SERGEY ERROR: Current sortOrder for route_points does not exist.';
      END;
    END IF;
    SET nextSortOrder = currentSortOrder + 1;
    RETURN nextSortOrder;
  END;

-- this function returns pointID for the next routePoint or NULL if it is already the last point
CREATE FUNCTION getNextPointID(_routeID INTEGER, _lastVisitedPointID INTEGER)
  RETURNS INTEGER
  RETURN (SELECT pointID
          FROM route_points
          WHERE routeID = _routeID AND sortOrder = getNextSortOrder(_routeID, _lastVisitedPointID));

CREATE FUNCTION getNextRoutePointID(_routeID INTEGER, _lastVisitedPointID INTEGER)
  RETURNS INTEGER
  RETURN (SELECT routePointID
          FROM route_points
          WHERE routeID = _routeID AND sortOrder = getNextSortOrder(_routeID, _lastVisitedPointID));


-- -------------------------------------------------------------------------------------------------------------------
--                                                SELECT_PROCEDURES
-- -------------------------------------------------------------------------------------------------------------------

-- запрос на выборку всех записей, которые должны быть в интерфейсе
-- нужно уметь ограничивать количество результирующих записей

CREATE FUNCTION getRoleIDByUserID(_userID INTEGER)
  RETURNS VARCHAR(32)
  BEGIN
    DECLARE result VARCHAR(32);
    SET result = (SELECT userRoleID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

CREATE FUNCTION getPointIDByUserID(_userID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT pointID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

-- get total time in minutes consumed for delivery
CREATE FUNCTION getDurationForRoute(_routeID INTEGER)
  RETURNS VARCHAR(255)
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE $currentRoutePointID INTEGER;
    DECLARE $previousRoutePointID INTEGER;
    DECLARE $timeForLoadingOperations INTEGER;
    DECLARE $timeCount INTEGER DEFAULT 0;
    DECLARE $timeResult INTEGER DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        routePointID,
        timeForLoadingOperations
      FROM route_points
      WHERE _routeID = route_points.routeID
      ORDER BY sortOrder;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
      FETCH cur
      INTO $currentRoutePointID, $timeForLoadingOperations;
      IF done
      THEN LEAVE read_loop; END IF;
      IF $previousRoutePointID IS NOT NULL
      THEN
        SET $timeCount = (SELECT timeForDistance
                          FROM relations_between_route_points
                          WHERE
                            routePointIDFirst = $previousRoutePointID AND routePointIDSecond = $currentRoutePointID);
      END IF;
      SET $timeResult = $timeResult + $timeCount + $timeForLoadingOperations;
      SET $previousRoutePointID = $currentRoutePointID;
    END LOOP;

    CLOSE cur;
    RETURN $timeResult;
-- SELECT firstPointArrivalTime FROM routes WHERE routeID = _routeID;
  END;

-- get DATE and TIME when route should be finished
CREATE FUNCTION getArrivalDateTime(_routeID INTEGER, _invoiceID INTEGER)
  RETURNS TIMESTAMP
  BEGIN
    DECLARE $routeStartDate TIMESTAMP;
    DECLARE $arrivalDateTime TIMESTAMP;

    SET $routeStartDate = (SELECT lastStatusUpdated
                           FROM invoice_history
                           WHERE _invoiceID = invoiceID AND invoiceStatusID = 'DEPARTURE');
-- отсчитывать от времени отправления или от времени начала маршрута?
    SET $arrivalDateTime = (SELECT TIMESTAMPADD(MINUTE, getDurationForRoute(_routeID), $routeStartDate));
    RETURN $arrivalDateTime;
  END;


-- arrivalTime is a delivery time + date when invoice has



-- startEntry - number of record to start from(0 is first record)
-- length - Number of records that the table can display in the current draw

-- if user Role is ADMIN then show all DATA, if another role then find user point
-- show only those records that contains user point as warehouse point or as route point
-- startEntry index begin with 0;
-- max amount of rows that will be returned

-- send request params from datatable
/*
draw:"1"                            порядковый номер, в ответе клиенту именно этот номер должен быть
columns[0][data]:"0"                устанавливает источник данных для колонки из объекта с данными, по умолчанию это просто порядковый номер столбца
columns[0][name]:""                 имя колонки, используется для удобного доступа к колонкам из datatables API
columns[0][searchable]:"true"       нужно ли использовать фильтр по этой колонке
columns[0][orderable]:"true"        запрещает или разрешает пользователю нажимать на кнопки сортировки, при этом из апи можно сортировать
columns[0][search][value]:""        Search value to apply to this specific column. в datatables можно делать не только глобальный поиск, но также и по отдельным колонкам.
columns[0][search][regex]:"false"   Flag to indicate if the search term for this column should be treated as regular expression (true) or not (false)
columns[1][data]:"1"
columns[1][name]:""
columns[1][searchable]:"true"
columns[1][orderable]:"true"
columns[1][search][value]:""
columns[1][search][regex]:"false"
...
columns[16][data]:"16"
columns[16][name]:""
columns[16][searchable]:"true"
columns[16][orderable]:"true"
columns[16][search][value]:""
columns[16][search][regex]:"false"
order[0][column]:"0"                индекс колонки к которой должен быть прменен ORDER BY
order[0][dir]:"asc"                 направление сортировки для этой колонки "asc" либо "desc"
start:"0"                           индекс первой записи из resulSet(startEntry)
length:"10"                         максимальное количесвто получаемых записей из БД(length)
search[value]:""                    Global search value. To be applied to all columns which have searchable as true.
search[regex]:"false"               true if the global filter should be treated as a regular expression for advanced searching, false otherwise
*/

CREATE FUNCTION splitString(stringSpl TEXT, delim VARCHAR(12), pos INT)
  RETURNS TEXT
  RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(stringSpl, delim, pos),
                           CHAR_LENGTH(SUBSTRING_INDEX(stringSpl, delim, pos - 1)) + 1),
                 delim, '');


CREATE FUNCTION generateHaving(map TEXT)
  RETURNS TEXT
  BEGIN

    DECLARE i INT DEFAULT 0;
    DECLARE pair VARCHAR(255) DEFAULT '----';
    DECLARE result TEXT DEFAULT '';

    IF (map = '')
    THEN RETURN 'TRUE';
    END IF;

    wloop: WHILE (TRUE) DO
      SET i = i + 1;
      SET pair = splitString(map, ';', i);
      IF pair = ''
      THEN LEAVE wloop;
      END IF;
      SET @columnName = splitString(pair, ',', 1);
      SET @searchString = splitString(pair, ',', 2);
      SET @searchString = CONCAT('%', @searchString, '%');

      SET result = CONCAT(result, @columnName, ' LIKE ', '\'', @searchString, '\'', ' AND ');
    END WHILE;

-- remove redundant END
    SET result = SUBSTR(result, 1, CHAR_LENGTH(result) - 4);
    SET result = CONCAT('(', result, ')');

    RETURN result;
  END;

CREATE FUNCTION generateOrderByPart(_orderby VARCHAR(255),_isDesc BOOLEAN)
  RETURNS VARCHAR(255)
  BEGIN
    IF _orderby <> ''
    THEN
      IF _isDesc
      THEN
        RETURN CONCAT(' ORDER BY ', _orderby, ' DESC');
      ELSE
        RETURN CONCAT(' ORDER BY ', _orderby);
      END IF;
    ELSE
      RETURN '';
    END IF;
  END;

-- _search - array of strings
-- _orderby 'id'  <> - название колонки из файла main.js
-- _search - передача column_name1,search_string1;column_name1,search_string1;... если ничего нет то передавать пустую строку

CREATE PROCEDURE selectData(_userID INTEGER, _startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255),
                            _isDesc BOOLEAN, _search TEXT)
  BEGIN
    SET @mainPart =
    'SELECT
      requests.requestNumber,
      invoices.insiderRequestNumber,
      invoices.invoiceNumber,
      clients.INN,
      delivery_points.pointName     AS `deliveryPoint`,
      w_points.pointName            AS `warehousePoint`,
      users.lastName,
      invoices.invoiceStatusID,
      invoice_statuses.invoiceStatusRusName,
      invoices.boxQty,

      route_lists.driver,
      route_lists.licensePlate,
      route_lists.palletsQty,
      route_lists.routeListNumber,
      route_lists.routeListID,
      routes.directionName,

      last_visited_points.pointName AS `currentPoint`,
      next_route_points.pointName   AS `nextPoint`,
      getArrivalDateTime(routes.routeID, invoices.invoiceID) AS `arrivalTime`

     FROM requests

      LEFT JOIN (invoices, clients, points AS delivery_points, points AS w_points, users)
        ON (
        invoices.requestID = requests.requestID AND
        invoices.warehousePointID = w_points.pointID AND
        requests.clientID = clients.clientID AND
        requests.destinationPointID = delivery_points.pointID AND
        requests.marketAgentUserID = users.userID
        )
      LEFT JOIN (invoice_statuses)
        ON (
          invoices.invoiceStatusID = invoice_statuses.invoiceStatusID  -- CHECK IT
        )
      -- because routeList in invoices table can be null, we use left join.
      LEFT JOIN (route_lists, routes)
        ON (
        invoices.routeListID = route_lists.routeListID AND
        route_lists.routeID = routes.routeID
        )
      LEFT JOIN (points AS last_visited_points)
        ON (
        invoices.lastVisitedUserPointID = last_visited_points.pointID
        )
      LEFT JOIN (route_points, points AS next_route_points)
        ON (
        route_lists.routeID = routes.routeID AND
        routes.routeID = route_points.routeID AND
        route_points.routePointID = getNextRoutePointID(routes.routeID, invoices.lastVisitedUserPointID) AND
        next_route_points.pointID = route_points.pointID
        ) ';


-- 1) если у пользователя роль админ, то показываем все записи из БД
-- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
-- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
-- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи

    SET @wherePart =
    'WHERE (
    (getRoleIDByUserID(?) = \'ADMIN\') OR
    (getRoleIDByUserID(?) = \'MARKET_AGENT\' AND requests.marketAgentUserID = ?) OR
    (getPointIDByUserID(?) = w_points.pointID) OR
    (getPointIDByUserID(?) IN (SELECT pointID FROM route_points WHERE route_lists.routeID = routeID))
    )';

    SET @havingPart = CONCAT(' HAVING ', generateHaving(_search));

    SET @orderByPart = generateOrderByPart(_orderby, _isDesc);

    SET @limitPart = ' LIMIT ?, ? ';

    SET @sqlString = CONCAT(@mainPart, @wherePart, @havingPart, @orderByPart, @limitPart);

    PREPARE statement FROM @sqlString;

    SET @_userID = _userID;
    SET @_startEntry = _startEntry;
    SET @_length = _length;

    EXECUTE statement
    USING @_userID, @_userID, @_userID, @_userID, @_userID, @_startEntry, @_length;
    DEALLOCATE PREPARE statement;
  END;

-- select users procedure
CREATE PROCEDURE selectUsers(_startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255), _isDesc BOOLEAN, _search TEXT)
  BEGIN

    SET @mainPart =
    'SELECT
      users.firstName,
      users.lastName,
      users.patronymic,
      users.position,
      users.phoneNumber,
      users.email,
      ''dummy'' AS password,
      points.pointName,
      user_roles.userRoleRusName
    FROM users
      INNER JOIN (user_roles, points) ON (
        users.pointID = points.pointID AND
        users.userRoleID = user_roles.userRoleID
        ) ';
    SET @havingPart = CONCAT(' HAVING ', generateHaving(_search));
    SET @orderByPart = generateOrderByPart(_orderby, _isDesc);
    SET @limitPart = ' LIMIT ?, ? ';

    SET @sqlString = CONCAT(@mainPart, @havingPart, @orderByPart, @limitPart);

    PREPARE statement FROM @sqlString;

    SET @_startEntry = _startEntry;
    SET @_length = _length;

    EXECUTE statement
    USING @_startEntry, @_length;
    DEALLOCATE PREPARE statement;

  END;


CREATE PROCEDURE `selectInvoiceStatusHistory`(_invoiceNumber VARCHAR(16))
  BEGIN
    SELECT
      pointName,
      firstName,
      lastName,
      patronymic,
      invoiceStatusRusName,
      invoice_history.lastStatusUpdated,
      routeListNumber,
      palletsQty,
      invoice_history.boxQty
    FROM invoice_history
      INNER JOIN (route_lists, users, points, invoice_statuses)
        ON (
        invoice_history.invoiceIDExternal = _invoiceNumber AND
        invoice_history.lastModifiedBy = users.userID AND
        invoice_history.invoiceStatusID = invoice_statuses.invoiceStatusID AND
        invoice_history.routeListID = route_lists.routeListID AND
        users.pointID = points.pointID
        )
    ORDER BY lastStatusUpdated;
  END;

-- procedure for getting time and distance between routePoints
CREATE PROCEDURE getRelationsBetweenRoutePoints(_routeID INTEGER)
  BEGIN
    SELECT
      CONCAT_WS('_', routePointIDFirst, routePointIDSecond) AS relationID,
      pointFirst.pointName                                  AS `pointNameFirst`,
      pointSecond.pointName                                 AS `pointNameSecond`,
      timeForDistance,
      distance,
      routePointFirst.sortOrder
    FROM routes
      JOIN (
          route_points AS routePointFirst,
          route_points AS routePointSecond,
          relations_between_route_points,
          points AS pointFirst,
          points AS pointSecond
        ) ON
            (routePointFirst.routePointID = routePointIDFirst AND routePointSecond.routePointID = routePointIDSecond AND
             routePointFirst.routeID = `routes`.routeID AND routePointSecond.routeID = `routes`.routeID AND
             routePointFirst.pointID = pointFirst.pointID AND routePointSecond.pointID = pointSecond.pointID)
      LEFT JOIN (`distances_between_points`)
        ON
          (pointIDFirst = routePointFirst.pointID AND pointIDSecond = routePointSecond.pointID) OR (pointIDFirst = routePointSecond.pointID AND pointIDSecond = routePointFirst.pointID)
    WHERE routes.RouteID = _routeID
    ORDER BY routePointFirst.sortOrder;
  END;

CREATE PROCEDURE `deleteRoutePoint`(_routePointID INTEGER)
  BEGIN
    DECLARE routePoint1 INTEGER;
    DECLARE routePoint2 INTEGER;
    DECLARE time INTEGER;
    SET @relCount = (SELECT COUNT(*) FROM relations_between_route_points WHERE routePointIDFirst = _routePointID OR routePointIDSecond = _routePointID);
    IF @relCount = 2
    THEN
      SELECT
        rbrp2.routePointIDFirst, rbrp1.routePointIDSecond, (rbrp1.timeForDistance + rbrp2.timeForDistance) as `timeForDistance`
      INTO
        routePoint1, routePoint2, time
      FROM
        relations_between_route_points as rbrp1,relations_between_route_points as rbrp2
      WHERE
        rbrp1.routePointIDFirst = _routePointID AND rbrp2.routePointIDSecond = _routePointID;
      INSERT INTO
        relations_between_route_points (routePointIDFirst, routePointIDSecond, timeForDistance)
        VALUE
        (routePoint1, routePoint2, time);
    END IF;
    DELETE FROM route_points WHERE _routePointID = routePointID;
  END;

-- TODO change procedure to trigger
CREATE PROCEDURE `createRoutePoint`(_sortOrder INTEGER, _tLoading INTEGER, _pointID INTEGER, _routeID INTEGER)
  BEGIN
    INSERT INTO `route_points` (sortOrder, timeForLoadingOperations, pointID, routeID) VALUE (_sortOrder, _tLoading, _pointID, _routeID);
    IF (SELECT count(*) FROM route_points WHERE _routeID = routeID) > 1
    THEN
      SET @routePointID1 = (SELECT routePointID FROM route_points WHERE routeID = _routeID AND sortOrder = _sortOrder-1);
      SET @routePointID2 = (SELECT routePointID FROM route_points WHERE routeID = _routeID AND sortOrder = _sortOrder);
      INSERT INTO relations_between_route_points (routePointIDFirst, routePointIDSecond, timeForDistance) VALUE (@routePointID1,@routePointID2,0);
    END IF;
  END;














