package ru.logistica.tms.dao;

import org.testng.annotations.*;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.constantsDao.DonutStatus;
import ru.logistica.tms.dao.constantsDao.Permission;
import ru.logistica.tms.dao.constantsDao.TimeDiff;
import ru.logistica.tms.dao.constantsDao.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;


public class ConstantCollectionsTest {
    private static ConstantCollections constantCollections = new ConstantCollections();
    private static ConstantsDao constantsDao = new ConstantsDaoImpl();

    @BeforeClass
    public void setUp() throws Exception {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);

//        ConstantsDao constantsDao = new ConstantsDaoImpl();
//        ConstantCollections.setUserRoles(constantsDao.getUserRoles());

    }

    @AfterClass
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test(dependsOnMethods = {"testGetDonutStatuses"})
    public void testSetUserRoles() throws Exception {
        constantCollections.setUserRoles(constantsDao.getUserRoles());
        Set<UserRole> userRoles = constantCollections.getUserRoles();
        System.out.println(userRoles.toString());
    }

    @Test(dependsOnMethods = {"testSetUserRoles"})
    public void testSetPermission() throws SQLException {
        constantCollections.setPermissions(constantsDao.getPermissions());
        Set<Permission> permissions = constantCollections.getPermissions();
        System.out.println(permissions.toString());
    }

    @Test(dependsOnMethods = {"testSetPermission"})
    public void testSetTimeDiff() throws SQLException {
        constantCollections.setTimeDiffs(constantsDao.getTimeDiffs());
        Set<TimeDiff> timeDiffs = constantCollections.getTimeDiffs();
        System.out.println(timeDiffs.toString());
    }

    @Test(dependsOnMethods = {"testSetTimeDiff"})
    public void testSetDonutStatuses() throws SQLException {
        constantCollections.setDonutStatuses(constantsDao.getDonutStatuses());
        Set<DonutStatus> donutStatuses = constantCollections.getDonutStatuses();
        System.out.println(donutStatuses.toString());
    }

    @Test
    public void testGetUserRoles() throws Exception {
        Set<UserRole> userRoles = constantsDao.getUserRoles();
        System.out.println(userRoles.toString());
    }

    @Test(dependsOnMethods = {"testGetUserRoles"})
    public void testGetPermission() throws SQLException {
        Set<Permission> permissions = constantsDao.getPermissions();
        System.out.println(permissions.toString());
    }

    @Test(dependsOnMethods = {"testGetPermission"})
    public void testGetTimeDiff() throws SQLException {
        Set<TimeDiff> timeDiffs = constantsDao.getTimeDiffs();
        System.out.println(timeDiffs.toString());
    }

    @Test(dependsOnMethods = {"testGetTimeDiff"})
    public void testGetDonutStatuses() throws SQLException {
        Set<DonutStatus> donutStatuses = constantsDao.getDonutStatuses();
        System.out.println(donutStatuses.toString());
        System.out.println();
    }
}