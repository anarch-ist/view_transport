package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class SupplierUserDaoImplTest {
    private static GenericDao<Supplier> supplierDao = new SupplierDaoImpl();
    private static Supplier supplier = new Supplier();
    private static SupplierUser supplierUser1 = new SupplierUser();
    private static SupplierUser supplierUser2 = new SupplierUser();
    private static List<SupplierUser> supplierUsers = new ArrayList<>();
    private static GenericUserDao<SupplierUser> supplierUserDao = new SupplierUserDaoImpl();


    @BeforeMethod
    public void setUp() throws Exception {
        // recreate database
        TestUtil.cleanDatabase(false);
        JdbcUtil.setConnection(TestUtil.createConnection());
        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
    }

    public void fillingData() throws SQLException, DaoException {
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
        supplierDao.saveOrUpdate(supplier);

        supplierUser1.setSupplier(supplier);
        supplierUser1.setUserId(null);
        supplierUser1.setLogin("user6");
        supplierUser1.setSalt("salt6salt6salt66");
        supplierUser1.setPassAndSalt("pass6");
        String userRoleId1 = "SUPPLIER_MANAGER";
        supplierUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser1.setUserName("Fedor");
        supplierUser1.setPhoneNumber("8-916-376-90-43");
        supplierUser1.setEmail("f@mail.ru");
        supplierUser1.setPosition("position6");

        supplierUser2.setSupplier(supplier);
        supplierUser2.setUserId(null);
        supplierUser2.setLogin("user7");
        supplierUser2.setSalt("salt7salt7salt77");
        supplierUser2.setPassAndSalt("pass7");
        supplierUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser2.setUserName("Kirill");
        supplierUser2.setPhoneNumber("8-909-348-34-45");
        supplierUser2.setEmail("kira@bk.ru");
        supplierUser2.setPosition("position7");

        supplierUsers.add(supplierUser1);
        supplierUsers.add(supplierUser2);

        supplierUserDao.save(supplierUser1);
        supplierUserDao.save(supplierUser2);
        JdbcUtil.getConnection().commit();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        fillingData();
        Collection<SupplierUser> users = supplierUserDao.getAll();
        JdbcUtil.getConnection().commit();
        Assert.assertEquals(users.size(), 2);
    }

    @Test
    public void testGetUserById() throws Exception {
        fillingData();
        SupplierUser user = supplierUserDao.getById(1);
        user.setUserId(null);
        Assert.assertEquals(user, supplierUser1);
    }

    @Test
    public void testSaveUser() throws Exception {
        fillingData();

        Collection<SupplierUser> users = supplierUserDao.getAll();
        for (SupplierUser supplierUser : users) {
            supplierUser.setUserId(null);
            for (SupplierUser user : supplierUsers) {
                if (user.getLogin().equals(supplierUser.getLogin())) {
                    Assert.assertEquals(supplierUser, user);
                }
            }
        }
    }

    @Test
    public void testUpdateUser() throws SQLException, DaoException {
        fillingData();

        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setSupplier(supplier);
        supplierUser.setUserId(null);
        supplierUser.setLogin("user6");
        supplierUser.setSalt("salt9salt9salt99");
        supplierUser.setPassAndSalt("pass9");
        String userRoleId1 = "SUPPLIER_MANAGER";
        supplierUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        supplierUser.setUserName("Fed");
        supplierUser.setPhoneNumber("8-916-376-90-43");
        supplierUser.setEmail("f@mail.ru");
        supplierUser.setPosition("position6");
        supplierUserDao.update(supplierUser);
        JdbcUtil.getConnection().commit();

        Collection<SupplierUser> users = supplierUserDao.getAll();
        for (AbstractUser user : users) {
            user.setUserId(null);
            if (user.getLogin().equals(supplierUser.getLogin())) {
                Assert.assertEquals(user, supplierUser);
            }
        }
    }

    @Test
    public void testGetByLogin() throws Exception {
        fillingData();
        SupplierUser user = supplierUserDao.getByLogin("user6");
        user.setUserId(null);
        Assert.assertEquals(user, supplierUser1);
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        fillingData();
        supplierUserDao.deleteUserByLogin("user7");
        JdbcUtil.getConnection().commit();
        Collection<SupplierUser> users = supplierUserDao.getAll();
        Assert.assertTrue(!users.contains(supplierUser2));
        Assert.assertEquals(users.size(), 1);
    }
}