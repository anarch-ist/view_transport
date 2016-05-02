package ru.logist.sbat.jsonToBean.jsonReader;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import ru.logist.sbat.jsonToBean.beans.StatusData;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class UtilTest {

    @Test(expectedExceptions = ValidatorException.class)
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