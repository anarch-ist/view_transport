DROP DATABASE IF EXISTS project_database;
CREATE DATABASE `project_database`
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE `project_database`;

#######################################################################################################################
#                                        USERS ROLES PERMISSIONS AND POINTS                                           #
#######################################################################################################################

CREATE TABLE user_roles (
  userRoleID   INTEGER AUTO_INCREMENT,
  userRoleName VARCHAR(64) NOT NULL,
  PRIMARY KEY (userRoleID)
);

# администратор, ему доступен полный графический интерфейс сайта и самые высокие права на изменение в БД:
# имеет право изменить роль пользователя
INSERT INTO user_roles (userRoleName) VALUES ('ADMIN');
# диспетчер склада, доступна часть GUI для выбора маршрута и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('W_DISPATCHER');
# диспетчер, доступен GUI для установки статуса накладных и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('DISPATCHER');
# клиент, доступен GUI для для просмотра данных своих ЗН(описание_проекта.odt) и права только на SELECT с его заявкой
INSERT INTO user_roles (userRoleName) VALUES ('CLIENT');
# торговый представитель, доступ только на чтение
INSERT INTO user_roles (userRoleName) VALUES ('MARKET_AGENT');
# временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
# что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
INSERT INTO user_roles (userRoleName) VALUES ('TEMP_REMOVED');

CREATE TABLE point_types (
  pointTypeID      INTEGER AUTO_INCREMENT,
  pointTypeName    VARCHAR(64) NOT NULL,
  pointTypeRusName VARCHAR(64) NOT NULL,
  PRIMARY KEY (pointTypeID)
);

INSERT INTO point_types (pointTypeName, pointTypeRusName) VALUES ('WAREHOUSE', 'Склад');
INSERT INTO point_types (pointTypeName, pointTypeRusName) VALUES ('AGENCY', 'Представительство');

CREATE TABLE points (
  pointID     INTEGER,
  pointName   VARCHAR(128) DEFAULT '' NOT NULL,
  region      VARCHAR(128)            NULL,
  district    VARCHAR(64)             NULL,
  locality    VARCHAR(64)             NULL,
  mailIndex   VARCHAR(6)              NULL,
  address     VARCHAR(256)            NOT NULL,
  email       VARCHAR(64)             NULL,
  phoneNumber VARCHAR(16)             NULL,
  pointTypeID INTEGER                 NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

# this table content is a subset of points table
CREATE TABLE warehouse_points (
  warehousePointID INTEGER,
  PRIMARY KEY (warehousePointID),
  FOREIGN KEY (warehousePointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE FUNCTION is_warehouse(pointTypeID INTEGER)
  RETURNS BOOLEAN
  BEGIN
    RETURN pointTypeID IN (SELECT pointTypeName
                           FROM point_types
                           WHERE pointTypeName = 'Склад');
  END;

CREATE TRIGGER before_points_insert
BEFORE INSERT ON points FOR EACH ROW
  BEGIN
    IF (is_warehouse(NEW.pointTypeID))
    THEN
      INSERT INTO warehouse_points VALUES (NEW.pointTypeID);
    END IF;
  END;

CREATE TRIGGER before_points_delete
BEFORE DELETE ON points FOR EACH ROW
  BEGIN
    IF (is_warehouse(old.pointTypeID))
    THEN
      DELETE FROM warehouse_points
      WHERE warehouse_points.warehousePointID = old.pointTypeID;
    END IF;
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
  userRoleID  INTEGER      NOT NULL,
  pointID     INTEGER      NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

# helper table, contains only manager users. Every time when add or update new user this table sync with users
CREATE TABLE manager_users (
  managerUserID INTEGER NOT NULL,
  PRIMARY KEY (managerUserID),
  FOREIGN KEY (managerUserID) REFERENCES users (userID)
);

CREATE FUNCTION is_manager(userRoleID INTEGER)
  RETURNS BOOLEAN
  BEGIN
    RETURN userRoleID IN (SELECT userRoleID
                          FROM user_roles
                          WHERE userRoleName = 'MARKET_AGENT');
  END;

CREATE TRIGGER before_users_insert
BEFORE INSERT ON users FOR EACH ROW
  BEGIN
    IF (is_manager(NEW.userRoleID))
    THEN
      INSERT INTO manager_users VALUES (NEW.userRoleID);
    END IF;
  END;

CREATE TRIGGER before_users_delete
BEFORE DELETE ON users FOR EACH ROW
  BEGIN
    IF (is_manager(old.userRoleID))
    THEN
      DELETE FROM manager_users
      WHERE manager_users.managerUserID = old.userID;
    END IF;
  END;

CREATE TRIGGER before_users_update
BEFORE UPDATE ON users FOR EACH ROW
  BEGIN

    IF (NEW.userID != old.userID)
    THEN
      UPDATE manager_users
      SET managerUserID = NEW.userID
      WHERE old.userID = manager_users.managerUserID;
    END IF;

    IF (NEW.userRoleID != old.userRoleID)
    THEN
      BEGIN
        IF (is_manager(NEW.userRoleID))
        THEN
          INSERT INTO manager_users VALUES (NEW.userRoleID);
        ELSE IF (is_manager(old.userRoleID))
        THEN
          DELETE FROM manager_users
          WHERE manager_users.managerUserID = old.userID;
        END IF;
        END IF;
      END;
    END IF;
  END;

CREATE TABLE permissions (
  permissionID   INTEGER AUTO_INCREMENT,
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
  roleID       INTEGER,
  permissionID INTEGER,
  PRIMARY KEY (roleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES permissions (permissionID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (roleID) REFERENCES user_roles (userRoleID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE PROCEDURE insert_permission_for_role(IN user_role_name VARCHAR(64), IN permission_name VARCHAR(64))
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
  SELECT *
  FROM (SELECT userRoleID
        FROM user_roles
        WHERE userRoleName = 'ADMIN') AS qwe1, (SELECT permissionID
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
  PRIMARY KEY (clientID)
);

CREATE TABLE requests (
  requestID          INTEGER,
  requestNumber      VARCHAR(16) NOT NULL,
  date               DATETIME    NOT NULL,
  managerUserID      INTEGER     NOT NULL,
  clientID           INTEGER     NOT NULL,
  destinationPointID INTEGER     NOT NULL, # адрес, куда должны быть доставлены все товары
  PRIMARY KEY (requestID),
  FOREIGN KEY (managerUserID) REFERENCES manager_users (managerUserID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


#######################################################################################################################
#                                          ROUTE AND ROUTE LISTS                                                      #
#######################################################################################################################


CREATE TABLE routes (
  routeID   INTEGER     NOT NULL,
  routeName VARCHAR(64) NOT NULL,
  PRIMARY KEY (routeID)
);

CREATE TABLE route_list_statuses (
  routeListStatusID   INTEGER AUTO_INCREMENT,
  routeListStatusName VARCHAR(16),
  PRIMARY KEY (routeListStatusID)
);

INSERT INTO route_list_statuses (routeListStatusName) VALUE ('CREATED');
INSERT INTO route_list_statuses (routeListStatusName) VALUE ('UPDATED');
INSERT INTO route_list_statuses (routeListStatusName) VALUE ('DELETED');

CREATE TABLE route_lists (
  routeListID    INTEGER,
  routListNumber VARCHAR(32)  NOT NULL,
  palletsQty     INTEGER      NULL,
  driver         VARCHAR(255) NULL,
  licensePlate   VARCHAR(9)   NULL, # государственный номер автомобиля
  routeID        INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
);

CREATE TABLE route_points (
  routePointID        INTEGER,
  sortOrder           INTEGER NOT NULL,
  tLoading            INTEGER NOT NULL, # в минутах
  timeToNextPoint     INTEGER NOT NULL, # в минутах
  distanceToNextPoint INTEGER NOT NULL, # в километрах
  arrivalTime         TIME    NOT NULL,
  pointID             INTEGER NOT NULL,
  routeID             INTEGER NOT NULL,
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE FUNCTION get_route_list_status_id_by_name(statusName VARCHAR(255))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeListStatusID
                  FROM route_list_statuses
                  WHERE routeListStatusID = statusName);
    RETURN result;
  END;

CREATE PROCEDURE insert_into_rout_list_history(
  routeListID       INTEGER,
  routListNumber    VARCHAR(32),
  palletsQty        INTEGER,
  driver            VARCHAR(255),
  licensePlate      VARCHAR(9),
  routeID           INTEGER,
  routeListStatusID INTEGER
)
  BEGIN
    INSERT INTO rout_list_history (timeMark, routeListID, routListNumber, palletsQty, driver, licensePlate, routeID, routeListStatusID)
    VALUES
      (NOW(), routeListID, routListNumber, palletsQty, driver, licensePlate, routeID, routeListStatusID);
  END;


CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.licensePlate, NEW.routeID, get_route_list_status_id_by_name('CREATED'));
  END;

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.licensePlate, NEW.routeID, get_route_list_status_id_by_name('UPDATED'));
  END;

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(OLD.routeListID, OLD.routListNumber, OLD.palletsQty, OLD.driver,
                                       OLD.licensePlate, OLD.routeID, get_route_list_status_id_by_name('DELETED'));
  END;

CREATE TABLE rout_list_history (
  routeListHistoryID BIGINT AUTO_INCREMENT,
  timeMark           DATETIME,
  routeListID        INTEGER,
  routListNumber     VARCHAR(32)  NOT NULL,
  palletsQty         INTEGER      NULL,
  driver             VARCHAR(255) NULL,
  licensePlate       VARCHAR(9)   NULL, # государственный номер автомобиля
  routeID            INTEGER      NOT NULL,
  routeListStatusID  INTEGER      NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (routeListStatusID) REFERENCES route_list_statuses (routeListStatusID)
);


#######################################################################################################################
#                                                INVOICES                                                             #
#######################################################################################################################


CREATE TABLE invoice_statuses (
  invoiceStatusID      INTEGER AUTO_INCREMENT,
  invoiceStatusName    VARCHAR(32)  NOT NULL,
  invoiceStatusRusName VARCHAR(128) NOT NULL,
  PRIMARY KEY (invoiceStatusID)
);

# duty statuses
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName)
VALUES ('CREATED', 'Внутренняя заявка добавлена в БД');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName)
VALUES ('DELETED', 'Внутренняя заявка добавлена в БД');
# insider request statuses
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName)
VALUES ('APPROVING', 'Выгружена на утверждение торговому представителю');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('RESERVED', 'Резерв');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('APPROVED', 'Утверждена к сборке');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('STOP_LIST', 'Стоп-лист');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('CREDIT_LIMIT', 'Кредитный лимит');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName)
VALUES ('RASH_CREATED', 'Создана расходная накладная');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('COLLECTING', 'Выдана на сборку');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('CHECK', 'На контроле');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('CHECK_PASSED', 'Контроль пройден');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('PACKAGING', 'Упаковано');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName)
VALUES ('READY', 'Проверка в зоне отгрузки/Готова к отправке');
# invoice statuses
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('DEPARTURE', 'Накладная убыла');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('ARRIVED', 'Накладная прибыла в пункт');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('ERROR', 'Ошибка. Возвращение в пункт');
INSERT INTO invoice_statuses (invoiceStatusName, invoiceStatusRusName) VALUES ('DELIVERED', 'Доставлено');

CREATE FUNCTION get_invoice_status_ID_by_name(statusName VARCHAR(255))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT invoiceStatusID
                  FROM invoice_statuses
                  WHERE invoiceStatusName = statusName);
    RETURN result;
  END;

# invoice объеденяет в себе внутреннюю заявку и накладную,
# при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
CREATE TABLE invoices (
  invoiceID        INTEGER,
  invoiceNumber    VARCHAR(16) NOT NULL,
  creationDate     DATETIME    NULL,
  deliveryDate     DATETIME    NULL,
  boxQty           INTEGER     NULL,
  sales_invoice    VARCHAR(16) NULL, # расходная накладная
  invoiceStatusID  INTEGER     NOT NULL,
  requestID        INTEGER     NOT NULL,
  warehousePointID INTEGER     NOT NULL,
  routeListID      INTEGER     NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (warehousePointID) REFERENCES warehouse_points (warehousePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routeListID) REFERENCES route_lists (routeListID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (invoiceNumber)
);

CREATE PROCEDURE insert_into_invoice_history(
  invoiceID        INTEGER,
  invoiceNumber    VARCHAR(16),
  creationDate     DATETIME,
  deliveryDate     DATETIME,
  boxQty           INTEGER,
  sales_invoice    VARCHAR(16),
  invoiceStatusID  INTEGER,
  requestID        INTEGER,
  warehousePointID INTEGER,
  routeListID      INTEGER
)
  BEGIN
    INSERT INTO invoice_history (timeMark, invoiceID, invoiceNumber, creationDate, deliveryDate, boxQty, sales_invoice, invoiceStatusID, requestID, warehousePointID, routeListID)
    VALUES
      (NOW(), invoiceID, invoiceNumber, creationDate, deliveryDate, boxQty, sales_invoice, invoiceStatusID, requestID,
       warehousePointID, routeListID);
  END;

CREATE TRIGGER after_invoice_insert AFTER INSERT ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      NEW.invoiceID, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.sales_invoice,
      get_invoice_status_ID_by_name('CREATED'), NEW.requestID, NEW.warehousePointID, NEW.routeListID);

CREATE TRIGGER after_invoice_update AFTER UPDATE ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      NEW.invoiceID, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty, NEW.sales_invoice,
      NEW.invoiceStatusID, NEW.requestID, NEW.warehousePointID, NEW.routeListID);

CREATE TRIGGER after_invoice_delete AFTER DELETE ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      OLD.invoiceID, OLD.invoiceNumber, OLD.creationDate, OLD.deliveryDate, OLD.boxQty, OLD.sales_invoice,
      get_invoice_status_ID_by_name('DELETED'), OLD.requestID, OLD.warehousePointID, OLD.routeListID);

CREATE TABLE invoice_history (
  invoiceHistoryID BIGINT AUTO_INCREMENT,
  timeMark         DATETIME,
  invoiceID        INTEGER,
  invoiceNumber    VARCHAR(16) NOT NULL,
  creationDate     DATETIME    NULL,
  deliveryDate     DATETIME    NULL,
  boxQty           INTEGER     NULL,
  sales_invoice    VARCHAR(16) NULL, # расходная накладная
  invoiceStatusID  INTEGER     NOT NULL,
  requestID        INTEGER     NOT NULL,
  warehousePointID INTEGER     NOT NULL,
  routeListID      INTEGER     NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
);


#######################################################################################################################
#                                               USER ACTION HISTORY                                                   #
#######################################################################################################################


CREATE TABLE tables (
  tableID   INTEGER AUTO_INCREMENT,
  tableName VARCHAR(64) NOT NULL,
  PRIMARY KEY (tableID)
);

INSERT INTO tables (tableName) SELECT TABLE_NAME
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
);

#TODO продумать политику ссылочной целостности и расставить соответствующие атрибуты у внешних ключей