package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.HibernateUtils;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.docPeriodDao.DocPeriodDao;
import ru.logistica.tms.dao.docPeriodDao.DocPeriodDaoImpl;
import ru.logistica.tms.dao.docPeriodDao.Period;
import ru.logistica.tms.dao.userDao.PermissionNames;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dto.DonutData;
import ru.logistica.tms.dto.UpdateDocPeriodsData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@WebServlet("/deleteDonutSinglePeriod")
public class DeleteDonutSinglePeriod extends AppHttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
        boolean isBegin = Boolean.parseBoolean(req.getParameter("isBegin"));
        User user = (User)req.getSession(false).getAttribute("user");
        Set<PermissionNames> permissionsForRole = user.getUserRole().getPermissionNames();

        try {
            if (permissionsForRole.contains(PermissionNames.DELETE_ANY_DONUT)) {
                DaoFacade.updateDonutPeriods(getUser(req).getUserId(), donutDocPeriodId, isBegin);
            } else if (permissionsForRole.contains(PermissionNames.DELETE_CREATED_DONUT)) {
                DaoFacade.updateDonutPeriodsIfCreated(getUser(req).getUserId(), donutDocPeriodId, isBegin);
            }
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }

}
