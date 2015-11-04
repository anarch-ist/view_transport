#DROP not working in phpMyAdmin because of security restrictions, do it manually
DROP DATABASE IF EXISTS requestdb;
CREATE DATABASE `requestdb` CHARACTER SET utf8 COLLATE utf8_bin;
USE `requestdb`;
# CREATE USER 'andy'@'localhost' IDENTIFIED BY 'andyandy';
# GRANT ALL PRIVILEGES ON `requestdb`.* TO 'andy'@'localhost' WITH GRANT OPTION;

CREATE TABLE `usertype` (  `UserTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `UserTypeText` varchar(64) COLLATE utf8_bin NOT NULL,  UNIQUE KEY `UserTypeID` (`UserTypeID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Enum of users'' types';
CREATE TABLE `userstatus` (  `UserStatus` varchar(16) COLLATE utf8_bin NOT NULL,  `Description` varchar(64) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`UserStatus`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `users` (  `UserID` varchar(16) COLLATE utf8_bin NOT NULL,  `InnerUserID` varchar(16) COLLATE utf8_bin NOT NULL,  `UserTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `FirstName` varchar(64) COLLATE utf8_bin NOT NULL,  `LastName` varchar(64) COLLATE utf8_bin NOT NULL,  `Patronymic` varchar(64) COLLATE utf8_bin NOT NULL,  `NickName` varchar(128) COLLATE utf8_bin NOT NULL,  `PassMD5` varchar(64) COLLATE utf8_bin DEFAULT NULL,  `Telephone` varchar(16) COLLATE utf8_bin NOT NULL,  `Email` varchar(64) COLLATE utf8_bin DEFAULT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '',  `UserStatus` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`UserID`,`UserTypeID`,`LastModBy`,`UserStatus`),  UNIQUE KEY `UserID` (`UserID`),  UNIQUE KEY `UserID_2` (`UserID`),  UNIQUE KEY `UserID_3` (`UserID`,`InnerUserID`),  UNIQUE KEY `UserID_4` (`UserID`),  UNIQUE KEY `UserID_5` (`UserID`),  KEY `UserTypeID` (`UserTypeID`),  KEY `UserStatus` (`UserStatus`),  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`UserTypeID`) REFERENCES `usertype` (`UserTypeID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`UserStatus`) REFERENCES `userstatus` (`UserStatus`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `invoice` (  `InvoiceID` varchar(16) COLLATE utf8_bin NOT NULL,  `InvoiceNumber` varchar(16) COLLATE utf8_bin NOT NULL,  `Date` datetime NOT NULL,  `BoxQuantity` int(11) NOT NULL,  `T_start` datetime NOT NULL,  `T_end` datetime NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`InvoiceID`,`InvoiceNumber`,`LastModBy`),  UNIQUE KEY `InvoiceID` (`InvoiceID`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `goodsforinvoice` (  `InvoiceID` varchar(16) COLLATE utf8_bin NOT NULL,  `GoodID` varchar(16) COLLATE utf8_bin NOT NULL,  `GoodQuantity` int(10) unsigned NOT NULL,  PRIMARY KEY (`InvoiceID`,`GoodID`),  CONSTRAINT `goodsforinvoice_ibfk_1` FOREIGN KEY (`InvoiceID`) REFERENCES `invoice` (`InvoiceID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `pointtype` (  `PointTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `PointTypeText` varchar(64) COLLATE utf8_bin NOT NULL,  UNIQUE KEY `PointTypeID` (`PointTypeID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `point` (  `PointID` varchar(16) COLLATE utf8_bin NOT NULL,  `InnerPointID` varchar(16) COLLATE utf8_bin NOT NULL,  `PointName` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',  `PointTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `Region` varchar(128) COLLATE utf8_bin DEFAULT NULL,  `District` varchar(64) COLLATE utf8_bin DEFAULT NULL,  `Locality` varchar(64) COLLATE utf8_bin NOT NULL,  `MailIndex` varchar(6) COLLATE utf8_bin NOT NULL,  `Address` varchar(256) COLLATE utf8_bin NOT NULL,  `Email` varchar(64) COLLATE utf8_bin DEFAULT NULL,  `Telephone` varchar(16) COLLATE utf8_bin NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`PointID`,`PointName`,`PointTypeID`,`LastModBy`),  UNIQUE KEY `PointID` (`PointID`),  UNIQUE KEY `PointID_2` (`PointID`,`PointName`),  KEY `PointTypeID` (`PointTypeID`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `point_ibfk_1` FOREIGN KEY (`PointTypeID`) REFERENCES `pointtype` (`PointTypeID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `point_ibfk_2` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `routepoint` (  `RoutePointID` varchar(16) COLLATE utf8_bin NOT NULL,  `PointID` varchar(16) COLLATE utf8_bin NOT NULL,  `PreviousRoutePointID` varchar(16) COLLATE utf8_bin DEFAULT NULL,  `tLoading` int(11) DEFAULT NULL,  `tToNextPoint` int(11) DEFAULT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`RoutePointID`,`PointID`,`LastModBy`),  UNIQUE KEY `RouteID` (`RoutePointID`),  KEY `PointID` (`PointID`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `routepoint_ibfk_1` FOREIGN KEY (`PointID`) REFERENCES `point` (`PointID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `routepoint_ibfk_2` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `route` (  `RouteID` varchar(16) COLLATE utf8_bin NOT NULL,  `InnerRouteID` varchar(16) COLLATE utf8_bin NOT NULL,  `RouteName` varchar(64) COLLATE utf8_bin NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`RouteID`,`LastModBy`),  UNIQUE KEY `RouteID` (`RouteID`,`RouteName`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `route_ibfk_1` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `routeandroutepoints` (  `RouteID` varchar(16) COLLATE utf8_bin NOT NULL,  `RoutePointID` varchar(16) COLLATE utf8_bin NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`RouteID`,`RoutePointID`,`LastModBy`),  KEY `LastModBy` (`LastModBy`),  KEY `RoutePointID` (`RoutePointID`),  CONSTRAINT `routeandroutepoints_ibfk_1` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `routeandroutepoints_ibfk_2` FOREIGN KEY (`RouteID`) REFERENCES `route` (`RouteID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `routeandroutepoints_ibfk_3` FOREIGN KEY (`RoutePointID`) REFERENCES `routepoint` (`RoutePointID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `request` (  `RequestID` varchar(16) COLLATE utf8_bin NOT NULL,  `RequestNumber` varchar(16) COLLATE utf8_bin NOT NULL,  `Date` datetime NOT NULL,  `MarketAgent` varchar(16) COLLATE utf8_bin NOT NULL,  `Client` varchar(16) COLLATE utf8_bin NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`RequestID`,`RequestNumber`,`MarketAgent`,`Client`,`LastModBy`),  UNIQUE KEY `RequestID` (`RequestID`),  KEY `MarketAgent` (`MarketAgent`),  KEY `Client` (`Client`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `request_ibfk_1` FOREIGN KEY (`MarketAgent`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `request_ibfk_2` FOREIGN KEY (`Client`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `request_ibfk_3` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `insiderequest` (  `InsideRequestID` varchar(16) COLLATE utf8_bin NOT NULL,  `InsideRequestNumber` varchar(16) COLLATE utf8_bin NOT NULL,  `Warehouse` varchar(16) COLLATE utf8_bin NOT NULL,  `Date` datetime NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`InsideRequestID`,`InsideRequestNumber`,`Warehouse`,`LastModBy`),  UNIQUE KEY `InsideRequestID` (`InsideRequestID`),  KEY `LastModBy` (`LastModBy`),  KEY `Warehouse` (`Warehouse`),  CONSTRAINT `insiderequest_ibfk_1` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `insiderequest_ibfk_2` FOREIGN KEY (`Warehouse`) REFERENCES `point` (`PointID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `requestandinsiderequest` (  `RequestID` varchar(16) COLLATE utf8_bin NOT NULL,  `InsideRequestID` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`RequestID`,`InsideRequestID`),  KEY `InsideRequestID` (`InsideRequestID`),  CONSTRAINT `requestandinsiderequest_ibfk_1` FOREIGN KEY (`RequestID`) REFERENCES `request` (`RequestID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `requestandinsiderequest_ibfk_2` FOREIGN KEY (`InsideRequestID`) REFERENCES `insiderequest` (`InsideRequestID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `invoicesforinsiderequest` (  `InvoiceID` varchar(16) COLLATE utf8_bin NOT NULL,  `InsideRequestID` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`InvoiceID`,`InsideRequestID`),  KEY `InsideRequestID` (`InsideRequestID`),  CONSTRAINT `invoicesforinsiderequest_ibfk_1` FOREIGN KEY (`InvoiceID`) REFERENCES `invoice` (`InvoiceID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `invoicesforinsiderequest_ibfk_2` FOREIGN KEY (`InsideRequestID`) REFERENCES `insiderequest` (`InsideRequestID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `routeforinvoice` (  `InvoiceID` varchar(16) COLLATE utf8_bin NOT NULL,  `RouteID` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`InvoiceID`,`RouteID`),  KEY `RouteID` (`RouteID`),  CONSTRAINT `routeforinvoice_ibfk_1` FOREIGN KEY (`InvoiceID`) REFERENCES `invoice` (`InvoiceID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `routeforinvoice_ibfk_2` FOREIGN KEY (`RouteID`) REFERENCES `route` (`RouteID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `requizit` (  `RequizitID` varchar(16) COLLATE utf8_bin NOT NULL,  `INN` varchar(32) COLLATE utf8_bin NOT NULL,  `KPP` varchar(64) COLLATE utf8_bin NOT NULL,  `CorAccount` varchar(64) COLLATE utf8_bin NOT NULL,  `CurAccount` varchar(64) COLLATE utf8_bin NOT NULL,  `BIK` varchar(64) COLLATE utf8_bin NOT NULL,  `BankName` varchar(128) COLLATE utf8_bin NOT NULL,  `ContractNumber` varchar(64) COLLATE utf8_bin DEFAULT NULL,  `DateOfSigning` date DEFAULT NULL,  `StartContractDate` date DEFAULT NULL,  `EndContractDate` date DEFAULT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`RequizitID`),  UNIQUE KEY `RequizitID` (`RequizitID`),  KEY `LastModBy` (`LastModBy`),  CONSTRAINT `requizit_ibfk_2` FOREIGN KEY (`LastModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `requizitsforuser` (  `UserID` varchar(16) COLLATE utf8_bin NOT NULL,  `RequizitID` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`UserID`,`RequizitID`),  KEY `RequizitID` (`RequizitID`),  CONSTRAINT `requizitsforuser_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `requizitsforuser_ibfk_2` FOREIGN KEY (`RequizitID`) REFERENCES `requizit` (`RequizitID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `dispatchersforpoint` (  `PointID` varchar(16) COLLATE utf8_bin NOT NULL,  `UserID` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`PointID`,`UserID`),  KEY `UserID` (`UserID`),  CONSTRAINT `dispatchersforpoint_ibfk_1` FOREIGN KEY (`PointID`) REFERENCES `point` (`PointID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `dispatchersforpoint_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `routingsheet` (  `RoutingSheetNumber` varchar(16) COLLATE utf8_bin NOT NULL,  `RouteID` varchar(16) COLLATE utf8_bin NOT NULL,  `DriverID` varchar(16) COLLATE utf8_bin NOT NULL,  `LastModBy` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`RoutingSheetNumber`,`RouteID`,`LastModBy`),  KEY `LastModBy` (`LastModBy`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `routingsheetandinvoices` (  `RoutingSheetNumber` varchar(16) COLLATE utf8_bin NOT NULL,  `InvoiceNumber` varchar(16) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`RoutingSheetNumber`,`InvoiceNumber`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `statuslist` (  `StatusTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `Status` varchar(32) COLLATE utf8_bin NOT NULL,  PRIMARY KEY (`StatusTypeID`),  UNIQUE KEY `StatusID` (`StatusTypeID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `statushistory` (  `StatusID` varchar(16) COLLATE utf8_bin NOT NULL,  `StatusTypeID` varchar(16) COLLATE utf8_bin NOT NULL,  `ObjectID` varchar(16) COLLATE utf8_bin NOT NULL,  `StatusStartDate` datetime NOT NULL,  `ExpectedStatusStartDate` datetime NOT NULL,  `ExpectedStatusEndDate` datetime NOT NULL,  `PointLeavingID` varchar(16) COLLATE utf8_bin DEFAULT NULL,  `PointDestinationID` varchar(16) COLLATE utf8_bin DEFAULT NULL,  `ModBy` varchar(16) COLLATE utf8_bin NOT NULL,  `PreviousStatusID` varchar(16) COLLATE utf8_bin DEFAULT NULL,  `IsEnabled` tinyint(1) NOT NULL DEFAULT '1',  PRIMARY KEY (`StatusID`,`StatusTypeID`,`ObjectID`,`ModBy`),  UNIQUE KEY `StatusID` (`StatusID`),  UNIQUE KEY `StatusID_2` (`StatusID`),  KEY `ObjectID` (`ObjectID`),  KEY `PointID` (`PointLeavingID`),  KEY `StatusTypeID` (`StatusTypeID`),  KEY `ModBy` (`ModBy`),  CONSTRAINT `statushistory_ibfk_1` FOREIGN KEY (`StatusTypeID`) REFERENCES `statuslist` (`StatusTypeID`) ON DELETE CASCADE ON UPDATE CASCADE,  CONSTRAINT `statushistory_ibfk_2` FOREIGN KEY (`ModBy`) REFERENCES `users` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pointtype values('0101', 'Склад');
insert into pointtype values('0102', 'Клиент');
insert into pointtype values('0103', 'Представительство');

insert into statuslist values('0301', 'Накладная создана');
insert into statuslist values('0302', 'Накладная движется к пункту');
insert into statuslist values('0303', 'Накладная прибыла в пункт');
insert into statuslist values('0304', 'Ошибка. Возвращение в пункт');
insert into statuslist values('0305', 'Товар получен');

insert into userstatus values('ACTIVE', 'Активный');
insert into userstatus values('DELETE', 'Удаленный');
insert into userstatus values('REMOVE', 'Временно удаленный');

insert into usertype values('0001', 'Администратор');
insert into usertype values('0002', 'Диспетчер');
insert into usertype values('0003', 'Торговый представитель');
insert into usertype values('0004', 'Клиент');

insert into users values('100000000', '0', '0001', 'ROOT', 'ROOT', 'ROOT', 'ROOTUSER', '', '+79151186753', NULL, '', 'ACTIVE');

#table names
/*
SHOW CREATE TABLE dispatchersforpoint;
SHOW CREATE TABLE goodsforinvoice;
SHOW CREATE TABLE insiderequest;
SHOW CREATE TABLE invoice;
SHOW CREATE TABLE invoicesforinsiderequest;
SHOW CREATE TABLE point;
SHOW CREATE TABLE pointtype;
SHOW CREATE TABLE request;
SHOW CREATE TABLE requestandinsiderequest;
SHOW CREATE TABLE requizit;
SHOW CREATE TABLE requizitsforuser;
SHOW CREATE TABLE route;
SHOW CREATE TABLE routeandroutepoints;
SHOW CREATE TABLE routeforinvoice;
SHOW CREATE TABLE routepoint;
SHOW CREATE TABLE routingsheet;
SHOW CREATE TABLE routingsheetandinvoices;
SHOW CREATE TABLE statushistory;
SHOW CREATE TABLE statuslist;
SHOW CREATE TABLE users;
SHOW CREATE TABLE userstatus;
SHOW CREATE TABLE usertype;
*/