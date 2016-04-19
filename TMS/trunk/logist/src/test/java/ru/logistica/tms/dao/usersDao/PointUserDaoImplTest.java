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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class PointUserDaoImplTest {
    private static KeyDifferenceDao<Point> keyDifferenceDao = new PointDaoImpl();
    private static Point point = new Point();
    private static PointUser pointUser1 = new PointUser();
    private static PointUser pointUser2 = new PointUser();
    private static List<PointUser> pointUsers = new ArrayList<>();
    private static GenericUserDao<PointUser> pointUserDao = new PointUserDaoImpl();

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

    public void fillingData() throws SQLException {
        try{
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
            keyDifferenceDao.saveOrUpdateKeyDifference(point);

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

            pointUserDao.saveOrUpdateUser(pointUser1);
            pointUserDao.saveOrUpdateUser(pointUser2);
            JdbcUtil.getConnection().commit();
        }catch (SQLException e){
            JdbcUtil.rollbackQuietly();
        }
    }


    @Test
    public void testGetAllUsers() throws Exception {
        try {
            fillingData();
            Set<PointUser> users = pointUserDao.getAllUsers();
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
            PointUser user = pointUserDao.getUserById(1);
            user.setUserId(null);
            Assert.assertEquals(user, pointUser1);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testSaveOrUpdateUser() throws Exception {
        try {
            fillingData();

            pointUserDao.saveOrUpdateUser(pointUser1);
            pointUserDao.saveOrUpdateUser(pointUser2);
            JdbcUtil.getConnection().commit();

            Set<PointUser> users = pointUserDao.getAllUsers();
            Iterator<PointUser> iterator = users.iterator();
            while (iterator.hasNext()){
                PointUser pointUser = iterator.next();
                pointUser.setUserId(null);
                for(PointUser user: pointUsers){
                    if(user.getLogin().equals(pointUser.getLogin())){
                        Assert.assertEquals(pointUser, user);
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
            PointUser user = pointUserDao.getByLogin("user5");
            user.setUserId(null);
            Assert.assertEquals(user, pointUser2);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }

    @Test
    public void testDeleteUserByLogin() throws Exception {
        try {
            fillingData();
            pointUserDao.deleteUserByLogin("user5");
            JdbcUtil.getConnection().commit();
            Set<PointUser> users = pointUserDao.getAllUsers();
            System.out.println(users.size());
            Assert.assertTrue(!users.contains(pointUser2));
            Assert.assertEquals(users.size(), 1);
        }catch (SQLException e){
            e.printStackTrace();
            JdbcUtil.rollbackQuietly();
        }
    }
}