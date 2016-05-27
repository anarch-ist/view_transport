package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebServlet("/setTableData")
public class SetTableDataServlet extends AjaxHttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String docAndDate = req.getParameter("docAndDate");
        String period = req.getParameter("period");
        JsonReader reader1 = Json.createReader(new StringReader(docAndDate));
        JsonReader reader2 = Json.createReader(new StringReader(period));

//        JsonStructure jsonst = reader.read();
//        Integer warehouseId = Integer.parseInt(req.getParameter("warehouseId"));
//        Integer docId = Integer.parseInt(req.getParameter("docId"));
//        Date utcDate = null;
//        try {
//            utcDate = GetTableDataServlet.dateFormat.parse(req.getParameter("date")); // UTC utcDate
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Integer windowSize = Integer.parseInt(getServletContext().getInitParameter("windowSize"));
//
//        Warehouse warehouse = DaoFacade.getWarehouseById(warehouseId);
//        Integer offset = AppContextCache.timeZoneAbbrIntegerMap.get(warehouse.getRusTimeZoneAbbr()).intValue();
//        long timeStampBegin = utcDate.getTime() - offset * 60 * 60 * 1000;
//        long timeStampEnd = timeStampBegin + windowSize * 60 * 1000;
//
//        List<DocPeriod> allPeriods = DaoFacade.getAllPeriodsForDoc(docId, new Date(timeStampBegin), new Date(timeStampEnd));
//
//
//        // create and send json object to client
//        JsonObjectBuilder sendObjectBuilder = Json.createObjectBuilder();
//        JsonArrayBuilder docPeriodArrayBuilder = Json.createArrayBuilder();
//        for (DocPeriod period : allPeriods) {
//            JsonObjectBuilder docPeriodBuilder = Json.createObjectBuilder();
//            docPeriodBuilder.add("periodBegin", (period.getPeriod().getPeriodBegin().getTime() - timeStampBegin)/(1000*60));
//            docPeriodBuilder.add("periodEnd", (period.getPeriod().getPeriodEnd().getTime() - timeStampBegin)/(1000*60));
//
//            if (period instanceof DonutDocPeriod) {
//                DonutDocPeriod donutDocPeriod = (DonutDocPeriod) period;
//                docPeriodBuilder.add("state", "OCCUPIED");
//                docPeriodBuilder.add("supplierName", donutDocPeriod.getSupplier().getInn());
//                docPeriodBuilder.add("supplierId", donutDocPeriod.getSupplier().getSupplierId());
//            } else {
//                docPeriodBuilder.add("state", "DISABLED");
//            }
//            docPeriodArrayBuilder.add(docPeriodBuilder);
//        }
//        JsonObject docPeriods = sendObjectBuilder.add("docPeriods", docPeriodArrayBuilder).build();
        sendJson(resp, docPeriods);
    }
}
