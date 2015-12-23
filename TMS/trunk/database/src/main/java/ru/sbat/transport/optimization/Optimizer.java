package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Optimizer implements IOptimizer{

    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.ENGLISH);
        for(Invoice invoice: unassignedInvoices){
        InvoiceTypes inv = null;
            if(invoice.getRoute() == null) {
//                Date departureTime = format.parse(invoice.getRoute().getTrackCourse().getDepartureTime());
//                Date deliveryTime = format.parse(invoice.getRequest().getPlannedDeliveryTime());
//                Calendar calendar = Calendar.getInstance();
//                int departureDayOfWeek = calendar.setTime(deliveryTime);
        }
        }
    }
}
