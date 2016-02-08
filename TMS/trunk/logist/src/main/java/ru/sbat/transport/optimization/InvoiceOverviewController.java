package ru.sbat.transport.optimization;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ru.sbat.transport.optimization.location.Point;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InvoiceOverviewController {
    private InvoiceContainer invoiceContainer;
    private PlannedSchedule plannedSchedule;
    private ObservableList<Invoice> invoiceObservableList;
    private Optimizer optimizer;

    public InvoiceOverviewController(InvoiceContainer invoiceContainer, PlannedSchedule plannedSchedule) {
        this.invoiceObservableList = FXCollections.observableArrayList(invoiceContainer);
        this.plannedSchedule = plannedSchedule;

        invoiceObservableList.addListener((ListChangeListener<Invoice>) c -> {
            c.next();
            if (c.wasAdded()) {
                List<? extends Invoice> addedSubList = c.getAddedSubList();
                List<Invoice> mySubList = new ArrayList<>(addedSubList);
                try {
                    optimizer.filtrate(plannedSchedule, mySubList);
                } catch (RouteNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(c.wasUpdated()){

            }

        });




    }

    public static void main(String[] args) {
        InvoiceContainer invoiceContainer = new InvoiceContainer();
        new InvoiceOverviewController(invoiceContainer, new PlannedSchedule());

        invoiceContainer.add(new Invoice());



    }
    public ObservableList<Invoice> getInvoiceObservableList() {
        return invoiceObservableList;
    }
}
