package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.PeriodsForInsertData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/insertDocPeriods")
public class InsertDocPeriods extends AppHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PeriodsForInsertData periodsForInsertData;
        try {
            periodsForInsertData = new PeriodsForInsertData(req.getParameter("periodsForInsert"));
        } catch (ValidateDataException e) {
            throw new ServletException(e);
        }
        String receivedData = req.getParameter("docDateSelection");
        DocDateSelectorData docDateSelectorData;
        try {
            docDateSelectorData = new DocDateSelectorData(receivedData);
        } catch (ValidateDataException e) {
            throw new ServletException(e);
        }
        try {
            DaoFacade.insertDocPeriods(getUser(req).getUserId(), periodsForInsertData, docDateSelectorData.docId);
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("getTableData").forward(req, resp);
    }
}
