package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDao;

public interface UserDao extends GenericDao<User, Integer> {
    User findByLogin(String login) throws DAOException;
}
