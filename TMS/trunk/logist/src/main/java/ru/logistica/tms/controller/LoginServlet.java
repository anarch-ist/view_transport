package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.usersDao.AbstractUser;
import ru.logistica.tms.dao.usersDao.AbstractUserDaoImpl;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.utils.JdbcUtil;
import ru.logistica.tms.dao.utils.SQLConnection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        super.init();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            SQLConnection sqlConnection = new SQLConnection();
            JdbcUtil.setConnection(sqlConnection.getConnection());

            ConstantsDao constantsDao = new ConstantsDaoImpl();
            ConstantCollections.setUserRoles(constantsDao.getUserRoles());

            String userLogin = request.getParameter("userLogin");
            String userPassword = request.getParameter("userPassword");
            GenericUserDao<AbstractUser> abstractUserDao = new AbstractUserDaoImpl();
            AbstractUser abstractUser = abstractUserDao.getByLogin(userLogin);
            if (abstractUser != null) {
                response.sendRedirect("success.jsp");
            } else {
                response.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }



//        LoginService loginService = new LoginService();
//        boolean authenticated = false;
//        try {
//            authenticated = loginService.authenticate(userLogin, userPassword);
//        } catch (DaoException e) {
//            logger.error(e);
//        }
//
//        if (authenticated) {
//            response.sendRedirect("success.jsp");
//            return;
//        } else {
//            response.sendRedirect("login.jsp");
//            return;
//        }
    }
}
