package ru.sbat.transport.optimization;


import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InvoiceContainer extends ArrayList<Invoice>{
    ActionListener actionListener;

    public void setOnRouteAssignedAction(ActionListener assignedAction){
        this.actionListener = assignedAction;

    }
}
