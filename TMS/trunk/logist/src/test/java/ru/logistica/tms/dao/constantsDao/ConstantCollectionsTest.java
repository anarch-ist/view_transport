package ru.logistica.tms.dao.constantsDao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.TestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Test(singleThreaded = true)
public class ConstantCollectionsTest {
    private static ConstantsDao constantsDao = new ConstantsDaoImpl();

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ConnectionManager.getConnection().close();
    }

    @Test
    public void testGetUserRoles() throws Exception {
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
        ConnectionManager.getConnection().commit();
        Set<UserRole> userRoles = ConstantCollections.getUserRoles();
        List<String> userRolesAsList = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            userRolesAsList.add(userRole.getUserRoleId());
        }
        Assert.assertTrue(userRolesAsList.contains("WH_DISPATCHER"));
        Assert.assertTrue(userRolesAsList.contains("W_BOSS"));
        Assert.assertTrue(userRolesAsList.contains("SUPPLIER_MANAGER"));
        Assert.assertTrue(userRolesAsList.size() == 3);
    }

    @Test
    public void testGetPermission() throws Exception {
        ConstantCollections.setPermissions(constantsDao.getPermissions());
        ConnectionManager.getConnection().commit();
        Set<Permission> permissions = ConstantCollections.getPermissions();
        Assert.assertTrue(permissions.size() != 0);
        Assert.assertEquals(permissions.size(), 3);
        boolean isContainedPermission = false;
        for (Permission permission : permissions) {
            if (permission.getPermissionId().equals("testPerm1")) {
                isContainedPermission = true;
            }
        }
        Assert.assertTrue(isContainedPermission);
    }

    @Test
    public void testGetTimeDiff() throws Exception {
        ConstantCollections.setTimeDiffs(constantsDao.getTimeDiffs());
        ConnectionManager.getConnection().commit();
        Set<TimeDiff> timeDiffs = ConstantCollections.getTimeDiffs();
        Assert.assertTrue(timeDiffs.size() != 0);
        Assert.assertEquals(timeDiffs.size(), 48);
        boolean isContainedTimeDiff = false;
        for (TimeDiff timeDiff : timeDiffs) {
            if (timeDiff.getTimeDiffId() == 48) {
                isContainedTimeDiff = true;
            }
        }
        Assert.assertTrue(isContainedTimeDiff);
    }

    @Test
    public void testGetDonutStatuses() throws Exception {
        ConstantCollections.setWantStatuses(constantsDao.getWantStatuses());
        ConnectionManager.getConnection().commit();
        Set<WantStatus> wantStatuses = ConstantCollections.getWantStatuses();
        Assert.assertTrue(wantStatuses.size() != 0);
        Assert.assertEquals(wantStatuses.size(), 5);
        boolean isContainedDonutStatus = false;
        for (WantStatus wantStatus : wantStatuses) {
            if (wantStatus.getWantStatusId().equals("ERROR") && wantStatus.getWantStatusRusName().equals("Ошибка")) {
                isContainedDonutStatus = true;
            }
        }
        Assert.assertTrue(isContainedDonutStatus);
    }
}