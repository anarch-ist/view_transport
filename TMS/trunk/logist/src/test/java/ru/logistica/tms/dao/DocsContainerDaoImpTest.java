package ru.logistica.tms.dao;

import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import static org.junit.Assert.*;


public class DocsContainerDaoImpTest {

    @BeforeClass
    public static void setUp() throws Exception {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);



    }

    @AfterClass
    public static void tearDown() throws Exception {
        JdbcUtil.getConnection().close();
    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void getById() throws Exception {

    }

    @Test
    public void save() throws Exception {
        DocsContainerDao docsContainerDao = new DocsContainerDaoImp();

        DocsContainer docsContainer = new DocsContainer();
        docsContainer.setContainerId(1);
        docsContainer.setDocState(DocState.FREE);
        docsContainer.setTimeDiffId(10);
        docsContainer.setDate(new Date(2016, 2, 12, 15, 0));
        docsContainer.setDocId(1);

        docsContainerDao.save(docsContainer);

    }
}