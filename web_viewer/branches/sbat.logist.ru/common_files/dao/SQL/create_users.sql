# позволяет не только менять данные, но и создавать новые таблицы, процедуры, ... DDL
# SET PASSWORD FOR 'root'@'localhost' = PASSWORD('rtbyg7895otlgorit');
# позволяет только менять данные, серверное приложение должно использовать только этого пользователя для работы с БД.
DROP USER 'andy'@'localhost';
CREATE USER 'andy'@'localhost'
  IDENTIFIED BY 'andyandy';
GRANT SELECT, UPDATE, INSERT, DELETE, EXECUTE ON `transmaster_transport_db`.* TO 'andy'@'localhost'
WITH GRANT OPTION;
