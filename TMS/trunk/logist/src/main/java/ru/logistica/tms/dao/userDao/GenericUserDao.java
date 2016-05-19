package ru.logistica.tms.dao.userDao;

import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDao;

import java.io.Serializable;

public interface GenericUserDao<T, Id extends Serializable> extends GenericDao<T, Id> {
    T findByLogin(Class<T> clazz, String login) throws DAOException;
}
