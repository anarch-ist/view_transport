DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- -------------------------------------------------------------------------------------------------------------------
--                                                 USERS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE user_roles (
  userRoleID VARCHAR(32),
  PRIMARY KEY (userRoleID)
);

-- BINDING ru.logistica.tms.dao.userDao.UserRoles
INSERT INTO user_roles (userRoleID)
VALUES
  -- обязан иметь склад, но у него нет ссылки на поставщика
  ('WH_BOSS'),
  -- обязан иметь склад, но у него нет ссылки на поставщика
  ('WH_DISPATCHER'),
  -- поставщик, не имеет склада, но ссылается на поставщика
  ('SUPPLIER_MANAGER')
;

CREATE TABLE users (
  userID        SERIAL,
  userLogin     VARCHAR(255) NOT NULL,
  salt          CHAR(16)     NOT NULL,
  passAndSalt   CHAR(32)     NOT NULL,
  userRoleID    VARCHAR(32)  NOT NULL,
  userName      VARCHAR(255) NOT NULL,
  phoneNumber   VARCHAR(255) NOT NULL,
  email         VARCHAR(255) NOT NULL,
  position      VARCHAR(64)  NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID),
  UNIQUE (userLogin)
);

-- BINDING ru.logistica.tms.dao.userDao.PermissionNamesames
CREATE TABLE permissions (
  permissionID VARCHAR(32),
  PRIMARY KEY (permissionID)
);

-- TODO create real permissions
INSERT INTO permissions (permissionID)
VALUES
  ('DELETE_ANY_DONUT'),
  ('DELETE_CREATED_DONUT');

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

--START_STM
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
--END_STM

SELECT insert_permission_for_role('WH_BOSS', 'DELETE_ANY_DONUT');
SELECT insert_permission_for_role('SUPPLIER_MANAGER', 'DELETE_CREATED_DONUT');

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

CREATE TABLE doc_periods (
  docPeriodID BIGSERIAL, -- Static
  docID       INTEGER     NOT NULL, -- Static
  periodBegin TIMESTAMPTZ NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodBegin) = '0'), -- Static
  periodEnd   TIMESTAMPTZ NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodEnd) = '0'), -- Static
  -- BINDING with web.xml (cellSize) 30 minutes.
  CONSTRAINT multiplicity_of_the_period CHECK (periodEnd > periodBegin AND
                                               (EXTRACT(EPOCH FROM (periodEnd - periodBegin)) :: INTEGER % 1800) = 0),
  PRIMARY KEY (docPeriodID),
  FOREIGN KEY (docID) REFERENCES docs (docID)
);

CREATE OR REPLACE FUNCTION doc_periods_no_intersections()
  RETURNS TRIGGER AS $doc_periods_no_intersections$
DECLARE
  qty INTEGER;
BEGIN
  qty := (SELECT COUNT(*)
          FROM doc_periods
          WHERE docid = NEW.docId AND
                (doc_periods.periodbegin, doc_periods.periodend) OVERLAPS (NEW.periodbegin, NEW.periodend));
  IF qty <> 0
  THEN RAISE EXCEPTION 'Период %, % занят частично или полностью.', NEW.periodbegin, NEW.periodend;
  ELSE RETURN NEW;
  END IF;
END;
$doc_periods_no_intersections$ LANGUAGE plpgsql;

CREATE TRIGGER doc_periods_no_intersections
BEFORE INSERT OR UPDATE ON doc_periods
FOR EACH ROW EXECUTE PROCEDURE doc_periods_no_intersections();

CREATE INDEX idx_time_limits_inversed
ON doc_periods (docID, periodBegin, periodEnd DESC);

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

-- -------------------------------------------------------------------------------------------------------------------
--                                            SUPPLIERS, DONUTS AND ORDERS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE suppliers (
  supplierID SERIAL, -- Static
  INN        VARCHAR(32) NOT NULL, -- Static
  PRIMARY KEY (supplierID)
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

CREATE TABLE donut_doc_periods (
  donutDocPeriodID  BIGINT,
  creationDate      DATE         NOT NULL DEFAULT NOW(), -- Static
  commentForDonut   TEXT         NOT NULL, -- Static
  driver            VARCHAR(255) NOT NULL, -- Static
  driverPhoneNumber VARCHAR(12)  NOT NULL, -- Static
  licensePlate      VARCHAR(9)   NOT NULL, -- Static
  palletsQty        INTEGER      NOT NULL CONSTRAINT positive_pallets_qty CHECK (palletsQty >= 0), -- Static
  supplierUserID    INTEGER      NOT NULL, -- Static
  PRIMARY KEY (donutDocPeriodID),
  FOREIGN KEY (donutDocPeriodID) REFERENCES doc_periods (docPeriodID),
  FOREIGN KEY (supplierUserID) REFERENCES supplier_users (userID)
);

CREATE TABLE orders (
  orderID                     SERIAL, -- Static
  orderNumber                 VARCHAR(16) NOT NULL, -- Static
  boxQty                      SMALLINT    NOT NULL CONSTRAINT positive_box_qty CHECK (boxQty >= 0), -- Static
  finalDestinationWarehouseID INTEGER     NOT NULL, -- Static
  donutDocPeriodID            INTEGER     NOT NULL, -- Dynamic
  orderStatus                 VARCHAR(32) NOT NULL, -- Dynamic
  commentForStatus            TEXT        NOT NULL, -- Dynamic
  -- BINDING ru.logistica.tms.dao.orderDao.OrderStatuses
  CONSTRAINT order_statuses CHECK (orderStatus IN
                                   ('CREATED', 'ARRIVED', 'CANCELLED_BY_WAREHOUSE_USER', 'CANCELLED_BY_SUPPLIER_USER', 'ERROR', 'DELIVERED')),
  PRIMARY KEY (orderID),
  FOREIGN KEY (finalDestinationWarehouseID) REFERENCES warehouses (warehouseID),
  FOREIGN KEY (donutDocPeriodID) REFERENCES donut_doc_periods (donutDocPeriodID)
);

-- -------------------------------------------------------------------------------------------------------------------
--                                                 AUDIT SCHEMA
-- -------------------------------------------------------------------------------------------------------------------

-- if you want functions work you must set local var with "SET LOCAL audit.current_user_id=userIdVal" inside transaction


DROP SCHEMA IF EXISTS audit CASCADE;
CREATE SCHEMA audit;


CREATE TABLE audit.doc_periods_audit (
  docPeriodsAuditID BIGSERIAL,
  operation         CHAR(1)     NOT NULL,
  stamp             TIMESTAMPTZ NOT NULL,
  userID            INTEGER     NOT NULL,

  docPeriodID       BIGINT      NOT NULL,
  docID             INTEGER     NOT NULL,
  periodBegin       TIMESTAMPTZ NOT NULL,
  periodEnd         TIMESTAMPTZ NOT NULL,
  PRIMARY KEY (docPeriodsAuditID)
);
CREATE OR REPLACE FUNCTION audit.process_doc_periods_audit() RETURNS TRIGGER AS $doc_periods_audit$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd)  SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.doc_periods_audit(operation, stamp, userID, docPeriodID, docID, periodBegin, periodEnd) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$doc_periods_audit$ LANGUAGE plpgsql;
CREATE TRIGGER doc_periods_audit
AFTER INSERT OR UPDATE OR DELETE ON doc_periods
FOR EACH ROW EXECUTE PROCEDURE audit.process_doc_periods_audit();


CREATE TABLE audit.donut_doc_periods_audit (
  donutDocPeriodsAuditID BIGSERIAL,
  operation              CHAR(1)      NOT NULL,
  stamp                  TIMESTAMPTZ  NOT NULL,
  userID                 INTEGER      NOT NULL,

  donutDocPeriodID       BIGINT       NOT NULL,
  creationDate           DATE         NOT NULL,
  commentForDonut        TEXT         NOT NULL,
  driver                 VARCHAR(255) NOT NULL,
  driverPhoneNumber      VARCHAR(12)  NOT NULL,
  licensePlate           VARCHAR(9)   NOT NULL,
  palletsQty             INTEGER      NOT NULL,
  supplierUserID         INTEGER      NOT NULL,
  PRIMARY KEY (donutDocPeriodsAuditID)
);
CREATE OR REPLACE FUNCTION audit.process_donut_doc_periods_audit() RETURNS TRIGGER AS $donut_doc_periods_audit$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID)   SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.donut_doc_periods_audit(operation, stamp, userID, donutDocPeriodID, creationDate, commentForDonut, driver, driverPhoneNumber, licensePlate, palletsQty, supplierUserID) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$donut_doc_periods_audit$ LANGUAGE plpgsql;
CREATE TRIGGER donut_doc_periods_audit
AFTER INSERT OR UPDATE OR DELETE ON donut_doc_periods
FOR EACH ROW EXECUTE PROCEDURE audit.process_donut_doc_periods_audit();


CREATE TABLE audit.orders_audit (
  ordersAuditID               BIGSERIAL,
  operation                   CHAR(1)     NOT NULL,
  stamp                       TIMESTAMPTZ NOT NULL,
  userID                      INTEGER     NOT NULL,

  orderID                     INTEGER,
  orderNumber                 VARCHAR(16) NOT NULL,
  boxQty                      SMALLINT    NOT NULL,
  finalDestinationWarehouseID INTEGER     NOT NULL,
  donutDocPeriodID            INTEGER     NOT NULL,
  orderStatus                 VARCHAR(32) NOT NULL,
  commentForStatus            TEXT        NOT NULL,
  PRIMARY KEY (ordersAuditID)
);
CREATE OR REPLACE FUNCTION audit.process_orders_audit() RETURNS TRIGGER AS $orders_audit$
DECLARE
  userID INTEGER;
BEGIN
  BEGIN
    -- BINDING DaoFacade.java passUserIdToAuditTrigger()
    userID := (SELECT current_setting('audit.current_user_id'))::INTEGER;
    EXCEPTION WHEN OTHERS THEN
    userID := NULL;
  END;
  IF (userID IS NULL)
  THEN RETURN NULL;
  END IF;
  IF (TG_OP = 'DELETE') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus) SELECT 'D', now(), userID, OLD.*;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus) SELECT 'U', now(), userID, NEW.*;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    INSERT INTO audit.orders_audit(operation, stamp, userID, orderID, orderNumber, boxQty, finalDestinationWarehouseID, donutDocPeriodID, orderStatus, commentForStatus) SELECT 'I', now(), userID, NEW.*;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$orders_audit$ LANGUAGE plpgsql;
CREATE TRIGGER orders_audit
AFTER INSERT OR UPDATE OR DELETE ON orders
FOR EACH ROW EXECUTE PROCEDURE audit.process_orders_audit();


-- -------------------------------------------------------------------------------------------------------------------
--                                                 DB USERS
-- -------------------------------------------------------------------------------------------------------------------

REVOKE ALL PRIVILEGES ON DATABASE postgres FROM app_user;
REVOKE SELECT ON pg_timezone_names FROM app_user;
REVOKE ALL PRIVILEGES ON SCHEMA pg_catalog FROM app_user;
DROP ROLE IF EXISTS app_user;
CREATE ROLE app_user LOGIN PASSWORD 'vghdfvce5485';
GRANT CONNECT ON DATABASE postgres TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT USAGE ON SCHEMA pg_catalog TO app_user;
GRANT USAGE ON SCHEMA audit TO app_user;
GRANT SELECT ON pg_catalog.pg_timezone_names TO app_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO app_user;
GRANT INSERT, UPDATE, DELETE ON TABLE public.doc_periods, public.donut_doc_periods, public.orders TO app_user;
GRANT ALL ON SEQUENCE public.doc_periods_docperiodid_seq, public.orders_orderid_seq TO app_user;
GRANT ALL ON SEQUENCE
audit.doc_periods_audit_docperiodsauditid_seq, audit.donut_doc_periods_audit_donutdocperiodsauditid_seq,
audit.orders_audit_ordersauditid_seq
TO app_user;
GRANT INSERT ON ALL TABLES IN SCHEMA audit TO app_user;


REVOKE ALL PRIVILEGES ON DATABASE postgres FROM admin_user;
REVOKE SELECT ON pg_timezone_names FROM admin_user;
REVOKE ALL PRIVILEGES ON SCHEMA pg_catalog FROM admin_user;
DROP ROLE IF EXISTS admin_user;
CREATE ROLE admin_user LOGIN PASSWORD 'dfnm2hk45';
GRANT CONNECT ON DATABASE postgres TO admin_user;
GRANT USAGE ON SCHEMA public TO admin_user;
GRANT USAGE ON SCHEMA audit TO admin_user;
GRANT USAGE ON SCHEMA pg_catalog TO admin_user;
GRANT SELECT ON pg_catalog.pg_timezone_names TO admin_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO admin_user;
GRANT SELECT ON ALL TABLES IN SCHEMA audit TO admin_user;
GRANT INSERT, UPDATE, DELETE ON TABLE
public.users, public.warehouse_users, public.supplier_users,
public.warehouses, public.suppliers, public.orders, public.docs,
public.doc_periods, public.donut_doc_periods
TO admin_user;
GRANT ALL ON SEQUENCE
public.doc_periods_docperiodid_seq, public.orders_orderid_seq, public.docs_docid_seq,
suppliers_supplierid_seq, users_userid_seq, warehouses_warehouseid_seq
TO admin_user;
