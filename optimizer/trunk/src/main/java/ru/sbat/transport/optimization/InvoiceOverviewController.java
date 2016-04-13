package ru.sbat.transport.optimization;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;


import java.util.List;



public class InvoiceOverviewController {
    private PlannedSchedule plannedSchedule;
    private ObservableList<Invoice> invoiceObservableList;
    private Optimizer optimizer;

    public InvoiceOverviewController(InvoiceContainer invoiceContainer, PlannedSchedule plannedSchedule) {
        this.invoiceObservableList = FXCollections.observableArrayList(invoiceContainer);
        this.plannedSchedule = plannedSchedule;

        invoiceObservableList.addListener(new ListChangeListener<Invoice>() {
            @Override
            public void onChanged(Change<? extends Invoice> c) {
                while (c.next()){
                    if(c.wasAdded()){
                        List<? extends Invoice> addedSubList = c.getAddedSubList();
                        InvoiceContainer invoiceContainer = new InvoiceContainer();
                        invoiceContainer.addAll(addedSubList);
//
                    }else if(c.wasUpdated()){

                    }else if(c.wasRemoved()){

                    }
                }
            }
        });
    }

    public ObservableList<Invoice> getInvoiceObservableList() {
        return invoiceObservableList;
    }
}