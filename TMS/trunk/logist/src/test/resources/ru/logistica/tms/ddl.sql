DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;


-- -------------------------------------------------------------------------------------------------------------------
--                                                 WAREHOUSES
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE warehouses (
  warehouseID   SERIAL, -- Static
  warehouseName VARCHAR(128) NOT NULL, -- Dynamic
  rusTimeZoneAbbr VARCHAR(6) NOT NULL, -- Dynamic
  PRIMARY KEY (warehouseID)
);

CREATE TABLE docs (
  docID       SERIAL, -- Static
  docName     VARCHAR(255) NOT NULL, -- Dynamic
  warehouseID INTEGER      NOT NULL, -- Static
  PRIMARY KEY (docID),
  FOREIGN KEY (warehouseID) REFERENCES warehouses (warehouseID)
);
-- if no period => OPENED, if doc_period => OCCUPIED, if

CREATE TABLE doc_periods (
  docPeriodID BIGSERIAL, -- Static
  docID       INTEGER                  NOT NULL, -- Static
  periodBegin TIMESTAMP WITH TIME ZONE NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodBegin) = '0'), -- Static
  periodEnd   TIMESTAMP WITH TIME ZONE NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodEnd) = '0'), -- Static
  -- длина периода кратна 30 минутам
  -- BINDING with web.xml (periodSize)
  CONSTRAINT multiplicity_of_the_period CHECK (periodEnd > periodBegin AND
                                               (EXTRACT(EPOCH FROM (periodEnd - periodBegin)) :: INTEGER % 1800) = 0),
  PRIMARY KEY (docPeriodID),
  FOREIGN KEY (docID) REFERENCES docs (docID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                            SUPPLIERS, DONUTS AND ORDERS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE suppliers (
  supplierID SERIAL, -- Static
  INN        VARCHAR(32) NOT NULL, -- Static
  PRIMARY KEY (supplierID)
);

CREATE TABLE donut_doc_periods (
  donutDocPeriodID  BIGINT,
  creationDate      DATE         NOT NULL DEFAULT NOW(), -- Static
  comment           TEXT         NOT NULL, -- Static
  driver            VARCHAR(255) NOT NULL, -- Static
  driverPhoneNumber VARCHAR(12)  NOT NULL, -- Static
  licensePlate      VARCHAR(9)   NOT NULL, -- Static
  palletsQty        INTEGER      NOT NULL CONSTRAINT positive_pallets_qty CHECK (palletsQty >= 0), -- Static
  supplierID        INTEGER      NOT NULL, -- Static
  PRIMARY KEY (donutDocPeriodID),
  FOREIGN KEY (donutDocPeriodID) REFERENCES doc_periods (docPeriodID),
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
);

CREATE TABLE orders (
  orderID                     SERIAL, -- Static
  orderNumber                 VARCHAR(16) NOT NULL, -- Static
  boxQty                      SMALLINT    NOT NULL CONSTRAINT positive_box_qty CHECK (boxQty > 0), -- Static
  finalDestinationWarehouseID INTEGER     NOT NULL, -- Static
  donutDocPeriodID            INTEGER     NOT NULL, -- Dynamic
  orderStatus                 VARCHAR(32) NOT NULL, -- Dynamic
  commentForStatus            TEXT        NOT NULL, -- Dynamic
  -- BINDING ru.logistica.tms.daoorderDao.OrderStatuses
  CONSTRAINT order_statuses CHECK (orderStatus IN
                                   ('CREATED', 'CANCELLED_BY_WAREHOUSE_USER', 'CANCELLED_BY_SUPPLIER_USER', 'ERROR', 'DELIVERED')),
  PRIMARY KEY (orderID),
  FOREIGN KEY (finalDestinationWarehouseID) REFERENCES warehouses (warehouseID),
  FOREIGN KEY (donutDocPeriodID) REFERENCES donut_doc_periods (donutDocPeriodID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 USERS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE user_roles (
  userRoleID VARCHAR(32), -- Static
  PRIMARY KEY (userRoleID)
);

-- BINDING ru.logistica.tms.daouserDao.UserRoles
INSERT INTO user_roles (userRoleID)
VALUES
  ('WH_BOSS'), -- обязан иметь склад, но у него нет ссылки на поставщика
  ('WH_DISPATCHER'), -- обязан иметь склад, но у него нет ссылки на поставщика
  ('SUPPLIER_MANAGER') -- поставщик, не имеет склада, но ссылается на поставщика
;

CREATE TABLE users (
  userID        SERIAL, -- Static
  userLogin     VARCHAR(255) NOT NULL, -- Dynamic
  salt          CHAR(16)     NOT NULL, -- Dynamic
  passAndSalt   CHAR(32)     NOT NULL, -- Dynamic
  userRoleID    VARCHAR(32)  NOT NULL, -- Static
  userName      VARCHAR(255) NOT NULL, -- Dynamic
  phoneNumber   VARCHAR(255) NOT NULL, -- Dynamic
  email         VARCHAR(255) NOT NULL, -- Dynamic
  position      VARCHAR(64)  NOT NULL, -- Static
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID),
  UNIQUE (userLogin)
);


-- WH_BOSS  WH_DISPATCHER
CREATE TABLE warehouse_users (
  userID      INTEGER, -- Static
  warehouseID INTEGER NOT NULL, -- Static
  PRIMARY KEY (userID),
  FOREIGN KEY (userID) REFERENCES users
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (warehouseID) REFERENCES warehouses (warehouseID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
);

-- SUPPLIER_MANAGER
CREATE TABLE supplier_users (
  userID     INTEGER, -- Static
  supplierID INTEGER NOT NULL, -- Static
  PRIMARY KEY (userID),
  FOREIGN KEY (userID) REFERENCES users
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
);


CREATE TABLE permissions (
  permissionID VARCHAR(32), -- Static
  PRIMARY KEY (permissionID)
);

-- TODO create real permissions
INSERT INTO permissions (permissionID)
VALUES
  ('testPerm1'),
  ('testPerm2'),
  ('testPerm3');

CREATE TABLE permissions_for_roles (
  userRoleID   VARCHAR(32), -- Static
  permissionID VARCHAR(32), -- Static
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

SELECT insert_permission_for_role('WH_BOSS', 'testPerm1');
SELECT insert_permission_for_role('WH_BOSS', 'testPerm2');
SELECT insert_permission_for_role('WH_DISPATCHER', 'testPerm2');
SELECT insert_permission_for_role('SUPPLIER_MANAGER', 'testPerm3');

-- -------------------------------------------------------------------------------------------------------------------
--                                                 AUDIT SCHEMA
-- -------------------------------------------------------------------------------------------------------------------

DROP SCHEMA IF EXISTS audit CASCADE;
CREATE SCHEMA audit;
-- TODO create audit trigger
-- NOT UPDATEABLE
-- CREATE TABLE audit.orders_progress (
--   orderID          INTEGER,
--   changeTimeStamp  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() CHECK (EXTRACT(TIMEZONE FROM changeTimeStamp) = '0'),
--   donutID          INTEGER                  NOT NULL, -- заявка создается синхронно с пончиком(списком заявок), по ходу движения заявки от склада к складу пончик меняется.
--   orderStatusID    VARCHAR(32)              NOT NULL,
--   commentForStatus TEXT                     NOT NULL,
--   PRIMARY KEY (orderID, changeTimeStamp),
--   FOREIGN KEY (orderID) REFERENCES orders (orderID),
--   FOREIGN KEY (donutID) REFERENCES donuts (donutID)
-- );
--
-- CREATE TABLE audit.doc_periods_progress (
--   docPeriodID     BIGINT,
--   changeTimeStamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() CHECK (EXTRACT(TIMEZONE FROM changeTimeStamp) = '0'), -- NOT UPDATEABLE
--   periodStateID   VARCHAR(32)              NOT NULL, -- NOT UPDATEABLE
--   donutID         INTEGER                  NULL,     -- NOT UPDATEABLE
--   -- маршрутный лист приклепляется только к периодам с состоянием 'OCCUPIED'
--   CONSTRAINT occupied_has_donut CHECK (donutID IS NOT NULL AND periodStateID = 'OCCUPIED' || donutID IS NULL AND
--                                        periodStateID <> 'OCCUPIED'),
--   PRIMARY KEY (docPeriodID, changeTimeStamp),
--   FOREIGN KEY (docPeriodID) REFERENCES doc_periods (docPeriodID),
--   FOREIGN KEY (donutID) REFERENCES donuts (donutID)
-- );

-- -------------------------------------------------------------------------------------------------------------------
--                                                 DB USERS
-- -------------------------------------------------------------------------------------------------------------------

-- create user if not exist
REVOKE ALL PRIVILEGES ON DATABASE postgres FROM app_user;
REVOKE SELECT ON pg_timezone_names FROM app_user;
REVOKE SELECT ON pg_timezone_abbrevs FROM app_user;
REVOKE ALL PRIVILEGES ON SCHEMA pg_catalog FROM app_user;
REVOKE ALL PRIVILEGES ON SCHEMA information_schema FROM app_user;
DROP ROLE IF EXISTS app_user;
CREATE ROLE app_user LOGIN PASSWORD 'vghdfvce5485';

-- TODO fix app privileges and create admin user with privileges
-- application user privileges:
GRANT CONNECT ON DATABASE postgres TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT USAGE ON SCHEMA pg_catalog TO app_user;
GRANT SELECT ON pg_timezone_names TO app_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO app_user;
GRANT INSERT, UPDATE ON TABLE doc_periods, donut_doc_periods, orders TO app_user;
GRANT ALL ON SEQUENCE doc_periods_docperiodid_seq TO app_user;
GRANT ALL ON SEQUENCE orders_orderid_seq TO app_user;


