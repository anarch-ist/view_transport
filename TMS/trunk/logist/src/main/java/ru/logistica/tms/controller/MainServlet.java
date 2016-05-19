package ru.logistica.tms.controller;


import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRole;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

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
        UserRoles userRoleId = userRole.getUserRoleId();

        // список всех складов(пары ключ-имя)
        // список всех доков для всех складов(id склада, id дока, имя дока)
        if (userRoleId == UserRoles.SUPPLIER_MANAGER) {
            Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();

            //allWarehousesWithDocs.
        } else if (userRoleId == UserRoles.WH_BOSS || userRoleId == UserRoles.WH_DISPATCHER) {
            Set<Doc> allDocsForWarehouseUserId = DaoFacade.getAllDocsForWarehouseUserId(user.getUserId());
        }


        request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
    }
}
