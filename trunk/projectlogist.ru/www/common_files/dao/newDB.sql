DROP DATABASE IF EXISTS project_database;
CREATE DATABASE `project_database` CHARACTER SET utf8 COLLATE utf8_bin;
USE `project_database`;

CREATE TABLE user_roles (
  userRoleID INTEGER AUTO_INCREMENT,
  userRoleName VARCHAR(64) NOT NULL,
  PRIMARY KEY (userRoleID)
);
# администратор, ему доступен полный графический интерфейс сайта, и самые высокие права на изменение в БД:
# имеет право изменить роль пользователя
#
INSERT INTO user_roles (userRoleName) VALUES ('ADMIN');
# диспетчер склада, доступна часть GUI для выбора маршрута и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('WAREHOUSE_MANAGER');
# диспетчер, доступен GUI для установки статуса накладных и соответсвующие права на изменения в БД
INSERT INTO user_roles (userRoleName) VALUES ('MANAGER');
# клиент, доступен GUI для для просмотра данных своих ЗН(описание_проекта.odt) и права только на SELECT с его заявкой
INSERT INTO user_roles (userRoleName) VALUES ('CLIENT');
# временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
# что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.
INSERT INTO user_roles (userRoleName) VALUES ('TEMP_REMOVED');


CREATE TABLE users (
  userID INTEGER AUTO_INCREMENT,
  firstName VARCHAR(64) NOT NULL,
  lastName VARCHAR(64) NOT NULL,
  patronymic VARCHAR(64),
  login VARCHAR(128) NOT NULL,
  passMD5 VARCHAR(64) NOT NULL,
  phoneNumber VARCHAR(16) NOT NULL,
  email VARCHAR(64),
  userRoleID INTEGER NOT NULL,
  PRIMARY KEY (userID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE permissions (
  permissionID INTEGER AUTO_INCREMENT,
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


CREATE TABLE permissions_for_roles (
  roleID INTEGER,
  permissionID INTEGER,
  PRIMARY KEY (roleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES permissions (permissionID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (roleID) REFERENCES user_roles (userRoleID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE PROCEDURE inset_permission_for_role(IN user_role_name VARCHAR(64), IN permission_name VARCHAR(64))
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
  SELECT * FROM (SELECT userRoleID FROM user_roles WHERE userRoleName='ADMIN') as qwe1, (SELECT permissionID FROM permissions) AS qwe;

# add permissions to 'WAREHOUSE_MANAGER'
# TODO
# add permissions to 'MANAGER'
# TODO
# add permissions to 'CLIENT'
CALL inset_permission_for_role('CLIENT', 'selectUser');
CALL inset_permission_for_role('CLIENT', 'selectPoint');
CALL inset_permission_for_role('CLIENT', 'selectRoute');
# TODO
# add permissions to 'TEMP_REMOVED'

