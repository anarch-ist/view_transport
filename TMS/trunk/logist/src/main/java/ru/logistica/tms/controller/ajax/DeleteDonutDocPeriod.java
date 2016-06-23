package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.userDao.PermissionNames;
import ru.logistica.tms.dao.userDao.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet("/deleteDonut")
public class DeleteDonutDocPeriod extends AppHttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
        User user = (User)req.getSession(false).getAttribute("user");
        Set<PermissionNames> permissionsForRole = user.getUserRole().getPermissionNames();

        try {
            if (permissionsForRole.contains(PermissionNames.DELETE_ANY_DONUT)) {
                DaoFacade.deleteDonutWithRequests(getUser(req).getUserId(), donutDocPeriodId);
            } else if (permissionsForRole.contains(PermissionNames.DELETE_CREATED_DONUT)) {
                DaoFacade.deleteDonutWithRequestsIfCreated(getUser(req).getUserId(), donutDocPeriodId);
            }
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }

}
