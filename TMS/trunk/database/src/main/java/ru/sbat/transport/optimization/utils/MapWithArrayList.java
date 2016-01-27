package ru.sbat.transport.optimization.utils;

import ru.sbat.transport.optimization.Invoice;
import ru.sbat.transport.optimization.location.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class MapWithArrayList implements Map<Invoice, ArrayList<Route>>{
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public ArrayList<Route> get(Object key) {
        return null;
    }

    @Override
    public ArrayList<Route> put(Invoice key, ArrayList<Route> value) {
        return null;
    }

    @Override
    public ArrayList<Route> remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Invoice, ? extends ArrayList<Route>> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Invoice> keySet() {
        return null;
    }

    @Override
    public Collection<ArrayList<Route>> values() {
        return null;
    }

    @Override
    public Set<Entry<Invoice, ArrayList<Route>>> entrySet() {
        return null;
    }
}
