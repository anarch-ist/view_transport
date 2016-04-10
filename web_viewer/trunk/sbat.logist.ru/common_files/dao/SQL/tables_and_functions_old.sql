SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';
DROP DATABASE IF EXISTS transmaster_transport_db;
CREATE DATABASE `transmaster_transport_db`
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE `transmaster_transport_db`;

-- _errorMessage must be less or equal 110 symbols
CREATE PROCEDURE generateLogistError(_errorMessage TEXT)
  BEGIN
    SET @message_text = concat('LOGIST ERROR: ', _errorMessage);
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @message_text;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                  DATA SOURCES
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE data_sources (
  dataSourceID VARCHAR(32) PRIMARY KEY
);
INSERT INTO data_sources
VALUES
  ('LOGIST_1C')
  ,('ADMIN_PAGE');

-- -------------------------------------------------------------------------------------------------------------------
--                                                 CLIENTS
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
  -- показывать заявки, которые проходят через пункт к которому приписан W_DISPATCHER
  -- диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
  ('W_DISPATCHER', 'Диспетчер_склада'),
  -- показывать заявки, которые проходят через пункт к которому приписан DISPATCHER
    -- диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
  ('DISPATCHER', 'Диспетчер'),
  -- пользователь клиента, доступен GUI для для просмотра всех заявок данного клиента.
  ('CLIENT_MANAGER', 'Пользователь_клиента'),
  -- торговый представитель, доступ только на чтение тех заявок, в которых он числится торговым
  ('MARKET_AGENT', 'Торговый_представитель')

  -- TODO сделать правильну работу для указанных ниже ролей пользователей, прописать ограничения в таблице users
  -- временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
  -- что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
  -- ('TEMP_REMOVED', 'Временно_удален'),
  -- ('VIEW_LAST_TEN', 'Последние_десять')
 ;

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
  phoneNumber         VARCHAR(255)    NULL,
  responsiblePersonId VARCHAR(128)   NULL,
  pointTypeID         VARCHAR(32)    NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE(pointIDExternal, dataSourceID)
);


-- TODO CONSTRAINT pointIDFirst must not be equal pointIDSecond
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
  userID         INTEGER AUTO_INCREMENT,
  userIDExternal VARCHAR(255) NOT NULL,
  dataSourceID   VARCHAR(32)  NOT NULL,
  login          VARCHAR(255) NOT NULL,
  salt           CHAR(16)     NOT NULL,
  passAndSalt    VARCHAR(64)  NOT NULL,
  userRoleID     VARCHAR(32)  NOT NULL,
  userName       VARCHAR(255) NULL,
  phoneNumber    VARCHAR(255) NULL,
  email          VARCHAR(255) NULL,
  position       VARCHAR(255) NULL, -- должность
  pointID        INTEGER      NULL, -- у ADMIN и CLIENT_MANAGER и MARKET_AGENT не может быть пункта, у W_DISPATCHER и DISPATCHER обязан быть пункт
  clientID       INTEGER      NULL, -- у CLIENT_MANAGER обязан быть clientID, у ADMIN W_DISPATCHER DISPATCHER и MARKET_AGENT его быть не должно
  PRIMARY KEY (userID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE (userIDExternal, dataSourceID),
  UNIQUE (login)
);

CREATE PROCEDURE checkUserConstraints(_userRoleID VARCHAR(255), _pointID INTEGER, _clientID INTEGER, _userIDExternal VARCHAR(255), _login VARCHAR(255))
  BEGIN
    IF (_userRoleID IN ('ADMIN', 'CLIENT_MANAGER', 'MARKET_AGENT') AND _pointID IS NOT NULL) THEN
      CALL generateLogistError(CONCAT('ADMIN, CLIENT_MANAGER OR MARKET_AGENT  can\'t have a point. userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;

    IF (_userRoleID IN ('DISPATCHER', 'W_DISPATCHER') AND _pointID IS NULL) THEN
      CALL generateLogistError(CONCAT('DISPATCHER OR W_DISPATCHER must have a point. userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;

    IF (_userRoleID = 'CLIENT_MANAGER' AND _clientID IS NULL) THEN
      CALL generateLogistError(CONCAT('CLIENT_MANAGER must have clientID. userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;

    IF (_userRoleID IN('DISPATCHER', 'W_DISPATCHER', 'MARKET_AGENT', 'ADMIN') AND _clientID IS NOT NULL) THEN
      CALL generateLogistError(CONCAT('clientID must be null for userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;
  END;

CREATE TRIGGER check_users_constraints_insert BEFORE INSERT ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login);

CREATE TRIGGER check_users_constraints_update BEFORE UPDATE ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login);


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
  ('updateRequestStatus'),
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
CALL insert_permission_for_role('CLIENT_MANAGER', 'updateRequestStatus');
CALL insert_permission_for_role('CLIENT_MANAGER', 'selectDataProcedure');

INSERT INTO points (pointIDExternal, dataSourceID, pointName, region, timeZone, docs, comments, openTime, closeTime,
                    district, locality, mailIndex, address, email, phoneNumber, responsiblePersonId, pointTypeID)
VALUES ('wle', 'LOGIST_1C', 'point1', 'moscow', 3, 1, 'some_comment1', '9:00:00', '17:00:00', 'some_district', 'efregrthr', '123456',
        'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'resp_personID1', 'WAREHOUSE');

INSERT INTO users (userID, userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)
  VALUES
  (1, 'eebrfiebreiubritbvritubvriutbv', 'ADMIN_PAGE', 'parser', 'nvuritneg4785231', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785231')),
   'ADMIN', 'parser', '', 'fff@fff', '', NULL, NULL),
  (2, 'eebrfiebreiubrrervritubvriutbv', 'ADMIN_PAGE', 'test', 'nvuritneg4785231', md5(CONCAT(md5('test'), 'nvuritneg4785231')),
   'ADMIN', 'ivanov i.i.', '904534356', 'test@test.ru', 'position', NULL, NULL);


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

-- CONSTRAINT routePointIDFirst not equal routePointIDSecond
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
      CALL generateLogistError('can\'t add new route point because same neighbors');
    END IF;
  END;
-- CONSTRAINT routePointIDFirst not equal routePointIDSecond
CREATE TRIGGER before_route_points_delete BEFORE DELETE ON route_points
FOR EACH ROW
  BEGIN

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
      CALL generateLogistError('can\'t remove route point because same neighbors');
    END IF;
  END;

-- таблица используется для оптимизации запроса big select
CREATE TABLE mat_view_route_points_sequential (
  routeID          INTEGER,
  routePointID     INTEGER,
  nextRoutePointID INTEGER      NULL,
  nextPointID      INTEGER      NULL,
  nextPointName    VARCHAR(255) NULL,
  timeToNextPoint  INTEGER      NULL,
  PRIMARY KEY (routeID, routePointID),
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (nextRoutePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (nextPointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE PROCEDURE refreshAdminDataView(_routeID INTEGER)
  BEGIN

    DELETE FROM mat_view_route_points_sequential WHERE routeID = _routeID;

    INSERT INTO mat_view_route_points_sequential
      SELECT
        mainRP.routeID,
        mainRP.routePointID,
        innerRP.routePointID AS nextRoutePointID,
        innerRP.pointID      AS nextPointID,
        points.pointName     AS nextPointName,
        relations_between_route_points.timeForDistance AS  timeToNextPoint
      FROM route_points mainRP
        LEFT JOIN route_points innerRP ON (
          innerRP.routeID = _routeID AND
          mainRP.routeID = _routeID AND
          innerRP.sortOrder = (SELECT innerRP2.sortOrder
                               FROM route_points innerRP2
                               WHERE (innerRP2.routeID = _routeID AND innerRP2.sortOrder > mainRP.sortOrder)
                               ORDER BY innerRP.sortOrder
                               LIMIT 1)
          )
        LEFT JOIN points ON points.pointID = innerRP.pointID
        LEFT JOIN relations_between_route_points ON (
          relations_between_route_points.routePointIDFirst = mainRP.routePointID AND
          relations_between_route_points.routePointIDSecond = innerRP.routePointID
          )
      WHERE mainRP.routeID = _routeID;

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
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
        END IF;

        -- если мы добавили новый пункт в начало
        IF (@previousRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
        -- если мы добавили новый пункт в конец
        ELSEIF (@nextRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (@previousRoutePointID, NEW.routePointID, 0);
        -- если мы добавили новый пункт в середину
        ELSE
          BEGIN
            UPDATE relations_between_route_points
            SET routePointIDSecond = NEW.routePointID, timeForDistance = 0
            WHERE routePointIDFirst = @previousRoutePointID AND routePointIDSecond = @nextRoutePointID;
            INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
          END;
        END IF;

        CALL refreshAdminDataView(NEW.routeID);
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
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
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

    CALL refreshAdminDataView(OLD.routeID);
  END;

CREATE TRIGGER after_route_points_update BEFORE UPDATE ON route_points
FOR EACH ROW
  BEGIN
    CALL generateLogistError('updates on route_points disabled');
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

CREATE TRIGGER after_update AFTER UPDATE ON relations_between_route_points
  FOR EACH ROW
  BEGIN
    UPDATE mat_view_route_points_sequential SET mat_view_route_points_sequential.timeToNextPoint = NEW.timeForDistance WHERE
      mat_view_route_points_sequential.routePointID = NEW.routePointIDFirst AND
      mat_view_route_points_sequential.nextRoutePointID IS NOT NULL AND
      mat_view_route_points_sequential.nextRoutePointID = NEW.routePointIDSecond;
  END;

-- ВНИМАНИЕ! при изменении в этой таблице нужно согласовать все с триггерами и таблицей route_list_history
CREATE TABLE route_lists (
  routeListID         INTEGER AUTO_INCREMENT,
  routeListIDExternal VARCHAR(255)  NOT NULL,
  dataSourceID        VARCHAR(32)  NOT NULL,
  routeListNumber     VARCHAR(255)  NOT NULL,
  creationDate        DATE         NULL,
  departureDate       DATE         NULL,
  palletsQty          INTEGER      NULL,
  forwarderId         VARCHAR(255) NULL,
  driverID            INTEGER      NULL,
  driverPhoneNumber   VARCHAR(255)  NULL,
  licensePlate        VARCHAR(255)  NULL, -- государственный номер автомобиля
  status              VARCHAR(32)  NOT NULL,
  routeID             INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (driverID) REFERENCES users (userID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE (routeListIDExternal, dataSourceID)
);

CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
     NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverID,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'CREATED');

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
     NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverID,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'UPDATED');

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), OLD.routeListID, OLD.routeListIDExternal, OLD.dataSourceID, OLD.routeListNumber, OLD.creationDate,
     OLD.departureDate, OLD.palletsQty, OLD.forwarderId, OLD.driverID,
     OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.routeID, 'DELETED');

CREATE TABLE route_list_history (
  routeListHistoryID  BIGINT AUTO_INCREMENT,
  timeMark            DATETIME                              NOT NULL,
  routeListID         INTEGER                               NOT NULL,
  routeListIDExternal VARCHAR(255)                           NOT NULL,
  dataSourceID        VARCHAR(32)                           NOT NULL,
  routeListNumber     VARCHAR(255)                           NOT NULL,
  creationDate        DATE                                  NULL,
  departureDate       DATE                                  NULL,
  palletsQty          INTEGER                               NULL,
  forwarderId         VARCHAR(255)                          NULL,
  driverId            VARCHAR(255)                          NULL,
  driverPhoneNumber   VARCHAR(255)                           NULL,
  licensePlate        VARCHAR(255)                           NULL, -- государственный номер автомобиля
  status              VARCHAR(32)                           NOT NULL,
  routeID             INTEGER                               NOT NULL,
  dutyStatus          ENUM('CREATED', 'UPDATED', 'DELETED') NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                REQUESTS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE request_statuses (
  requestStatusID      VARCHAR(32),
  requestStatusRusName VARCHAR(128),
  sequence             TINYINT,
  PRIMARY KEY (requestStatusID)
);

INSERT INTO request_statuses
VALUES
  -- duty statuses
  ('CREATED', 'Внутренняя заявка добавлена в БД', 0),
  ('DELETED', 'Внутренняя заявка удалена из БД', -1),
  -- insider request statuses
  ('UNKNOWN', 'неизвестный статус', 1),
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
  ('ADJUSTMENTS_MADE', 'Сделаны коргыукытировки\распечатаны документы', 9),
  ('PACKAGING', 'Упаковано', 10),
  ('CHECK_BOXES', 'Проверка коробок в зоне отгрузки', 11),
  ('READY', 'Проверка в зоне отгрузки/Готова к отправке', 12),
  ('TRANSPORTATION', 'Маршрутный лист закрыт, товар передан экспедитору на погрузку', 13),
  -- invoice statuses
  ('DEPARTURE', 'В транзите', 14),
  ('ARRIVED', 'Накладная прибыла в пункт', 15),
  ('ERROR', 'Ошибка. Возвращение в пункт', -4),
  ('DELIVERED', 'Доставлено', 16);

CREATE TABLE request_statuses_for_user_role (
  userRoleID      VARCHAR(32),
  requestStatusID VARCHAR(32),
  PRIMARY KEY (userRoleID, requestStatusID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID),
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
);

INSERT INTO request_statuses_for_user_role
VALUES
  ('ADMIN', 'APPROVING'),
  ('ADMIN', 'RESERVED'),
  ('ADMIN', 'APPROVED'),
  ('ADMIN', 'STOP_LIST'),
  ('ADMIN', 'READY'),
  ('ADMIN', 'DEPARTURE'),
  ('ADMIN', 'ERROR'),
  ('ADMIN', 'ARRIVED'),
  ('W_DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'ARRIVED'),
  ('DISPATCHER', 'ERROR'),
  ('DISPATCHER', 'DELIVERED'),
  ('MARKET_AGENT', 'ERROR'),
  ('MARKET_AGENT', 'DELIVERED'),
  ('CLIENT_MANAGER', 'ERROR'),
  ('CLIENT_MANAGER', 'DELIVERED');


-- request объеденяет в себе внутреннюю заявку и накладную,
-- при создании request мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
-- ВНИМАНИЕ! при изменении порядка полей их также нужно менять в триггерах и в таблице requests_history!!!
CREATE TABLE requests (

  -- JSON fields
  requestID               INTEGER AUTO_INCREMENT,
  requestIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  requestNumber           VARCHAR(255)    NOT NULL,
  requestDate             DATE           NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
  clientID                INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL, -- пункт доставки (addressId)
  marketAgentUserID       INTEGER        NOT NULL, -- traderId
  invoiceNumber           VARCHAR(255)   NOT NULL,
  invoiceDate             DATE           NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATE           NULL,
  firma                   VARCHAR(255)   NULL,
  storage                 VARCHAR(255)   NULL, -- наименование склада на котором собиралась накладная
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  deliveryOption          VARCHAR(255)   NULL, -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,

  -- logist fields
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  -- TODO это поле нужно перенести в users, саму таблицу users разбить на три таблицы из-за пункта.
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  requestStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист

  PRIMARY KEY (requestID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (marketAgentUserID) REFERENCES users (userID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
    ON DELETE RESTRICT
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
  UNIQUE (requestIDExternal, dataSourceID)
);

CREATE TRIGGER check_for_market_agents_and_warehouse_point BEFORE INSERT ON requests
FOR EACH ROW
  BEGIN

    (SELECT
       userRoleID,
       login
     INTO @userRoleID, @login
     FROM users
     WHERE userID = NEW.marketAgentUserID);

    IF (@userRoleID <> 'MARKET_AGENT')
    THEN
      CALL generateLogistError(CONCAT('Can\'t insert row: only MARKET_AGENT users allowed for login = ', @login));
    END IF;

    SET @pointType = (SELECT pointTypeID
                      FROM points
                      WHERE pointID = NEW.warehousePointID);
    IF (@pointType IS NOT NULL)
    THEN
      BEGIN
        IF (@pointType <> 'WAREHOUSE')
        THEN
          CALL generateLogistError('Can\'t insert row: only WAREHOUSE points allowed');
        END IF;
      END;
    END IF;

  END;

CREATE TRIGGER after_request_insert AFTER INSERT ON requests
FOR EACH ROW
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
       NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate, NEW.documentNumber, NEW.documentDate,
       NEW.firma, NEW.storage, NEW.contactName, NEW.contactPhone, NEW.deliveryOption, NEW.deliveryDate, NEW.boxQty, NEW.weight, NEW.volume, NEW.goodsCost,
       NEW.lastStatusUpdated, NEW.lastModifiedBy, 'CREATED', NEW.commentForStatus, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

-- таблица обновляется через триггеры на таблице requests и через запрос к таблице mat_view_route_points_sequential внутри этих триггеров
CREATE TABLE mat_view_arrival_time_for_request (
  requestID                   INTEGER,
  arrivalTimeToNextRoutePoint DATETIME,
  PRIMARY KEY (requestID),
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TRIGGER after_request_update AFTER UPDATE ON requests
FOR EACH ROW
  BEGIN
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
       NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate, NEW.documentNumber, NEW.documentDate,
       NEW.firma, NEW.storage, NEW.contactName, NEW.contactPhone, NEW.deliveryOption, NEW.deliveryDate, NEW.boxQty, NEW.weight, NEW.volume, NEW.goodsCost,
       NEW.lastStatusUpdated, NEW.lastModifiedBy, NEW.requestStatusID, NEW.commentForStatus, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

    -- расчет времени прибытия заявки в следующий пункт маршрута
    IF (NEW.requestStatusID = 'DEPARTURE') THEN
      BEGIN
        SET @timeToNextPoint = (SELECT timeToNextPoint FROM mat_view_route_points_sequential WHERE routePointID = NEW.lastVisitedRoutePointID);
        SET @arrivalTimeToNextPoint = TIMESTAMPADD(MINUTE, @timeToNextPoint, NEW.lastStatusUpdated);

        INSERT INTO mat_view_arrival_time_for_request VALUE (NEW.requestID, @arrivalTimeToNextPoint)
        ON DUPLICATE KEY UPDATE
          arrivalTimeToNextRoutePoint = VALUES(arrivalTimeToNextRoutePoint);
      END;
    END IF;

  END;


CREATE TRIGGER after_request_delete AFTER DELETE ON requests
FOR EACH ROW
  INSERT INTO requests_history
    VALUE
    (NULL, NOW(), OLD.requestID, OLD.requestIDExternal, OLD.dataSourceID, OLD.requestNumber, OLD.requestDate,
     OLD.clientID, OLD.destinationPointID, OLD.marketAgentUserID, OLD.invoiceNumber, OLD.invoiceDate, OLD.documentNumber, OLD.documentDate,
     OLD.firma, OLD.storage, OLD.contactName, OLD.contactPhone, OLD.deliveryOption, OLD.deliveryDate, OLD.boxQty, OLD.weight, OLD.volume, OLD.goodsCost,
     OLD.lastStatusUpdated, OLD.lastModifiedBy, 'DELETED', OLD.commentForStatus, OLD.warehousePointID, OLD.routeListID, OLD.lastVisitedRoutePointID);

CREATE TRIGGER before_request_update BEFORE UPDATE ON requests
FOR EACH ROW
    -- берем пользователя, который изменил статус на один из request statuses, затем находим его пункт маршрута, и этот
    -- пункт записываем в таблицу requests в поле lastVisitedRoutePointID
    -- находим маршрут по которому едет накладная.
    IF (NEW.routeListID IS NOT NULL) THEN
      BEGIN

        SET @routeID = (SELECT routeID FROM route_lists WHERE NEW.routeListID = route_lists.routeListID);
        -- находим пункт пользователя, который изменил статус накладной.
        -- при появлении маршрутного листа сразу выставляем последний посещенный пункт, как первый пункт маршрута
        IF (NEW.lastVisitedRoutePointID IS NULL) THEN
          -- установка самого первого пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID) ORDER BY sortOrder LIMIT 1);
        -- если была установка статуса в ARRIVED, то последний посещенный пункт меняется на следующий
        ELSEIF (NEW.requestStatusID = 'ARRIVED' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder FROM route_points WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем следующий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder > @lastRoutePointSortOrder) ORDER BY sortOrder LIMIT 1);
          END;
        ELSEIF (NEW.requestStatusID = 'ERROR' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder FROM route_points WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем предыдущий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder < @lastRoutePointSortOrder) ORDER BY sortOrder LIMIT 1);
          END;
        ELSEIF (NEW.requestStatusID = 'DELIVERED' AND NEW.lastVisitedRoutePointID IS NOT NULL) THEN
          -- установка самого последнего пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID) ORDER BY sortOrder DESC LIMIT 1);
        END IF;
      END;
    ELSEIF (OLD.routeListID IS NOT NULL) THEN
      SET NEW.lastVisitedRoutePointID = NULL;
    END IF;


CREATE TABLE requests_history (
  requstHistoryID        BIGINT AUTO_INCREMENT,
  autoTimeMark            DATETIME       NOT NULL,

  -- JSON fields
  requestID               INTEGER,
  requestIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  requestNumber           VARCHAR(255)    NOT NULL,
  requestDate             DATE           NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
  clientID                INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL,     -- пункт доставки (addressId)
  marketAgentUserID       INTEGER        NOT NULL, -- traderId
  invoiceNumber           VARCHAR(255)   NOT NULL,
  invoiceDate             DATE           NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATE           NULL,
  firma                   VARCHAR(255)   NULL,
  storage                 VARCHAR(255)   NULL,     -- наименование склада на котором собиралась накладная
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  deliveryOption          VARCHAR(255)   NULL,     -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,

  -- logist fields
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  requestStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист

  PRIMARY KEY (requstHistoryID),
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- -------------------------------------------------------------------------------------------------------------------
--                                                   EXCHANGE_TABLE
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE exchange (
  packageNumber  INTEGER,
  serverName     VARCHAR(32),
  dataSource     VARCHAR(32),
  packageCreated DATETIME,
  packageData    LONGTEXT, -- наличие самих данных в соответсвующем порядке позволяет в любой момент пересоздать всю БД.
  PRIMARY KEY (packageNumber, serverName, dataSource),
  FOREIGN KEY (dataSource) REFERENCES data_sources (dataSourceID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- -------------------------------------------------------------------------------------------------------------------
--                                                   GETTERS
-- -------------------------------------------------------------------------------------------------------------------


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

CREATE FUNCTION getClientIDByUserID(_userID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT clientID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

-- находит все routeID, которые содержат указанный пункт
CREATE FUNCTION selectAllRoutesIDWithThatPointAsString(_pointID INTEGER)
  RETURNS TEXT
  BEGIN
    SET @result = (SELECT GROUP_CONCAT(DISTINCT routes.routeID)
                   FROM routes
                     INNER JOIN (route_points, points)
                       ON (routes.routeID = route_points.routeID AND route_points.pointID = points.pointID)
                   WHERE _pointID = points.pointID);
    RETURN @result;
  END;


-- -------------------------------------------------------------------------------------------------------------------
--                                                BIG SELECT
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE transmaster_transport_db.mat_view_parser_data (
  requestID            INTEGER,
  requestIDExternal    VARCHAR(255),
  requestNumber        VARCHAR(255),
  requestDate          DATE,
  invoiceNumber        VARCHAR(255),
  invoiceDate          DATE,
  documentNumber       VARCHAR(255),
  documentDate         DATE,
  firma                VARCHAR(255),
  storage              VARCHAR(255),
  boxQty               INTEGER,
  marketAgentUserID    INTEGER, -- служебное поле
  routeListID          INTEGER, -- служебное поле
  clientID             INTEGER, -- служебное поле
  clientIDExternal     VARCHAR(255),
  INN                  VARCHAR(255),
  clientName           VARCHAR(255),
  userName             VARCHAR(255), -- имя торгового представителя
  deliveryPointName    VARCHAR(255),
  warehousePointName   VARCHAR(255),
  warehousePointID     INTEGER, -- служебное поле
  routeID              INTEGER, -- служебное поле
  routeName            VARCHAR(255),
  driverId             VARCHAR(255),
  routeListNumber      VARCHAR(255),
  PRIMARY KEY (requestID)
);

-- таблица содержащая размеры массива данных с заявками
CREATE TABLE mat_view_row_count_for_user (
  userID      INTEGER,
  userRole    VARCHAR(32),
  total_count INTEGER,
  PRIMARY KEY (userID)
);

-- записываем в эту таблицу все данные, которые не меняются от выгрузки к выгрузке
CREATE PROCEDURE refreshMaterializedView()
  BEGIN

    TRUNCATE mat_view_parser_data;
    TRUNCATE mat_view_row_count_for_user;

    INSERT INTO mat_view_parser_data
      SELECT
        requests.requestID, -- служебное поле
        requests.requestIDExternal,
        requests.requestNumber,
        requests.requestDate,
        requests.invoiceNumber,
        requests.invoiceDate,
        requests.documentNumber,
        requests.documentDate,
        requests.firma,
        requests.storage,
        requests.boxQty,
        requests.marketAgentUserID, -- служебное поле
        requests.routeListID, -- служебное поле
        clients.clientID, -- служебное поле
        clients.clientIDExternal,
        clients.INN,
        clients.clientName,
        users.userName,
        delivery_points.pointName             AS deliveryPointName,
        w_points.pointName                    AS warehousePointName,
        w_points.pointID                      AS warehousePointID, -- служебное поле
        routes.routeID, -- служебное поле
        routes.routeName,
        (SELECT userName FROM users WHERE userID = route_lists.driverID) AS driverId,
        route_lists.routeListNumber
      FROM requests
        INNER JOIN (request_statuses, clients, users)
          ON (
          requests.requestStatusID = request_statuses.requestStatusID AND
          requests.clientID = clients.clientID AND
          requests.marketAgentUserID = users.userID
          )
        LEFT JOIN (points AS delivery_points, points AS w_points)
          ON (
          requests.warehousePointID = w_points.pointID AND
          requests.destinationPointID = delivery_points.pointID
          )
        LEFT JOIN (route_lists, routes)
          ON (
          requests.routeListID = route_lists.routeListID AND
          route_lists.routeID = routes.routeID
          );

    -- расчет размеров всех результатов для пользователей
     INSERT INTO mat_view_row_count_for_user
     -- MARKET_AGENT
       (SELECT
          mat_view_parser_data.marketAgentUserID,
          users.userRoleID,
          COUNT(mat_view_parser_data.requestID)
        FROM users
          INNER JOIN mat_view_parser_data ON (users.userID = mat_view_parser_data.marketAgentUserID)
        GROUP BY mat_view_parser_data.marketAgentUserID
        ORDER BY NULL)

       UNION ALL
       -- CLIENT_MANAGER
       (SELECT
          users.userID,
          users.userRoleID,
          COUNT(mat_view_parser_data.requestID)
        FROM users
          INNER JOIN mat_view_parser_data ON (users.clientID = mat_view_parser_data.clientID)
        GROUP BY mat_view_parser_data.clientID
        ORDER BY NULL)

       UNION ALL
       -- ADMIN
       (SELECT
          users.userID,
          users.userRoleID,
          COUNT(mat_view_parser_data.requestID)
        FROM users
          INNER JOIN mat_view_parser_data
        WHERE userRoleID = 'ADMIN'
        GROUP BY users.userID
        ORDER BY NULL);

  END;

CREATE FUNCTION splitString(stringSpl TEXT, delim VARCHAR(12), pos INT)
  RETURNS TEXT
  RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(stringSpl, delim, pos),
                           CHAR_LENGTH(SUBSTRING_INDEX(stringSpl, delim, pos - 1)) + 1),
                 delim, '');

CREATE FUNCTION getPrefix(_colName VARCHAR(255))
  RETURNS VARCHAR(255)
  BEGIN
    IF (_colName IN ('requestIDExternal',
                     'requestNumber',
                     'requestDate',
                     'invoiceNumber',
                     'invoiceDate',
                     'documentNumber',
                     'documentDate',
                     'firma',
                     'storage',
                     'boxQty',
                     'INN',
                     'clientIDExternal',
                     'clientName',
                     'userName',
                     'deliveryPointName',
                     'warehousePointName',
                     'routeName',
                     'driverId',
                     'routeListNumber',
                     'routeListID'))
    THEN
      RETURN 'mat_view_parser_data';
    ELSEIF (_colName IN ('requestStatusID', 'commentForStatus'))
      THEN RETURN 'requests';
    ELSEIF (_colName = 'requestStatusRusName')
      THEN RETURN 'request_statuses';
    ELSEIF (_colName = 'pointName')
      THEN RETURN 'points';
    ELSEIF (_colName = 'nextPointName')
      THEN RETURN 'mat_view_route_points_sequential';
    ELSEIF (_colName IN ('licensePlate', 'palletsQty'))
      THEN RETURN 'route_lists';
    ELSEIF (_colName = 'arrivalTimeToNextRoutePoint')
      THEN RETURN 'mat_view_arrival_time_for_request';
    END IF;
    RETURN '';
  END;

CREATE FUNCTION generateLikeCondition(map TEXT)
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
      SET @fullColumnName = CONCAT(getPrefix(@columnName), '.', @columnName);
      SET @searchString = splitString(pair, ',', 2);
      SET @searchString = CONCAT('%', @searchString, '%');

      SET result = CONCAT(result, @fullColumnName, ' LIKE ', '\'', @searchString, '\'', ' AND ');
    END WHILE;

    -- remove redundant END
    SET result = SUBSTR(result, 1, CHAR_LENGTH(result) - 4);
    SET result = CONCAT('(', result, ')');

    RETURN result;
  END;


CREATE FUNCTION generateOrderByPart(_orderby VARCHAR(255),_isDesc BOOLEAN)
  RETURNS VARCHAR(255)
  BEGIN

    IF (_orderby = '') THEN RETURN '';
    END IF;

    IF (_isDesc)
    THEN
      RETURN CONCAT(' ORDER BY ', _orderby, ' DESC');
    ELSE
      RETURN CONCAT(' ORDER BY ', _orderby);
    END IF;

  END;

-- _search - array of strings
-- _orderby 'id'  <> - название колонки из файла main.js
-- _search - передача column_name1,search_string1;column_name1,search_string1;... если ничего нет то передавать пустую строку

CREATE PROCEDURE selectData(_userID INTEGER, _startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255),
                            _isDesc BOOLEAN, _search TEXT)
  BEGIN

    SET @userRoleID = getRoleIDByUserID(_userID);

    SET @isAdmin = FALSE;
    SET @isMarketAgent = FALSE;
    SET @isClientManager = FALSE;
    SET @isDispatcherOrWDispatcher = FALSE;

    IF (@userRoleID = 'ADMIN') THEN
      SET @isAdmin = TRUE;
    ELSEIF (@userRoleID = 'MARKET_AGENT') THEN
      SET @isMarketAgent = TRUE;
    ELSEIF (@userRoleID = 'CLIENT_MANAGER') THEN
      SET @isClientManager = TRUE;
    ELSEIF (@userRoleID = 'DISPATCHER' OR @userRoleID = 'W_DISPATCHER') THEN
      SET @isDispatcherOrWDispatcher = TRUE;
    END IF;

    IF (@isClientManager) THEN
      SET @clientID = getClientIDByUserID(_userID);
    END IF;

    IF (@isDispatcherOrWDispatcher) THEN
      SET @userPointID = getPointIDByUserID(_userID);
      SET @allRoutesWithUserPointID = selectAllRoutesIDWithThatPointAsString(@userPointID);
    END IF;

    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи
    SET @columnsPart =
    '
SELECT
  mat_view_parser_data.requestIDExternal,
  mat_view_parser_data.requestNumber,
  mat_view_parser_data.requestDate,
  mat_view_parser_data.invoiceNumber,
  mat_view_parser_data.invoiceDate,
  mat_view_parser_data.documentNumber,
  mat_view_parser_data.documentDate,
  mat_view_parser_data.firma,
  mat_view_parser_data.storage,
  mat_view_parser_data.boxQty,
  mat_view_parser_data.INN,
  mat_view_parser_data.clientIDExternal,
  mat_view_parser_data.clientName,
  mat_view_parser_data.userName,
  mat_view_parser_data.deliveryPointName,
  mat_view_parser_data.warehousePointName,
  mat_view_parser_data.routeName,
  mat_view_parser_data.driverId,
  mat_view_parser_data.routeListNumber,
  mat_view_parser_data.routeListID,
  requests.requestStatusID,
  requests.commentForStatus,
  request_statuses.requestStatusRusName,
  points.pointName,
  mat_view_route_points_sequential.nextPointName,
  route_lists.licensePlate,
  route_lists.palletsQty,
  mat_view_arrival_time_for_request.arrivalTimeToNextRoutePoint

FROM mat_view_parser_data

  INNER JOIN (requests) ON (
    requests.requestID = mat_view_parser_data.requestID
    )
  INNER JOIN (request_statuses) ON (
    request_statuses.requestStatusID = requests.requestStatusID
    )
  LEFT JOIN (route_points) ON (
    route_points.routePointID = requests.lastVisitedRoutePointID
    )
  LEFT JOIN (points) ON (
    points.pointID = route_points.pointID
    )
  LEFT JOIN (routes) ON (
    routes.routeID = mat_view_parser_data.routeID
    )
  LEFT JOIN (route_lists) ON (
    route_lists.routeListID = mat_view_parser_data.routeListID
    )
  LEFT JOIN (mat_view_route_points_sequential) ON (
    mat_view_route_points_sequential.routePointID = requests.lastVisitedRoutePointID
    )
  LEFT JOIN (mat_view_arrival_time_for_request) ON (
    mat_view_arrival_time_for_request.requestID = mat_view_parser_data.requestID
    )
    ';
    SET @wherePart = '';

    IF @isAdmin THEN
      SET @wherePart = CONCAT('WHERE ', generateLikeCondition(_search));
    ELSEIF @isMarketAgent THEN
      SET @wherePart = CONCAT('WHERE (mat_view_parser_data.marketAgentUserID = ', _userID,') AND ', generateLikeCondition(_search));
    ELSEIF @isClientManager THEN
      SET @wherePart = CONCAT('WHERE (mat_view_parser_data.clientID = ', @clientID,') AND ', generateLikeCondition(_search));
    ELSEIF @isDispatcherOrWDispatcher THEN
      SET @wherePart = CONCAT('WHERE (mat_view_parser_data.routeID IN (', @allRoutesWithUserPointID,')) AND ', generateLikeCondition(_search));
    END IF;

    SET @orderByPart = generateOrderByPart(_orderby, _isDesc);

    SET @limitPart = ' LIMIT ?, ? ';

    SET @sqlString = CONCAT(@columnsPart, @wherePart, @orderByPart, @limitPart);

    PREPARE getDataStm FROM @sqlString;

    SET @_startEntry = _startEntry;
    SET @_length = _length;

    EXECUTE getDataStm
    USING @_startEntry, @_length;
    DEALLOCATE PREPARE getDataStm;

    -- filtered в случае, если присутвуют фильтры, то возвращается всегда -1.
    SELECT 200*40 as `totalFiltered`;
    -- SELECT FOUND_ROWS() as `totalFiltered`;

    -- total
    IF (@isAdmin OR @isMarketAgent OR @isClientManager)
    THEN
      BEGIN
        SELECT total_count AS totalCount
        FROM mat_view_row_count_for_user
        WHERE mat_view_row_count_for_user.userID = _userID;
      END;
    ELSEIF (@isDispatcherOrWDispatcher) THEN
      SET @countTotalSql = CONCAT('SELECT COUNT(*) as `totalCount` FROM mat_view_parser_data ', @wherePart);
      PREPARE getTotalStm FROM @countTotalSql;
      EXECUTE getTotalStm;
      DEALLOCATE PREPARE getTotalStm;
    END IF;

  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                USERS SELECT
-- -------------------------------------------------------------------------------------------------------------------

CREATE VIEW transmaster_transport_db.all_users AS
  SELECT
    users.userName,
    users.position,
    users.phoneNumber,
    users.email,
    'dummy' AS password,
    points.pointName,
    user_roles.userRoleRusName
  FROM users
    INNER JOIN (user_roles) ON (
      users.userRoleID = user_roles.userRoleID
      )
    LEFT JOIN (points) ON (
      users.pointID = points.pointID
      );

-- select users procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE selectUsers(_startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255), _isDesc BOOLEAN, _search TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM all_users
    WHERE (_search = '' OR userName LIKE @searchString OR position LIKE @searchString OR phoneNumber LIKE @searchString
           OR email LIKE @searchString OR pointName LIKE @searchString OR userRoleRusName LIKE @searchString)
    ORDER BY NULL ,
      CASE WHEN _orderby = '' THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'userName' THEN userName END ASC,
      CASE WHEN _isDesc AND _orderby = 'position' THEN position END ASC,
      CASE WHEN _isDesc AND _orderby = 'phoneNumber' THEN phoneNumber END ASC,
      CASE WHEN _isDesc AND _orderby = 'email' THEN email END ASC,
      CASE WHEN _isDesc AND _orderby = 'pointName' THEN pointName END ASC,
      CASE WHEN _isDesc AND _orderby = 'userRoleRusName' THEN userRoleRusName END ASC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'userName' THEN userName END DESC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'position' THEN position END DESC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'phoneNumber' THEN phoneNumber END DESC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'email' THEN email END DESC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'pointName' THEN pointName END DESC,
      CASE WHEN NOT(_isDesc) AND _orderby = 'userRoleRusName' THEN userRoleRusName END DESC
    LIMIT _startEntry, _length;

    -- filtered users
    SELECT FOUND_ROWS() as `totalFiltered`;

    -- total users
    SELECT COUNT(*) as `totalCount` FROM all_users;

  END;


-- -------------------------------------------------------------------------------------------------------------------
--                                    REQUEST STATUS HISTORY SELECT
-- -------------------------------------------------------------------------------------------------------------------


-- get history for some request
CREATE PROCEDURE selectRequestStatusHistory(_requestIDExternal VARCHAR(255))
  BEGIN
    SELECT
      requests_history.lastStatusUpdated    AS timeMarkWhenRequestWasChanged,
      requests_history.boxQty               AS boxQty,
      request_statuses.requestStatusRusName AS requestStatusRusName,
      points.pointName                      AS pointWhereStatusWasChanged,
      users.userName                        AS userNameThatChangedStatus,
      route_lists.routeListIDExternal       AS routeListIDExternal

    FROM requests_history
      INNER JOIN (request_statuses)
        ON (
        requests_history.requestStatusID = request_statuses.requestStatusID
        )
      LEFT JOIN (points, route_points) ON (
        requests_history.lastVisitedRoutePointID = route_points.routePointID AND
        route_points.pointID = points.pointID
        )
      LEFT JOIN (users) ON (
        requests_history.lastModifiedBy = users.userID
        )
      LEFT JOIN (route_lists) ON (
        requests_history.routeListID = route_lists.routeListID
        )
    WHERE requests_history.requestIDExternal = _requestIDExternal
    ORDER BY lastStatusUpdated;
  END;


-- -------------------------------------------------------------------------------------------------------------------
--                                TIME AND DISTANCE BETWEEN ROUTE POINTS SELECT
-- -------------------------------------------------------------------------------------------------------------------


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


-- -------------------------------------------------------------------------------------------------------------------
--                                                 INDEXES
-- -------------------------------------------------------------------------------------------------------------------

-- т.к. сортировка может использоваться для любой части  mat_view_parser_data, то на все поля вешается индекс
CREATE INDEX ind1 ON mat_view_parser_data (requestIDExternal(20));
CREATE INDEX ind2 ON mat_view_parser_data (requestNumber(20));
CREATE INDEX ind3 ON mat_view_parser_data (requestDate);
CREATE INDEX ind4 ON mat_view_parser_data (invoiceNumber(20));
CREATE INDEX ind5 ON mat_view_parser_data (invoiceDate);
CREATE INDEX ind6 ON mat_view_parser_data (documentNumber(20));
CREATE INDEX ind7 ON mat_view_parser_data (documentDate);
CREATE INDEX ind8 ON mat_view_parser_data (firma(20));
CREATE INDEX ind9 ON mat_view_parser_data (storage(20));
CREATE INDEX ind10 ON mat_view_parser_data (boxQty);
CREATE INDEX ind11 ON mat_view_parser_data (INN(20));
CREATE INDEX ind12 ON mat_view_parser_data (clientIDExternal(20));
CREATE INDEX ind13 ON mat_view_parser_data (clientName(20));
CREATE INDEX ind14 ON mat_view_parser_data (userName(20));
CREATE INDEX ind15 ON mat_view_parser_data (deliveryPointName(20));
CREATE INDEX ind16 ON mat_view_parser_data (warehousePointName(20));
CREATE INDEX ind17 ON mat_view_parser_data (routeName(20));
CREATE INDEX ind18 ON mat_view_parser_data (driverId(20));
CREATE INDEX ind19 ON mat_view_parser_data (routeListNumber(20));
CREATE INDEX ind20 ON mat_view_parser_data (routeListID);
CREATE INDEX ind21 ON requests (requestStatusID(20));
CREATE INDEX ind22 ON requests (commentForStatus(20));
CREATE INDEX ind23 ON request_statuses (requestStatusRusName(20));
CREATE INDEX ind24 ON points (pointName(20));
CREATE INDEX ind25 ON mat_view_route_points_sequential (nextPointName(20));
CREATE INDEX ind26 ON route_lists (licensePlate(20));
CREATE INDEX ind27 ON route_lists (palletsQty);
CREATE INDEX ind28 ON mat_view_arrival_time_for_request (arrivalTimeToNextRoutePoint);

-- индекс для поиска следующего пункта маршрута
CREATE INDEX ind30 ON mat_view_route_points_sequential (routePointID);
