package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.*;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


public class SupplierUserDaoImplTest {
    private static KeyDifferenceDao<Supplier> keyDifferenceDao = new SupplierDaoImpl();
    private static Supplier supplier = new Supplier();
    private static SupplierUser supplierUser1 = new SupplierUser();
    private static SupplierUser supplierUser2 = new SupplierUser();
    private static List<SupplierUser> supplierUsers = new ArrayList<>();
    private static GenericUserDao<SupplierUser> supplierUserDao = new SupplierUserDaoImpl();


    @BeforeMethod
    public void setUp() throws Exception {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        // recreate database
        TestUtil.cleanDatabase(false);

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);

        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
    }

    public void fillingData() throws SQLException {
        try{
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
            keyDifferenceDao.saveOrUpdateKeyDifference(supplier);

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

            supplierUserDao.saveOrUpdateUser(supplierUser1);
            supplierUserDao.saveOrUpdateUser(supplierUser2);
            JdbcUtil.getConnection().commit();
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    @AfterMethod
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        try {
            fillingData();
            Set<SupplierUser> users = supplierUserDao.getAllUsers();
            System.out.println(users.size());
            System.out.println(users);
            JdbcUtil.getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        try {
            fillingData();
            SupplierUser user = supplierUserDao.getUserById(1);
            user.setUserId(null);
            Assert.assertEquals(user, supplierUser1);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testSaveOrUpdateUser() throws Exception {
        try {
            fillingData();

            supplierUserDao.saveOrUpdateUser(supplierUser1);
            supplierUserDao.saveOrUpdateUser(supplierUser2);
            JdbcUtil.getConnection().commit();

            Set<SupplierUser> users = supplierUserDao.getAllUsers();
            Iterator<SupplierUser> iterator = users.iterator();
            while (iterator.hasNext()){
                SupplierUser supplierUser = iterator.next();
                supplierUser.setUserId(null);
                for(SupplierUser user: supplierUsers){
                    if(user.getLogin().equals(supplierUser.getLogin())){
                        Assert.assertEquals(supplierUser, user);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testGetByLogin() throws Exception {
        try {
            fillingData();
            SupplierUser user = supplierUserDao.getByLogin("user6");
            user.setUserId(null);
            Assert.assertEquals(user, supplierUser1);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        try {
            fillingData();
            supplierUserDao.deleteUserByLogin("user7");
            JdbcUtil.getConnection().commit();
            Set<SupplierUser> users = supplierUserDao.getAllUsers();
            System.out.println(users.size());
            Assert.assertTrue(!users.contains(supplierUser2));
            Assert.assertEquals(users.size(), 1);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }
}