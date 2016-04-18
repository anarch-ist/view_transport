package ru.logistica.tms.dao.usersDao;

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
    private static GenericUserDao pointUserDao = new PointUserDaoImpl();
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
            pointUser1.setUserId(4);
            pointUser1.setLogin("user4");
            pointUser1.setSalt("salt4salt4salt44");
            pointUser1.setPassAndSalt("pass4");
            String userRoleId1 = "W_BOSS";
            pointUser1.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId1));
            pointUser1.setUserName("Kolya");
            pointUser1.setPhoneNumber("8-945-348-34-45");
            pointUser1.setEmail("k@bl.ru");
            pointUser1.setPosition("position4");
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
            pointUserDao.saveOrUpdateUser(pointUser1);
            JdbcUtil.getConnection().commit();

            Set<PointUser> users = pointUserDao.getAllUsers();
            Iterator<PointUser> iterator = users.iterator();
            while (iterator.hasNext()){
                PointUser pointUser = iterator.next();
                if(pointUser.getUserId().equals(pointUser1.getUserId())){
//                    Assert.assertEquals(pointUser1, pointUser);
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