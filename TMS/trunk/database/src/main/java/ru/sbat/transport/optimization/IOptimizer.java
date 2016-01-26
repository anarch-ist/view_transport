package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IOptimizer {

    /**
     * mutable method, set routes for unassigned invoices.
     * @param plannedSchedule
     * @param additionalSchedule
     * @param unassignedInvoices
     * @throws ParseException
     */
    void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException, RouteNotFoundException;

    InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices);

    void filtrate(PlannedSchedule plannedSchedule, List<Invoice> unassignedInvoices) throws RouteNotFoundException;

    Date[] getPossibleDepartureDate(Route route, Invoice invoice);

    boolean isFittingForDeliveryTime(Route route, Invoice invoice, Date date);
}
