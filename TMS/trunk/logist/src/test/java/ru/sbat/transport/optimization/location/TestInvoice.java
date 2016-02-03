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
        calendar.set(2016, 1, 2, 17, 10);
        Date date = calendar.getTime();
        invoice.setCreationDate(date);
        Assert.assertEquals(3, invoice.getWeekDay());
    }
}
