CREATE TABLE test_ttdb.delivery_route_points
(
  delivery_route_point_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  routeId INT NOT NULL,
  pointId INT NOT NULL
);
ALTER TABLE test_ttdb.delivery_route_points ADD CONSTRAINT delivery_route_points_routeId_pointId_pk UNIQUE (routeId, pointId);


# Use this query to fill the table
INSERT INTO delivery_route_points (routeId, pointId) SELECT DISTINCTROW mat_view_big_select.routeID, mat_view_big_select.deliveryPointID FROM mat_view_big_select WHERE routeID IS NOT NULL AND deliveryPointID IS NOT NULL;