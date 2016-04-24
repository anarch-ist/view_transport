SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';
DROP DATABASE IF EXISTS backup_db;
CREATE DATABASE backup_db
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE backup_db;


CREATE TABLE exchange (
  packageNumber  INTEGER,
  serverName     VARCHAR(32),
  packageAdded   TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
  packageCreated DATETIME,
  packageData    LONGTEXT, -- наличие самих данных в соответсвующем порядке позволяет в любой момент пересоздать всю БД.
  PRIMARY KEY (packageNumber, serverName)
);