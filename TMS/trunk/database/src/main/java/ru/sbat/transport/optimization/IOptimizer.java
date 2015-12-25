package ru.sbat.transport.optimization;

import ru.sbat.transport.optimization.schedule.AdditionalSchedule;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;
import ru.sbat.transport.optimization.utils.InvoiceTypes;

import java.text.ParseException;
import java.util.List;

public interface IOptimizer {

    void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) throws ParseException;

    InvoiceTypes getInvoiceTypes(List<Invoice> unassignedInvoices);


}
