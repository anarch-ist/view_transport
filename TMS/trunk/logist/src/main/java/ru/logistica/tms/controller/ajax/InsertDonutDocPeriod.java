package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.userDao.SupplierUser;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.DonutInsertData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/insertDonut")
public class InsertDonutDocPeriod extends AjaxHttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String donutData = req.getParameter("createdDonut");
        String docDateSelection = req.getParameter("docDateSelection");
        SupplierUser supplierUser = (SupplierUser)req.getSession(false).getAttribute("user");

        DonutInsertData donutInsertData;
        DocDateSelectorData docDateSelectorData;
        try {
            docDateSelectorData = new DocDateSelectorData(docDateSelection);
            donutInsertData = new DonutInsertData(donutData);
//            System.out.println(donut);
        } catch (ValidateDataException e) {
            throw new ServletException(e);
        }

        try {
            DaoFacade.insertDonut(donutInsertData, docDateSelectorData, supplierUser);
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("getTableData").forward(req, resp);
    }
}
