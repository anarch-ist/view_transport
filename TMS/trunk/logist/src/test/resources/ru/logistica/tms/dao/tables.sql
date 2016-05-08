DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- -------------------------------------------------------------------------------------------------------------------
--                                                 SUPPLIERS
-- -------------------------------------------------------------------------------------------------------------------

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
--                                                 DONUTS(route lists)
-- -------------------------------------------------------------------------------------------------------------------

-- содержит в себе список всех заказов с указанием дока - в который привозится товар, и временных промежутков(timeDiff)
CREATE TABLE donuts (
  donutID           SERIAL,
  creationDate      DATE         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  palletsQty        INTEGER      NULL,
  supplierID        INTEGER      NOT NULL,
  driver            VARCHAR(255) NULL,
  driverPhoneNumber VARCHAR(12)  NULL,
  licensePlate      VARCHAR(9)   NULL, -- государственный номер автомобиля
  comment           TEXT         NULL,
  PRIMARY KEY (donutID),
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 WAREHOUSES
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE warehouses (
  warehouseID         SERIAL,
  warehouseName       VARCHAR(128) NOT NULL,
  region              VARCHAR(128) NULL,
  district            VARCHAR(64)  NULL,
  locality            VARCHAR(64)  NULL,
  mailIndex           VARCHAR(6)   NULL,
  address             TEXT         NULL,
  email               VARCHAR(255) NULL,
  phoneNumber         VARCHAR(16)  NULL,
  responsiblePersonId VARCHAR(128) NULL,
  PRIMARY KEY (warehouseID)
);

CREATE TABLE docs (
  docID       SERIAL,
  docName     VARCHAR(255) NOT NULL,
  warehouseID INTEGER      NOT NULL,
  PRIMARY KEY (docID),
  FOREIGN KEY (warehouseID) REFERENCES warehouses (warehouseID)
);

CREATE TYPE PERIOD_STATE AS ENUM ('OPENED', 'CLOSED', 'OCCUPIED');

CREATE TABLE periods (
  periodID    BIGSERIAL,
  docID       INTEGER      NOT NULL,
  periodBegin TIMESTAMP    NOT NULL,
  periodEnd   TIMESTAMP    NOT NULL,
  state       PERIOD_STATE NOT NULL,
  donutID     INTEGER      NULL,
  CHECK (periodEnd > periodBegin AND (EXTRACT(EPOCH FROM (periodEnd - periodBegin))::INTEGER % 1800) = 0),
  -- маршрутный лист приклепляется только к периодам с состоянием 'OCCUPIED'
  CHECK (donutID IS NOT NULL AND state::TEXT = 'OCCUPIED' || donutID IS NULL AND state::TEXT <> 'OCCUPIED'),
  PRIMARY KEY (periodID),
  FOREIGN KEY (docID) REFERENCES docs (docID),
  FOREIGN KEY (donutID) REFERENCES donuts (donutID)
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
  ('WH_DISPATCHER', 'Диспетчер склада'), -- обязан иметь пункт, но у него нет ссылки на поставщика
  ('SUPPLIER_MANAGER', 'Пользователь поставщика') -- поставщик, не имеет пункта, но ссылается на поставщика
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
CREATE TABLE warehouse_users (
  userID      INTEGER,
  warehouseID INTEGER NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userID) REFERENCES users
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (warehouseID) REFERENCES warehouses (warehouseID)
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
--                                          WANTS(requests)
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE want_statuses (
  wantStatusID      VARCHAR(32),
  wantStatusRusName VARCHAR(255),
  PRIMARY KEY (wantStatusID)
);

INSERT INTO want_statuses
VALUES
  ('CREATED', 'Создан пакет для отправки'),
  ('CANCELLED_WH', 'Отменен складом'),
  ('CANCELLED_S', 'Отмена поставщиком'),
  ('ERROR', 'Ошибка'),
  ('DELIVERED', 'Доставлен');

CREATE TABLE wants (
  wantID             SERIAL,
  wantNumber         VARCHAR(16)    NOT NULL,
  wantDate           DATE           NULL,
  supplierID         INTEGER        NOT NULL,
  destinationPointID INTEGER        NULL, -- пункт доставки (конечный)
  invoiceNumber      VARCHAR(255)   NULL,
  invoiceDate        DATE           NULL,
  deliveryDate       TIMESTAMP      NULL,
  boxQty             INTEGER        NULL,
  weight             INTEGER        NULL, -- масса в граммах
  volume             INTEGER        NULL, -- в кубических сантиметрах
  goodsCost          DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP, -- date and time when status was updated by any user
  lastModifiedBy     INTEGER        NULL, -- один из пользователей - это parser.
  wantStatusID       VARCHAR(32)    NOT NULL,
  commentForStatus   TEXT           NULL,
  donutID            INTEGER        NULL,
  PRIMARY KEY (wantID),
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES warehouses (warehouseID)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
  ON DELETE SET NULL
  ON UPDATE CASCADE,
  FOREIGN KEY (wantStatusID) REFERENCES want_statuses (wantStatusID)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,
  FOREIGN KEY (donutID) REFERENCES donuts (donutID)
  ON DELETE SET NULL
  ON UPDATE CASCADE
);
