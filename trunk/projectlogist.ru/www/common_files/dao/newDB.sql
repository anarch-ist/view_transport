CREATE DATABASE project_database;
USE project_database;

#��������� �� ������ ������ ������, �� � ��������� ����� �������, ���������, ... DDL
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('rtbyg7895otlgorit');
#��������� ������ ������ ������, ��������� ���������� ������ ������������ ������ ����� ������������ ��� ������ � ��.
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

#�������������, ��� �������� ������ ����������� ��������� �����, � ����� ������� ����� �� ��������� � ��:
# ����� ����� �������� ���� ������������
#
INSERT INTO user_roles (userTypeText) VALUES ('ADMIN');
#��������� ������, �������� ����� GUI ��� ������ �������� � �������������� ����� �� ��������� � ��
INSERT INTO user_roles (userTypeText) VALUES ('WAREHOUSE_MANAGER');
#���������, �������� GUI ��� ��������� ������� ��������� � �������������� ����� �� ��������� � ��
INSERT INTO user_roles (userTypeText) VALUES ('MANAGER');
#������, �������� GUI ��� ��� ��������� ������ ����� ��(��������_�������.odt) � ����� ������ �� SELECT � ��� �������
INSERT INTO user_roles (userTypeText) VALUES ('CLIENT');
#�������� ������, �������� GUI ������ ��� �������� �����������, ����� ����� ������� ����� ���������� �������� ���������,
#��� ������ ������������ ��������������� � �������, �� �������� ������. ������ ������ �� ������ � ��.
INSERT INTO user_roles (userTypeText) VALUES ('TEMP_REMOVED');






CREATE TABLE permissions {

}



#typical permissions
# addUser
#
#
#


