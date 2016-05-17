package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDaoImpl;

public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {

    @Override
    public User findByLogin(String login) throws DAOException {
        try {
            return getSession().bySimpleNaturalId(User.class).load(login);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
