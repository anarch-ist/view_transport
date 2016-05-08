package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Test(singleThreaded = true)
public class WarehouseUserDaoImplTest {
    private static GenericDao<Warehouse> pointDao = new WarehouseDaoImpl();
    private static Warehouse warehouse = new Warehouse();
    private static WarehouseUser warehouseUser1 = new WarehouseUser();
    private static WarehouseUser warehouseUser2 = new WarehouseUser();
    private static List<WarehouseUser> warehouseUsers = new ArrayList<>();
    private static GenericUserDao<WarehouseUser> pointUserDao = new WarehouseUserDaoImpl();


    @BeforeClass
    public static void setUp() throws DaoException, SQLException, URISyntaxException {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());

        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());

        warehouse.setWarehouseId(1);
        warehouse.setWarehouseName("warehouse");
        warehouse.setRegion("EKT");
        warehouse.setDistrict("Sverd");
        warehouse.setLocality("Ural");
        warehouse.setMailIndex("454363");
        warehouse.setAddress("EKT, street Lenina, 3, 4");
        warehouse.setEmail("ural@yandex.ru");
        warehouse.setPhoneNumber("8-989-535-34-21");
        warehouse.setResponsiblePersonId("person");
        pointDao.saveOrUpdate(warehouse);

        warehouseUser1.setWarehouse(warehouse);
        warehouseUser1.setUserId(null);
        warehouseUser1.setLogin("user4");
        warehouseUser1.setPassword("pass4");
        String userRoleId1 = "W_BOSS";
        warehouseUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        warehouseUser1.setUserName("Kolya");
        warehouseUser1.setPhoneNumber("8-945-348-34-45");
        warehouseUser1.setEmail("k@bk.ru");
        warehouseUser1.setPosition("position4");

        warehouseUser2.setWarehouse(warehouse);
        warehouseUser2.setUserId(null);
        warehouseUser2.setLogin("user5");
        warehouseUser2.setPassword("pass5");
        String userRoleId2 = "WH_DISPATCHER";
        warehouseUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId2));
        warehouseUser2.setUserName("Kate");
        warehouseUser2.setPhoneNumber("8-945-846-24-15");
        warehouseUser2.setEmail("kate@bk.ru");
        warehouseUser2.setPosition("position5");

        warehouseUsers.add(warehouseUser1);
        warehouseUsers.add(warehouseUser2);

        pointUserDao.save(warehouseUser1);
        pointUserDao.save(warehouseUser2);
        ConnectionManager.getConnection().commit();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ConnectionManager.getConnection().close();
    }

    @Test(expectedExceptions = DaoException.class)
    public void testSaveUserExistedUser() throws Exception {
        try {
            pointUserDao.save(warehouseUser1);
        }
        finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testSaveUserExistedUser"})
    public void testUpdateUser() throws SQLException, DaoException {
        WarehouseUser warehouseUser = new WarehouseUser();
        warehouseUser.setWarehouse(warehouse);
        warehouseUser.setUserId(null);
        warehouseUser.setLogin("user4");
        warehouseUser.setPassword("pass8");
        String userRoleId1 = "W_BOSS";
        warehouseUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        warehouseUser.setUserName("Kol");
        warehouseUser.setPhoneNumber("8-945-348-34-45");
        warehouseUser.setEmail("k@bk.ru");
        warehouseUser.setPosition("position4");
        pointUserDao.update(warehouseUser);
        ConnectionManager.getConnection().commit();

        Collection<WarehouseUser> users = pointUserDao.getAll();
        ConnectionManager.getConnection().commit();
        for (User user : users) {
            user.setUserId(null);
            if (user.getLogin().equals(warehouseUser.getLogin())) {
                Assert.assertEquals(user, warehouseUser);
            }
        }
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testUpdateUser"})
    public void testUpdateUserNotExistedUser() throws SQLException, DaoException {
        try {
            WarehouseUser warehouseUser = new WarehouseUser();
            warehouseUser.setWarehouse(warehouse);
            warehouseUser.setUserId(null);
            warehouseUser.setLogin("user9");
            warehouseUser.setPassword("pass8");
            String userRoleId1 = "W_BOSS";
            warehouseUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            warehouseUser.setUserName("Kol");
            warehouseUser.setPhoneNumber("8-945-348-34-45");
            warehouseUser.setEmail("k@bk.ru");
            warehouseUser.setPosition("position4");
            pointUserDao.update(warehouseUser);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testUpdateUserNotExistedUser"})
    public void testGetAllUsers() throws Exception {
        Collection<WarehouseUser> users = pointUserDao.getAll();
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(users.size(), 2);
    }

    @Test(dependsOnMethods = {"testGetAllUsers"})
    public void testGetUserById() throws Exception {
        WarehouseUser user = pointUserDao.getById(2);
        user.setUserId(null);
        Assert.assertEquals(user, warehouseUser2);
    }

    @Test(dependsOnMethods = {"testGetUserById"})
    public void testGetUserByIdNotExistedId() throws Exception {
        WarehouseUser user = pointUserDao.getById(9);
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetUserByIdNotExistedId"})
    public void testGetByLogin() throws Exception {
        WarehouseUser user = pointUserDao.getByLogin("user5");
        user.setUserId(null);
        Assert.assertEquals(user, warehouseUser2);
    }

    @Test(dependsOnMethods = {"testGetByLogin"})
    public void testGetByLoginNotExistedLogin() throws Exception {
        WarehouseUser user = pointUserDao.getByLogin("user9");
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetByLoginNotExistedLogin"})
    public void testDeleteUserByLogin() throws Exception {
        pointUserDao.deleteUserByLogin("user5");
        ConnectionManager.getConnection().commit();
        Collection<WarehouseUser> users = pointUserDao.getAll();
        Assert.assertTrue(!users.contains(warehouseUser2));
        Assert.assertEquals(users.size(), 1);
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testDeleteUserByLogin"})
    public void testDeleteUserByLoginNoResult() throws Exception {
        try {
            pointUserDao.deleteUserByLogin("user9");
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testDeleteUserByLoginNoResult"})
    public void testGetAllUsersNoResult() throws Exception {
        pointUserDao.deleteUserByLogin("user4");
        Collection<WarehouseUser> users = pointUserDao.getAll();
        Assert.assertTrue(users.size() == 0);
    }
}