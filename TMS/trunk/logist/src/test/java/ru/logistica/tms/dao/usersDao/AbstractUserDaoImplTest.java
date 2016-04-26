package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.ConnectionManager;

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

    @BeforeClass
    public void fillingData() throws Exception {
        TestUtil.cleanDatabase(false); // recreate database
        ConnectionManager.setConnection(TestUtil.createConnection());
        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());

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

        ConnectionManager.getConnection().commit();
    }

    @AfterClass
    public void tearDown() throws SQLException {
        ConnectionManager.getConnection().close();
    }

    @Test(expectedExceptions = DaoException.class)
    public void testSaveUserExistedUser() throws Exception {
        try {
            genericUserDao.save(abstractUser1);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testSaveUserExistedUser"})
    public void testUpdateUser() throws Exception {
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
        ConnectionManager.getConnection().commit();
        Collection<AbstractUser> users = genericUserDao.getAll();
        for (AbstractUser user : users) {
            if (user.getLogin().equals(abstractUser.getLogin())) {
                Assert.assertEquals(user, abstractUser);
            }
        }
    }

    @Test(expectedExceptions = DaoException.class, dependsOnMethods = {"testUpdateUser"})
    public void testUpdateUserNotExistedUser() throws Exception {
        try {
            AbstractUser abstractUser = new AbstractUser();
            abstractUser.setUserId(1);
            abstractUser.setLogin("user4");
            abstractUser.setSalt("salt4salt4salt44");
            abstractUser.setPassAndSalt("pass4");
            String userRoleId1 = "W_BOSS";
            abstractUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            abstractUser.setUserName("Misha");
            abstractUser.setPhoneNumber("8-916-367-48-76");
            abstractUser.setEmail("misha@mail.ru");
            abstractUser.setPosition("boss");
            genericUserDao.update(abstractUser);
        }finally {
            ConnectionManager.getConnection().commit();
        }
    }

    @Test(dependsOnMethods = {"testUpdateUserNotExistedUser"})
    public void testGetAllUsers() throws Exception {
        Collection<AbstractUser> users = genericUserDao.getAll();
        System.out.println(users);
        Assert.assertEquals(users.size(), 3);
        ConnectionManager.getConnection().commit();
    }

    @Test(dependsOnMethods = {"testGetAllUsers"})
    public void testGetUserById() throws Exception {
        AbstractUser abstractUser = genericUserDao.getById(2);
        Assert.assertEquals(abstractUser, abstractUser2);
    }

    @Test(dependsOnMethods = {"testGetUserById"})
    public void testGetByIdNoResult() throws Exception {
        AbstractUser abstractUser = genericUserDao.getById(4);
        Assert.assertEquals(abstractUser, null);
    }

    @Test(dependsOnMethods = {"testGetByIdNoResult"})
    public void testGetByLogin() throws Exception {
        AbstractUser abstractUser = genericUserDao.getByLogin("user3");
        Assert.assertEquals(abstractUser, abstractUser3);
    }

    @Test(dependsOnMethods = {"testGetByLogin"})
    public void testGetByLoginNoResult() throws Exception {
        AbstractUser abstractUser = genericUserDao.getByLogin("user5");
        Assert.assertEquals(abstractUser, null);
    }

    @Test(dependsOnMethods = {"testGetByLoginNoResult"})
    public void testDeleteUserByLogin() throws Exception {
        genericUserDao.deleteUserByLogin("user3");
        ConnectionManager.getConnection().commit();
        Collection<AbstractUser> users = genericUserDao.getAll();
        Assert.assertTrue(!users.contains(abstractUser3));
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
        Collection<AbstractUser> users = genericUserDao.getAll();
        Assert.assertTrue(users.size() == 0);
    }






}