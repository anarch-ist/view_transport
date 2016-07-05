SET TIMEZONE = 'UTC';
TRUNCATE donut_doc_periods CASCADE;
TRUNCATE doc_periods CASCADE;
TRUNCATE docs CASCADE;
TRUNCATE warehouses CASCADE;
TRUNCATE suppliers CASCADE;
TRUNCATE orders CASCADE;
TRUNCATE users CASCADE;
TRUNCATE warehouse_users CASCADE;
TRUNCATE supplier_users CASCADE;
ALTER SEQUENCE doc_periods_docperiodid_seq RESTART;
ALTER SEQUENCE docs_docid_seq RESTART;
ALTER SEQUENCE orders_orderid_seq RESTART;
ALTER SEQUENCE suppliers_supplierid_seq RESTART;
ALTER SEQUENCE users_userid_seq RESTART;
ALTER SEQUENCE warehouses_warehouseid_seq RESTART;

-- EET, MSK, SAMT, YEKT, OMST, KRAT, IRKT, YAKT, VLAT, MAGT, PETT

------------------------ MAKE INSERTS ONLY WITH SEQUENCES, NEVER SPECIFY ID MANUALLY ----------------------------------
INSERT INTO suppliers (inn) VALUES ('supplier1Inn'); -- id == 1
INSERT INTO suppliers (inn) VALUES ('supplier2Inn'); -- id == 2
INSERT INTO warehouses (warehousename, rustimezoneabbr) VALUES ('msk_warehouse', 'MSK'); -- id == 1
INSERT INTO warehouses (warehousename, rustimezoneabbr) VALUES ('ekt_warehouse', 'YEKT'); -- id == 2
INSERT INTO docs (docname, warehouseid) VALUES
  ('msk_warehouse_doc1', 1), -- id == 1
  ('msk_warehouse_doc2', 1), -- id == 2
  ('msk_warehouse_doc3', 1), -- id == 3
  ('ekt_warehouse_doc1', 2), -- id == 4
  ('ekt_warehouse_doc2', 2), -- id == 5
  ('ekt_warehouse_doc3', 2)  -- id == 6
;


-- create users
INSERT INTO users (userlogin, salt, passandsalt, userroleid, username, phonenumber, email, position) -- id == 1
VALUES ('us1', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'SUPPLIER_MANAGER', 'ivanov ivan', '9260943566', 'ask_sergey@inbox.ru', 'manager');
INSERT INTO supplier_users VALUES (1, 1);

INSERT INTO users (userlogin, salt, passandsalt, userroleid, username, phonenumber, email, position) -- id == 2
VALUES ('ub1', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'WH_BOSS', 'сидоров петр', '9260943566', 'sidor@s.ru', 'boss');
INSERT INTO warehouse_users VALUES (2, 1);

INSERT INTO users (userlogin, salt, passandsalt, userroleid, username, phonenumber, email, position) -- id == 3
VALUES ('us2', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'SUPPLIER_MANAGER', 'smirnova', '9070543566', 'smirnovayi@logicsmart.ru', 'manager');
INSERT INTO supplier_users VALUES (3, 2);

INSERT INTO users (userlogin, salt, passandsalt, userroleid, username, phonenumber, email, position) -- id == 4
VALUES ('ud1', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'WH_DISPATCHER', 'komova olga', '90163569874', 'olga@s.ru', 'dispatcher');
INSERT INTO warehouse_users VALUES (4, 1);

-- create WH_SECURITY_OFFICER
INSERT INTO users (userlogin, salt, passandsalt, userroleid, username, phonenumber, email, position) -- id == 5
VALUES ('uo1', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'WH_SECURITY_OFFICER', 'sidorov petr', '90163569874', 'sidor@s.ru', 'officer');
INSERT INTO warehouse_users VALUES (5, 1);



-- create docPeriods
INSERT INTO doc_periods (docid, periodbegin, periodend) VALUES (1, '2016-10-19 10:00:00 Z', '2016-10-19 10:30:00 Z'); -- id == 1

INSERT INTO doc_periods (docid, periodbegin, periodend) VALUES (1, '2016-10-19 11:30:00 Z', '2016-10-19 13:00:00 Z'); -- id == 2
INSERT INTO donut_doc_periods (donutdocperiodid, creationdate, commentfordonut, driver, driverphonenumber, licenseplate, palletsqty, supplieruserid)
VALUES (2, '2016-10-19', 'donut_comment', 'driver1', '89055678654', '123erk', 3, 1);

INSERT INTO doc_periods (docid, periodbegin, periodend) VALUES (1, '2016-10-19 18:00:00 Z', '2016-10-19 18:30:00 Z'); -- id == 3
INSERT INTO donut_doc_periods (donutdocperiodid, creationdate, commentfordonut, driver, driverphonenumber, licenseplate, palletsqty, supplieruserid)
VALUES (3, '2016-10-19', 'donut_comment', 'driver2', '8903435642', '754erk', 6, 3);

INSERT INTO doc_periods (docid, periodbegin, periodend) VALUES (1, '2016-10-19 13:00:00 Z', '2016-10-19 13:30:00 Z'); -- id == 4

INSERT INTO orders (ordernumber, boxqty, finaldestinationwarehouseid, donutdocperiodid, orderstatus, commentforstatus, invoicenumber, goodscost, orderpalletsqty) -- id == 1
  VALUES ('orderNumber1', 2, 1, 2, 'CREATED', 'commentForStatus', 'invNumber1', 12000.00, 2);

-- SELECT md5(CONCAT(md5('testtest'), 'jrteOl270Hx8gS75'));


