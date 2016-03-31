package ru.sbat.transport.optimization.location;

import org.junit.*;
import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.InvoiceContainer;
import ru.sbat.transport.optimization.Request;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestInvoice {
    static InvoiceContainer invoiceContainer = new InvoiceContainer();
    static TradeRepresentativePoint z = new TradeRepresentativePoint("Z");
    static WarehousePoint c = new WarehousePoint("C");

    @BeforeClass
    public static void fullInvoiceContainer(){
        Request request = Util.createRequest(z,  Util.createDate(2016, Calendar.MARCH, 20, 20,  45));
        Invoice invoice = Util.createInvoice(c,  Util.createDate(2016, Calendar.MARCH, 5, 12, 0),     request, 5);
        invoiceContainer.add(invoice);
    }

    @Test
    public void testGetWeekDay(){
        Invoice invoice = new Invoice();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 1, 2, 17, 10);
        Date date = calendar.getTime();
        invoice.setCreationDate(date);
        Assert.assertEquals(3, invoice.getWeekDay());
    }

    @Test
    public void testCalculateWeekDays(){
        List<Integer> numberOfWeek = invoiceContainer.get(0).calculateNumbersOfWeeksBetweenDates(invoiceContainer.get(0).getCreationDate(), invoiceContainer.get(0).getRequest().getPlannedDeliveryDate());
        Assert.assertEquals(10, numberOfWeek.get(0), 0);
        Assert.assertEquals(11, numberOfWeek.get(1), 0);
        Assert.assertEquals(12, numberOfWeek.get(2), 0);
    }
}
