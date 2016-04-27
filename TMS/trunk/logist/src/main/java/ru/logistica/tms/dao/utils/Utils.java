package ru.logistica.tms.dao.utils;

import ru.logistica.tms.dao.DaoException;

public class Utils {
    public static void runWithExceptionRedirect(Exec exec) throws DaoException {
        try {
            exec.execute();
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public interface Exec {
        void execute() throws Exception;
    }
}
