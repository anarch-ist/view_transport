package ru.logistica.tms.dao.docsContainerDao;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.ConnectionManager;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

@Test(singleThreaded = true)
public class DocsContainerDaoImpTest {
    private static DocsContainerDao docsContainerDao = new DocsContainerDaoImp();
    private static DocsContainer docsContainer1;
    private static DocsContainer docsContainer2;

    @BeforeClass
    public static void setUp() throws SQLException, URISyntaxException {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        ConnectionManager.getConnection().close();
    }

    @Test
    public void testSave() throws Exception {
        DocsContainerDao docsContainerDao = new DocsContainerDaoImp();

        docsContainer1 = new DocsContainer();
        docsContainer1.setContainerId(1);
        docsContainer1.setDocState(DocState.FREE);
        docsContainer1.setTimeDiffId(10);
        docsContainer1.setDate(new Date(2016, 2, 12));
        docsContainer1.setDocId(1);

        docsContainer2 = new DocsContainer();
        docsContainer2.setContainerId(2);
        docsContainer2.setDocState(DocState.OCCUPIED);
        docsContainer2.setTimeDiffId(15);
        docsContainer2.setDate(new Date(2016, 3, 12));
        docsContainer2.setDocId(1);

        docsContainerDao.save(docsContainer1);
        docsContainerDao.save(docsContainer2);
        ConnectionManager.getConnection().commit();
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testGetById() throws Exception {
        DocsContainer docsContainerReceived = docsContainerDao.getById(2);
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(docsContainerReceived.getContainerId(), docsContainer2.getContainerId());
        Assert.assertEquals(docsContainerReceived.getDocId(), docsContainer2.getDocId());
        Assert.assertEquals(docsContainerReceived.getDate(), docsContainer2.getDate());
        Assert.assertEquals(docsContainerReceived.getTimeDiffId(), docsContainer2.getTimeDiffId());
        Assert.assertEquals(docsContainerReceived.getDocState(), docsContainer2.getDocState());
    }

    @Test(dependsOnMethods = {"testGetById"})
    public void testGetAll() throws Exception {
        Set<DocsContainer> sets = docsContainerDao.getAll();
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(sets.size(), 2);
    }

}
