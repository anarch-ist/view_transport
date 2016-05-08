DROP SCHEMA public CASCADE;
CREATE SCHEMA public;


-- -------------------------------------------------------------------------------------------------------------------
--                                                 SUPPLIERS AND DONUTS(route lists)
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE suppliers (
  supplierID         SERIAL,
  INN                VARCHAR(32)  NOT NULL,
  PRIMARY KEY (supplierID)
);

-- содержит в себе список всех заказов с указанием дока - в который привозится товар, и временных промежутков(timeDiff)
CREATE TABLE donuts (
  donutID           SERIAL,
  creationDate      DATE         NOT NULL DEFAULT NOW(),
  comment           TEXT         NOT NULL,
  driver            VARCHAR(255) NOT NULL,
  driverPhoneNumber VARCHAR(12)  NOT NULL,
  licensePlate      VARCHAR(9)   NOT NULL, -- государственный номер автомобиля
  palletsQty        INTEGER      NOT NULL CONSTRAINT positive_pallets_qty CHECK (palletsQty >= 0),
  supplierID        INTEGER      NOT NULL,
  PRIMARY KEY (donutID),
  FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
);


-- -------------------------------------------------------------------------------------------------------------------
--                                                 WAREHOUSES
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE warehouses (
  warehouseID         SERIAL,
  warehouseName       VARCHAR(128) NOT NULL,
  PRIMARY KEY (warehouseID)
);

CREATE TABLE docs (
  docID       SERIAL,
  docName     VARCHAR(255) NOT NULL,
  warehouseID INTEGER      NOT NULL,
  PRIMARY KEY (docID),
  FOREIGN KEY (warehouseID) REFERENCES warehouses (warehouseID)
);

CREATE TABLE periods (
  periodID    BIGSERIAL,
  docID       INTEGER                  NOT NULL,
  periodBegin TIMESTAMP WITH TIME ZONE NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodBegin) = '0'), -- NOT UPDATEABLE
  periodEnd   TIMESTAMP WITH TIME ZONE NOT NULL CHECK (EXTRACT(TIMEZONE FROM periodEnd) = '0'), -- NOT UPDATEABLE
  -- длина периода кратна 30 минутам
  CONSTRAINT multiplicity_of_the_period CHECK (periodEnd > periodBegin AND
                                               (EXTRACT(EPOCH FROM (periodEnd - periodBegin)) :: INTEGER % 1800) = 0),
  PRIMARY KEY (periodID),
  FOREIGN KEY (docID) REFERENCES docs (docID)
);

CREATE TYPE PERIOD_STATE AS ENUM ('OPENED', 'CLOSED', 'OCCUPIED');
CREATE TABLE periods_progress (
  periodID        BIGINT,
  changeTimeStamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() CHECK (EXTRACT(TIMEZONE FROM changeTimeStamp) = '0'), -- NOT UPDATEABLE
  state           PERIOD_STATE             NOT NULL, -- NOT UPDATEABLE
  donutID         INTEGER                  NULL,     -- NOT UPDATEABLE
  -- маршрутный лист приклепляется только к периодам с состоянием 'OCCUPIED'
  CONSTRAINT occupied_has_donut CHECK (donutID IS NOT NULL AND state :: TEXT = 'OCCUPIED' || donutID IS NULL AND
                                       state :: TEXT <> 'OCCUPIED'),
  PRIMARY KEY (periodID, changeTimeStamp),
  FOREIGN KEY (donutID) REFERENCES donuts (donutID)
);

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
  ('CANCELLED_BY_WAREHOUSE_USER', 'Отменен складом'),
  ('CANCELLED_BY_SUPPLIER_USER', 'Отмена поставщиком'),
  ('ERROR', 'Ошибка'),
  ('DELIVERED', 'Доставлен');

CREATE TABLE wants (
  wantID                      SERIAL,
  wantNumber                  VARCHAR(16) NOT NULL,
  boxQty                      SMALLINT    NOT NULL CONSTRAINT positive_box_qty CHECK (boxQty > 0),
  finalDestinationWarehouseID INTEGER     NOT NULL, -- конечный пункт доставки
  PRIMARY KEY (wantID),
  FOREIGN KEY (finalDestinationWarehouseID) REFERENCES warehouses (warehouseID)
);

-- NOT UPDATEABLE
CREATE TABLE wants_progress (
  wantID           INTEGER,
  changeTimeStamp  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() CHECK (EXTRACT(TIMEZONE FROM changeTimeStamp) = '0'),
  wantStatusID     VARCHAR(32)              NOT NULL,
  commentForStatus TEXT                     NOT NULL,
  donutID          INTEGER                  NOT NULL, -- заявка создается синхронно с пончиком(списком заявок), по ходу движения заявки от склада к складу пончик меняется.
  PRIMARY KEY (wantID, changeTimeStamp),
  FOREIGN KEY (wantID) REFERENCES wants (wantID),
  FOREIGN KEY (wantStatusID) REFERENCES want_statuses (wantStatusID),
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
  userName    VARCHAR(255) NOT NULL,
  phoneNumber VARCHAR(255) NOT NULL,
  email       VARCHAR(255) NOT NULL,
  position    VARCHAR(64)  NOT NULL, -- должность
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