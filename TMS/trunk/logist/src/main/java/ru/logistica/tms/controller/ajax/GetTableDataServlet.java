package ru.logistica.tms.controller.ajax;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/getTableData")
public class GetTableDataServlet extends AjaxHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("date"));
        System.out.println(req.getParameter("warehouseId"));
        System.out.println(req.getParameter("docId"));
    }
}
