package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.userDao.SupplierUser;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.Donut;
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

        Donut donut;
        DocDateSelectorData docDateSelectorData;
        try {
            docDateSelectorData = new DocDateSelectorData(docDateSelection);
            donut = new Donut(donutData);
//            System.out.println(donut);
        } catch (ValidateDataException e) {
            throw new ServletException(e);
        }

        DaoFacade.insertDonut(donut, docDateSelectorData, supplierUser.getSupplier());

        req.getRequestDispatcher("getTableData").forward(req, resp);
    }
}
