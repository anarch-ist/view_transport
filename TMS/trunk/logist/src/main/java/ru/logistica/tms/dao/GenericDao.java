package ru.logistica.tms.dao;


import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, Id extends Serializable> {

    /**
     * sync save data into database
     *
     * @param entity hibernate entity
     * @return primary key for saved object
     */
    Id save(T entity) throws DAOException;

    void persist(T entity) throws DAOException;

    /**
     * entity become managed and invoking any setter reflected in database
     *
     * @param entity hibernate entity
     */
    void update(T entity) throws DAOException;

    void delete(T entity) throws DAOException;

    List<T> findAll(Class<T> clazz) throws DAOException;

    T findById(Class<T> clazz, Id id) throws DAOException;


}
