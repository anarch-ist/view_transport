package ru.sbat.transport.optimization;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class InvoiceContainer  extends ArrayList<Invoice>{
    private ObservableList<Invoice> invoiceObservableList = FXCollections.observableArrayList();

}
