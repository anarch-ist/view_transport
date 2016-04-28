DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- -------------------------------------------------------------------------------------------------------------------
--                                                 SUPPLIERS
-- -------------------------------------------------------------------------------------------------------------------

-- entity
CREATE TABLE suppliers (
  supplierID         SERIAL,
  INN                VARCHAR(32)  NOT NULL,
  clientName         VARCHAR(255) NULL,
  KPP                VARCHAR(64)  NULL,
  corAccount         VARCHAR(64)  NULL,
  curAccount         VARCHAR(64)  NULL,
  BIK                VARCHAR(64)  NULL,
  bankName           VARCHAR(128) NULL,
  contractNumber     VARCHAR(64)  NULL,
  dateOfSigning      DATE         NULL,
  startContractDate  DATE         NULL,
  endContractDate    DATE         NULL,
  PRIMARY KEY (supplierID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 POINTS
-- -------------------------------------------------------------------------------------------------------------------

-- entity
CREATE TABLE points (
  pointID             SERIAL,
  pointName           VARCHAR(128)   NOT NULL,
  region              VARCHAR(128)   NULL,
  district            VARCHAR(64)    NULL,
  locality            VARCHAR(64)    NULL,
  mailIndex           VARCHAR(6)     NULL,
  address             TEXT           NULL,
  email               VARCHAR(255)   NULL,
  phoneNumber         VARCHAR(16)    NULL,
  responsiblePersonId VARCHAR(128)   NULL,
  PRIMARY KEY (pointID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 USERS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE user_roles (
  userRoleID      VARCHAR(32),
  userRoleRusName VARCHAR(128),
  PRIMARY KEY (userRoleID)
);

INSERT INTO user_roles (userRoleID, userRoleRusName)
VALUES
  ('W_BOSS', 'Руководитель склада'), -- обязан иметь пункт, но у него нет ссылки на поставщика
  ('WH_DISPATCHER', 'Диспетчер_склада'), -- обязан иметь пункт, но у него нет ссылки на поставщика
  ('SUPPLIER_MANAGER', 'Пользователь_поставщика') -- поставщик, не имеет пункта, но ссылается на поставщика
;

-- entity
CREATE TABLE users (
  userID      SERIAL,
  userLogin   VARCHAR(255) NOT NULL,
  salt        CHAR(16)     NOT NULL,
  passAndSalt CHAR(32)     NOT NULL,
  userRoleID  VARCHAR(32)  NOT NULL,
  userName    VARCHAR(255) NULL,
  phoneNumber VARCHAR(255) NULL,
  email       VARCHAR(255) NULL,
  position    VARCHAR(64)  NULL, -- должность
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  UNIQUE (userLogin)
);

-- W_BOSS  WH_DISPATCHER
CREATE TABLE point_users (
  userID  INTEGER,
  pointID INTEGER NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userID) REFERENCES users
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
);

-- SUPPLIER_MANAGER
CREATE TABLE suppliers_users (
  userID     INTEGER,
  supplierID INTEGER NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userID) REFERENCES users
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
);


CREATE TABLE permissions (
  permissionID VARCHAR(32),
  PRIMARY KEY (permissionID)
);

-- TODO create real permissions
INSERT INTO permissions (permissionID)
VALUES
  ('testPerm1'),
  ('testPerm2'),
  ('testPerm3');

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

CREATE OR REPLACE FUNCTION insert_permission_for_role(_userRoleID VARCHAR(32), _permissionID VARCHAR(32))
  RETURNS VOID AS
$$
BEGIN
  INSERT INTO permissions_for_roles (userRoleID, permissionID) SELECT
                                                                 user_roles.userRoleID,
                                                                 permissions.permissionID
                                                               FROM user_roles, permissions
                                                               WHERE user_roles.userRoleID = _userRoleID AND
                                                                     permissions.permissionID = _permissionID;
END;
$$
LANGUAGE plpgsql;

SELECT insert_permission_for_role('W_BOSS', 'testPerm1');
SELECT insert_permission_for_role('W_BOSS', 'testPerm2');
SELECT insert_permission_for_role('WH_DISPATCHER', 'testPerm2');
SELECT insert_permission_for_role('SUPPLIER_MANAGER', 'testPerm3');

-- -------------------------------------------------------------------------------------------------------------------
--                                        USERS ROLES PERMISSIONS AND POINTS
-- -------------------------------------------------------------------------------------------------------------------



CREATE TABLE docs (
  docID   INTEGER,
  docName VARCHAR(255),
  PRIMARY KEY (docID)
);

INSERT INTO docs VALUES (1, 'test_doc');

CREATE TABLE docs_for_points (
  docID   INTEGER,
  pointID INTEGER,
  PRIMARY KEY (docID, pointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID),
  FOREIGN KEY (docID) REFERENCES docs (docID)
);

CREATE TABLE time_diffs (
  timeDiffID SMALLINT,
  PRIMARY KEY (timeDiffID)
);

INSERT INTO time_diffs VALUES
  (1), (2), (3), (4), (5), (6), (7), (8), (9), (10), (11), (12),
  (13), (14), (15), (16), (17), (18), (19), (20), (21), (22), (23), (24),
  (25), (26), (27), (28), (29), (30), (31), (32), (33), (34), (35), (36),
  (37), (38), (39), (40), (41), (42), (43), (44), (45), (46), (47), (48);

CREATE TYPE DOC_STATE AS ENUM ('FREE', 'OCCUPIED', 'OCCUPIED_BY_BOSS');
-- entity
CREATE TABLE docs_container (
  containerID SERIAL,
  docID       INTEGER,
  timeDiffID  INTEGER,
  date        TIMESTAMP,
  docState    DOC_STATE DEFAULT 'FREE',
  UNIQUE (docID, timeDiffID, date),
  PRIMARY KEY (containerID),
  FOREIGN KEY (docID) REFERENCES docs (docID),
  FOREIGN KEY (timeDiffID) REFERENCES time_diffs (timeDiffID)
);


CREATE TYPE STATUS AS ENUM ('CREATED','UPDATED','DELETED');

CREATE TABLE docs_container_history (
  docsHistoryID SERIAL,
  containerID   INTEGER,
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
  donutHistoryID    SERIAL,
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
  wantsHistoryID     SERIAL,
  autoTimeMark       TIMESTAMP      NOT NULL,
  wantsID                 SERIAL,
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
  packageCreated TIMESTAMP,
  packageData TEXT, -- наличие самих данных в соответсвующем порядке позволяет в любой момент пересоздать всю БД.
  PRIMARY KEY (packageNumber, serverName)
);

