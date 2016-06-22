SET TIMEZONE = 'UTC';
TRUNCATE doc_periods CASCADE;
INSERT INTO doc_periods (docid, periodbegin, periodend)
VALUES (1, '2016-10-19 5:00:00 Z', '2016-10-19 6:30:00 Z'); -- id == 1
INSERT INTO doc_periods (docid, periodbegin, periodend)
VALUES (1, '2016-10-19 7:00:00 Z', '2016-10-19 7:30:00 Z'); -- id == 1
INSERT INTO doc_periods (docid, periodbegin, periodend)
VALUES (1, '2016-10-19 7:30:00 Z', '2016-10-19 8:30:00 Z'); -- id == 1
INSERT INTO doc_periods (docid, periodbegin, periodend)
VALUES (1, '2016-10-19 10:00:00 Z', '2016-10-19 10:30:00 Z'); -- id == 1

BEGIN TRANSACTION;
SET LOCAL audit.userId:=1;
INSERT INTO doc_periods (docid, periodbegin, periodend)
VALUES (1, '2016-10-19 11:00:00 Z', '2016-10-19 11:30:00 Z'); -- id == 1
COMMIT;
