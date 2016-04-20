package ru.logistica.tms.service;

import ru.logistica.tms.dao.usersDao.AbstractUser;
import ru.logistica.tms.dao.usersDao.AbstractUserDaoImpl;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.SQLException;

public class LoginService {


    public boolean authenticate(String userLogin, String password) throws DaoException {
        GenericUserDao<AbstractUser> abstractUserGenericUserDao = new AbstractUserDaoImpl();
        AbstractUser byLogin = abstractUserGenericUserDao.getByLogin(userLogin);
        try {
            JdbcUtil.getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (byLogin != null) {
            return true;
        }
        return false;
    }
}
