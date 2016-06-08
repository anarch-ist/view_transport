package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/removeDonut")
public class RemoveDonut extends AjaxHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject dataForDelete = JsonUtils.parseStringAsObject(req.getParameter("dataForDelete"));
        long docPeriodId = dataForDelete.getJsonNumber("docPeriodId").longValueExact();
        DaoFacade.deleteDonutWithRequests(docPeriodId);
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }
}
