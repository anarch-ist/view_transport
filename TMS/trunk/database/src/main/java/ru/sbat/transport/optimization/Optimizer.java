package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.List;

public class Optimizer implements IOptimizer{

    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException {

    }

    @Override
    public InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices) {
        InvoiceTypes invoiceTypes = null;
        for(Invoice invoice: unassignedInvoices){

            if(invoice.getRoute() == null) {
                if(invoice.getRoute().getArrivalTime() < invoice.getRequest().getPlannedDeliveryTime() && (invoice.getRoute().get(invoice.getRoute().size()-1).getDayOfWeek().equals(invoice.getRequest().getDayOfWeek() || )){

                }else if(invoice.getRoute().getArrivalTime() > invoice.getRequest().getPlannedDeliveryTime()){

                }
            }
        }
        return invoiceTypes;
    }
}
