package ru.logistica.tms.dao.docsContainerDao;



import ru.logistica.tms.dao.DaoException;

import java.util.Set;

public interface DocsContainerDao {
    Set<DocsContainer> getAll() throws DaoException;
    DocsContainer getById(int id) throws DaoException;
    void save(DocsContainer docsContainer) throws DaoException;
}
