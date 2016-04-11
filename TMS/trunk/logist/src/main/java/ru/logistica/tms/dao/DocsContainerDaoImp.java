package ru.logistica.tms.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

class DocsContainerDaoImp implements DocsContainerDao{
    @Override
    public Set<DocsContainer> getAll() throws SQLException {
        String sql = "SELECT * from docs_container"; // name of table
        Set<DocsContainer> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
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
        return result;
    }

    @Override
    public DocsContainer getById(int id) throws SQLException {
        String sql = "SELECT * from docs_container WHERE id = ?"; // name of table
        DocsContainer docsContainer = new DocsContainer();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            docsContainer.setContainerId(resultSet.getInt("containerID"));
            docsContainer.setDocId(resultSet.getInt("docID"));
            docsContainer.setTimeDiffId(resultSet.getInt("timeDiffID"));
            docsContainer.setDate(resultSet.getDate("date"));
            docsContainer.setDocState(Enum.valueOf(DocState.class, resultSet.getString("docState")));
        }
        return docsContainer;
    }

    @Override
    public void save(DocsContainer docsContainer) throws SQLException {
        String sql = "INSERT INTO docs_container VALUES (?, ?, ?, ?, ?)"; // name of table
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, (int)docsContainer.getContainerId());
            statement.setInt(2, docsContainer.getDocId());
            statement.setInt(3, docsContainer.getTimeDiffId());
            statement.setDate(4, new java.sql.Date(docsContainer.getDate().getTime()));
            statement.setString(5, String.valueOf(docsContainer.getDocState()));
            statement.execute();
        }
    }
}
