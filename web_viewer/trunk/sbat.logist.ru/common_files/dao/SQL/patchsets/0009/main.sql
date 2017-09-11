ALTER TABLE transmaster_transport_db.points ADD requests_count INT DEFAULT 0 NULL;

USE transmaster_transport_db;
CREATE EVENT countRequestForPoints
  ON SCHEDULE EVERY 1 DAY STARTS '2017-08-21 04:00:00'
DO
  UPDATE points p, (SELECT DISTINCT destinationPointID AS pID, COUNT(r.requestID) AS count  from transmaster_transport_db.points LEFT JOIN requests as r ON destinationPointID=points.pointID  WHERE x<>0.0 GROUP BY pointName) rcount
  SET p.requests_count = rcount.count WHERE p.pointID = rcount.pID;