package ru.logistica.tms.dao.usersDao;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.pointsDao.Point;
import ru.logistica.tms.dao.pointsDao.PointDaoImpl;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PointUserDaoImplTest {
    private static GenericDao<Point> pointDao = new PointDaoImpl();
    private static Point point = new Point();
    private static PointUser pointUser1 = new PointUser();
    private static PointUser pointUser2 = new PointUser();
    private static List<PointUser> pointUsers = new ArrayList<>();
    private static GenericUserDao<PointUser> pointUserDao = new PointUserDaoImpl();

    @BeforeMethod
    public void setUp() throws Exception {

        // recreate database
        TestUtil.cleanDatabase(false);
        JdbcUtil.setConnection(TestUtil.createConnection());
        ConstantsDao constantsDao = new ConstantsDaoImpl();
        ConstantCollections.setUserRoles(constantsDao.getUserRoles());
    }

    @AfterMethod
    public void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    public void fillingData() throws DaoException, SQLException {
        point.setPointId(1);
        point.setPointName("point");
        point.setRegion("EKT");
        point.setDistrict("Sverd");
        point.setLocality("Ural");
        point.setMailIndex("454363");
        point.setAddress("EKT, street Lenina, 3, 4");
        point.setEmail("ural@yandex.ru");
        point.setPhoneNumber("8-989-535-34-21");
        point.setResponsiblePersonId("person");
        pointDao.saveOrUpdate(point);

        pointUser1.setPoint(point);
        pointUser1.setUserId(null);
        pointUser1.setLogin("user4");
        pointUser1.setSalt("salt4salt4salt44");
        pointUser1.setPassAndSalt("pass4");
        String userRoleId1 = "W_BOSS";
        pointUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        pointUser1.setUserName("Kolya");
        pointUser1.setPhoneNumber("8-945-348-34-45");
        pointUser1.setEmail("k@bk.ru");
        pointUser1.setPosition("position4");

        pointUser2.setPoint(point);
        pointUser2.setUserId(null);
        pointUser2.setLogin("user5");
        pointUser2.setSalt("salt5salt5salt55");
        pointUser2.setPassAndSalt("pass5");
        String userRoleId2 = "WH_DISPATCHER";
        pointUser2.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId2));
        pointUser2.setUserName("Kate");
        pointUser2.setPhoneNumber("8-945-846-24-15");
        pointUser2.setEmail("kate@bk.ru");
        pointUser2.setPosition("position5");

        pointUsers.add(pointUser1);
        pointUsers.add(pointUser2);

        pointUserDao.save(pointUser1);
        pointUserDao.save(pointUser2);
        JdbcUtil.getConnection().commit();
    }


    @Test
    public void testGetAllUsers() throws Exception {
        fillingData();
        Collection<PointUser> users = pointUserDao.getAll();
        JdbcUtil.getConnection().commit();
        Assert.assertEquals(users.size(), 2);
    }

    @Test
    public void testGetUserById() throws Exception {
        fillingData();
        PointUser user = pointUserDao.getById(1);
        user.setUserId(null);
        Assert.assertEquals(user, pointUser1);
    }

    @Test
    public void testSaveUser() throws Exception {
        fillingData();

        Collection<PointUser> users = pointUserDao.getAll();
        for (PointUser pointUser : users) {
            pointUser.setUserId(null);
            for (PointUser user : pointUsers) {
                if (user.getLogin().equals(pointUser.getLogin())) {
                    Assert.assertEquals(pointUser, user);
                }
            }
        }
    }

    @Test
    public void testUpdateUser() throws SQLException, DaoException {
        fillingData();

        PointUser pointUser = new PointUser();
        pointUser.setPoint(point);
        pointUser.setUserId(null);
        pointUser.setLogin("user4");
        pointUser.setSalt("salt8salt8salt88");
        pointUser.setPassAndSalt("pass8");
        String userRoleId1 = "W_BOSS";
        pointUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
        pointUser.setUserName("Kol");
        pointUser.setPhoneNumber("8-945-348-34-45");
        pointUser.setEmail("k@bk.ru");
        pointUser.setPosition("position4");
        pointUserDao.update(pointUser);
        JdbcUtil.getConnection().commit();

        Collection<PointUser> users = pointUserDao.getAll();
        for (AbstractUser user : users) {
            user.setUserId(null);
            if (user.getLogin().equals(pointUser.getLogin())) {
                Assert.assertEquals(user, pointUser);
            }
        }
    }

    @Test
    public void testGetByLogin() throws Exception {
        fillingData();
        PointUser user = pointUserDao.getByLogin("user5");
        user.setUserId(null);
        Assert.assertEquals(user, pointUser2);
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        fillingData();
        pointUserDao.deleteUserByLogin("user5");
        JdbcUtil.getConnection().commit();
        Collection<PointUser> users = pointUserDao.getAll();
        Assert.assertTrue(!users.contains(pointUser2));
        Assert.assertEquals(users.size(), 1);
    }
}