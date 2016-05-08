
CREATE TABLE time_diffs (
  timeDiffID SMALLINT,
  PRIMARY KEY (timeDiffID)
);

INSERT INTO time_diffs VALUES
  (1), (2), (3), (4), (5), (6), (7), (8), (9), (10), (11), (12),
  (13), (14), (15), (16), (17), (18), (19), (20), (21), (22), (23), (24),
  (25), (26), (27), (28), (29), (30), (31), (32), (33), (34), (35), (36),
  (37), (38), (39), (40), (41), (42), (43), (44), (45), (46), (47), (48);



-- entity
CREATE TABLE docs_container (
  containerID SERIAL,
  docID       INTEGER                     NOT NULL,
  timeDiffID  INTEGER                     NOT NULL,
  date        TIMESTAMP                   NOT NULL,
  docState    PERIOD_STATE DEFAULT 'FREE' NOT NULL,
  donutID     INTEGER                     NULL,
  UNIQUE (docID, timeDiffID, date),
  PRIMARY KEY (containerID),
  FOREIGN KEY (docID) REFERENCES docs (docID),
  FOREIGN KEY (timeDiffID) REFERENCES time_diffs (timeDiffID),
  FOREIGN KEY (donutID) REFERENCES donuts (donutID)
);


CREATE TYPE STATUS AS ENUM ('CREATED','UPDATED','DELETED');

CREATE TABLE docs_container_history (
  docsHistoryID SERIAL,
  containerID   INTEGER,
  docID         INTEGER,
  timeDiffID    INTEGER,
  date          TIMESTAMP,
  docState      PERIOD_STATE DEFAULT 'FREE',
  status        STATUS,
  PRIMARY KEY (docsHistoryID)
);

CREATE FUNCTION insertHistory() RETURNS trigger AS $emp_stamp$
BEGIN
  -- Check that empname and salary are given
  INSERT INTO docs_container_history (containerID, docID, timeDiffID, date, docState, status)
  VALUES
    (NEW.containerID, NEW.docID, NEW.timeDiffID, NEW.date, NEW.docState, 'UPDATED');
  RETURN NEW;
END;
$emp_stamp$ LANGUAGE plpgsql;

CREATE FUNCTION insertHistoryDelete() RETURNS trigger AS $emp_stamp$
BEGIN
  -- Check that empname and salary are given
  INSERT INTO docs_container_history
  VALUES
    (NULL, OLD.containerID, OLD.docID, OLD.timeDiffID, OLD.date, OLD.docState, 'UPDATED');
  RETURN OLD;
END;
$emp_stamp$ LANGUAGE plpgsql;


CREATE TRIGGER after_docs_container_insert AFTER INSERT OR UPDATE ON docs_container
FOR EACH ROW
EXECUTE PROCEDURE insertHistory();

CREATE TRIGGER after_docs_container_delete AFTER DELETE ON docs_container
FOR EACH ROW
EXECUTE PROCEDURE insertHistoryDelete();




CREATE TRIGGER after_donut_list_insert AFTER INSERT ON donut_lists
FOR EACH ROW
INSERT INTO donut_list_history
VALUES
  (NULL, NOW(), NEW. donutID, NEW.donutIDExternal, NEW.dataSourceID, NEW.donutNumber, NEW.creationDate,
         NEW.palletsQty, NEW.boxQty, NEW.driver,
         NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.supplierId, NEW.comment, 'CREATED');

CREATE TRIGGER after_donut_list_update AFTER UPDATE ON donut_lists
FOR EACH ROW
INSERT INTO donut_list_history
VALUES
  (NULL, NOW(), NEW. donutID, NEW.donutIDExternal, NEW.dataSourceID, NEW.donutNumber, NEW.creationDate,
         NEW.palletsQty, NEW.boxQty, NEW.driver,
         NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.supplierId, NEW.comment, 'UPDATED');

CREATE TRIGGER after_donut_list_delete AFTER DELETE ON donut_lists
FOR EACH ROW
INSERT INTO donut_list_history
VALUES
  (NULL, NOW(), OLD.donutID, OLD.donutIDExternal, OLD.dataSourceID, OLD.donutNumber, OLD.creationDate,
         OLD.palletsQty, OLD.boxQty, OLD.driver,
         OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.supplierID, OLD.comment, 'DELETED');



CREATE TABLE donuts_history(
  donutHistoryID    SERIAL,
  timeMark          TIMESTAMP    NOT NULL,
  donutID           SERIAL,
  creationDate      DATE         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  palletsQty        INTEGER      NULL,
  supplierID        INTEGER      NOT NULL,
  driver            VARCHAR(255) NULL,
  driverPhoneNumber VARCHAR(12)  NULL,
  licensePlate      VARCHAR(9)   NULL, -- государственный номер автомобиля
  comment           VARCHAR(255) NULL,
  transitPointID    INTEGER      NOT NULL, -- пункт доставки (куда привезут сначала) - перенести на уровень пончика
  PRIMARY KEY (donutHistoryID)
);


CREATE TABLE wants_history (
  wantHistoryID      SERIAL,
  autoTimeMark       TIMESTAMP      NOT NULL,
  wantID             SERIAL,
  wantNumber         VARCHAR(16)    NOT NULL,
  wantDate           DATE           NULL,
  supplierID         INTEGER        NOT NULL,
  destinationPointID INTEGER        NULL, -- пункт доставки (конечный)
  invoiceNumber      VARCHAR(255)   NULL,
  invoiceDate        DATE           NULL,
  deliveryDate       TIMESTAMP      NULL,
  boxQty             INTEGER        NULL,
  weight             INTEGER        NULL, -- масса в граммах
  volume             INTEGER        NULL, -- в кубических сантиметрах
  goodsCost          DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP, -- date and time when status was updated by any user
  lastModifiedBy     INTEGER        NULL, -- один из пользователей - это parser.
  wantStatusID       VARCHAR(32)    NOT NULL,
  commentForStatus   TEXT           NULL,
  donutID            INTEGER        NULL,

  PRIMARY KEY (wantHistoryID),
  FOREIGN KEY (wantStatusID) REFERENCES want_statuses (wantStatusID)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);