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
    private static GenericUserDao supplierUserDao = new SupplierUserDaoImpl();
    private static List<AbstractUser> abstractUsers = new ArrayList<>();
    private static AbstractUser abstractUser1 = new AbstractUser();
    private static AbstractUser abstractUser2 = new AbstractUser();
    private static AbstractUser abstractUser3 = new AbstractUser();
    private static GenericUserDao genericUserDao = new AbstractUserDaoImpl();

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
            supplier.setInn("inn");
            supplier.setClientName("Kate");
            supplier.setKpp("kpp");
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
            supplierUser1.setUserId(5);
            supplierUser1.setLogin("user5");
            supplierUser1.setSalt("salt5salt5salt55");
            supplierUser1.setPassAndSalt("pass5");
            String userRoleId1 = "SUPPLIER_MANAGER";
            supplierUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            supplierUser1.setUserName("Katya");
            supplierUser1.setPhoneNumber("8-945-348-34-45");
            supplierUser1.setEmail("kat@bl.ru");
            supplierUser1.setPosition("position5");

            supplierUser2.setSupplier(supplier);
            supplierUser2.setUserId(6);
            supplierUser2.setLogin("user6");
            supplierUser2.setSalt("salt6salt6salt66");
            supplierUser2.setPassAndSalt("pass6");
            supplierUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            supplierUser2.setUserName("Motya");
            supplierUser2.setPhoneNumber("8-945-348-34-45");
            supplierUser2.setEmail("m@bl.ru");
            supplierUser2.setPosition("position6");
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    public void fillingTable() throws SQLException {
        try {
            abstractUsers.add(abstractUser1);
            abstractUsers.add(abstractUser2);
            abstractUsers.add(abstractUser3);

            abstractUser1.setUserId(1);
            abstractUser2.setUserId(2);
            abstractUser3.setUserId(3);

            abstractUser1.setLogin("user1");
            abstractUser2.setLogin("user2");
            abstractUser3.setLogin("user3");

            abstractUser1.setSalt("salt1salt1salt11");
            abstractUser2.setSalt("salt2salt2salt22");
            abstractUser3.setSalt("salt3salt3salt33");

            abstractUser1.setPassAndSalt("pass1");
            abstractUser2.setPassAndSalt("pass2");
            abstractUser3.setPassAndSalt("pass3");

            String userRoleId1 = "W_BOSS";
            abstractUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            String userRoleId2 = "SUPPLIER_MANAGER";
            abstractUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId2));
            String userRoleId3 = "WH_DISPATCHER";
            abstractUser3.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId3));

            abstractUser1.setUserName("Masha");
            abstractUser2.setUserName("Sasha");
            abstractUser3.setUserName("Dasha");

            abstractUser1.setPhoneNumber("8-909-675-45-45");
            abstractUser2.setPhoneNumber("8-903-676-75-40");
            abstractUser3.setPhoneNumber("8-917-567-48-76");

            abstractUser1.setEmail("masha@mail.ru");
            abstractUser2.setEmail("sasha@bk.com");
            abstractUser3.setEmail("dasha@yandex.ru");

            abstractUser1.setPosition("boss");
            abstractUser2.setPosition("manager");
            abstractUser3.setPosition("dispatcher");

            genericUserDao.saveOrUpdateUser(abstractUser1);
            genericUserDao.saveOrUpdateUser(abstractUser2);
            genericUserDao.saveOrUpdateUser(abstractUser3);

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

    }

    @Test
    public void testGetUserById() throws Exception {

    }

    @Test
    public void testSaveOrUpdateUser() throws Exception {
        try {
            fillingTable();
            fillingData();
            supplierUserDao.saveOrUpdateUser(supplierUser1);
            supplierUserDao.saveOrUpdateUser(supplierUser2);
            JdbcUtil.getConnection().commit();

            Set<SupplierUser> users = supplierUserDao.getAllUsers();
            Iterator<SupplierUser> iterator = users.iterator();
            while (iterator.hasNext()){
                SupplierUser supplierUserForCompare = iterator.next();
                if(supplierUserForCompare.getUserId().equals(supplierUser1.getUserId())){
                }
            }
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testGetByLogin() throws Exception {

    }

    @Test
    public void testDeleteUserByLogin() throws Exception {

    }
}