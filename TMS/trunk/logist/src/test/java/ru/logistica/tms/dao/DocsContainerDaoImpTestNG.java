package ru.logistica.tms.dao;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class DocsContainerDaoImpTestNG {

    @BeforeClass
    public void setUp() throws SQLException {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, user, password);

        JdbcUtil.setConnection(connection);
    }

    @AfterClass
    public void tearDown() throws SQLException {
        JdbcUtil.getConnection().close();
    }
//(dependsOnMethods = {"testSave"})
    @Test
    public void testGetAll() throws Exception {
        DocsContainerDao docsContainerDao = new DocsContainerDaoImp();

        DocsContainer docsContainer = new DocsContainer();
        docsContainer.setContainerId(1);
        docsContainer.setDocState(DocState.FREE);
        docsContainer.setTimeDiffId(10);
        docsContainer.setDate(new Date(2016, 2, 12));
        docsContainer.setDocId(1);

        docsContainerDao.save(docsContainer);

        Set<DocsContainer> sets = docsContainerDao.getAll();
        Iterator iterator = sets.iterator();
        DocsContainer docsContainerForCompare = null;
        while (iterator.hasNext()){
            docsContainerForCompare = (DocsContainer) iterator.next();
        }

        Assert.assertEquals(docsContainerForCompare.getContainerId(), docsContainer.getContainerId());
        Assert.assertEquals(docsContainerForCompare.getDocId(), docsContainer.getDocId());
        Assert.assertEquals(docsContainerForCompare.getDate(), docsContainer.getDate());
        Assert.assertEquals(docsContainerForCompare.getTimeDiffId(), docsContainer.getTimeDiffId());
        Assert.assertEquals(docsContainerForCompare.getDocState(), docsContainer.getDocState());

    }

    @Test
    public void testSave() throws Exception {
        DocsContainerDao docsContainerDao = new DocsContainerDaoImp();

        DocsContainer docsContainer = new DocsContainer();
        docsContainer.setContainerId(1);
        docsContainer.setDocState(DocState.FREE);
        docsContainer.setTimeDiffId(10);
        docsContainer.setDate(new Date(2016, 2, 12));
        docsContainer.setDocId(1);

        docsContainerDao.save(docsContainer);

    }

    @Test
    public void testGetById() throws SQLException {
        DocsContainerDao docsContainerDao = new DocsContainerDaoImp();

        DocsContainer docsContainer1 = new DocsContainer();
        docsContainer1.setContainerId(1);
        docsContainer1.setDocState(DocState.FREE);
        docsContainer1.setTimeDiffId(10);
        docsContainer1.setDate(new Date(2016, 2, 12));
        docsContainer1.setDocId(1);

        DocsContainer docsContainer2 = new DocsContainer();
        docsContainer2.setContainerId(2);
        docsContainer2.setDocState(DocState.OCCUPIED);
        docsContainer2.setTimeDiffId(15);
        docsContainer2.setDate(new Date(2016, 3, 12));
        docsContainer2.setDocId(1);

        docsContainerDao.save(docsContainer1);
        docsContainerDao.save(docsContainer2);
        DocsContainer docsContainerReceived = docsContainerDao.getById(2);

        Assert.assertEquals(docsContainerReceived.getContainerId(), docsContainer2.getContainerId());
        Assert.assertEquals(docsContainerReceived.getDocId(), docsContainer2.getDocId());
        Assert.assertEquals(docsContainerReceived.getDate(), docsContainer2.getDate());
        Assert.assertEquals(docsContainerReceived.getTimeDiffId(), docsContainer2.getTimeDiffId());
        Assert.assertEquals(docsContainerReceived.getDocState(), docsContainer2.getDocState());

    }
}
