package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/setOrderStatusesToArrived")
public class SetOrderStatusesToArrived extends AppHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
            DaoFacade.setOrderStatusesToArrived(getUser(req).getUserId(), donutDocPeriodId);
        } catch (NumberFormatException | DaoScriptException e) {
            throw new ServletException(e.getMessage(), e);
        }
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }
}
