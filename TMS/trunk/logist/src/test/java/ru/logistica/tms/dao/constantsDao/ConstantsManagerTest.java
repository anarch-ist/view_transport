package ru.logistica.tms.dao.constantsDao;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.util.Set;


public class ConstantsManagerTest {
    private static ConstantsManager constantsManager = new ConstantsManager();
    private static ConstantCollections constantCollections = new ConstantCollections();

    @BeforeMethod
    public void setUp() throws Exception {
        JdbcUtil.setConnection(TestUtil.createConnection());
        constantsManager.getAllConstants();
    }

    @Test
    public void testGetAllConstantsUserRole() throws Exception {
        Set<UserRole> userRoles = constantCollections.getUserRoles();
        Assert.assertTrue(userRoles.size() != 0);
        Assert.assertEquals(userRoles.size(), 3);
        boolean isContainedUserRole = false;
        for (UserRole userRole : userRoles) {
            if (userRole.getUserRoleId().equals("SUPPLIER_MANAGER") && userRole.getUserRoleRusName().equals("Пользователь_поставщика")) {
                isContainedUserRole = true;
            }
        }
        Assert.assertTrue(isContainedUserRole);
    }

    @Test
    public void testGetAllConstantsPermission() throws Exception {
        Set<Permission> permissions = constantCollections.getPermissions();
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
    public void testGetAllConstantsTimeDiff() throws Exception {
        Set<TimeDiff> timeDiffs = constantCollections.getTimeDiffs();
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
    public void testGetAllConstantsDonutStatus() throws Exception {
        Set<DonutStatus> donutStatuses = constantCollections.getDonutStatuses();
        Assert.assertTrue(donutStatuses.size() != 0);
        Assert.assertEquals(donutStatuses.size(), 5);
        boolean isContainedDonutStatus = false;
        for (DonutStatus donutStatus : donutStatuses) {
            if (donutStatus.getDonutStatusId().equals("ERROR") && donutStatus.getDonutStatusRusName().equals("Ошибка")) {
                isContainedDonutStatus = true;
            }
        }
        Assert.assertTrue(isContainedDonutStatus);
    }
}