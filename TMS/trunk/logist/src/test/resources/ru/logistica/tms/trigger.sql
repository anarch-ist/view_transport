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
-- free periods : (-8, 5:00), (6:30-7:00), (8:30-10:00), (10:30, +8)
-- INSERT INTO doc_periods (docid, periodbegin, periodend) VALUES (1, '2016-10-19 10:00:00 Z', '2016-10-19 10:30:00 Z'); -- id == 1


-- CREATE FUNCTION emp_stamp() RETURNS trigger AS $emp_stamp$
-- BEGIN
--   -- Check that empname and salary are given
--   IF NEW.empname IS NULL THEN
--     RAISE EXCEPTION 'empname cannot be null';
--   END IF;
--   IF NEW.salary IS NULL THEN
--     RAISE EXCEPTION '% cannot have null salary', NEW.empname;
--   END IF;
--
--   -- Who works for us when she must pay for it?
--   IF NEW.salary < 0 THEN
--     RAISE EXCEPTION '% cannot have a negative salary', NEW.empname;
--   END IF;
--
--   -- Remember who changed the payroll when
--   NEW.last_date := current_timestamp;
--   NEW.last_user := current_user;
--   RETURN NEW;
-- END;
-- $emp_stamp$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER emp_stamp BEFORE INSERT OR UPDATE ON emp
-- FOR EACH ROW EXECUTE PROCEDURE emp_stamp();




-- SELECT has_empty_space(1, TIMESTAMPTZ '2016-10-19 8:30:00 Z', TIMESTAMPTZ '2016-10-19 10:00:00 Z');