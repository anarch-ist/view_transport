package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.userDao.Permission;
import ru.logistica.tms.dao.userDao.PermissionNames;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.util.EmailUtils;
import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/deleteDonut")
public class DeleteDonutDocPeriod extends AjaxHttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
        User user = (User)req.getSession(false).getAttribute("user");
        Set<PermissionNames> permissionsForRole = user.getUserRole().getPermissionNames();

        try {
            if (permissionsForRole.contains(PermissionNames.DELETE_ANY_DONUT)) {
                DaoFacade.deleteDonutWithRequests(donutDocPeriodId);
            } else if (permissionsForRole.contains(PermissionNames.DELETE_CREATED_DONUT)) {
                DaoFacade.deleteDonutWithRequestsIfCreated(donutDocPeriodId);
            }
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }
        //if (true) throw new ServletException("YESY");
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }

}
