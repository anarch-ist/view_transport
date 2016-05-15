package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.TestUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Test(singleThreaded = true)
public class UserDaoImplTest {
    private static GenericUserDao<User> genericUserDao = new UserDaoImpl();
    private static List<User> users = new ArrayList<>();
    private static User user1 = new User();
    private static User user2 = new User();
    private static User user3 = new User();

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false); // recreate database
        ConnectionManager.setConnection(TestUtil.createConnection());

        users.add(user1);
        users.add(user2);
        users.add(user3);

        user1.setUserId(1);
        user2.setUserId(2);
        user3.setUserId(3);

        user1.setLogin("user1");
        user2.setLogin("user2");
        user3.setLogin("user3");

        user1.setPassword("pass1");
        user2.setPassword("pass2");
        user3.setPassword("pass3");

        user1.setUserRole(User.UserRole.W_BOSS);
        user2.setUserRole(User.UserRole.SUPPLIER_MANAGER);
        user3.setUserRole(User.UserRole.WH_DISPATCHER);

        user1.setUserName("Masha");
        user2.setUserName("Sasha");
        user3.setUserName("Dasha");

        user1.setPhoneNumber("8-909-675-45-45");
        user2.setPhoneNumber("8-903-676-75-40");
        user3.setPhoneNumber("8-917-567-48-76");

        user1.setEmail("masha@mail.ru");
        user2.setEmail("sasha@bk.com");
        user3.setEmail("dasha@yandex.ru");

        user1.setPosition("boss");
        user2.setPosition("manager");
        user3.setPosition("dispatcher");

        genericUserDao.save(user1);
        genericUserDao.save(user2);
        genericUserDao.save(user3);

        ConnectionManager.getConnection().commit();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        ConnectionManager.getConnection().close();
    }

    @Test(expectedExceptions = DaoException.class)
    public void testSaveUserExistedUser() throws Exception {
        try {
            genericUserDao.save(user1);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testSaveUserExistedUser"})
    public void testUpdateUser() throws Exception {
        User abstractUser = new User();
        abstractUser.setUserId(1);
        abstractUser.setLogin("user1");
        abstractUser.setPassword("pass4");
        abstractUser.setUserRole(User.UserRole.W_BOSS);
        abstractUser.setUserName("Misha");
        abstractUser.setPhoneNumber("8-916-367-48-76");
        abstractUser.setEmail("misha@mail.ru");
        abstractUser.setPosition("boss");
        genericUserDao.update(abstractUser);
        ConnectionManager.getConnection().commit();
        Collection<User> users = genericUserDao.getAll();
        for (User user : users) {
            if (user.getLogin().equals(abstractUser.getLogin())) {
                Assert.assertEquals(user, abstractUser);
            }
        }
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testUpdateUser"})
    public void testUpdateUserNotExistedUser() throws Exception {
        try {
            User user = new User();
            user.setUserId(1);
            user.setLogin("user4");
            user.setPassword("pass4");
            user.setUserRole(User.UserRole.W_BOSS);
            user.setUserName("Misha");
            user.setPhoneNumber("8-916-367-48-76");
            user.setEmail("misha@mail.ru");
            user.setPosition("boss");
            genericUserDao.update(user);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testUpdateUserNotExistedUser"})
    public void testGetAllUsers() throws Exception {
        Collection<User> users = genericUserDao.getAll();
        Assert.assertEquals(users.size(), 3);
        ConnectionManager.getConnection().commit();
    }

    @Test(dependsOnMethods = {"testGetAllUsers"})
    public void testGetUserById() throws Exception {
        User user = genericUserDao.getById(2);
        Assert.assertEquals(user, user2);
    }

    @Test(dependsOnMethods = {"testGetUserById"})
    public void testGetByIdNoResult() throws Exception {
        User user = genericUserDao.getById(4);
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetByIdNoResult"})
    public void testGetByLogin() throws Exception {
        User user = genericUserDao.getByLogin("user3");
        Assert.assertEquals(user, user3);
    }

    @Test(dependsOnMethods = {"testGetByLogin"})
    public void testGetByLoginNoResult() throws Exception {
        User user = genericUserDao.getByLogin("user5");
        Assert.assertEquals(user, null);
    }

    @Test(dependsOnMethods = {"testGetByLoginNoResult"})
    public void testDeleteUserByLogin() throws Exception {
        genericUserDao.deleteUserByLogin("user3");
        ConnectionManager.getConnection().commit();
        Collection<User> users = genericUserDao.getAll();
        Assert.assertTrue(!users.contains(user3));
        Assert.assertEquals(users.size(), 2);
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testDeleteUserByLogin"})
    public void testDeleteUserByLoginNotResult() throws Exception {
        try {
            genericUserDao.deleteUserByLogin("user9");
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testDeleteUserByLoginNotResult"})
    public void testGetAllUsersNoResult() throws Exception {
        genericUserDao.deleteUserByLogin("user1");
        genericUserDao.deleteUserByLogin("user2");
        Collection<User> users = genericUserDao.getAll();
        Assert.assertTrue(users.size() == 0);
    }






}