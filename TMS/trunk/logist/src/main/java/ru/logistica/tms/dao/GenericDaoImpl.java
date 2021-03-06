package ru.logistica.tms.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public abstract class GenericDaoImpl<T, Id extends Serializable> implements GenericDao<T, Id> {

    protected Session getSession() {
        return HibernateUtils.getCurrentSession();
    }

    @Override
    public void update(T entity) throws DAOException {
        try {
            getSession().update(entity);
        }
        catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(T entity) throws DAOException {
        try {
            getSession().delete(entity);
        }
        catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }

    }

    @Override
    public Id save(T entity) throws DAOException {
        try {
            return (Id)getSession().save(entity);
        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public void persist(T entity) throws DAOException {
        try {
            getSession().persist(entity);
        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> findAll(Class<T> clazz) throws DAOException {
        try {
            Session hibernateSession = this.getSession();
            Query query = hibernateSession.createQuery("from " + clazz.getName());
            return query.list();
        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }

    }

    @Override
    public T findById(Class<T> clazz, Id id) throws DAOException {
        try {
            Session hibernateSession = this.getSession();
            T result = (T) hibernateSession.get(clazz, id);
            if (result == null)
                throw new OutOfDateException();
            return result;
        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }

    }

}
