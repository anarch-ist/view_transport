package ru.sbat.transport.optimization;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ru.sbat.transport.optimization.location.Route;
import ru.sbat.transport.optimization.optimazerException.RouteNotFoundException;
import ru.sbat.transport.optimization.schedule.PlannedSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
                        try {
                            System.out.println("Элемент добавлен");
                            Map<Invoice, ArrayList<Route>> routesForAddedInvoices = optimizer.filtrate(plannedSchedule, invoiceContainer);
                        } catch (RouteNotFoundException e) {
                            e.printStackTrace();
                        }
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