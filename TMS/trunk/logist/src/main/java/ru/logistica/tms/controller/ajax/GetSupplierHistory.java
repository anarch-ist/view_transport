package ru.logistica.tms.controller.ajax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dto.SupplierDonuts;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/getSupplierHistory")
public class GetSupplierHistory extends AppHttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SupplierDonuts allDonutsForSupplier = DaoFacade.getAllDonutsForSupplier(Long.parseLong(req.getParameter("docPeriodId")), "<br/>");
            req.setAttribute("supplierName", allDonutsForSupplier.getSupplierName());
            req.setAttribute("allDonutsForSupplier", allDonutsForSupplier);
        } catch (DaoScriptException | NumberFormatException e) {
            logger.error(e);
            throw new ServletException(e.getMessage(), e);
        }
        req.getRequestDispatcher("/WEB-INF/pages/supplierHistory.jsp").forward(req, resp);
    }
}
