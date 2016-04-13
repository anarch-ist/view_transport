package ru.logistica.tms.dao;

import org.testng.annotations.*;
import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import static org.testng.Assert.*;


public class ConstantCollectionsTest {
    private static ConstantCollections constantCollections = new ConstantCollections();

    @BeforeClass
    public void setUp() throws Exception {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);
    }

    @AfterClass
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void testGetUserRoles() throws Exception {
        Set<UserRole> userRoles = constantCollections.getUserRoles();
        System.out.println(userRoles.toString());
    }

    @Test(dependsOnMethods = {"testGetUserRoles"})
    public void testGetPermission() throws SQLException {
        Set<Permission> permissions = constantCollections.getPermissions();
        System.out.println(permissions.toString());
    }

    @Test(dependsOnMethods = {"testGetPermission"})
    public void testGetTimeDiff() throws SQLException {
        Set<TimeDiff> timeDiffs = constantCollections.getTimeDiffs();
        System.out.println(timeDiffs.toString());
    }

    @Test(dependsOnMethods = {"testGetTimeDiff"})
    public void testGetDonutStatuses() throws SQLException {
        Set<DonutStatus> donutStatuses = constantCollections.getDonutStatuses();
        System.out.println(donutStatuses.toString());
    }
}