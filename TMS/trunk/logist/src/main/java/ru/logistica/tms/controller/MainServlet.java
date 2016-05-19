package ru.logistica.tms.controller;


import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // достать сессию, оттуда достать роль пользователя.
        // извлечь все данные для соответсвующей роли пользователя
        // записать данные в dto объекты и передать их в контексте запроса
        //
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        UserRole userRole = user.getUserRole();

//        if (userRole == User.UserRole.SUPPLIER_MANAGER) {
//            //DaoManager.
//        }


        request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
    }
}
