package ru.logistica.tms.dao.constantsDao;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Set;

import static org.testng.Assert.*;


public class ConstantsManagerTest {
    private static ConstantsManager constantsManager = new ConstantsManager();
    private static ConstantCollections constantCollections = new ConstantCollections();

    @BeforeMethod
    public void setUp() throws Exception {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);

        constantsManager.getAllConstants();
    }

    @Test
    public void testGetAllConstantsUserRole() throws Exception {
        Set<UserRole> userRoles = constantCollections.getUserRoles();
        Assert.assertTrue(userRoles.size() != 0);
        Assert.assertEquals(userRoles.size(), 3);
        boolean isContainedUserRole = false;
        Iterator<UserRole> iterator = userRoles.iterator();
        while (iterator.hasNext()){
            UserRole userRole = iterator.next();
            if(userRole.getUserRoleId().equals("SUPPLIER_MANAGER") && userRole.getUserRoleRusName().equals("Пользователь_поставщика")){
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
        Iterator<Permission> iterator = permissions.iterator();
        while (iterator.hasNext()){
            Permission permission = iterator.next();
            if(permission.getPermissionId().equals("testPerm1")){
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
        Iterator<TimeDiff> iterator = timeDiffs.iterator();
        while (iterator.hasNext()){
            TimeDiff timeDiff = iterator.next();
            if(timeDiff.getTimeDiffId() == 48){
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
        Iterator<DonutStatus> iterator = donutStatuses.iterator();
        while (iterator.hasNext()){
            DonutStatus donutStatus = iterator.next();
            if(donutStatus.getDonutStatusId().equals("ERROR") && donutStatus.getDonutStatusRusName().equals("Ошибка")){
                isContainedDonutStatus = true;
            }
        }
        Assert.assertTrue(isContainedDonutStatus);
    }
}