package ru.logistica.tms.dao;



import java.sql.SQLException;
import java.util.Set;

public interface DocsContainerDao {
    Set<DocsContainer> getAll() throws SQLException;
    DocsContainer getById(int id) throws SQLException;
    void save(DocsContainer docsContainer) throws SQLException;
}
