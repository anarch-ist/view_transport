package ru.logistica.tms.dao.docPeriodDao;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao.HibernateUtils;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docDao.DocDao;
import ru.logistica.tms.dao.docDao.DocDaoImpl;
import ru.logistica.tms.dao.supplierDao.Supplier;
import ru.logistica.tms.dao.supplierDao.SupplierDao;
import ru.logistica.tms.dao.supplierDao.SupplierDaoImpl;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;
import ru.logistica.tms.util.UtcSimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DocPeriodImplTest extends HibernateTest {
    private static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'"); //ISO-8601


    private Doc doc;
    private Warehouse warehouse;
    private Supplier supplier;
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
        SupplierDao supplierDao = new SupplierDaoImpl();
        supplier = new Supplier();
        supplier.setInn("erferf");
        Integer supplierId = supplierDao.save(supplier);
        this.supplier = supplierDao.findById(Supplier.class, supplierId);
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
        firstPeriodEndTimestamp = dateFormat.parse("10-05-2016T20:30:00Z");
        docPeriod.setPeriod(new Period(firstPeriodBeginTimestamp, firstPeriodEndTimestamp));
        Long id = docPeriodDao.save(docPeriod);
        Assert.assertTrue(id > 0);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testPersist() throws Exception {
        HibernateUtils.beginTransaction();
        DonutDocPeriod donutDocPeriod = new DonutDocPeriod();
        donutDocPeriod.setDoc(doc);
        secondPeriodBeginTimestamp = dateFormat.parse("10-05-2016T20:30:00Z");
        secondPeriodEndTimestamp = dateFormat.parse("10-05-2016T21:00:00Z");
        donutDocPeriod.setPeriod(new Period(secondPeriodBeginTimestamp, secondPeriodEndTimestamp));
        donutDocPeriod.setComment("fffu");
        donutDocPeriod.setCreationDate(new Date());
        donutDocPeriod.setDriver("driver");
        donutDocPeriod.setDriverPhoneNumber("dr_phobe");
        donutDocPeriod.setLicensePlate("lic_pl");
        donutDocPeriod.setPalletsQty((short)3);
        donutDocPeriod.setSupplier(supplier);
        docPeriodDao.persist(donutDocPeriod);
        HibernateUtils.commitTransaction();
    }


    @Test(dependsOnMethods = {"testPersist"})
    public void testFindAllPeriodsBetweenTimeStampsForDoc() throws Exception {
        HibernateUtils.beginTransaction();
        List<DocPeriod> allPeriodsBetweenTimeStamps = docPeriodDao.findAllPeriodsBetweenTimeStampsForDoc(doc.getDocId(), dateFormat.parse("10-05-2016T19:00:00Z"), dateFormat.parse("10-05-2016T22:00:00Z"));
        Assert.assertTrue(allPeriodsBetweenTimeStamps.size() == 2);
        allPeriodsBetweenTimeStamps = docPeriodDao.findAllPeriodsBetweenTimeStampsForDoc(doc.getDocId(), dateFormat.parse("10-05-2016T20:00:00Z"), dateFormat.parse("10-05-2016T21:00:00Z"));
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