package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.location.RoutePoint;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface IOptimizer {

    Map<Invoice, ArrayList<DeliveryRoute>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException;

    ArrayList<DeliveryRoute> getDeliveryRoute(Invoice invoice, PlannedSchedule plannedSchedule, Point point) throws RouteNotFoundException;

    void optimize(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice) throws ParseException, RouteNotFoundException;

    ArrayList<Invoice> shift (PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice);

    Map<Invoice, ArrayList<DeliveryRoute>> useAdditionalSchedule(AdditionalSchedule additionalSchedule, PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, ArrayList<DeliveryRoute>> routesForInvoice) throws RouteNotFoundException;

    ArrayList<Date> getPossibleDepartureDateFromRoutePoint(Route route, RoutePoint routePoint, Invoice invoice);

    Map<RoutePoint, ArrayList<Date>> getArrivalDateInEachRoutePointInRoute(Route route, Invoice invoice);

    ArrayList<PairDate> getDepartureArrivalDatesBetweenTwoRoutePoints(Route departureRoute, Route arrivalRoute, Invoice invoice, RoutePoint departureRoutePoint, RoutePoint arrivalRoutePoint);
}
