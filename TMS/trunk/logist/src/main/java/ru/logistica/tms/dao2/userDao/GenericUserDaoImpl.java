package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDaoImpl;

import java.io.Serializable;

public class GenericUserDaoImpl<T, Id extends Serializable> extends GenericDaoImpl implements GenericUserDao<T, Id> {

    @Override
    public T findByLogin(Class<T> clazz, String login) throws DAOException {
        try {
            return getSession().bySimpleNaturalId(clazz).load(login);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
