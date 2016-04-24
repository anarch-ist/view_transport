package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


    private static final Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter("userLogin");
        String userPassword = request.getParameter("userPassword");
        LoginService loginService = new LoginService();
        boolean authenticated = false;
        try {
            authenticated = loginService.authenticate(userLogin, userPassword);
        } catch (DaoException e) {
            logger.error(e);
        }

        if (authenticated) {
            response.sendRedirect("success.jsp");
            return;
        } else {
            response.sendRedirect("login.jsp");
            return;
        }
    }
}
