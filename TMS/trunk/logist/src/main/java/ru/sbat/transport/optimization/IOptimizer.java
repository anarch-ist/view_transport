package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOptimizer {

    /**
     * mutable method, set routes for unassigned invoices.
     * @param plannedSchedule
     * @param additionalSchedule
     * @param invoiceContainer
     * @throws ParseException
     */
    void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, InvoiceContainer invoiceContainer) throws ParseException, RouteNotFoundException;

    InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices);

    Map<Invoice, ArrayList<Route>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException;

    ArrayList<Date> getPossibleDepartureDate(Route route, Invoice invoice);

    Date getPossibleArrivalDate(Route route, Invoice invoice, Date date);

    boolean isFittingForDeliveryTime(Route route, Invoice invoice, Date date);
}
