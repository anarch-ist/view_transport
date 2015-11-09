# list with only manager Users
CREATE VIEW manager_users (managerUserID) AS
  SELECT users.userID
  FROM users
  WHERE users.userRoleID IN (SELECT user_roles.userRoleID
                             FROM user_roles
                             WHERE user_roles.userRoleName = 'MANAGER');


CREATE FUNCTION getInvoiceStatusIDByName(statusName VARCHAR(255))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT invoiceStatusID FROM invoice_statuses WHERE invoiceStatusName=statusName);
    RETURN result;
  END;

CREATE FUNCTION get_route_list_status_id_by_name(statusName VARCHAR(255))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT routeListStatusID
                  FROM route_list_statuses
                  WHERE routeListStatusID = statusName);
    RETURN result;
  END;



# helper table, contains only manager users. Every time when add or update new user this table sync with users
CREATE TABLE manager_users (
  managerUserID INTEGER,
  PRIMARY KEY (managerUserID),
  FOREIGN KEY (managerUserID) REFERENCES users (userID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TRIGGER before_users_insert
BEFORE INSERT ON users FOR EACH ROW
  BEGIN
    IF (is_manager(NEW.userRoleID))
    THEN
      INSERT INTO manager_users VALUES (NEW.userRoleID);
    END IF;
  END;

CREATE TRIGGER before_users_delete
BEFORE DELETE ON users FOR EACH ROW
  BEGIN
    IF (is_manager(old.userRoleID))
    THEN
      DELETE FROM manager_users
      WHERE manager_users.managerUserID = old.userID;
    END IF;
  END;

CREATE TRIGGER before_users_update
BEFORE UPDATE ON users FOR EACH ROW
  BEGIN

    IF (NEW.userID != old.userID)
    THEN
      UPDATE manager_users
      SET managerUserID = NEW.userID
      WHERE old.userID = manager_users.managerUserID;
    END IF;

    IF (NEW.userRoleID != old.userRoleID)
    THEN
      BEGIN
        IF (is_manager(NEW.userRoleID))
        THEN
          INSERT INTO manager_users VALUES (NEW.userRoleID);
        ELSE IF (is_manager(old.userRoleID))
        THEN
          DELETE FROM manager_users
          WHERE manager_users.managerUserID = old.userID;
        END IF;
        END IF;
      END;
    END IF;
  END;




# this table content is a subset of points table
CREATE TABLE warehouse_points (
  warehousePointID INTEGER,
  PRIMARY KEY (warehousePointID),
  FOREIGN KEY (warehousePointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TRIGGER before_points_insert
BEFORE INSERT ON points FOR EACH ROW
  BEGIN
    IF (is_warehouse(NEW.pointTypeID))
    THEN
      INSERT INTO warehouse_points VALUES (NEW.pointTypeID);
    END IF;
  END;

CREATE TRIGGER before_points_delete
BEFORE DELETE ON points FOR EACH ROW
  BEGIN
    IF (is_warehouse(OLD.pointTypeID))
    THEN
      DELETE FROM warehouse_points
      WHERE warehouse_points.warehousePointID = OLD.pointTypeID;
    END IF;
  END;