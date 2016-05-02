package ru.logist.sbat.functional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logist.sbat.fileExecutor.CommandException;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.jsonToBean.jsonReader.JsonPException;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;
import ru.logist.sbat.testUtils.TestHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class FunctionalTests {
    public static DBManager dbManager;
    private static final Logger logger = LogManager.getLogger();
    private static Connection connection;
    private static TestHelper testHelper;
    private static void loadFile(String fileName) throws URISyntaxException, IOException, JsonPException, ParseException, ValidatorException, CommandException, DBCohesionException, SQLException {
        Path path = Paths.get(FunctionalTests.class.getResource(fileName).toURI());
        testHelper.loadFile(path);
    }

    @BeforeClass
    public static void setUp() throws Exception{
        testHelper = new TestHelper();
        connection = testHelper.prepareDatabase();
        loadFile("EKA_fixed.zip");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (dbManager != null)
            dbManager.close();
    }

    @Test
    public void createRequestsTest() throws Exception {
        // create two requests

        loadFile("1.zip");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM requests WHERE requestIDExternal IN('5B4BONOV', '5B4BQNOV')");
        if (resultSet.next())
            Assert.assertEquals(resultSet.getInt(1), 2);
        else
            throw new AssertionError();
        statement.close();
    }

    @Test(dependsOnMethods = {"createRequestsTest"})
    public void updateRequestsTest() throws Exception {
        loadFile("2.zip");
        // for 5B4BONOV : set of new values
        // "requestNumber": "INT-102647"
        // "requestDate": "20160406"
        // "invoiceNumber": "INT-102647"
        // "firma": "тест2"
        // "clientId": "143782"
        // "traderId": "10000157"
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT requestNumber, requestDate, invoiceNumber, firma, clients.clientIDExternal, users.userIDExternal FROM requests " +
                        "INNER JOIN clients ON(requests.clientID = clients.clientID)" +
                        "INNER JOIN users ON(requests.marketAgentUserID = users.userID)" +
                        " WHERE requestIDExternal = '5B4BONOV'"
        );
        if (resultSet.next()) {
            Assert.assertEquals(resultSet.getString("requestNumber"), "INT-102647");
            Assert.assertEquals(resultSet.getDate("requestDate"), Date.valueOf("2016-04-06"));
            Assert.assertEquals(resultSet.getString("invoiceNumber"), "INT-102647");
            Assert.assertEquals(resultSet.getString("firma"), "тест2");
            Assert.assertEquals(resultSet.getString("clientIDExternal"), "143782");
            Assert.assertEquals(resultSet.getString("userIDExternal"), "10000157");
        } else
            throw new AssertionError();
        statement.close();
    }

    @Test(dependsOnMethods = {"updateRequestsTest"})
    public void updateStatusInRequestsTest() throws Exception {
        loadFile("3.zip");
        // for 5B4BONOV : update status
        // "num_boxes": 1,
        // "status": "CHECK_PASSED",
        // "timeOutStatus": "05.04.16,09:17:56",
        // "Comment": ""
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT boxQty, requestStatusID, lastStatusUpdated, commentForStatus FROM requests " +
                        " WHERE requestIDExternal = '5B4BONOV'"
        );
        if (resultSet.next()) {
            Assert.assertEquals(resultSet.getInt("boxQty"), 1);
            Assert.assertEquals(resultSet.getString("requestStatusID"), "CHECK_PASSED");
            Assert.assertEquals(resultSet.getTimestamp("lastStatusUpdated"), Timestamp.valueOf("2016-04-05 09:17:56"));
            Assert.assertEquals(resultSet.getString("commentForStatus"), "");
        } else
            throw new AssertionError();
        statement.close();
    }

    @Test(dependsOnMethods = {"updateStatusInRequestsTest"})
    public void updateStatusInRequests2Test() throws Exception {
        loadFile("4.zip");
        // for 5B4BQNOV : update status
        // "requestId": "5B4BQNOV",
        // "num_boxes": 4,
        // "status": "COLLECTING",
        // "timeOutStatus": "05.04.16,12:35:57",
        // "Comment": "ТЕСТТЕСТТЕСТ!"
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT boxQty, requestStatusID, lastStatusUpdated, commentForStatus FROM requests " +
                        " WHERE requestIDExternal = '5B4BQNOV'"
        );
        if (resultSet.next()) {
            Assert.assertEquals(resultSet.getInt("boxQty"), 4);
            Assert.assertEquals(resultSet.getString("requestStatusID"), "COLLECTING");
            Assert.assertEquals(resultSet.getTimestamp("lastStatusUpdated"), Timestamp.valueOf("2016-04-05 12:35:57"));
            Assert.assertEquals(resultSet.getString("commentForStatus"), "ТЕСТТЕСТТЕСТ!");
        } else
            throw new AssertionError();
        statement.close();
    }

    @Test(dependsOnMethods = {"updateStatusInRequests2Test"})
    public void createRouteListTest() throws Exception {
        loadFile("5.zip");
//        "updateRouteLists": [
//        {
//            "routerSheetId": "5BSWJNOV",
//                "routerSheetNumber": "Ек-4000215",
//                "routerSheetDate": "20160408",
//                "departureDate": "",
//                "forwarderId": "",
//                "driverId": "",
//                "pointDepartureId": "44",
//                "pointArrivalId": "99",
//                "directId": "NULL",
//                "status": "CREATED",
//                "invoices": [
//            "5B4BONOV", "5B4BQNOV"]
//        }
//        ]

        // this is a trunk route so new route should be generated
        Statement routesStatement = connection.createStatement();
        ResultSet routesResultSet = routesStatement.executeQuery(
                "SELECT routeName FROM routes " +
                        " WHERE directionIDExternal = '44MAG99';"
        );
        if (routesResultSet.next()) {
            Assert.assertEquals(routesResultSet.getString("routeName"), "Новосибирск-Ошибка");
        }
        else
            throw new AssertionError();
        routesStatement.close();

        // check that new route list was created
        Statement routeListsStatement = connection.createStatement();
        ResultSet routeListResultSet = routeListsStatement.executeQuery(
                "SELECT routeListIDExternal, routeListNumber, creationDate, departureDate, forwarderId, driverID, status FROM route_lists " +
                        " WHERE routeListIDExternal = '5BSWJNOV';"
        );
        if (routeListResultSet.next()) {
            Assert.assertEquals(routeListResultSet.getString("routeListNumber"), "Ек-4000215");
            Assert.assertEquals(routeListResultSet.getDate("departureDate"), null);
            Assert.assertEquals(routeListResultSet.getDate("creationDate"), Date.valueOf("2016-04-08"));
            Assert.assertEquals(routeListResultSet.getString("status"), "CREATED");
            Assert.assertEquals(routeListResultSet.getInt("driverID"), 0); // 0 if driverId is null
        } else
            throw new AssertionError();
        routeListsStatement.close();

        // check that requests was assigned with this route list
        Statement requestsStatement = connection.createStatement();
        ResultSet requestsResultSet = requestsStatement.executeQuery(
                "SELECT COUNT(routeListIDExternal) FROM requests INNER JOIN route_lists ON requests.routeListID = route_lists.routeListID" +
                        " WHERE requests.requestIDExternal IN('5B4BONOV', '5B4BQNOV');"
        );

        if (requestsResultSet.next()) {
            Assert.assertEquals(requestsResultSet.getInt(1), 2);
        } else
            throw new AssertionError();
        requestsStatement.close();
    }

    @Test(dependsOnMethods = {"createRouteListTest"})
    public void deleteRequestFromRouteListTest() throws Exception {
        loadFile("6.zip");
        // only one request instead of two
        //        "updateRouteLists": [
        //        {
        //            "routerSheetId": "5BSWJNOV",
        //            "status": "CREATED",
        //            "invoices": ["5B4BONOV"]
        //        }]
        Statement requestsStatement = connection.createStatement();
        ResultSet requestsResultSet = requestsStatement.executeQuery(
                "SELECT routeListID FROM requests WHERE requests.requestIDExternal = '5B4BQNOV';"
        );

        if (requestsResultSet.next()) {
            Assert.assertEquals(requestsResultSet.getInt("routeListID"), 0);
        } else
            throw new AssertionError();
        requestsStatement.close();
    }

    @Test(dependsOnMethods = {"deleteRequestFromRouteListTest"})
    public void updateRouteListTest() throws Exception {
        loadFile("7.zip");
        // two requests again
        //        "updateRouteLists": [
        //        {
        //            "routerSheetId": "5BSWJNOV",
        //            "status": "CREATED",
        //            "invoices": ["5B4BONOV","5B4BQNOV"]
        //        }]
        Statement requestsStatement = connection.createStatement();
        ResultSet requestsResultSet = requestsStatement.executeQuery(
                "SELECT COUNT(routeListIDExternal) FROM requests INNER JOIN route_lists ON requests.routeListID = route_lists.routeListID" +
                        " WHERE requests.requestIDExternal IN('5B4BONOV', '5B4BQNOV');"
        );

        if (requestsResultSet.next()) {
            Assert.assertEquals(requestsResultSet.getInt(1), 2);
        } else
            throw new AssertionError();
        requestsStatement.close();
    }

    @Test(dependsOnMethods = {"updateRouteListTest"})
    public void setDriverInRouteListTest() throws Exception {
        loadFile("8.zip");
        // assign driverId
        //        "updateRouteLists": [
        //        {
        //            "routerSheetId": "5BSWJNOV",
        //            "driverId": "10000408",
        //            "invoices": ["5B4BONOV","5B4BQNOV"]
        //        }]
        Statement routeListsStatement = connection.createStatement();
        ResultSet resultSet = routeListsStatement.executeQuery(
                "SELECT users.userIDExternal FROM route_lists INNER JOIN users ON route_lists.driverID = users.userID" +
                        " WHERE routeListIDExternal = '5BSWJNOV';"
        );
        if (resultSet.next()) {
            Assert.assertEquals(resultSet.getString("userIDExternal"), "10000408");
        } else
            throw new AssertionError();
        routeListsStatement.close();
    }

    @Test(dependsOnMethods = {"setDriverInRouteListTest"})
    public void routeListIdIsNotNullAfterRequestUpdateTest() throws Exception {
        loadFile("9.zip"); // create new route list and assign it to requests
        loadFile("10.zip");// update requests
        Statement requestsStatement = connection.createStatement();
        ResultSet requestsResultSet = requestsStatement.executeQuery(
                "SELECT COUNT(routeListID) FROM requests " +
                        "WHERE requests.requestIDExternal IN('5B4BONOV', '5B4BQNOV') AND requests.routeListID IS NOT NULL;"
        );
        if (requestsResultSet.next()) {
            Assert.assertEquals(requestsResultSet.getInt(1), 2);
        } else
            throw new AssertionError();
        requestsStatement.close();
    }

}
