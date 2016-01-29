package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.Invoice;

import java.util.Calendar;
import java.util.Date;

public class TestInvoice {
    @Test
    public void testGetWeekDay(){
        Invoice invoice = new Invoice();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 10);
        Date date = calendar.getTime();
        invoice.setCreationDate(date);
        Assert.assertEquals(3, invoice.getWeekDay());
    }
}
