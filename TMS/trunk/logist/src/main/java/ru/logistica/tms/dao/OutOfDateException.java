package ru.logistica.tms.dao;

public class OutOfDateException extends DAOException {
    public OutOfDateException() {
        super("Данные изменились, операция не актуальна на выбранных данных.");
    }
}
