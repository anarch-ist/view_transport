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
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE(pointIDExternal, dataSourceID)
);

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

-- TODO CONSTRAINT у пользователей с определнной ролью обязан быть пункт
-- TODO CONSTRAINT у администратора не может быть пункта
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
  passAndSalt VARCHAR(64)  NOT NULL DEFAULT 'cnorebutbvoertvb3040384342u4hf',
  userRoleID  VARCHAR(32)  NOT NULL,
  pointID     INTEGER      NULL, -- у пользователя не обязан быть пункт.
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
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
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
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
CALL insert_permission_for_role('CLIENT_MANAGER', 'updateInvoiceStatus');
CALL insert_permission_for_role('CLIENT_MANAGER', 'selectDataProcedure');

INSERT INTO users
  VALUE
  (1, 'parser', '', '', '', '','', 'fff@fff', '', 'nvuritneg4785', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785')), 'ADMIN', NULL);


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
  marketAgentUserID  INTEGER      NOT NULL,
  clientID           INTEGER      NOT NULL,
  destinationPointID INTEGER      NULL, -- адрес, куда должны быть доставлены все товары
  PRIMARY KEY (requestID),
  FOREIGN KEY (marketAgentUserID) REFERENCES users (userID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
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
      SET MESSAGE_TEXT = 'LOGIST ERROR: Can\'t insert row: only MARKET_AGENT users allowed';
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
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE(routeID, sortOrder)
);

-- CONSTRAINT routePointIDFirst must not be equal routePointIDSecond
CREATE TRIGGER before_route_points_insert BEFORE INSERT ON route_points
FOR EACH ROW
  BEGIN
    -- получить пункт сверху и снизу
    SET @nextPointID = (SELECT pointID
                        FROM route_points
                        WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder > NEW.sortOrder
                        ORDER BY sortOrder ASC
                        LIMIT 1);
    SET @previousPointID = (SELECT pointID
                            FROM route_points
                            WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder < NEW.sortOrder
                            ORDER BY sortOrder DESC
                            LIMIT 1);
    IF ((@nextPointID IS NOT NULL AND @previousPointID IS NOT NULL AND ((NEW.pointID = @nextPointID) OR (NEW.pointID = @previousPointID))) OR
        (@nextPointID IS NULL AND (NEW.pointID = @previousPointID)) OR
        (@previousPointID IS NULL AND (NEW.pointID = @nextPointID)))
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'LOGIST ERROR: can\'t add new route point because same neighbors';
    END IF;
  END;
-- CONSTRAINT routePointIDFirst must not be equal routePointIDSecond
CREATE TRIGGER before_route_points_delete BEFORE DELETE ON route_points
FOR EACH ROW
  BEGIN
    -- получить пункт сверху и снизу
    SET @nextPointID = (SELECT pointID
                        FROM route_points
                        WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder > OLD.sortOrder
                        ORDER BY sortOrder ASC
                        LIMIT 1);
    SET @previousPointID = (SELECT pointID
                            FROM route_points
                            WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder < OLD.sortOrder
                            ORDER BY sortOrder DESC
                            LIMIT 1);
    IF (((@nextPointID IS NOT NULL AND @previousPointID IS NOT NULL) AND (@nextPointID = @previousPointID)) OR
        (@nextPointID IS NULL AND (OLD.pointID = @previousPointID)) OR
        (@previousPointID IS NULL AND (OLD.pointID = @nextPointID)))
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'LOGIST ERROR: can\'t remove route point because same neighbors';
    END IF;
  END;

CREATE TRIGGER after_route_points_insert AFTER INSERT ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*) FROM route_points WHERE NEW.routeID = route_points.routeID) > 1
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder > NEW.sortOrder ORDER BY sortOrder ASC LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder < NEW.sortOrder ORDER BY sortOrder DESC LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL ) THEN
          SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'LOGIST ERROR: nextRoutePointID and previousRoutePointID is NULL';
        END IF;

        -- если мы добавили новый пункт в начало
        IF (@previousRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
        -- если мы добавили новый пункт в конец
        ELSEIF (@nextRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (@previousRoutePointID, NEW.routePointID, 0);
        -- TODO была ситуация, когда после вставки в середину данные не добавились в relations_between_route_points
        -- если мы добавили новый пункт в середину
        ELSE
          BEGIN
            UPDATE relations_between_route_points
            SET routePointIDSecond = NEW.routePointID, timeForDistance = 0
            WHERE routePointIDFirst = @previousRoutePointID AND routePointIDSecond = @nextRoutePointID;
            INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
          END;
        END IF;

      END;
    END IF;
  END;

CREATE TRIGGER after_route_points_delete AFTER DELETE ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*) FROM route_points WHERE OLD.routeID = route_points.routeID) >= 2
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder > OLD.sortOrder ORDER BY sortOrder ASC LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder < OLD.sortOrder ORDER BY sortOrder DESC LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL ) THEN
          SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'LOGIST ERROR: nextRoutePointID and previousRoutePointID is NULL';
        END IF;

        -- если мы удалили пункт из начала
        IF (@previousRoutePointID IS NULL) THEN
          DELETE FROM relations_between_route_points WHERE routePointIDFirst = OLD.routePointID;
        -- если мы удалили пункт с конца
        ELSEIF (@nextRoutePointID IS NULL) THEN
          DELETE FROM relations_between_route_points WHERE routePointIDFirst = OLD.routePointID OR routePointIDSecond = OLD.routePointID;
        -- если мы удалили пункт из середины
        ELSE
          BEGIN
            DELETE FROM relations_between_route_points WHERE routePointIDSecond = OLD.routePointID;
            UPDATE relations_between_route_points
            SET routePointIDFirst = @previousRoutePointID, timeForDistance = 0
            WHERE routePointIDFirst = OLD.routePointID;
          END;
        END IF;

      END;
    END IF;
  END;

-- TODO рассмотреть два случая - если меняется sortOrder и если меняется сам пункт и если происходит то и другое одновременно
CREATE TRIGGER after_route_points_update BEFORE UPDATE ON route_points
FOR EACH ROW
  BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'LOGIST ERROR: updates on route_points temporally disabled';
  END;

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
  forwarderId         VARCHAR(255) NULL,
  driverId            VARCHAR(255) NULL,
  driverPhoneNumber   VARCHAR(12)  NULL,
  licensePlate        VARCHAR(9)   NULL, -- государственный номер автомобиля
  status              VARCHAR(32)  NOT NULL,
  routeID             INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE (routeListNumber),
  UNIQUE (routeListIDExternal, dataSourceID)
);

CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
     NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverId,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'CREATED');

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
     NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverId,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'UPDATED');

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), OLD.routeListID, OLD.routeListIDExternal, OLD.dataSourceID, OLD.routeListNumber, OLD.creationDate,
     OLD.departureDate, OLD.palletsQty, OLD.forwarderId, OLD.driverId,
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
  forwarderId         VARCHAR(255)                          NULL,
  driverId            VARCHAR(255)                          NULL,
  driverPhoneNumber   VARCHAR(12)                           NULL,
  licensePlate        VARCHAR(9)                            NULL, -- государственный номер автомобиля
  status              VARCHAR(32)                           NOT NULL,
  routeID             INTEGER                               NOT NULL,
  dutyStatus          ENUM('CREATED', 'UPDATED', 'DELETED') NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
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
  ('MARKET_AGENT', 'DELIVERED'),
  ('CLIENT_MANAGER', 'ERROR');

-- invoice объеденяет в себе внутреннюю заявку и накладную,
-- при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
-- ВНИМАНИЕ! при изменении порядка полей их также нужно менять в триггерах и в таблице invoice_history!!!
CREATE TABLE invoices (
  invoiceID               INTEGER AUTO_INCREMENT,
  invoiceIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATETIME       NULL,
  firma                   VARCHAR(255)   NULL,
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  creationDate            DATETIME       NULL,
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  storage                 VARCHAR(255)   NULL, -- наименование склада на котором собиралась накладная
  deliveryOption          VARCHAR(255)   NULL, -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  -- TODO это поле нужно перенести в users, саму таблицу users разбить на три таблицы из-за пункта.
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  invoiceStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  requestID               INTEGER        NOT NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  FOREIGN KEY (warehousePointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeListID) REFERENCES route_lists (routeListID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (lastVisitedRoutePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (invoiceIDExternal, dataSourceID)
);

CREATE TRIGGER check_for_warehousePoint
BEFORE INSERT ON invoices FOR EACH ROW
  BEGIN
    SET @pointType = (SELECT pointTypeID
                     FROM points
                     WHERE pointID = NEW.warehousePointID);

    IF (@pointType IS NOT NULL)
    THEN
      BEGIN
        IF (@pointType <> 'WAREHOUSE') THEN
          SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'LOGIST ERROR: Can\'t insert row: only warehouse points allowed';
        END IF;
      END;
    END IF;
  END;

CREATE TRIGGER after_invoice_insert AFTER INSERT ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
    VALUE
    (NULL, NOW(), NEW.invoiceID, NEW.invoiceIDExternal, NEW.dataSourceID, NEW.documentNumber, NEW.documentDate,
     NEW.firma, NEW.contactName, NEW.contactPhone, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.weight,
     NEW.volume, NEW.goodsCost, NEW.storage, NEW.deliveryOption, NEW.lastStatusUpdated, NEW.lastModifiedBy,
     'CREATED', NEW.commentForStatus, NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

CREATE TRIGGER after_invoice_update AFTER UPDATE ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
    VALUE
    (NULL, NOW(), NEW.invoiceID, NEW.invoiceIDExternal, NEW.dataSourceID, NEW.documentNumber, NEW.documentDate,
     NEW.firma, NEW.contactName, NEW.contactPhone, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.weight,
     NEW.volume, NEW.goodsCost, NEW.storage, NEW.deliveryOption, NEW.lastStatusUpdated, NEW.lastModifiedBy,
     NEW.invoiceStatusID, NEW.commentForStatus, NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

CREATE TRIGGER after_invoice_delete AFTER DELETE ON invoices
FOR EACH ROW
  INSERT INTO invoice_history
    VALUE
    (NULL, NOW(), OLD.invoiceID, OLD.invoiceIDExternal, OLD.dataSourceID, OLD.documentNumber, OLD.documentDate,
     OLD.firma, OLD.contactName, OLD.contactPhone, OLD.creationDate, OLD.deliveryDate, OLD.boxQty, OLD.weight,
     OLD.volume, OLD.goodsCost, OLD.storage, OLD.deliveryOption, OLD.lastStatusUpdated, OLD.lastModifiedBy,
     'CREATED', OLD.commentForStatus, OLD.requestID, OLD.warehousePointID, OLD.routeListID, OLD.lastVisitedRoutePointID);

CREATE TRIGGER before_invoice_update BEFORE UPDATE ON invoices
FOR EACH ROW
  BEGIN
    -- берем пользователя, который изменил статус на один из invoice statuses, затем находим его пункт маршрута, и этот
    -- пункт записываем в таблицу invoices в поле lastVisitedRoutePointID
    IF (NEW.invoiceStatusID = 'ARRIVED' OR NEW.invoiceStatusID = 'ERROR' OR NEW.invoiceStatusID = 'DELIVERED')
    THEN
      BEGIN
        IF (NEW.routeListID IS NULL) THEN
          SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'LOGIST ERROR: routeList in invoices must not be null';
        END IF;
        -- находим маршрут по которому едет накладная.
        SET @routeID = (SELECT routeID FROM route_lists WHERE NEW.routeListID = route_lists.routeListID);
        -- находим пункт пользователя, который изменил статус накладной.
        SET @pointID = (SELECT pointID FROM users WHERE users.userID = NEW.lastModifiedBy);

        -- TODO проставить всю эту логику вместе с Юлей, учесть зависимость от пользователя от пункта пользователя и от статуса накладной
        -- при появлении маршрутного листа сразу выставляем последний посещенный пункт, как первый пункт маршрута
        IF (NEW.routeListID IS NOT NULL AND NEW.lastVisitedRoutePointID IS NULL) THEN
          -- установка самого первого пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID) ORDER BY sortOrder LIMIT 1);
        -- если была установка статуса в ARRIVED, то послдний посещенный пункт меняется на следующий
        ELSEIF (NEW.invoiceStatusID = 'ARRIVED' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder FROM route_points WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем следующий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder > @lastRoutePointSortOrder) ORDER BY sortOrder LIMIT 1);
          END;
        ELSEIF (NEW.invoiceStatusID = 'ERROR' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder FROM route_points WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем предыдущий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder < @lastRoutePointSortOrder) ORDER BY sortOrder LIMIT 1);
          END;
        ELSEIF (NEW.invoiceStatusID = 'DELIVERED' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          -- установка самого последнего пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID) ORDER BY sortOrder DESC LIMIT 1);
        END IF;
      END;
    END IF;
  END;

CREATE TABLE invoice_history (
  invoiceHistoryID        BIGINT AUTO_INCREMENT,
  autoTimeMark            DATETIME       NOT NULL,
  invoiceID               INTEGER        NOT NULL,
  invoiceIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATETIME       NULL,
  firma                   VARCHAR(255)   NULL,
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  creationDate            DATETIME       NULL,
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  storage                 VARCHAR(255)   NULL, -- наименование склада на котором собиралась накладная
  deliveryOption          VARCHAR(255)   NULL, -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  invoiceStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  requestID               INTEGER        NOT NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- -------------------------------------------------------------------------------------------------------------------
--                                                   EXCHANGE_TABLES
-- -------------------------------------------------------------------------------------------------------------------

-- 1) сначала парсер обнаруживает событие появления нового файла, или нескольких новых файлов. Если к примеру парсер в течение суток не работал, то в каталоге набралось некоторое количесвто новых выгрузок,
-- парсер должен обнаружить их все, отсортировать по номеру пакета и начать загрузку.
-- 2) как только начинается загрузка файла, парсер пишет об этом в таблицу exchangeHistory, если в процессе возникли ошибки, то они также пишутся в БД. В случае успешной загрузки делается запись в exchange.
-- 3)
-- when new file comes parser start event and make a try to write data into database. if succeded then
CREATE TABLE exchange (
  packageNumber INTEGER,
  serverName VARCHAR(32),
  dataSource VARCHAR(32),
  packageCreated DATETIME,
  packageData LONGTEXT, -- наличие самих данных в соответсвующем порядке позволяет в любой момент пересоздать всю БД.
  PRIMARY KEY (packageNumber, serverName, dataSource)
);
-- Rows are compressed as they are inserted, https://dev.mysql.com/doc/refman/5.5/en/archive-storage-engine.html

-- CREATE TABLE exchangeHistory (
--
-- )

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
    SET result = (SELECT routes.routeID
                  FROM routes
                  WHERE routes.routeName = _routeName);
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

CREATE FUNCTION getRouteIDByDirectionIDExternal(_directionIDExternal VARCHAR(64), _dataSourceID VARCHAR(32))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routes.routeID FROM routes WHERE routes.directionIDExternal = _directionIDExternal AND routes.dataSourceID = _dataSourceID);
    RETURN result;
  END;

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

CREATE FUNCTION getNextRoutePointID(_routeID INTEGER, _lastVisitedRoutePointID INTEGER)
  RETURNS INTEGER
  BEGIN
    -- получаем порядковый номер последнего routePoint
    SET @lastRoutePointSortOrder = (SELECT sortOrder
                                    FROM route_points
                                    WHERE route_points.routePointID = _lastVisitedRoutePointID);
    -- устанавливаем следующий пункт маршрута
    RETURN (SELECT routePointID
            FROM route_points
            WHERE (routeID = _routeID AND sortOrder > @lastRoutePointSortOrder)
            ORDER BY sortOrder
            LIMIT 1);
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                SELECT_PROCEDURES
-- -------------------------------------------------------------------------------------------------------------------


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
      \'no data\'                                            AS insiderRequestNumber,
      invoices.invoiceIDExternal                             AS invoiceNumber,
      clients.INN,
      delivery_points.pointName                              AS `deliveryPoint`,
      w_points.pointName                                     AS `warehousePoint`,
      users.lastName,
      invoices.invoiceStatusID,
      invoice_statuses.invoiceStatusRusName,
      invoices.boxQty,

      route_lists.driverId                                   AS driver,
      route_lists.licensePlate,
      route_lists.palletsQty,
      route_lists.routeListNumber,
      route_lists.routeListID,
      routes.routeName                                       AS directionName,

      last_visited_points.pointName                          AS `currentPoint`,
      next_route_points.pointName                            AS `nextPoint`,
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
      LEFT JOIN (route_points AS rp1, points AS last_visited_points)
        ON (
        rp1.routePointID = invoices.lastVisitedRoutePointID AND
        last_visited_points.pointID = rp1.pointID
        )
      LEFT JOIN (route_points AS rp2, points AS next_route_points)
        ON (
        route_lists.routeID = routes.routeID AND
        routes.routeID = rp2.routeID AND
        rp2.routePointID = getNextRoutePointID(routes.routeID, invoices.lastVisitedRoutePointID) AND
        next_route_points.pointID = rp2.pointID
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
-- _search - строка для глобального поиска по всем колонкам
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

    SET @searchString = CONCAT('%', _search, '%');
    SET @searchCondition = CONCAT('users.firstName LIKE '           , '\'', @searchString, '\'', ' OR ',
                                  'users.lastName LIKE '            , '\'', @searchString, '\'', ' OR ',
                                  'users.patronymic LIKE '          , '\'', @searchString, '\'', ' OR ',
                                  'users.position LIKE '            , '\'', @searchString, '\'', ' OR ',
                                  'users.phoneNumber LIKE '         , '\'', @searchString, '\'', ' OR ',
                                  'users.email LIKE '               , '\'', @searchString, '\'', ' OR ',
                                  'points.pointName LIKE '          , '\'', @searchString, '\'', ' OR ',
                                  'user_roles.userRoleRusName LIKE ', '\'', @searchString, '\'');

    SET @havingPart = CONCAT(' HAVING ', @searchCondition);
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