package ru.logistica.tms.controller.ajax;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/setTableData")
public class SetTableDataServlet extends AjaxHttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String docAndDate = req.getParameter("selectionObject");

        String period = req.getParameter("period");
        System.out.println("WRITE INTO DB: " + docAndDate + "  " + period);

//        JsonReader reader1 = Json.createReader(new StringReader(docAndDate));
//        JsonReader reader2 = Json.createReader(new StringReader(period));

        req.getRequestDispatcher("getTableData").forward(req, resp);
    }
}
