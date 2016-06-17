package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dto.OpenDocPeriodsData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/openDocPeriods")
public class OpenDocPeriods extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            OpenDocPeriodsData openDocPeriodsData = new OpenDocPeriodsData(req.getParameter("openPeriodsData"));
            DaoFacade.openPeriods(openDocPeriodsData);
        } catch (ValidateDataException | DaoScriptException e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("getTableData").forward(req, resp);
    }
}
