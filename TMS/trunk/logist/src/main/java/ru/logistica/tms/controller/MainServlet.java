package ru.logistica.tms.controller;

import ru.logistica.tms.dao.DaoManager;
import ru.logistica.tms.dao.usersDao.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    public static final String SUPPLIER_MANAGER = "SUPPLIER_MANAGER";
    public static final String W_BOSS = "W_BOSS";
    public static final String WH_DISPATCHER = "WH_DISPATCHER";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // достать сессию, оттуда достать роль пользователя.
        // извлечь все данные для соответсвующей роли пользователя
        // записать данные в dto объекты и передать их в контексте запроса
        //
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        String userRoleId = user.getUserRole().getUserRoleId();
        if (userRoleId.equals(SUPPLIER_MANAGER)) {
            //DaoManager.
        }


        request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
    }
}
