package ru.logistica.tms.service;

import ru.logistica.tms.dao.usersDao.AbstractUser;
import ru.logistica.tms.dao.usersDao.AbstractUserDaoImpl;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.ConnectionManager;
import ru.logistica.tms.dao.utils.SQLConnection;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {


    public boolean authenticate(String userLogin, String password) throws DaoException {

        // get connection from connection pool
        try {
            Connection connection = new SQLConnection().getConnection();
            connection.setAutoCommit(false);
            ConnectionManager.setConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        GenericUserDao<AbstractUser> abstractUserGenericUserDao = new AbstractUserDaoImpl();
        AbstractUser byLogin = abstractUserGenericUserDao.getByLogin(userLogin);
        System.out.println(byLogin);
        try {
            ConnectionManager.getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (byLogin != null) {
            return true;
        }
        return false;
    }
}
