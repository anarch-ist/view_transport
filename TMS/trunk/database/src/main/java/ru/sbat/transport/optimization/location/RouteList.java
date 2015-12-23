package ru.sbat.transport.optimization.location;

import ru.sbat.transport.optimization.Invoice;

import java.util.ArrayList;
import java.util.Set;


public class RouteList extends ArrayList{
    Set<Invoice> routePoints;
    String nameDriver;
    String carNumber;
}
