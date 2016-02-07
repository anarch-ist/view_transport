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

CREATE PROCEDURE examplePrStatement(id VARCHAR(255), _orderBy VARCHAR(255))
  BEGIN
    SET @s = CONCAT('SELECT * FROM points WHERE pointID <> ? ORDER BY ', _orderBy);
    PREPARE statement FROM @s;
    SET @_id = id;
    EXECUTE statement USING @_id;
    DEALLOCATE PREPARE statement;
  END;


DROP PROCEDURE selectData;

-- _search - array of strings
-- _orderby 'id'  <> - название колонки из файла main.js
-- _search - передача column_name1,search_string1;column_name1,search_string1;... если ничего нет то передавать пустую строку

CREATE PROCEDURE selectData(_userID INTEGER, _startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255),
                            _isDesc BOOLEAN, _search TEXT)
  BEGIN
    SET @mainPart =
    'SELECT
      requests.requestNumber,
      invoices.insiderRequestNumber,
      invoices.invoiceNumber,
      clients.INN,
      delivery_points.pointName     AS `deliveryPoint`,
      w_points.pointName            AS `warehousePoint`,
      users.lastName,
      invoices.invoiceStatusID,
      invoice_statuses.invoiceStatusRusName,
      invoices.boxQty,

      route_lists.driver,
      route_lists.licensePlate,
      route_lists.palletsQty,
      route_lists.routeListNumber,
      route_lists.routeListID,
      routes.directionName,

      last_visited_points.pointName AS `currentPoint`,
      next_route_points.pointName   AS `nextPoint`,
      \'arrival_Time\'

     FROM requests

      LEFT JOIN (invoices, clients, points AS delivery_points, points AS w_points, users)
        ON (
        invoices.requestID = requests.requestID AND
        invoices.warehousePointID = w_points.pointID AND
        requests.clientID = clients.clientID AND
        requests.destinationPointID = delivery_points.pointID AND
        requests.marketAgentUserID = users.userID
        )
      LEFT JOIN (invoice_statuses)
        ON (
          invoices.invoiceStatusID = invoice_statuses.invoiceStatusID  -- CHECK IT
        )
      -- because routeList in invoices table can be null, we use left join.
      LEFT JOIN (route_lists, routes)
        ON (
        invoices.routeListID = route_lists.routeListID AND
        route_lists.routeID = routes.routeID
        )
      LEFT JOIN (points AS last_visited_points)
        ON (
        invoices.lastVisitedUserPointID = last_visited_points.pointID
        )
      LEFT JOIN (route_points, points AS next_route_points)
        ON (
        route_lists.routeID = routes.routeID AND
        routes.routeID = route_points.routeID AND
        route_points.routePointID = getNextRoutePointID(routes.routeID, invoices.lastVisitedUserPointID) AND
        next_route_points.pointID = route_points.pointID
        ) ';


    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи

    SET @wherePart =
    'WHERE (
    (getRoleIDByUserID(?) = \'ADMIN\') OR
    (getRoleIDByUserID(?) = \'MARKET_AGENT\' AND requests.marketAgentUserID = ?) OR
    (getPointIDByUserID(?) = w_points.pointID) OR
    (getPointIDByUserID(?) IN (SELECT pointID FROM route_points WHERE route_lists.routeID = routeID))
    )';
    SET @havingPart = CONCAT(' HAVING ', generateHaving(_search));

    IF _orderby <> ''
    THEN
      IF _isDesc
      THEN
        SET @orderByPart = CONCAT(' ORDER BY ', _orderby, ' DESC');
      ELSE
        SET @orderByPart = CONCAT(' ORDER BY ', _orderby);
      END IF;
    ELSE
      SET @orderByPart = '';
    END IF;

    SET @limitPart = ' LIMIT ?, ? ';
    SET @sqlString = CONCAT(@mainPart, @wherePart, @havingPart, @orderByPart, @limitPart);

    PREPARE statement FROM @sqlString;

    SET @_userID = _userID;
    SET @_startEntry = _startEntry;
    SET @_length = _length;

    EXECUTE statement
    USING @_userID, @_userID, @_userID, @_userID, @_userID, @_startEntry, @_length;
    DEALLOCATE PREPARE statement;
  END;




CREATE TABLE clients (
  clientIDExternal  VARCHAR(255) NOT NULL,
  dataSourceID      VARCHAR(32)  NOT NULL,
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE(clientIDExternal, dataSourceID)
);































