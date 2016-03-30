SHOW VARIABLES;
SHOW VARIABLES LIKE '%innodb_log_waits%';



CREATE PROCEDURE examplePrStatement(id VARCHAR(255), _orderBy VARCHAR(255))
  BEGIN
    SET @s = CONCAT('SELECT * FROM points WHERE pointID <> ? ORDER BY ', _orderBy);
    PREPARE statement FROM @s;
    SET @_id = id;
    EXECUTE statement USING @_id;
    DEALLOCATE PREPARE statement;
  END;


DROP PROCEDURE selectData;

-- ----------------------------

USE transmaster_transport_db;

SELECT * FROM big_select LIMIT 10000; -- должен работать мгновенно

CALL refreshMaterializedView();

SELECT COUNT(*) FROM big_select_materialized; -- 14ms

CALL selectData(1, 0, 10000, '', TRUE, 'requestIDExternal,12;');


CREATE FULLTEXT INDEX ind1 ON big_select_materialized (requestIDExternal(10));
CREATE FULLTEXT INDEX ind2 ON big_select_materialized (requestNumber(10));
CREATE INDEX ind3 ON big_select_materialized (requestDate);
CREATE FULLTEXT INDEX ind4 ON big_select_materialized (invoiceNumber(10));
CREATE INDEX ind5 ON big_select_materialized (invoiceDate);
CREATE FULLTEXT INDEX ind6 ON big_select_materialized (documentNumber(10));

CALL selectDataTest(1, 0, 10000, 'derfreg', TRUE, '12');
DROP PROCEDURE selectDataTest;
CREATE PROCEDURE selectDataTest(_userID INTEGER, _startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255),
                                _isDesc BOOLEAN, _reqIDExtSearchString VARCHAR(255))
  BEGIN
    SET @userRoleID = getRoleIDByUserID(_userID);

    SET @isAdmin = FALSE;
    SET @isMarketAgent = FALSE;
    SET @isClientManager = FALSE;
    SET @isDispatcherOrWDispatcher = FALSE;

    IF (@userRoleID = 'ADMIN') THEN
      SET @isAdmin = TRUE;
    ELSEIF (@userRoleID = 'MARKET_AGENT') THEN
      SET @isMarketAgent = TRUE;
    ELSEIF (@userRoleID = 'CLIENT_MANAGER') THEN
      SET @isClientManager = TRUE;
    ELSEIF (@userRoleID = 'DISPATCHER' OR @userRoleID = 'W_DISPATCHER') THEN
      SET @isDispatcherOrWDispatcher = TRUE;
    END IF;

    IF (@isClientManager) THEN
      SET @clientID = getClientIDByUserID(_userID);
    END IF;

    IF (@isDispatcherOrWDispatcher) THEN
      SET @userPointID = getPointIDByUserID(_userID);
    END IF;


    -- берем из таблицы requests нашего пользователя, оттуда вытаскиваем routeList, оттуда берем маршрут и оттуда все пункты маршрута

    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут заявки проходит через пользователя, то показываем ему эти записи
    SELECT SQL_CALC_FOUND_ROWS
      requestIDExternal,
      requestNumber,
      requestDate,
      invoiceNumber,
      invoiceDate,
      documentNumber,
      documentDate,
      firma,
      storage,
      commentForStatus,
      boxQty,
      requestStatusRusName,
      clientIDExternal,
      INN,
      clientName,
      userName,
      deliveryPointName,
      warehousePointName,
      currentPointName,
      nextPointName,
      routeName,
      driverId,
      licensePlate,
      palletsQty,
      routeListNumber,
      arrivalTime,
      requestStatusID,
      routeListID
    FROM big_select_materialized
    WHERE (
      (@isAdmin) OR
      (@isMarketAgent AND big_select_materialized.marketAgentUserID = _userID) OR
      (@isClientManager AND big_select_materialized.clientID = @clientID) OR
      (@isDispatcherOrWDispatcher AND big_select_materialized.warehousePointID = @userPointID) OR
      -- TODO fix it
      (@isDispatcherOrWDispatcher AND big_select_materialized.routeID IN (SELECT routes.routeID
                                                                          FROM routes
                                                                            INNER JOIN (route_points, points)
                                                                              ON (routes.routeID = route_points.routeID
                                                                                  AND
                                                                                  route_points.pointID = points.pointID)
                                                                          WHERE @userPointID = points.pointID))

    )
    HAVING requestIDExternal LIKE CONCAT('%', _reqIDExtSearchString, '%')
    ORDER BY requestIDExternal
    LIMIT _startEntry, _length
    ;

    -- filtered
    SELECT FOUND_ROWS() as `totalFiltered`;

  END;

SELECT * FROM big_select_materialized WHERE firma LIMIT 10000;
