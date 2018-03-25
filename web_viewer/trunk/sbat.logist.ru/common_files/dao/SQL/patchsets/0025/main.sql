create table freight
(
  freight_id int(10) unsigned auto_increment
    primary key,
  transport_company_id int null,
  driver_id int null,
  vehicle_id int null,
  vehicle_2_id int null,
  vehicle_3_id int null,
  route_id int null,
  distance int null,
  continuance int null,
  fuel_consumption int null,
  stall_hours int null,
  unique_addresses int null,
  total_box_amount int null,
  speed_readings_end int null,
  speed_readings_begin int null
);

ALTER TABLE route_lists ADD freight_id INT NULL;

ALTER TABLE freight ADD status_id VARCHAR(64) NULL;
ALTER TABLE freight ADD freight_number VARCHAR(64) NULL;
ALTER TABLE freight
  ADD CONSTRAINT freight_request_statuses_requestStatusID_fk
FOREIGN KEY (status_id) REFERENCES request_statuses (requestStatusID);

GRANT ALL ON TABLE freight TO 'andy'@'localhost';