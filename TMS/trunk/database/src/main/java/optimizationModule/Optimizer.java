package optimizationModule;

import optimizationModule.schedule.AdditionalSchedule;
import optimizationModule.schedule.PlannedSchedule;
import optimizationModule.utils.InvoiceTypes;

import java.util.List;

public class Optimizer implements IOptimizer{

    @Override
    public void optimize(PlannedSchedule plannedSchedule, AdditionalSchedule additionalSchedule, List<Invoice> unassignedInvoices) {
        for(Invoice invoice: unassignedInvoices){
        InvoiceTypes inv = null;
            if(invoice.getRoute() == null) {
                int plannedDeliveryTime = invoice.getRequest().getPlannedDeliveryTime();
                int transitTimeOfPlannedMachine = invoice.getRoute().getTimeToNextArrival();
                int departureTime = invoice.getRoute().getDepartureTime();
                if(plannedDeliveryTime - transitTimeOfPlannedMachine < departureTime + 2){
                    inv = InvoiceTypes.B;
                }else if(plannedDeliveryTime - transitTimeOfPlannedMachine > departureTime + 2){
                    inv = InvoiceTypes.C;
                }else{

                }
        }
        }
    }
}
