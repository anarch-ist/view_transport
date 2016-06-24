package ru.logistica.tms.dao;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.TestUtil;

public class jdbcRecrTest {

    @BeforeClass
    public void setUp() throws Exception {

    }

    @Test
    public void testName() throws Exception {
        TestUtil.splitSqlFile();

    }

    @AfterClass
    public void tearDown() throws Exception {

    }
}
