USE transmaster_transport_db;
CREATE EVENT setTransitToDelivered
  ON SCHEDULE EVERY 1 DAY STARTS '2017-06-23 01:00:00'
DO
  UPDATE transmaster_transport_db.requests SET requestStatusID = 'DELIVERED' WHERE requestStatusID='DEPARTURE';