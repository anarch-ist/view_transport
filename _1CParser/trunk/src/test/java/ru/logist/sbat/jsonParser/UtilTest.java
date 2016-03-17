package ru.logist.sbat.jsonParser;

import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testGetParameterizedString() throws Exception {
        String result1 = Util.getParameterizedString("bla{},{}blabla  {}", "par1", "par2", "par3");
        Assert.assertEquals("bla[par1],[par2]blabla  [par3]", result1);

        String result2 = Util.getParameterizedString("bla{}", "par1");
        Assert.assertEquals("bla[par1]", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParameterizedStringIllegalArg() throws Exception {
        Util.getParameterizedString("bla{},{}blabla  {}", "par1");
    }

    @Test(expected = ValidatorException.class)
    public void checkIllegalTypeTest() {
        Util.checkCorrectType("ed", Long.class, new JSONObject());
    }

    @Test
    public void checkCorrectTypeTest() {
        Util.checkCorrectType(1L, Long.class, new JSONObject());
    }
}