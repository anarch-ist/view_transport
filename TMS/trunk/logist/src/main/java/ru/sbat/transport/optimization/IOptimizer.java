package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.location.DeliveryRoute;
import ru.sbat.transport.optimization.optimazerException.IncorrectRequirement;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IOptimizer {

    Map<Invoice, List<DeliveryRoute>> filtrate(PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer) throws RouteNotFoundException;

    void optimize(PlannedSchedule plannedSchedule, Map<Invoice, List<DeliveryRoute>> routesForInvoice) throws ParseException, RouteNotFoundException, IncorrectRequirement;

    List<Invoice> shift (PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, List<DeliveryRoute>> routesForInvoice);

    Map<Invoice, List<DeliveryRoute>> useAdditionalSchedule(AdditionalSchedule additionalSchedule, PlannedSchedule plannedSchedule, InvoiceContainer invoiceContainer, Map<Invoice, List<DeliveryRoute>> routesForInvoice) throws RouteNotFoundException;
}
