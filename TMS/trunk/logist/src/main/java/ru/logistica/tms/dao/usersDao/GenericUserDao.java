package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;

public interface GenericUserDao<T> extends GenericDao<T>{
    T getByLogin(String login) throws DaoException;
    void deleteUserByLogin(String login) throws DaoException;
}
