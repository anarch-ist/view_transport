DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- _errorMessage must be less or equal 110 symbols
-- CREATE PROCEDURE generateLogistError(_errorMessage TEXT)
-- BEGIN
-- SET @message_text = concat('LOGIST ERROR: ', _errorMessage);
-- SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @message_text;
-- END;

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

-- entity
CREATE TABLE suppliers (
  supplierID          SERIAL,
  supplierIDExternal  VARCHAR(255) NOT NULL,
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
  PRIMARY KEY (supplierID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE(supplierIDExternal, dataSourceID)
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
  ('W_BOSS', 'Руководитель склада'),
  -- показывать заявки, которые проходят через пункт к которому приписан W_DISPATCHER
  -- диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
  ('WH_DISPATCHER', 'Диспетчер_склада'),
  -- показывать заявки, которые проходят через пункт к которому приписан DISPATCHER
  -- диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
  ('SUPPLIER_MANAGER', 'Пользователь_поставщика')

-- TODO сделать правильну работу для указанных ниже ролей пользователей, прописать ограничения в таблице users
-- временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
-- что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
-- ('TEMP_REMOVED', 'Временно_удален'),
-- ('VIEW_LAST_TEN', 'Последние_десять')
;
-- entity
CREATE TABLE points (
  pointID             SERIAL,
  pointIDExternal     VARCHAR(128)   NOT NULL,
  dataSourceID        VARCHAR(32)    NOT NULL,
  pointName           VARCHAR(128)   NOT NULL,
  region              VARCHAR(128)   NULL,
  district            VARCHAR(64)    NULL,
  locality            VARCHAR(64)    NULL,
  mailIndex           VARCHAR(6)     NULL,
  address             TEXT           NULL,
  email               VARCHAR(255)   NULL,
  phoneNumber         VARCHAR(16)    NULL,
  responsiblePersonId VARCHAR(128)   NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  UNIQUE(pointIDExternal, dataSourceID)
);

CREATE TABLE docs (
  docID INTEGER,
  docName VARCHAR(255),
  PRIMARY KEY (docID)
);

INSERT INTO docs VALUES(1, 'test_doc');

CREATE TABLE docs_for_points (
  docID INTEGER,
  pointID INTEGER,
  PRIMARY KEY (docID, pointID),
  FOREIGN KEY (pointID) REFERENCES points(pointID),
  FOREIGN KEY (docID) REFERENCES docs(docID)
);

CREATE TABLE time_diffs (
  timeDiffID SMALLINT,
  PRIMARY KEY (timeDiffID)
);

INSERT INTO time_diffs VALUES
  (1),  (2),  (3),  (4),  (5),  (6),  (7),  (8),  (9),  (10), (11), (12),
  (13), (14), (15), (16), (17), (18), (19), (20), (21), (22), (23), (24),
  (25), (26), (27), (28), (29), (30), (31), (32), (33), (34), (35), (36),
  (37), (38), (39), (40), (41), (42), (43), (44), (45), (46), (47), (48);

CREATE TYPE DOC_STATE AS ENUM ('FREE', 'OCCUPIED', 'OCCUPIED_BY_BOSS');
-- entity
CREATE TABLE docs_container (
  containerID SERIAL,
  docID      INTEGER,
  timeDiffID INTEGER,
  date       TIMESTAMP,
  docState   DOC_STATE DEFAULT 'FREE',
  UNIQUE (docID, timeDiffID, date),
  PRIMARY KEY (containerID),
  FOREIGN KEY (docID) REFERENCES docs (docID),
  FOREIGN KEY (timeDiffID) REFERENCES time_diffs (timeDiffID)
);


CREATE TYPE STATUS AS ENUM ('CREATED','UPDATED','DELETED');

CREATE TABLE docs_container_history(
  docsHistoryID SERIAL8,
  containerID    INTEGER,
  docID         INTEGER,
  timeDiffID    INTEGER,
  date          TIMESTAMP,
  docState      DOC_STATE DEFAULT 'FREE',
  status        STATUS,
  PRIMARY KEY (docsHistoryID)
);

CREATE FUNCTION insertHistory() RETURNS trigger AS $emp_stamp$
BEGIN
  -- Check that empname and salary are given
  INSERT INTO docs_container_history (containerID, docID, timeDiffID, date, docState, status)
  VALUES
    (NEW.containerID, NEW.docID, NEW.timeDiffID, NEW.date, NEW.docState, 'UPDATED');
  RETURN NEW;
END;
$emp_stamp$ LANGUAGE plpgsql;

CREATE FUNCTION insertHistoryDelete() RETURNS trigger AS $emp_stamp$
BEGIN
  -- Check that empname and salary are given
  INSERT INTO docs_container_history
  VALUES
    (NULL, OLD.containerID, OLD.docID, OLD.timeDiffID, OLD.date, OLD.docState, 'UPDATED');
  RETURN OLD;
END;
$emp_stamp$ LANGUAGE plpgsql;


CREATE TRIGGER after_docs_container_insert AFTER INSERT OR UPDATE ON docs_container
FOR EACH ROW
EXECUTE PROCEDURE insertHistory();

CREATE TRIGGER after_docs_container_delete AFTER DELETE ON docs_container
FOR EACH ROW
EXECUTE PROCEDURE insertHistoryDelete();
-- entity
CREATE TABLE users (
  userID         SERIAL,
  userIDExternal VARCHAR(255) NOT NULL,
  dataSourceID   VARCHAR(32)  NOT NULL,
  login          VARCHAR(255) NOT NULL,
  salt           CHAR(16)     NOT NULL,
  passAndSalt    VARCHAR(64)  NOT NULL,
  userRoleID     VARCHAR(32)  NOT NULL,
  userName       VARCHAR(255) NULL,
  phoneNumber    VARCHAR(255) NULL,
  email          VARCHAR(255) NULL,
  position       VARCHAR(64)  NULL, -- должность
  pointID        INTEGER      NULL, -- у ADMIN и CLIENT_MANAGER и MARKET_AGENT не может быть пункта, у W_DISPATCHER и DISPATCHER обязан быть пункт
  supplierID       INTEGER      NULL, -- у CLIENT_MANAGER обязан быть clientID, у ADMIN W_DISPATCHER DISPATCHER и MARKET_AGENT его быть не должно
  PRIMARY KEY (userID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  FOREIGN KEY (supplierID ) REFERENCES suppliers (supplierID )
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  UNIQUE (userIDExternal, dataSourceID),
  UNIQUE (login)
);

-- CREATE PROCEDURE checkUserConstraints(_userRoleID VARCHAR(255), _pointID INTEGER, _supplierID INTEGER, _userIDExternal VARCHAR(255), _login VARCHAR(255))
-- BEGIN
-- IF (_userRoleID IN ( 'SUPPLIER_MANAGER') AND _pointID IS NOT NULL) THEN
-- CALL generateLogistError(CONCAT('W_BOSS  can\'t have a point. userIDExternal = ', _userIDExternal, ', login = ', _login));
--     END IF;
--
--     IF (_userRoleID IN ('W_BOSS','WH_DISPATCHER') AND _pointID IS NULL) THEN
--       CALL generateLogistError(CONCAT('WH_DISPATCHER must have a point. userIDExternal = ', _userIDExternal, ', login = ', _login));
--     END IF;
--
--     IF (_userRoleID = 'SUPPLIER_MANAGER' AND _supplierID IS NULL) THEN
--       CALL generateLogistError(CONCAT('SUPPLIER_MANAGER must have clientID. userIDExternal = ', _userIDExternal, ', login = ', _login));
--     END IF;
--   END;
--
-- CREATE TRIGGER check_users_constraints_insert BEFORE INSERT ON users
-- FOR EACH ROW
--   CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.supplierID, NEW.userIDExternal, NEW.login);
--
-- CREATE TRIGGER check_users_constraints_update BEFORE UPDATE ON users
-- FOR EACH ROW
--   CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.supplierID, NEW.userIDExternal, NEW.login);


CREATE TABLE permissions (
  permissionID VARCHAR(32),
  PRIMARY KEY (permissionID)
);



-- -------------------------------------------------------------------------------------------------------------------
--                                          ROUTE AND ROUTE LISTS
-- -------------------------------------------------------------------------------------------------------------------




CREATE TABLE donut_statuses (
  donutStatusID      VARCHAR(32),
  donutStatusRusName VARCHAR(255),
  PRIMARY KEY (donutStatusID)
);

INSERT INTO donut_statuses
VALUES
  ('CREATED', 'Создан пакет для отправки'),
  ('CANCELLED_WH', 'Отменен складом'),
  ('CANCELLED_S', 'Отмена поставщиком'),
  ('ERROR', 'Ошибка'),
  ('DELIVERED', 'Доставлен');
-- entity
CREATE TABLE donut_lists (
  donutID           SERIAL,
  creationDate      DATE         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  palletsQty        INTEGER      NULL,
  supplierID        INTEGER      NOT NULL,
  driver            VARCHAR(255) NULL,
  driverPhoneNumber VARCHAR(12)  NULL,
  licensePlate      VARCHAR(9)   NULL, -- государственный номер автомобиля
  status            VARCHAR(32)  NOT NULL,
  comment           VARCHAR(255) NULL,
  transitPointID    INTEGER      NOT NULL, -- пункт доставки (куда привезут сначала) - перенести на уровень пончика
  PRIMARY KEY (donutID),
  FOREIGN KEY (status) REFERENCES donut_statuses (donutStatusID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID),
  FOREIGN KEY (transitPointID) REFERENCES points (pointID)
);

-- CREATE TRIGGER after_donut_list_insert AFTER INSERT ON donut_lists
-- FOR EACH ROW
--   INSERT INTO donut_list_history
--   VALUES
--     (NULL, NOW(), NEW. donutID, NEW.donutIDExternal, NEW.dataSourceID, NEW.donutNumber, NEW.creationDate,
--      NEW.palletsQty, NEW.boxQty, NEW.driver,
--      NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.supplierId, NEW.comment, 'CREATED');
--
-- CREATE TRIGGER after_donut_list_update AFTER UPDATE ON donut_lists
-- FOR EACH ROW
--   INSERT INTO donut_list_history
--   VALUES
--     (NULL, NOW(), NEW. donutID, NEW.donutIDExternal, NEW.dataSourceID, NEW.donutNumber, NEW.creationDate,
--      NEW.palletsQty, NEW.boxQty, NEW.driver,
--      NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.supplierId, NEW.comment, 'UPDATED');
--
-- CREATE TRIGGER after_donut_list_delete AFTER DELETE ON donut_lists
-- FOR EACH ROW
--   INSERT INTO donut_list_history
--   VALUES
--     (NULL, NOW(), OLD.donutID, OLD.donutIDExternal, OLD.dataSourceID, OLD.donutNumber, OLD.creationDate,
--      OLD.palletsQty, OLD.boxQty, OLD.driver,
--      OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.supplierID, OLD.comment, 'DELETED');

CREATE TABLE donut_list_history (
  donutHistoryID    SERIAL8,
  timeMark          TIMESTAMP    NOT NULL,
  donutID           SERIAL,
  creationDate      DATE         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  palletsQty        INTEGER      NULL,
  supplierID        INTEGER      NOT NULL,
  driver            VARCHAR(255) NULL,
  driverPhoneNumber VARCHAR(12)  NULL,
  licensePlate      VARCHAR(9)   NULL, -- государственный номер автомобиля
  status            VARCHAR(32)  NOT NULL,
  comment           VARCHAR(255) NULL,
  transitPointID    INTEGER      NOT NULL, -- пункт доставки (куда привезут сначала) - перенести на уровень пончика
  PRIMARY KEY (donutHistoryID),
  FOREIGN KEY (status) REFERENCES donut_statuses (donutStatusID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
);

-- листы заказа
-- entity
CREATE TABLE wants (
  wantsID                 SERIAL,
--   wantsIDExternal         VARCHAR(255)   NOT NULL,
--   dataSourceID            VARCHAR(32)    NOT NULL,
  wantsNumber             VARCHAR(16)    NOT NULL,
  wantstDate              DATE           NULL,
  supplierID              INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL, -- пункт доставки (конечный)
  invoiceNumber           VARCHAR(255)   NULL,
  invoiceDate             DATE           NULL,
  deliveryDate            TIMESTAMP      NULL,
  boxQty                  INTEGER        NULL,
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP, -- date and time when status was updated by any user
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  wantsStatusID           VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  donutID                 INTEGER        NULL,
  PRIMARY KEY (wantsID),
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (wantsStatusID) REFERENCES donut_statuses (donutStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (donutID) REFERENCES donut_lists (donutID)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);


CREATE TABLE wants_history (
  wantsHistoryID     SERIAL8,
  autoTimeMark       TIMESTAMP      NOT NULL,
  wantsID                 SERIAL,
  --   wantsIDExternal         VARCHAR(255)   NOT NULL,
  --   dataSourceID            VARCHAR(32)    NOT NULL,
  wantsNumber             VARCHAR(16)    NOT NULL,
  wantstDate              DATE           NULL,
  supplierID              INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL, -- пункт доставки (конечный)
  invoiceNumber           VARCHAR(255)   NULL,
  invoiceDate             DATE           NULL,
  deliveryDate            TIMESTAMP      NULL,
  boxQty                  INTEGER        NULL,
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP, -- date and time when status was updated by any user
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  wantsStatusID           VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  donutID                 INTEGER        NULL,

  PRIMARY KEY (wantsHistoryID),
  FOREIGN KEY (wantsStatusID) REFERENCES donut_statuses (donutStatusID)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                   EXCHANGE_TABLE
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE exchange (
  packageNumber INTEGER,
  serverName VARCHAR(32),
  dataSource VARCHAR(32),
  packageCreated TIMESTAMP,
  packageData TEXT, -- наличие самих данных в соответсвующем порядке позволяет в любой момент пересоздать всю БД.
  PRIMARY KEY (packageNumber, serverName, dataSource)
);
-- Rows are compressed as they are inserted, https://dev.mysql.com/doc/refman/5.5/en/archive-storage-engine.html

-- -------------------------------------------------------------------------------------------------------------------
--                                                   GETTERS
-- -------------------------------------------------------------------------------------------------------------------


-- CREATE FUNCTION getRoleIDByUserID(_userID INTEGER)
--   RETURNS VARCHAR(32)
--   BEGIN
--     DECLARE result VARCHAR(32);
--     SET result = (SELECT userRoleID
--                   FROM users
--                   WHERE userID = _userID);
--     RETURN result;
--   END;
--
-- CREATE FUNCTION getPointIDByUserID(_userID INTEGER)
--   RETURNS INTEGER
--   BEGIN
--     DECLARE result INTEGER;
--     SET result = (SELECT pointID
--                   FROM users
--                   WHERE userID = _userID);
--     RETURN result;
--   END;
--
-- CREATE FUNCTION getClientIDByUserID(_userID INTEGER)
--   RETURNS INTEGER
--   BEGIN
--     DECLARE result INTEGER;
--     SET result = (SELECT supplierID
--                   FROM users
--                   WHERE userID = _userID);
--     RETURN result;
--   END;


