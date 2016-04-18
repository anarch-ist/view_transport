package ru.logistica.tms.dao;


import java.sql.SQLException;

public interface KeyDifferenceDao<T extends KeyDifference> {
    T getKeyDifferenceById(Integer id) throws SQLException;
    Integer saveOrUpdateKeyDifference(T user) throws SQLException;
}
