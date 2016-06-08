package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/updateDonut")
public class UpdateDonut extends AjaxHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject dataForDelete = JsonUtils.parseStringAsObject(req.getParameter("dataForUpdate"));
        long docPeriodId = dataForDelete.getJsonNumber("docPeriodId").longValueExact();
        long docPeriodBegin = dataForDelete.getJsonNumber("periodBegin").longValueExact();
        long docPeriodEnd = dataForDelete.getJsonNumber("periodEnd").longValueExact();
        DaoFacade.updateDonutPeriod(docPeriodId, new Date(docPeriodBegin) ,new Date(docPeriodEnd));
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }
}
