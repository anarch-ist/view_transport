CREATE DATABASE project_database;
USE project_database;

#позвол€ет не только мен€ть данные, но и создавать новые таблицы, проуедуры, ... DDL
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('rtbyg7895otlgorit');
#позвол€ет только мен€ть данные, серверное приложение должно использовать только этого пользовател€ дл€ работы с Ѕƒ.
CREATE USER 'andy'@'localhost' IDENTIFIED BY 'andyandy';
GRANT SELECT, UPDATE, INSERT, DELETE, EXECUTE on `project_database`.* TO 'andy'@'localhost' WITH GRANT OPTION;


CREATE TABLE users (
    userID INTEGER AUTO_INCREMENT,
    firstName VARCHAR(64) NOT NULL,
    lastName VARCHAR(64) NOT NULL,
    patronymic VARCHAR(64),
    login VARCHAR(128) NOT NULL,
    passMD5 VARCHAR(64) NOT NULL,
    phoneNumber VARCHAR(16) NOT NULL,
    email VARCHAR(64),
    userStatus VARCHAR(16) NOT NULL,
    PRIMARY KEY (userID),
    FOREIGN KEY (userTypeID) REFERENCES user_types (userTypeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (userStatus) REFERENCES user_status (userStatus) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_roles (
    userTypeID INTEGER AUTO_INCREMENT,
    userTypeText VARCHAR(64) NOT NULL,
    PRIMARY KEY (userTypeID)
);

#администратор, ему доступен полный графический интерфейс сайта, и самые высокие права на изменение в Ѕƒ:
# имеет право изменить роль пользовател€
#
INSERT INTO user_roles (userTypeText) VALUES ('ADMIN');
#диспетчер склада, доступна часть GUI дл€ выбора маршрута и соответсвующие права на изменени€ в Ѕƒ
INSERT INTO user_roles (userTypeText) VALUES ('WAREHOUSE_MANAGER');
#диспетчер, доступен GUI дл€ установки статуса накладных и соответсвующие права на изменени€ в Ѕƒ
INSERT INTO user_roles (userTypeText) VALUES ('MANAGER');
#клиент, доступен GUI дл€ дл€ просмотра данных своих «Ќ(описание_проекта.odt) и права только на SELECT с его за€вкой
INSERT INTO user_roles (userTypeText) VALUES ('CLIENT');
#временно удален, доступен GUI только дл€ страницы авторизации, также после попытки войти необходимо выводить сообщение,
#что данный пользователь зарегистрирован в системе, но временно удален. ѕолный запрет на доступ к Ѕƒ.
INSERT INTO user_roles (userTypeText) VALUES ('TEMP_REMOVED');






CREATE TABLE permissions {

}



#typical permissions
# addUser
#
#
#


