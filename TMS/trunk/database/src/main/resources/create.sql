
------------------------------------------------------------------------------------------------------------------------
--                                        USERS ROLES PERMISSIONS AND POINTS                                          --
------------------------------------------------------------------------------------------------------------------------


CREATE TABLE user_roles (
  userRoleID VARCHAR(32),
  PRIMARY KEY (userRoleID)
);

INSERT INTO user_roles (userRoleID)
VALUES
-- администратор, ему доступен полный графический интерфейс сайта и самые высокие права на изменение в БД:
-- имеет право изменить роль пользователя
('ADMIN'),
-- диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
('W_DISPATCHER'),
-- диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
('DISPATCHER'),
-- пользователь клиента, доступен GUI для просмотра данных заявок клиента, которые проходят через пункт к которому привязан пользователь с ролью CLIENT_USER, возможность проставлять статус "доставлен"
('CLIENT_USER'),
-- пользователь клиента, доступен GUI для для просмотра всех заявок данного клиента, а не только тех, которые проходят через его пункт.
('CLIENT_MANAGER'),
-- торговый представитель, доступ только на чтение тех заявок, в которых он числится торговым
('MARKET_AGENT'),
-- временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
-- что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
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
  pointID     SERIAL, -- like integer but with autoIncrement
  pointName   VARCHAR(128)                                        NOT NULL,
  region      VARCHAR(128)                                        NULL,
  timeZone    SMALLINT CHECK (timeZone >= -12 AND timeZone <= 12) NULL, -- часовой пояс по гринвичу GMT + value
  docs        SMALLINT CHECK (docs >= 0 AND docs <= 100)          NULL, -- количество окон разгрузки
  comments    TEXT                                                NULL,
  openTime    TIME                                                NULL, -- например 9:00
  closeTime   TIME CHECK (closeTime > openTime)                   NULL, -- например 17:00
  district    VARCHAR(64)                                         NULL,
  locality    VARCHAR(64)                                         NULL,
  mailIndex   VARCHAR(6)                                          NULL,
  address     VARCHAR(256)                                        NOT NULL,
  email       VARCHAR(64)                                         NULL,
  phoneNumber VARCHAR(16)                                         NULL,
  pointTypeID VARCHAR(32)                                         NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
  ON DELETE NO ACTION
  ON UPDATE CASCADE,
  UNIQUE (pointName)
);

CREATE TABLE users (
  userID      SERIAL,
  firstName   VARCHAR(64)  NULL,
  lastName    VARCHAR(64)  NULL,
  patronymic  VARCHAR(64)  NULL,
  position    VARCHAR(64)  NULL, -- должность
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

CREATE OR REPLACE FUNCTION insert_permission_for_role(user_role_name VARCHAR(32), permission_name VARCHAR(32))
  RETURNS VOID AS
$$
BEGIN
  INSERT INTO permissions_for_roles (userRoleID, permissionID) SELECT
                                                                 user_roles.userRoleID,
                                                                 permissions.permissionID
                                                               FROM user_roles, permissions
                                                               WHERE user_roles.userRoleID = user_role_name AND
                                                                     permissions.permissionID = permission_name;
END;
$$
LANGUAGE plpgsql;

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
SELECT insert_permission_for_role('CLIENT', 'selectUser');  --like CALL insert_permission_for_role
SELECT insert_permission_for_role('CLIENT', 'selectPoint');
SELECT insert_permission_for_role('CLIENT', 'selectRoute');

