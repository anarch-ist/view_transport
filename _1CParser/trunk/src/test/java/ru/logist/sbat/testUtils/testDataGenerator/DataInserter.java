package ru.logist.sbat.testUtils.testDataGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.DBUtils;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonToBean.beans.PointData;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DataInserter {
    private static final int TOTAL_POINTS = 30_000; // 30_000
    private static final int TOTAL_ROUTES = 50_000; // 50_000
    private static final int TOTAL_CLIENTS = 40_000; // 40_000
    private static final int TOTAL_USERS = 60_000; // 60_000
    private static final int TOTAL_ROUTE_LISTS = 10_000; // 10_000
    private static final int TOTAL_REQUESTS = 100_000; // 100_000


    private static final Set<String> DAYS_OF_WEEK = new HashSet<>(Arrays.asList(new String[]{
            "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
    }));

    public static final String ADMIN = "ADMIN";
    public static final String W_DISPATCHER = "W_DISPATCHER";
    public static final String DISPATCHER = "DISPATCHER";
    public static final String CLIENT_MANAGER = "CLIENT_MANAGER";
    public static final String MARKET_AGENT = "MARKET_AGENT";
    private static final Set<String> USER_ROLES = new HashSet<>(Arrays.asList(new String[]{
            ADMIN, W_DISPATCHER, DISPATCHER, CLIENT_MANAGER, MARKET_AGENT
    }));
    private static final Set<String> ROUTE_LIST_STATUSES = new HashSet<>(Arrays.asList(new String[]{
            "APPROVED", "CREATED"
    }));

    private List<Long> generatedClientsId;
    private List<Long> generatedPointsId;
    private List<Long> generatedRoutesId;
    private List<Long> generatedUsersId;
    private List<Long> generatedRouteListsId;
    private Connection connection;


    private List<Long> getGeneratedKeys(PreparedStatement statement) throws SQLException {
        List<Long> generatedRoutesId = new ArrayList<>();
        ResultSet rs = statement.getGeneratedKeys();
        while (rs != null && rs.next()) {
            generatedRoutesId.add(rs.getLong(1));
        }
        return generatedRoutesId;
    }

    private List<Long> getKeysForMarketAgentUser() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT userID FROM users WHERE userRoleID = ?");
        statement.setString(1, MARKET_AGENT);
        List<Long> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs != null && rs.next()) {
            result.add(rs.getLong(1));
        }
        statement.close();
        return result;
    }

    private List<Long> getKeysForWarehousePoints() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT pointID FROM points WHERE pointTypeID = ?");
        statement.setString(1, "WAREHOUSE");
        List<Long> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs != null && rs.next()) {
            result.add(rs.getLong(1));
        }
        statement.close();
        return result;
    }


    private Integer getParserUserId() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT userID FROM users WHERE login = 'parser'");
        if (resultSet!=null && resultSet.next()) {
            return resultSet.getInt(1);
        }
        if (resultSet != null) {
            resultSet.close();
        }
        statement.close();
        throw new NullPointerException();
    }

    public DataInserter(Connection connection) {
        this.connection = connection;
    }



    public void generatePoints() {
        PreparedStatement pointsStatement = null;
        try {
            pointsStatement = this.connection.prepareStatement(
                    "INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, email, pointTypeID, responsiblePersonId)\n" +
                            "VALUE (?, ?, ?, ?, ?, ?, ?);\n"
            , Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < TOTAL_POINTS; i++) {
                pointsStatement.setString(1, RandomStringUtils.randomAlphabetic(10) + "IDExternal");
                pointsStatement.setString(2, DBManager.LOGIST_1C);
                pointsStatement.setString(3, RandomStringUtils.randomAlphabetic(10) + "PointName");
                pointsStatement.setString(4, RandomStringUtils.randomAlphabetic(10) + "PointAddress");
                pointsStatement.setString(5, RandomStringUtils.randomAlphabetic(10) + "PointEmails");
                pointsStatement.setString(6, Util.getRandomElementFromSet(PointData.POINT_TYPES));
                pointsStatement.setString(7, RandomStringUtils.randomAlphabetic(10) + "RespPersonId");
                pointsStatement.addBatch();
            }
            pointsStatement.executeBatch();
            generatedPointsId = getGeneratedKeys(pointsStatement);

            this.connection.commit();
            System.out.println("points generated");
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(pointsStatement);
        }
    }


    public void generateRoutes() {

        PreparedStatement routesStatement = null;

        try {

            routesStatement = this.connection.prepareStatement(
                    "INSERT INTO routes (directionIDExternal, dataSourceID, routeName, firstPointArrivalTime, daysOfWeek) VALUE (?, ?, ?, ?, ?);"
            , Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < TOTAL_ROUTES; i++) {
                routesStatement.setString(1,  RandomStringUtils.randomAlphabetic(10) + "IDExternal");
                routesStatement.setString(2, DBManager.LOGIST_1C);
                routesStatement.setString(3,  RandomStringUtils.randomAlphabetic(10) + "DirectionName");
                routesStatement.setTime(4,  new Time(RandomUtils.nextLong(0, 24*60*60*1000)));
                routesStatement.setString(5,  Util.getRandomElementFromSet(DAYS_OF_WEEK));
                routesStatement.addBatch();
            }
            routesStatement.executeBatch();
            generatedRoutesId = getGeneratedKeys(routesStatement);

            // идти циклом по всем маршрутам
            PreparedStatement routePointsStatement = this.connection.prepareStatement("" +
                    "INSERT INTO route_points (sortOrder, timeForLoadingOperations, pointID, routeID) " +
                    "VALUES (?, ?, ?, ?), (?, ?, ?, ?)");
            for (Long aGeneratedRoutesId : generatedRoutesId) {
                List<Long> randomPointsId = Util.getRandomElementsFromList(generatedPointsId, 2);

                routePointsStatement.setInt(1, 1);
                routePointsStatement.setInt(2, RandomUtils.nextInt(60, 460));
                routePointsStatement.setInt(3, randomPointsId.get(0).intValue());
                routePointsStatement.setInt(4, aGeneratedRoutesId.intValue());

                routePointsStatement.setInt(5, 2);
                routePointsStatement.setInt(6, RandomUtils.nextInt(60, 460));
                routePointsStatement.setInt(7, randomPointsId.get(1).intValue());
                routePointsStatement.setInt(8, aGeneratedRoutesId.intValue());
                routePointsStatement.addBatch();
            }
            routePointsStatement.executeBatch();
            this.connection.commit();
            System.out.println("routes and routePoints generated");

        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(routesStatement);
        }

    }

    public void generateClients() {
        PreparedStatement clientsStatement = null;
        try {
            clientsStatement = this.connection.prepareStatement(
                    "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                            "  VALUE (?, ?, ?, ?);"
            , Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < TOTAL_CLIENTS; i++) {
                clientsStatement.setString(1, RandomStringUtils.randomAlphabetic(10) + "clientIDExternal");
                clientsStatement.setString(2, DBManager.LOGIST_1C);
                clientsStatement.setString(3, RandomStringUtils.randomAlphabetic(10) + "clientName");
                clientsStatement.setString(4, RandomStringUtils.randomAlphabetic(10) + "INN");
                clientsStatement.addBatch();
            }
            clientsStatement.executeBatch();
            generatedClientsId = getGeneratedKeys(clientsStatement);
            this.connection.commit();
            System.out.println("clients generated");
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(clientsStatement);
        }
    }

    public void generateUsers() {
        PreparedStatement usersStatement = null;
        try {
            usersStatement = this.connection.prepareStatement(
                    "INSERT INTO users (userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)\n" +
                            "  VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n"
            , Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < TOTAL_USERS; i++) {
                usersStatement.setString(1, RandomStringUtils.randomAlphabetic(10));
                usersStatement.setString(2, DBManager.LOGIST_1C);
                String salt = Utils.generateSalt();
                String userLogin = RandomStringUtils.randomAlphabetic(6);
                String userPassword = userLogin + "pass";

                usersStatement.setString(3, userLogin);
                usersStatement.setString(4, salt);
                usersStatement.setString(5, Utils.generatePassAndSalt(userPassword, salt));
                String userRole = Util.getRandomElementFromSet(USER_ROLES);
                usersStatement.setString(6, userRole);
                usersStatement.setString(7, RandomStringUtils.randomAlphabetic(10));
                usersStatement.setString(8, RandomStringUtils.randomAlphabetic(10));
                usersStatement.setString(9, RandomStringUtils.randomAlphabetic(10));
                usersStatement.setString(10, RandomStringUtils.randomAlphabetic(10)); // position
                if (userRole.equals(MARKET_AGENT) || userRole.equals(ADMIN)) {
                    usersStatement.setNull(11, Types.INTEGER); // pointID
                    usersStatement.setNull(12, Types.INTEGER); // clientID
                } else if (userRole.equals(CLIENT_MANAGER)) {
                    usersStatement.setNull(11, Types.INTEGER); // pointID
                    usersStatement.setInt(12, Util.getRandomElementFromList(generatedClientsId).intValue()); // clientID
                } else if (userRole.equals(W_DISPATCHER) || userRole.equals(DISPATCHER)) {
                    usersStatement.setInt(11, Util.getRandomElementFromList(generatedPointsId).intValue());
                    usersStatement.setNull(12, Types.INTEGER);
                }
                usersStatement.addBatch();
                System.out.println("LOGIN = "+userLogin);
                System.out.println("PASS  = "+userPassword);
                System.out.println("ROLE  = "+userRole);
                System.out.println("------------------");
            }
            usersStatement.executeBatch();
            generatedUsersId = getGeneratedKeys(usersStatement);

            this.connection.commit();
            System.out.println("users generated");
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(usersStatement);
        }
    }



    public void generateRouteLists() {
        PreparedStatement routeListsStatement = null;
        try {
            routeListsStatement = this.connection.prepareStatement(
                    "INSERT INTO route_lists\n" +
                            "  VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n"
                    , Statement.RETURN_GENERATED_KEYS);
            RandomDate randomDate = new RandomDate(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 28));
            for (int i = 0; i < TOTAL_ROUTE_LISTS; i++) {
                //common values
                routeListsStatement.setString(1, RandomStringUtils.randomAlphabetic(10));
                routeListsStatement.setString(2, DBManager.LOGIST_1C);
                routeListsStatement.setString(3, RandomStringUtils.randomAlphabetic(10));
                LocalDate creationDate = randomDate.nextDate();
                LocalDate departureDate = creationDate.plus(3, ChronoUnit.DAYS);
                routeListsStatement.setDate(4, Date.valueOf(creationDate)); // creationDate
                routeListsStatement.setDate(5, Date.valueOf(departureDate)); // departureDate
                routeListsStatement.setNull(6, Types.INTEGER); // palletsQty
                routeListsStatement.setString(7, RandomStringUtils.randomAlphabetic(10));
                routeListsStatement.setNull(8, Types.INTEGER);
                routeListsStatement.setString(9, null); // driverPhoneNumber
                routeListsStatement.setString(10, null); // license plate
                routeListsStatement.setString(11, Util.getRandomElementFromSet(ROUTE_LIST_STATUSES));
                routeListsStatement.setInt(12, Util.getRandomElementFromList(generatedRoutesId).intValue());
                routeListsStatement.addBatch();
            }
            routeListsStatement.executeBatch();
            generatedRouteListsId = getGeneratedKeys(routeListsStatement);
            this.connection.commit();
            System.out.println("route lists generated");
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(routeListsStatement);
        }
    }


    public void generateRequests() {

        PreparedStatement requestsStatement = null;
        try {
            Integer parserUserId = getParserUserId();

            List<Long> keysForMarketAgentUser = getKeysForMarketAgentUser();
            List<Long> keysForWarehousePoints = getKeysForWarehousePoints();

            RandomDate randomDate = new RandomDate(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 28));
            requestsStatement = this.connection.prepareStatement(
                    "INSERT INTO requests (requestIDExternal, dataSourceID, requestNumber, requestDate, clientID,\n" +
                            "                      destinationPointID, marketAgentUserID, invoiceNumber, invoiceDate,\n" +
                            "                      documentNumber, documentDate, firma, storage, contactName, contactPhone,\n" +
                            "                      deliveryOption, deliveryDate, boxQty, weight, volume, goodsCost,\n" +
                            "                      lastStatusUpdated, lastModifiedBy, requestStatusID, commentForStatus,\n" +
                            "                      warehousePointID, routeListID, lastVisitedRoutePointID)\n" +
                            "    VALUES\n" +
                            "      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
                    );

            for (int i = 0; i < TOTAL_REQUESTS; i++) {
                requestsStatement.setString(1, RandomStringUtils.randomAlphabetic(10));
                requestsStatement.setString(2, DBManager.LOGIST_1C);
                requestsStatement.setString(3, RandomStringUtils.randomAlphabetic(10));
                requestsStatement.setDate(4, randomDate.nextSqlDate());
                requestsStatement.setInt(5, Util.getRandomElementFromList(generatedClientsId).intValue());
                requestsStatement.setInt(6, Util.getRandomElementFromList(generatedPointsId).intValue());
                requestsStatement.setInt(7, Util.getRandomElementFromList(keysForMarketAgentUser).intValue());
                requestsStatement.setString(8, RandomStringUtils.randomAlphabetic(10));
                requestsStatement.setDate(9, randomDate.nextSqlDate());
                requestsStatement.setString(10, RandomStringUtils.randomAlphabetic(10)); // documentNumber
                requestsStatement.setDate(11, randomDate.nextSqlDate());
                requestsStatement.setString(12, "firma");
                requestsStatement.setString(13, "storage1");
                requestsStatement.setString(14, "con_name");
                requestsStatement.setString(15, "con_phone");
                requestsStatement.setString(16, "del_opt");
                requestsStatement.setDate(17, randomDate.nextSqlDate()); // delivery date
                requestsStatement.setInt(18, RandomUtils.nextInt(1, 9)); // box qty
                requestsStatement.setInt(19, RandomUtils.nextInt(10, 100)); // weight
                requestsStatement.setInt(20, RandomUtils.nextInt(10, 100)); // volume
                requestsStatement.setInt(21, RandomUtils.nextInt(10, 100)); // goods cost
                requestsStatement.setNull(22, Types.DATE); // last status updated
                requestsStatement.setInt(23, parserUserId);
                requestsStatement.setString(24, "CREATED");
                requestsStatement.setString(25, "comment");
                requestsStatement.setInt(26, Util.getRandomElementFromList(keysForWarehousePoints).intValue()); //warehousePointID
                requestsStatement.setInt(27, Util.getRandomElementFromList(generatedRouteListsId).intValue()); //routeLists
                requestsStatement.setNull(28, Types.INTEGER); //routeLists
                requestsStatement.addBatch();
            }
            requestsStatement.executeBatch();
            this.connection.commit();
            System.out.println("requests generated");
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.rollbackQuietly(this.connection);
        } finally {
            DBUtils.closeStatementQuietly(requestsStatement);
        }
    }

}
