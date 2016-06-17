package ru.logistica.tms.dao;

public class DaoScriptException extends Throwable {
    public DaoScriptException() {
    }

    public DaoScriptException(String message) {
        super(message);
    }

    public DaoScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoScriptException(Throwable cause) {
        super(cause);
    }
}
