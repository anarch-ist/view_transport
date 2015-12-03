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
  pointTypeID VARCHAR(32),
  PRIMARY KEY (pointTypeID)
);

INSERT INTO point_types
VALUES
  ('WAREHOUSE'),
  ('AGENCY');

CREATE TABLE points (
  pointID     INTEGER AUTO_INCREMENT,
  pointName   VARCHAR(128)   NOT NULL,
  region      VARCHAR(128)   NULL,
  timeZone    TINYINT SIGNED NULL, # сдвиг времени по гринвичу GMT + value
  docs        TINYINT SIGNED NULL, # количество окон разгрузки
  comments    LONGTEXT       NULL,
  openTime    TIME           NULL, # например 9:00
  closeTime   TIME           NULL, # например 17:00
  district    VARCHAR(64)    NULL,
  locality    VARCHAR(64)    NULL,
  mailIndex   VARCHAR(6)     NULL,
  address     VARCHAR(256)   NOT NULL,
  email       VARCHAR(64)    NULL,
  phoneNumber VARCHAR(16)    NULL,
  pointTypeID VARCHAR(32)    NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE (pointName)
);


CREATE TABLE users (
  userID      INTEGER AUTO_INCREMENT,
  firstName   VARCHAR(64)  NULL,
  lastName    VARCHAR(64)  NULL,
  patronymic  VARCHAR(64)  NULL,
  position    VARCHAR(64)  NULL, # должность
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
      SET MESSAGE_TEXT = 'Can\'t insert row: only manager users allowed';
    END IF;
  END;


#######################################################################################################################
#                                          ROUTE AND ROUTE LISTS                                                      #
#######################################################################################################################


CREATE TABLE tariffs (
  tariffID INTEGER AUTO_INCREMENT,
  cost     DECIMAL(12, 2) NULL, # цена доставки
  capacity DECIMAL(4, 2)  NULL, # грузоподъёмность в тоннах
  carrier  VARCHAR(64), # перевозчик
  PRIMARY KEY (tariffID)
);

CREATE TABLE routes (
  routeID       INTEGER AUTO_INCREMENT,
  routeName     VARCHAR(64)    NOT NULL,
  directionName VARCHAR(255)   NULL, # имя направления из предметной области 1С, каждому направлению соответствует один маршрут
  tariffID INTEGER NULL,
  PRIMARY KEY (routeID),
  FOREIGN KEY (tariffID) REFERENCES tariffs (tariffID)
    ON DELETE SET NULL
    ON UPDATE CASCADE
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
  arrivalTime         TIME    NOT NULL, # время прибытия в данный пункт
  pointID             INTEGER NOT NULL,
  routeID             INTEGER NOT NULL,
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE route_lists (
  routeListID       INTEGER AUTO_INCREMENT,
  routListNumber    VARCHAR(32)  NOT NULL,
  palletsQty        INTEGER      NULL,
  driver            VARCHAR(255) NULL,
  driverPhoneNumber VARCHAR(12)  NULL,
  licensePlate      VARCHAR(9)   NULL, # государственный номер автомобиля
  routeID           INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  UNIQUE (routListNumber)
);

CREATE PROCEDURE insert_into_rout_list_history(
  routeListID       INTEGER,
  routListNumber    VARCHAR(32),
  palletsQty        INTEGER,
  driver            VARCHAR(255),
  driverPhoneNumber VARCHAR(12),
  licensePlate      VARCHAR(9),
  routeID           INTEGER,
  routeListStatusID VARCHAR(32)
)
  BEGIN
    INSERT INTO rout_list_history (timeMark, routeListID, routListNumber, palletsQty, driver, driverPhoneNumber, licensePlate, routeID, routeListStatusID)
    VALUES
      (NOW(), routeListID, routListNumber, palletsQty, driver, driverPhoneNumber, licensePlate, routeID, routeListStatusID);
  END;


CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.driverPhoneNumber,
                                       NEW.licensePlate, NEW.routeID, 'CREATED');
  END;

CREATE TRIGGER after_route_list_update AFTER UPDATE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(NEW.routeListID, NEW.routListNumber, NEW.palletsQty, NEW.driver,
                                       NEW.driverPhoneNumber,
                                       NEW.licensePlate, NEW.routeID, 'UPDATED');
  END;

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  BEGIN
    CALL insert_into_rout_list_history(OLD.routeListID, OLD.routListNumber, OLD.palletsQty, OLD.driver,
                                       OLD.driverPhoneNumber,
                                       OLD.licensePlate, OLD.routeID, 'DELETED');
  END;

CREATE TABLE rout_list_history (
  routeListHistoryID BIGINT AUTO_INCREMENT,
  timeMark           DATETIME,
  routeListID        INTEGER,
  routListNumber     VARCHAR(32)  NOT NULL,
  palletsQty         INTEGER      NULL,
  driver             VARCHAR(255) NULL,
  driverPhoneNumber  VARCHAR(12)  NULL,
  licensePlate       VARCHAR(9)   NULL, # государственный номер автомобиля
  routeID            INTEGER      NOT NULL,
  routeListStatusID  VARCHAR(32)  NOT NULL,
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
  PRIMARY KEY (invoiceStatusID)
);

INSERT INTO invoice_statuses
VALUES
  # duty statuses
  ('CREATED'),
  ('DELETED'),
  # insider request statuses
  ('APPROVING'),
  ('RESERVED'),
  ('APPROVED'),
  ('STOP_LIST'),
  ('CREDIT_LIMIT'),
  ('RASH_CREATED'),
  ('COLLECTING'),
  ('CHECK'),
  ('CHECK_PASSED'),
  ('PACKAGING'),
  ('READY'),
  # invoice statuses
  ('DEPARTURE'),
  ('ARRIVED'),
  ('ERROR'),
  ('DELIVERED');

CREATE TABLE invoice_statuses_for_user_role (
  userRoleID VARCHAR(32),
  invoiceStatusID VARCHAR(32),
  PRIMARY KEY (userRoleID, invoiceStatusID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles(userRoleID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses(invoiceStatusID)
);

-- TODO fill this list
INSERT INTO invoice_statuses_for_user_role
VALUES
  ('W_DISPATCHER', 'APPROVING'),
  ('W_DISPATCHER', 'RESERVED'),
  ('W_DISPATCHER', 'APPROVED'),
  ('W_DISPATCHER', 'STOP_LIST'),
  ('W_DISPATCHER', 'READY'),
  ('W_DISPATCHER', 'DEPARTURE'),
  ('W_DISPATCHER', 'ERROR'),
  ('DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'ARRIVED'),
  ('DISPATCHER', 'ERROR'),
  ('DISPATCHER', 'DELIVERED'),
  ('MARKET_AGENT', 'DEPARTURE'),
  ('MARKET_AGENT', 'ARRIVED'),
  ('MARKET_AGENT', 'ERROR'),
  ('MARKET_AGENT', 'DELIVERED');


# invoice объеденяет в себе внутреннюю заявку и накладную,
# при создании invoice мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
CREATE TABLE invoices (
  invoiceID              INTEGER AUTO_INCREMENT,
  insiderRequestNumber   VARCHAR(16)    NOT NULL,
  invoiceNumber          VARCHAR(16)    NOT NULL,
  creationDate           DATETIME       NULL,
  deliveryDate           DATETIME       NULL,
  boxQty                 INTEGER        NULL,
  weight                 INTEGER        NULL, # масса в граммах
  volume                 INTEGER        NULL, # в кубических сантиметрах
  goodsCost              DECIMAL(12, 2) NULL, # цена всех товаров в накладной
  sales_invoice          VARCHAR(16)    NULL, # расходная накладная
  invoiceStatusID        VARCHAR(32)    NOT NULL,
  requestID              INTEGER        NOT NULL,
  warehousePointID       INTEGER        NOT NULL,
  routeListID            INTEGER        NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedUserPointID INTEGER        NULL, # может быть NULL до тех пор пока не создан маршрутный лист
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
  FOREIGN KEY (lastVisitedUserPointID) REFERENCES points (pointID)
    ON DELETE CASCADE
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
      SET MESSAGE_TEXT = 'Can\'t insert row: only warehouse points allowed';
    END IF;
  END;


CREATE PROCEDURE insert_into_invoice_history(
  invoiceID              INTEGER,
  insiderRequestNumber   VARCHAR(16),
  invoiceNumber          VARCHAR(16),
  creationDate           DATETIME,
  deliveryDate           DATETIME,
  boxQty                 INTEGER,
  weight                 INTEGER,
  volume                 INTEGER,
  goodsCost              DECIMAL(12, 2),
  sales_invoice          VARCHAR(16),
  invoiceStatusID        VARCHAR(32),
  requestID              INTEGER,
  warehousePointID       INTEGER,
  routeListID            INTEGER,
  lastVisitedUserPointID INTEGER
)
  BEGIN
    INSERT INTO invoice_history (timeMark, invoiceID, insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume, goodsCost, sales_invoice, invoiceStatusID, requestID, warehousePointID, routeListID, lastVisitedUserPointID)
    VALUES
      (NOW(), invoiceID, insiderRequestNumber, invoiceNumber, creationDate, deliveryDate, boxQty, weight, volume,
       goodsCost, sales_invoice,
       invoiceStatusID, requestID,
       warehousePointID, routeListID, lastVisitedUserPointID);
  END;

CREATE TRIGGER after_invoice_insert AFTER INSERT ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      NEW.invoiceID, NEW.insiderRequestNumber, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty,
      NEW.weight, NEW.volume, NEW.goodsCost, NEW.sales_invoice,
      'CREATED', NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedUserPointID);


CREATE TRIGGER before_invoice_update BEFORE UPDATE ON invoices
FOR EACH ROW
  BEGIN
    # берем пользователя, который изменил статус на один из # invoice statuses, затем находим его пункт, и этот
    # пункт записываем в таблицу invoices в поле lastVisitedUserPointID
    IF (NEW.invoiceStatusID = 'DEPARTURE' OR NEW.invoiceStatusID = 'ARRIVED' OR NEW.invoiceStatusID = 'ERROR' OR NEW.invoiceStatusID = 'DELIVERED')
    # LAST_INSERT_ID обязан возвращать id из таблицы user_action_history, т.е. на уровне приложения сначала нужно записать данные о пользователе, а затем менять invoice
    THEN
      BEGIN
        DECLARE _userID INTEGER;
        SET _userID = (SELECT userID FROM user_action_history WHERE (tableID = 'invoices' AND userActionHistoryID = LAST_INSERT_ID()));
        SET NEW.lastVisitedUserPointID = (SELECT pointID FROM users WHERE userID = _userID);
      END;
    END IF ;

  END ;

CREATE TRIGGER after_invoice_update AFTER UPDATE ON invoices
FOR EACH ROW
  BEGIN
    CALL insert_into_invoice_history(
        NEW.invoiceID, NEW.insiderRequestNumber, NEW.invoiceNumber, NEW.creationDate, NEW.deliveryDate, NEW.boxQty,
        NEW.weight, NEW.volume, NEW.goodsCost, NEW.sales_invoice,
        NEW.invoiceStatusID, NEW.requestID, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedUserPointID);
  END;

CREATE TRIGGER after_invoice_delete AFTER DELETE ON invoices
FOR EACH ROW
  CALL insert_into_invoice_history(
      OLD.invoiceID, OLD.insiderRequestNumber, OLD.invoiceNumber, OLD.creationDate, OLD.deliveryDate, OLD.boxQty,
      OLD.weight, OLD.volume, OLD.goodsCost, OLD.sales_invoice,
      'DELETED', OLD.requestID, OLD.warehousePointID, OLD.routeListID, OLD.lastVisitedUserPointID);

CREATE TABLE invoice_history (
  invoiceHistoryID       BIGINT AUTO_INCREMENT,
  timeMark               DATETIME,
  invoiceID              INTEGER,
  insiderRequestNumber   VARCHAR(16)    NOT NULL,
  invoiceNumber          VARCHAR(16)    NOT NULL,
  creationDate           DATETIME       NULL,
  deliveryDate           DATETIME       NULL,
  boxQty                 INTEGER        NULL,
  weight                 INTEGER        NULL, # масса в граммах
  volume                 INTEGER        NULL, # в кубических сантиметрах
  goodsCost              DECIMAL(12, 2) NULL, # цена всех товаров в накладной
  sales_invoice          VARCHAR(16)    NULL, # расходная накладная
  invoiceStatusID        VARCHAR(32)    NOT NULL,
  requestID              INTEGER        NOT NULL,
  warehousePointID       INTEGER        NOT NULL,
  routeListID            INTEGER        NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedUserPointID INTEGER        NULL, # может быть NULL до тех пор пока не создан маршрутный лист
  PRIMARY KEY (invoiceHistoryID),
  FOREIGN KEY (invoiceStatusID) REFERENCES invoice_statuses (invoiceStatusID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


#######################################################################################################################
#                                               USER ACTION HISTORY                                                   #
#######################################################################################################################


CREATE TABLE tables (
  tableID VARCHAR(64),
  PRIMARY KEY (tableID)
);

INSERT INTO tables (tableID) SELECT information_schema.TABLES.TABLE_NAME
                               FROM information_schema.TABLES
                               WHERE TABLE_SCHEMA = 'project_database';

# invoices - диспетчер меняет статус
# информация о том. какой пользователь какую таблицу изменил есть на уровне сессии приложения
# на уровне приложения, во время установки нового статуса накладной - сначала идет запись в таблицу user_action_history а затем изменение статуса
CREATE TABLE user_action_history (
  userActionHistoryID BIGINT AUTO_INCREMENT,
  timeMark            DATETIME,
  userID              INTEGER,
  tableID             VARCHAR(64),
  action              ENUM('insert', 'update', 'delete'),
  PRIMARY KEY (userActionHistoryID),
  FOREIGN KEY (tableID) REFERENCES tables (tableID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


#######################################################################################################################
#                                                   GETTERS                                                           #
#######################################################################################################################


CREATE FUNCTION getPointIDByName(name VARCHAR(128))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT pointID
                  FROM points
                  WHERE name = pointName);
    RETURN result;
  END;

CREATE FUNCTION getUserIDByLogin(_login VARCHAR(128))
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

CREATE FUNCTION getRoutIDByRoutName(_routName VARCHAR(64))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeID
                  FROM routes
                  WHERE routeName = _routName);
    RETURN result;
  END;

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

# this function returns pointID for the next routePoint or NULL if it is already the last point
CREATE FUNCTION getNextPointID(_routeID INTEGER, _lastVisitedPointID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    DECLARE currentSortOrder INTEGER;
    DECLARE nextSortOrder INTEGER;

    SET currentSortOrder = (SELECT sortOrder FROM route_points WHERE (routeID = _routeID AND pointID = _lastVisitedPointID));

    IF (currentSortOrder IS NULL ) THEN
      BEGIN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Current sort order does not exist.';
      END;
    END IF;

    SET nextSortOrder = currentSortOrder + 1;
    SET result = (SELECT pointID FROM route_points WHERE routeID = _routeID AND sortOrder = nextSortOrder);

    RETURN result;
  END;

CREATE FUNCTION getNextRoutePointID(_routeID INTEGER, _lastVisitedPointID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    DECLARE currentSortOrder INTEGER;
    DECLARE nextSortOrder INTEGER;

    SET currentSortOrder = (SELECT sortOrder FROM route_points WHERE (routeID = _routeID AND pointID = _lastVisitedPointID));

    IF (currentSortOrder IS NULL ) THEN
      BEGIN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Current sort order does not exist.';
      END;
    END IF;

    SET nextSortOrder = currentSortOrder + 1;
    SET result = (SELECT routePointID FROM route_points WHERE routeID = _routeID AND sortOrder = nextSortOrder);

    RETURN result;
  END;



#######################################################################################################################
#                                                SELECT_PROCEDURES                                                    #
#######################################################################################################################

# запрос на выборку всех записей, которые должны быть в интерфейсе
# нужно уметь ограничивать количество результирующих записей

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

-- if user role is 'ADMIN' then return true
-- if user point inside route then return true
CREATE FUNCTION userFilter(_userID INTEGER, _routeID INTEGER)
  RETURNS BOOLEAN
  BEGIN
    IF (getRoleIDByUserID(_userID) = 'ADMIN')
    THEN
      RETURN FALSE ;
    ELSE
      -- get all points from _routeID
      RETURN getPointIDByUserID(_userID) IN (SELECT pointID FROM route_points WHERE _routeID = routeID);
    END IF;
  END;

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

CREATE PROCEDURE selectData(_userID INTEGER, startEntry INTEGER, length INTEGER)
  BEGIN

    SELECT
      requests.requestNumber,
      invoices.insiderRequestNumber,
      invoices.invoiceNumber,
      clients.INN,
      delivery_points.pointName as `deliveryPoint`,
      w_points.pointName as `warehousePoint`,
      users.lastName,
      invoices.invoiceStatusID,
      invoices.boxQty,

      route_lists.driver,
      route_lists.licensePlate,
      route_lists.palletsQty,
      route_lists.routListNumber,
      routes.directionName,

      last_visited_points.pointName as `currentPoint`,
      next_route_points.pointName as `nextPoint`,
      route_points.arrivalTime


    FROM invoices

      INNER JOIN (requests, clients, points AS delivery_points, points AS w_points, users)
        ON (
        invoices.requestID = requests.requestID AND
        invoices.warehousePointID = w_points.pointID AND
        requests.clientID = clients.clientID AND
        requests.destinationPointID = delivery_points.pointID AND
        requests.marketAgentUserID = users.userID
        )
      # because routeList in invoices table can be null, we use left join.
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
        )

    WHERE (
      (getRoleIDByUserID(_userID) = 'ADMIN') OR
      (getPointIDByUserID(_userID) = w_points.pointID) OR
      (getPointIDByUserID(_userID) IN (SELECT pointID FROM route_points WHERE route_lists.routeID = routeID))
    )

    LIMIT startEntry, length;

  END;
