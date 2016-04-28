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
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Test(singleThreaded = true)
public class SupplierUserDaoImplTest {
    private static GenericDao<Supplier> supplierDao = new SupplierDaoImpl();
    private static Supplier supplier = new Supplier();
    private static SupplierUser supplierUser1 = new SupplierUser();
    private static SupplierUser supplierUser2 = new SupplierUser();
    private static List<SupplierUser> supplierUsers = new ArrayList<>();
    private static GenericUserDao<SupplierUser> supplierUserDao = new SupplierUserDaoImpl();

    @BeforeClass
    public static void setUp() throws SQLException, DaoException, URISyntaxException {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());
        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());

        supplier.setSupplierID(1);
        supplier.setInn("547658768");
        supplier.setClientName("Roman");
        supplier.setKpp("74678788436");
        supplier.setCorAccount("89475984567");
        supplier.setCurAccount("239489");
        supplier.setBik("46467676");
        supplier.setBankName("ural");
        supplier.setContractNumber("8989");
        supplier.setDateOfSigning(new Date(2016, 4, 22));
        supplier.setStartContractDate(new Date(2016, 4, 24));
        supplier.setEndContractDate(new Date(2016, 7, 24));
        supplierDao.saveOrUpdate(supplier); //!!!!!!!!!!!!!!!

        supplierUser1.setSupplier(supplier);
        supplierUser1.setUserId(null);
        supplierUser1.setLogin("user6");
        supplierUser1.setPassword("pass6");
        String userRoleId1 = "SUPPLIER_MANAGER";
        supplierUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser1.setUserName("Fedor");
        supplierUser1.setPhoneNumber("8-916-376-90-43");
        supplierUser1.setEmail("f@mail.ru");
        supplierUser1.setPosition("position6");

        supplierUser2.setSupplier(supplier);
        supplierUser2.setUserId(null);
        supplierUser2.setLogin("user7");
        supplierUser2.setPassword("pass7");
        supplierUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser2.setUserName("Kirill");
        supplierUser2.setPhoneNumber("8-909-348-34-45");
        supplierUser2.setEmail("kira@bk.ru");
        supplierUser2.setPosition("position7");

        supplierUsers.add(supplierUser1);
        supplierUsers.add(supplierUser2);

        supplierUserDao.save(supplierUser1);
        supplierUserDao.save(supplierUser2);
        ConnectionManager.getConnection().commit();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ConnectionManager.getConnection().close();
    }

    @Test(expectedExceptions = DaoException.class)
    public void testSaveUserExistedUser() throws Exception {
        try {
            supplierUserDao.save(supplierUser1);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testSaveUserExistedUser"})
    public void testUpdateUser() throws SQLException, DaoException {
        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setSupplier(supplier);
        supplierUser.setUserId(null);
        supplierUser.setLogin("user6");
        supplierUser.setPassword("testtest");
        String userRoleId1 = "SUPPLIER_MANAGER";
        supplierUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser.setUserName("Fed");
        supplierUser.setPhoneNumber("8-916-376-90-43");
        supplierUser.setEmail("f@mail.ru");
        supplierUser.setPosition("position6");
        supplierUserDao.update(supplierUser);
        ConnectionManager.getConnection().commit();

        Collection<SupplierUser> users = supplierUserDao.getAll();
        for (User user : users) {
            user.setUserId(null);
            if (user.getLogin().equals(supplierUser.getLogin())) {
                Assert.assertEquals(user, supplierUser);
            }
        }
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testUpdateUser"})
    public void testUpdateUserNotExistedUser() throws SQLException, DaoException {
        try {
            SupplierUser supplierUser = new SupplierUser();
            supplierUser.setSupplier(supplier);
            supplierUser.setUserId(null);
            supplierUser.setLogin("user9");
            supplierUser.setPassword("pass8");
            String userRoleId1 = "W_BOSS";
            supplierUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            supplierUser.setUserName("Kol");
            supplierUser.setPhoneNumber("8-945-348-34-45");
            supplierUser.setEmail("k@bk.ru");
            supplierUser.setPosition("position4");
            supplierUserDao.update(supplierUser);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testUpdateUserNotExistedUser"})
    public void testGetAllUsers() throws Exception {
        Collection<SupplierUser> users = supplierUserDao.getAll();
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(users.size(), 2);
    }

    @Test(dependsOnMethods = {"testGetAllUsers"})
    public void testGetUserById() throws Exception {
        SupplierUser user = supplierUserDao.getById(2);
        user.setUserId(null);
        Assert.assertEquals(user, supplierUser2);
    }

    @Test(dependsOnMethods = {"testGetUserById"})
    public void testGetUserByIdNotExistedId() throws Exception {
        SupplierUser user = supplierUserDao.getById(9);
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetUserByIdNotExistedId"})
    public void testGetByLogin() throws Exception {
        SupplierUser user = supplierUserDao.getByLogin("user7");
        user.setUserId(null);
        Assert.assertEquals(user, supplierUser2);
    }

    @Test(dependsOnMethods = {"testGetByLogin"})
    public void testGetByLoginNotExistedLogin() throws Exception {
        SupplierUser user = supplierUserDao.getByLogin("user9");
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetByLoginNotExistedLogin"})
    public void testDeleteUserByLogin() throws Exception {
        supplierUserDao.deleteUserByLogin("user7");
        ConnectionManager.getConnection().commit();
        Collection<SupplierUser> users = supplierUserDao.getAll();
        Assert.assertTrue(!users.contains(supplierUser2));
        Assert.assertEquals(users.size(), 1);
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testDeleteUserByLogin"})
    public void testDeleteUserByLoginNoResult() throws Exception {
        try{
            supplierUserDao.deleteUserByLogin("user9");
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testDeleteUserByLoginNoResult"})
    public void testGetAllUsersNoResult() throws Exception {
        supplierUserDao.deleteUserByLogin("user6");
        Collection<SupplierUser> users = supplierUserDao.getAll();
        Assert.assertTrue(users.size() == 0);
    }

}