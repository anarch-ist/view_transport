package ru.sbat.transport.optimization;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;


public class InvoiceOverviewController {
    private InvoiceContainer invoiceContainer;
    private PlannedSchedule plannedSchedule;

    ObservableList<Invoice> observableListInvoice = FXCollections.observableArrayList(invoiceContainer);

    public void onChanged(ListChangeListener.Change change) {
        System.out.println("Detected a change! ");
        while (change.next()) {
        }
    }
}
