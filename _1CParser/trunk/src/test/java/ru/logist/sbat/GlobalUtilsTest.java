package ru.logist.sbat;

import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class GlobalUtilsTest {
    @Test
    public void testGetParameterizedString() throws Exception {
        String result1 = GlobalUtils.getParameterizedString("bla{},{}blabla  {}", "par1", "par2", "par3");
        Assert.assertEquals("bla[par1],[par2]blabla  [par3]", result1);

        String result2 = GlobalUtils.getParameterizedString("bla{}", "par1");
        Assert.assertEquals("bla[par1]", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParameterizedStringIllegalArg() throws Exception {
        GlobalUtils.getParameterizedString("bla{},{}blabla  {}", "par1");
    }

    @Test
    public void testParameterizedStringBigData() throws Exception {
        String result = GlobalUtils.getParameterizedString("bla{}", RandomStringUtils.random(10000));
        Assert.assertTrue(result.length() < (GlobalUtils.MAX_SYMBOLS_IN_PARAM_STRING + 20));
    }

}