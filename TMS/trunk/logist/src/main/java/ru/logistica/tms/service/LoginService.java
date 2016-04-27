package ru.logistica.tms.service;

import ru.logistica.tms.dao.usersDao.User;
import ru.logistica.tms.dao.usersDao.UserDaoImpl;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.ConnectionManager;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {


    public boolean authenticate(String userLogin, String password) throws DaoException {

//        // get connection from connection pool
//        try {
//            Connection connection = new SQLConnection().getConnection();
//            connection.setAutoCommit(false);
//            ConnectionManager.setConnection(connection);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (NamingException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        return false;
    }
}
