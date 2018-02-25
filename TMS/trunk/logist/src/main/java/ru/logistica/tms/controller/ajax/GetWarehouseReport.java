/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javax.servlet.RequestDispatcher
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.annotation.WebServlet
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  ru.logistica.tms.controller.ajax.AppHttpServlet
 *  ru.logistica.tms.dao.DaoFacade
 *  ru.logistica.tms.dao.DaoScriptException
 *  ru.logistica.tms.dto.DocReports
 */
package ru.logistica.tms.controller.ajax;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.controller.ajax.AppHttpServlet;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dto.DocReports;

@WebServlet(value={"/getWarehouseReport"})
public class GetWarehouseReport
        extends AppHttpServlet {
    private static final Logger logger = LogManager.getLogger();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            DocReports docReports = DaoFacade.getDocReportsForWarehouse(dateParser.parse(req.getParameter("periodBegin")), dateParser.parse(req.getParameter("periodEnd")), (int)Integer.parseInt(req.getParameter("warehouseId")), (String)"\\n");
            req.setAttribute("docReports", docReports);
            req.setAttribute("warehouseName", docReports.getWarehouseName());
            req.setAttribute("periodBegin", req.getParameter("periodBegin"));
            req.setAttribute("periodEnd", req.getParameter("periodEnd"));
        }
        catch (NumberFormatException | ParseException | DaoScriptException e) {
            logger.error((Object)e);
            throw new ServletException(e.getMessage(), (Throwable)e);
        }
        req.getRequestDispatcher("/WEB-INF/pages/warehouseReport.jsp").forward((ServletRequest)req, (ServletResponse)resp);
    }
}