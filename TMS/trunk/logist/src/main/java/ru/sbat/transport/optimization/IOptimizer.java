package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOptimizer {

    /**
     * mutable method, set routes for unassigned invoices.
     * @param plannedSchedule
//     * @param additionalSchedule
     * @param invoiceContainer
     * @throws ParseException
     */
    void optimize(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<Route>> routesForInvoice) throws ParseException, RouteNotFoundException;

    InvoiceType getInvoiceTypes(List<Invoice> unassignedInvoices);

    Map<Invoice, ArrayList<DeliveryRoute>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException;

    ArrayList<Date> getPossibleDepartureDate(Route route, Invoice invoice);

    Date getPossibleArrivalDate(Route route, Invoice invoice, Date date);

    boolean isFittingForDeliveryTime(Route route, Invoice invoice, Date date);

    Map<RoutePoint, Date> getArrivalDateInEachRoutePointInRoute(Route route, Invoice invoice);

    ArrayList<DeliveryRoute> getDeliveryRoute(RoutePoint routePoint, int index, Route route, Invoice invoice, PlannedSchedule plannedSchedule);
}
