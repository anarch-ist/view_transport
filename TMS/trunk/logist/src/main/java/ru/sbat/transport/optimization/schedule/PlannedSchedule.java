package ru.sbat.transport.optimization.schedule;


import ru.sbat.transport.optimization.InvoiceContainer;

import java.util.Observable;
import java.util.Observer;


public class PlannedSchedule extends AbstractSchedule implements Observer{
    private InvoiceContainer invoiceContainer;

    @Override
    public void update(Observable o, Object arg) {

    }
}
