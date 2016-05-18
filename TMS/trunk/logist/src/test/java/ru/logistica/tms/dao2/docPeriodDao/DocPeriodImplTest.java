package ru.logistica.tms.dao2.docPeriodDao;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao2.HibernateUtils;
import ru.logistica.tms.dao2.docDao.Doc;
import ru.logistica.tms.dao2.docDao.DocDao;
import ru.logistica.tms.dao2.docDao.DocDaoImpl;
import ru.logistica.tms.dao2.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao2.warehouseDao.Warehouse;
import ru.logistica.tms.dao2.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao2.warehouseDao.WarehouseDaoImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DocPeriodImplTest extends HibernateTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'"); //ISO-8601
    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Doc doc;
    private Warehouse warehouse;
    DocPeriodDao docPeriodDao = new DocPeriodImpl();
    private Date firstPeriodBeginTimestamp;
    private Date firstPeriodEndTimestamp;
    private Date secondPeriodBeginTimestamp;
    private Date secondPeriodEndTimestamp;

    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();

        HibernateUtils.beginTransaction();
        warehouse = new Warehouse();
        warehouse.setWarehouseName("testWH");
        warehouse.setRusTimeZoneAbbr(RusTimeZoneAbbr.MSK);

        doc = new Doc();
        doc.setDocName("doc_name");
        doc.setWarehouse(warehouse);

        WarehouseDao warehouseDao = new WarehouseDaoImpl();
        warehouseDao.persist(warehouse);
        DocDao docDao = new DocDaoImpl();
        docDao.persist(doc);
        HibernateUtils.commitTransaction();
    }

    @Test
    public void testSave() throws Exception {
        HibernateUtils.beginTransaction();
        DocPeriod docPeriod = new DocPeriod();
        docPeriod.setDoc(doc);
        firstPeriodBeginTimestamp = dateFormat.parse("10-05-2016T20:00:00Z");
        System.out.println(firstPeriodBeginTimestamp);
        firstPeriodEndTimestamp = dateFormat.parse("10-05-2016T20:30:00Z");
        docPeriod.setPeriod(new Period(firstPeriodBeginTimestamp, firstPeriodEndTimestamp));
        Long id = docPeriodDao.save(docPeriod);
        Assert.assertTrue(id > 0);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testPersist() throws Exception {
        HibernateUtils.beginTransaction();
        DocPeriod docPeriod = new DocPeriod();
        docPeriod.setDoc(doc);
        secondPeriodBeginTimestamp = dateFormat.parse("10-05-2016T20:30:00Z");
        secondPeriodEndTimestamp = dateFormat.parse("10-05-2016T21:00:00Z");
        docPeriod.setPeriod(new Period(secondPeriodBeginTimestamp, secondPeriodEndTimestamp));
        docPeriodDao.persist(docPeriod);
        HibernateUtils.commitTransaction();
    }


    @Test(dependsOnMethods = {"testPersist"})
    public void testFindAllPeriodsBetweenTimeStamps() throws Exception {
        HibernateUtils.beginTransaction();
        List<DocPeriod> allPeriodsBetweenTimeStamps = docPeriodDao.findAllPeriodsBetweenTimeStamps(dateFormat.parse("10-05-2016T19:00:00Z"), dateFormat.parse("10-05-2016T22:00:00Z"));
        Assert.assertTrue(allPeriodsBetweenTimeStamps.size() == 2);
        allPeriodsBetweenTimeStamps = docPeriodDao.findAllPeriodsBetweenTimeStamps(dateFormat.parse("10-05-2016T20:00:00Z"), dateFormat.parse("10-05-2016T21:00:00Z"));
        Assert.assertTrue(firstPeriodBeginTimestamp.equals(allPeriodsBetweenTimeStamps.get(0).getPeriod().getPeriodBegin()));
        Assert.assertTrue(firstPeriodEndTimestamp.equals(allPeriodsBetweenTimeStamps.get(0).getPeriod().getPeriodEnd()));
        Assert.assertTrue(secondPeriodBeginTimestamp.equals(allPeriodsBetweenTimeStamps.get(1).getPeriod().getPeriodBegin()));
        Assert.assertTrue(secondPeriodEndTimestamp.equals(allPeriodsBetweenTimeStamps.get(1).getPeriod().getPeriodEnd()));
        HibernateUtils.commitTransaction();
    }

    @Test(enabled = false)
    public void testUpdate() throws Exception {

    }

    @Test(enabled = false)
    public void testDelete() throws Exception {

    }


    @Test(enabled = false)
    public void testFindAll() throws Exception {

    }

    @Test(enabled = false)
    public void testFindById() throws Exception {

    }
}