package ru.logistica.tms.dao.utils;

import java.util.Collection;

public interface GenericDao<T> {
    T getById(Integer id) throws DaoException;
    Collection<T> getAll() throws DaoException;
    void deleteById(Integer id) throws DaoException;

    /**
     *
     * @param entity
     * @return primary key
     */
    Integer saveOrUpdate(T entity) throws DaoException;

    Integer save(T entity) throws DaoException;
    void update(T entity) throws DaoException;
}
