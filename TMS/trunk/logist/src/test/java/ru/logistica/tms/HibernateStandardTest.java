package ru.logistica.tms;

import org.testng.annotations.Test;
import ru.logistica.tms.dao.HibernateUtils;

public abstract class HibernateStandardTest extends HibernateTest {
    @Test
    public void testSave() throws Exception {
        HibernateUtils.beginTransaction();
        testSaveImpl();
        HibernateUtils.commitTransaction();
    }

    protected abstract void testSaveImpl() throws Exception;

    @Test(dependsOnMethods = {"testSave"})
    public void testFindById() throws Exception {
        HibernateUtils.beginTransaction();
        testFindByIdImpl();
        HibernateUtils.commitTransaction();
    }

    protected abstract void testFindByIdImpl() throws Exception;

    @Test(dependsOnMethods = {"testFindById"})
    public void testUpdate() throws Exception {
        HibernateUtils.beginTransaction();
        testUpdateImpl();
        HibernateUtils.commitTransaction();
    }

    protected abstract void testUpdateImpl() throws Exception;

    @Test(dependsOnMethods = {"testUpdate"})
    public void testFindAll() throws Exception {
        HibernateUtils.beginTransaction();
        testFindAllImpl();
        HibernateUtils.commitTransaction();
    }

    protected abstract void testFindAllImpl() throws Exception;

    @Test(dependsOnMethods = {"testFindAll"})
    public void testDelete() throws Exception {
        HibernateUtils.beginTransaction();
        testDeleteImpl();
        HibernateUtils.commitTransaction();
    }

    protected abstract void testDeleteImpl() throws Exception;

}
