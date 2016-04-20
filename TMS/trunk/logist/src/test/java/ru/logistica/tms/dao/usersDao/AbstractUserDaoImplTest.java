package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AbstractUserDaoImplTest {
    private static GenericUserDao<AbstractUser> genericUserDao = new AbstractUserDaoImpl();
    private static List<AbstractUser> abstractUsers = new ArrayList<>();
    private static AbstractUser abstractUser1 = new AbstractUser();
    private static AbstractUser abstractUser2 = new AbstractUser();
    private static AbstractUser abstractUser3 = new AbstractUser();

    public void fillingData() throws Exception {
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

        genericUserDao.save(abstractUser1);
        genericUserDao.save(abstractUser2);
        genericUserDao.save(abstractUser3);

        JdbcUtil.getConnection().commit();
    }

    @BeforeMethod
    public void setUp() throws SQLException, URISyntaxException {
        TestUtil.cleanDatabase(false); // recreate database
        JdbcUtil.setConnection(TestUtil.createConnection());
        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
    }

    @AfterMethod
    public void tearDown() throws SQLException {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        fillingData();
        Collection<AbstractUser> users = genericUserDao.getAll();
        JdbcUtil.getConnection().commit();
    }

    @Test
    public void testGetUserById() throws Exception {
        fillingData();
        AbstractUser abstractUser = genericUserDao.getById(2);
        Assert.assertEquals(abstractUser, abstractUser2);
    }

    @Test
    public void testSaveUser() throws Exception {
        fillingData();

        Collection<AbstractUser> users = genericUserDao.getAll();
        for (AbstractUser abstractUser : users) {
            for (AbstractUser user : abstractUsers) {
                if (user.getUserId().equals(abstractUser.getUserId())) {
                    Assert.assertEquals(abstractUser, user);
                }
            }
        }
    }

    @Test
    public void testUpdateUser() throws Exception {
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
        genericUserDao.update(abstractUser);
        JdbcUtil.getConnection().commit();
//        if (integer != null) throw new NullPointerException();

        Collection<AbstractUser> users = genericUserDao.getAll();
        for (AbstractUser user : users) {
            if (user.getLogin().equals(abstractUser.getLogin())) {
                Assert.assertEquals(user, abstractUser);
            }
        }
    }

    @Test
    public void testGetByLogin() throws Exception {
        fillingData();
        AbstractUser abstractUser = genericUserDao.getByLogin("user3");
        Assert.assertEquals(abstractUser, abstractUser3);
    }

    @Test
    public void testGetByLoginNoResult() throws Exception {
        fillingData();
        AbstractUser abstractUser = genericUserDao.getByLogin("user5");
        Assert.assertEquals(abstractUser, null);
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        fillingData();
        genericUserDao.deleteUserByLogin("user3");
        JdbcUtil.getConnection().commit();
        Collection<AbstractUser> users = genericUserDao.getAll();
        Assert.assertTrue(!users.contains(abstractUser3));
        Assert.assertEquals(users.size(), 2);
    }
}