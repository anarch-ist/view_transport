package ru.logistica.tms.dao.constantsDao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class ConstantCollectionsTest {
    private static ConstantsDao constantsDao = new ConstantsDaoImpl();

    @BeforeClass
    public void setUp() throws Exception {
        JdbcUtil.setConnection(TestUtil.createConnection());
    }

    @AfterClass
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test(dependsOnMethods = {"testGetDonutStatuses"})
    public void testSetUserRoles() throws Exception {
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
        JdbcUtil.getConnection().commit();
        Set<UserRole> userRoles = ConstantCollections.getUserRoles();
        List<String> userRolesAsList = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            userRolesAsList.add(userRole.getUserRoleId());
        }
        Assert.assertEquals(userRolesAsList, Arrays.asList("WH_DISPATCHER", "W_BOSS", "SUPPLIER_MANAGER"));
    }

    @Test(dependsOnMethods = {"testSetUserRoles"})
    public void testSetPermission() throws SQLException {
        ConstantCollections.setPermissions(constantsDao.getPermissions());
        Set<Permission> permissions = ConstantCollections.getPermissions();
    }

    @Test(dependsOnMethods = {"testSetPermission"})
    public void testSetTimeDiff() throws SQLException {
        ConstantCollections.setTimeDiffs(constantsDao.getTimeDiffs());
        Set<TimeDiff> timeDiffs = ConstantCollections.getTimeDiffs();
    }

    @Test(dependsOnMethods = {"testSetTimeDiff"})
    public void testSetDonutStatuses() throws SQLException {
        ConstantCollections.setDonutStatuses(constantsDao.getDonutStatuses());
        Set<DonutStatus> donutStatuses = ConstantCollections.getDonutStatuses();
    }

    @Test
    public void testGetUserRoles() throws Exception {
        Set<UserRole> userRoles = constantsDao.getUserRoles();
    }

    @Test(dependsOnMethods = {"testGetUserRoles"})
    public void testGetPermission() throws SQLException {
        Set<Permission> permissions = constantsDao.getPermissions();
    }

    @Test(dependsOnMethods = {"testGetPermission"})
    public void testGetTimeDiff() throws SQLException {
        Set<TimeDiff> timeDiffs = constantsDao.getTimeDiffs();
    }

    @Test(dependsOnMethods = {"testGetTimeDiff"})
    public void testGetDonutStatuses() throws SQLException {
        Set<DonutStatus> donutStatuses = constantsDao.getDonutStatuses();
    }
}