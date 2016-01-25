package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.Invoice;

import java.util.Date;

public class TestInvoice {
    @Test
    public void testGetWeekDay(){
        Invoice invoice = new Invoice();
        invoice.setCreationDate(new Date());
        Assert.assertEquals(2, invoice.getWeekDay());
    }
}
