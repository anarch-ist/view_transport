package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDao;

import java.io.Serializable;

public interface GenericUserDao<T, Id extends Serializable> extends GenericDao {
    T findByLogin(Class<T> clazz, String login) throws DAOException;
}
