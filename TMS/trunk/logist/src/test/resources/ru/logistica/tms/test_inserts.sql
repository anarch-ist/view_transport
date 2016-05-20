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

INSERT INTO suppliers VALUES (1, 'supplier1Inn');
INSERT INTO warehouses VALUES (1, 'msk_warehouse', 'MSK');
INSERT INTO docs VALUES
  (1, 'msk_warehouse_doc1', 1),
  (2, 'msk_warehouse_doc2', 1),
  (3, 'msk_warehouse_doc3', 1)
;

INSERT INTO doc_periods VALUES (1, 1, '2016-10-19 10:00:00 Z', '2016-10-19 10:30:00 Z');
INSERT INTO doc_periods VALUES (2, 1, '2016-03-02 12:30:00 Z', '2016-03-02 13:00:00 Z');
INSERT INTO doc_periods VALUES (3, 1, '2016-03-02 13:00:00 Z', '2016-03-02 13:30:00 Z');
INSERT INTO donut_doc_periods VALUES (2, '2016-03-02', 'donut_comment', 'driver1', '89055678654', '123erk', 3, 1);

INSERT INTO orders VALUES (1, 'orderNumber1', 2, 1, 2, 'CREATED', 'commentForStatus');

INSERT INTO users VALUES (1, 'user1', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'SUPPLIER_MANAGER', 'ivanov ivan', '9260943566', 'ivan@s.ru', 'manager');
INSERT INTO supplier_users VALUES (1, 1);

INSERT INTO users VALUES (2, 'user2', 'jrteOl270Hx8gS75', '4e8941fd14c700ec2dd42f36b2e7cedf', 'WH_BOSS', 'сидоров петр', '9260943566', 'sidor@s.ru', 'boss');
INSERT INTO warehouse_users VALUES (2, 1);



