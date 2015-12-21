package optimizationModule;

import optimizationModule.schedule.AbstractSchedule;
import optimizationModule.schedule.AdditionalSchedule;
import optimizationModule.schedule.PlannedSchedule;

import java.util.AbstractList;
import java.util.List;

public interface IOptimizer {

    void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices);

}
