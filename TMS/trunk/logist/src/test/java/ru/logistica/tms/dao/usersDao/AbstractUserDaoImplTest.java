package ru.logistica.tms.dao.usersDao;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.testng.Assert.*;


public class AbstractUserDaoImplTest {
    private static ConstantCollections constantCollections = new ConstantCollections();
    private static ConstantsDao constantsDao = new ConstantsDaoImpl();

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

    }

    @Test
    public void testGetUserById() throws Exception {

    }

    @Test
    public void testSaveOrUpdateUser() throws Exception {

    }

    @Test
    public void testGetByLogin() throws Exception {

    }

    @Test
    public void testDeleteUserByLogin() throws Exception {

    }
}