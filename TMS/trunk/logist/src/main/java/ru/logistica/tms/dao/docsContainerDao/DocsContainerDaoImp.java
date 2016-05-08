package ru.logistica.tms.dao.docsContainerDao;


import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

class DocsContainerDaoImp implements DocsContainerDao{
    @Override
    public Set<DocsContainer> getAll() throws DaoException {
        final Set<DocsContainer> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from docs_container"; // name of table
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        DocsContainer docsContainer = new DocsContainer();
                        docsContainer.setContainerId(resultSet.getInt("containerID"));
                        docsContainer.setDocId(resultSet.getInt("docID"));
                        docsContainer.setTimeDiffId(resultSet.getInt("timeDiffID"));
                        docsContainer.setDate(resultSet.getDate("date"));
                        docsContainer.setDocState(Enum.valueOf(DocState.class, resultSet.getString("docState")));
                        result.add(docsContainer);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public DocsContainer getById(final int id) throws DaoException {
        final DocsContainer[] docsContainer = new DocsContainer[1];
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from docs_container WHERE containerId = ?"; // name of table
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        docsContainer[0] = new DocsContainer();
                        docsContainer[0].setContainerId(resultSet.getInt("containerID"));
                        docsContainer[0].setDocId(resultSet.getInt("docID"));
                        docsContainer[0].setTimeDiffId(resultSet.getInt("timeDiffID"));
                        docsContainer[0].setDate(resultSet.getDate("date"));
                        docsContainer[0].setDocState(Enum.valueOf(DocState.class, resultSet.getString("docState")));
                    }
                }
            }
        });
        return docsContainer[0];
    }

    @Override
    public void save(final DocsContainer docsContainer) throws DaoException {
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO docs_container VALUES (?, ?, ?, ?, ?)"; // name of table
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, docsContainer.getContainerId());
                    statement.setInt(2, docsContainer.getDocId());
                    statement.setInt(3, docsContainer.getTimeDiffId());
                    statement.setDate(4, new java.sql.Date(docsContainer.getDate().getTime()));
                    statement.setString(5, String.valueOf(docsContainer.getDocState()));
                    statement.execute();
                }
            }
        });

    }
}
