package ru.logist.sbat.jsonParser;

import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.junit.Test;
import ru.logist.sbat.jsonParser.beans.StatusData;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

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
    public void checkIllegalTypeTest() throws ValidatorException {
        Util.checkCorrectType("ed", Long.class, new JSONObject());
    }

    @Test
    public void checkCorrectTypeTest() throws ValidatorException {
        Util.checkCorrectType(1L, Long.class, new JSONObject());
    }

    @Test
    public void getDateTimeTest() throws Exception {
        Timestamp timestamp = Util.getDateTime(StatusData.timeOutStatusFormatter, "05.04.16,12:45:03");
        System.out.println(timestamp);
    }

    @Test
    public void getDateTest() throws Exception {
        Date date = Util.getDate(DateTimeFormatter.BASIC_ISO_DATE, "20160405");
        System.out.println(date);
    }
}