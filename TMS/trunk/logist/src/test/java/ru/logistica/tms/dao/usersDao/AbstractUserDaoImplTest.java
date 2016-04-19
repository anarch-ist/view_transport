package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class AbstractUserDaoImplTest {
    private static ConstantCollections constantCollections = new ConstantCollections();
    private static ConstantsDao constantsDao = new ConstantsDaoImpl();
    private static GenericUserDao genericUserDao = new AbstractUserDaoImpl();
    private static List<AbstractUser> abstractUsers = new ArrayList<>();
    private static AbstractUser abstractUser1 = new AbstractUser();
    private static AbstractUser abstractUser2 = new AbstractUser();
    private static AbstractUser abstractUser3 = new AbstractUser();

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

    @AfterMethod
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        try {
            fillingData();
            Set<AbstractUser> users = genericUserDao.getAllUsers();
            System.out.println(users.size());
            JdbcUtil.getConnection().commit();
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        try {
            fillingData();
            AbstractUser abstractUser = genericUserDao.getUserById(2);
            Assert.assertEquals(abstractUser, abstractUser2);
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    public void fillingData() throws SQLException {
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

    @Test
    public void testSaveUser() throws Exception {
        try{
            fillingData();

            Set<AbstractUser> users = genericUserDao.getAllUsers();
            Iterator<AbstractUser> iterator = users.iterator();
            while (iterator.hasNext()){
                AbstractUser abstractUser = iterator.next();
                for(AbstractUser user: abstractUsers){
                    if(user.getUserId().equals(abstractUser.getUserId())){
                        Assert.assertEquals(abstractUser, user);
                    }
                }
            }
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testUpdateUser() throws Exception {
        try{
            fillingData();
            AbstractUser abstractUser = new AbstractUser();
            abstractUser.setUserId(1);
            abstractUser.setLogin("user1");
            abstractUser.setSalt("salt4salt4salt44");
            abstractUser.setPassAndSalt("pass4");
            String userRoleId1 = "W_BOSS";
            abstractUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            abstractUser.setUserName("Misha");
            abstractUser.setPhoneNumber("8-916-367-48-76");
            abstractUser.setEmail("misha@mail.ru");
            abstractUser.setPosition("boss");

            genericUserDao.saveOrUpdateUser(abstractUser);

            JdbcUtil.getConnection().commit();

            Set<AbstractUser> users = genericUserDao.getAllUsers();
            System.out.println("size = " + users.size());
            Iterator<AbstractUser> iterator = users.iterator();
            while (iterator.hasNext()){
                AbstractUser user = iterator.next();
                if(user.getLogin().equals(abstractUser.getLogin())){
                    Assert.assertEquals(user, abstractUser);
                }
            }
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
        }

    @Test
    public void testGetByLogin() throws Exception {
        try {
            fillingData();
            AbstractUser abstractUser = genericUserDao.getByLogin("user3");
            Assert.assertEquals(abstractUser, abstractUser3);
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        try {
            fillingData();
            genericUserDao.deleteUserByLogin("user3");
            JdbcUtil.getConnection().commit();
            Set<AbstractUser> users = genericUserDao.getAllUsers();
            System.out.println(users.size());
            Assert.assertTrue(!users.contains(abstractUser3));
            Assert.assertEquals(users.size(), 2);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }
}