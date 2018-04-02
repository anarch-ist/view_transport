ALTER TABLE transmaster_transport_db.routes ADD type TINYINT(1) DEFAULT 0 NULL;
ALTER TABLE transmaster_transport_db.routes ADD creationDate TIMESTAMP DEFAULT NOW() NULL;
