package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.supplierDao.Supplier;
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.ValidateDataException;
import ru.logistica.tms.util.UtcSimpleDateFormat;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@WebServlet("/getTableData")
public class GetTableDataServlet extends AjaxHttpServlet {
    /*
    var exampleData = [
                {docPeriodId: 10, periodBegin: 630, periodEnd: 690, state: "CLOSED",   owned: true},
                {docPeriodId: 11, periodBegin: 690, periodEnd: 720, state: "OCCUPIED", owned: false, supplierName: "someSupplier"},
                {docPeriodId: 12, periodBegin: 720, periodEnd: 780, state: "OCCUPIED", owned: true,  supplierName: "someSupplier2"},
                {docPeriodId: 13, periodBegin: 840, periodEnd: 870, state: "OCCUPIED", owned: true,  supplierName: "someSupplier2"}
            ];
            */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String receivedData = req.getParameter("docDateSelection");
        DocDateSelectorData docDateSelectorData;
        try {
            docDateSelectorData = new DocDateSelectorData(receivedData);
        } catch (ValidateDataException e) {
            throw new ServletException(e);
        }

        Integer windowSize = Integer.parseInt(getServletContext().getInitParameter("windowSize"));

        //Warehouse warehouse = DaoFacade.getWarehouseById(docDateSelectorData.warehouseId);
        //Integer offset = AppContextCache.timeZoneAbbrIntegerMap.get(warehouse.getRusTimeZoneAbbr()).intValue();
        long timeStampBegin = docDateSelectorData.utcDate;
        long timeStampEnd = timeStampBegin + windowSize * 60 * 1000;

        Object user = req.getSession(false).getAttribute("user");
        List<DocPeriod> allPeriods = DaoFacade.getAllPeriodsForDoc(docDateSelectorData.docId, new Date(timeStampBegin), new Date(timeStampEnd));

        // create and send json object to client
        JsonObjectBuilder sendObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder docPeriodArrayBuilder = Json.createArrayBuilder();
        for (DocPeriod period : allPeriods) {
            boolean isDonutDocPeriod = (period instanceof DonutDocPeriod);
            JsonObjectBuilder docPeriodBuilder = Json.createObjectBuilder();
            docPeriodBuilder.add("docPeriodId", period.getDocPeriodId());
            docPeriodBuilder.add("periodBegin", (period.getPeriod().getPeriodBegin().getTime() - timeStampBegin)/(1000*60));
            docPeriodBuilder.add("periodEnd", (period.getPeriod().getPeriodEnd().getTime() - timeStampBegin)/(1000*60));
            setOwned(user, period, isDonutDocPeriod, docPeriodBuilder);

            if (isDonutDocPeriod) {
                DonutDocPeriod donutDocPeriod = (DonutDocPeriod) period;
                docPeriodBuilder.add("state", "OCCUPIED");
                docPeriodBuilder.add("supplierName", donutDocPeriod.getSupplier().getInn());
            } else {
                docPeriodBuilder.add("state", "CLOSED");
            }
            docPeriodArrayBuilder.add(docPeriodBuilder);
        }
        JsonObject docPeriods = sendObjectBuilder.add("docPeriods", docPeriodArrayBuilder).build();
        sendJson(resp, docPeriods);
    }

    private void setOwned(Object user, DocPeriod period, boolean isDonutDocPeriod, JsonObjectBuilder docPeriodBuilder) throws ServletException {
        if (user instanceof WarehouseUser) {
            WarehouseUser warehouseUser = (WarehouseUser) user;
            if (warehouseUser.getUserRole().getUserRoleId() == UserRoles.WH_BOSS) {
                if (!period.getDoc().getWarehouse().equals(warehouseUser.getWarehouse())) {
                    throw new ServletException("not accessed");
                }
                docPeriodBuilder.add("owned", true);

            } else if(warehouseUser.getUserRole().getUserRoleId() == UserRoles.WH_DISPATCHER) {
                if (isDonutDocPeriod) {
                    docPeriodBuilder.add("owned", true);
                } else {
                    docPeriodBuilder.add("owned", false);
                }
            } else {
                throw new ServletException("bad role");
            }
        }
        else if (user instanceof SupplierUser) {
            if (isDonutDocPeriod) {
                SupplierUser supplierUser = (SupplierUser) user;
                DonutDocPeriod donutDocPeriod = (DonutDocPeriod) period;
                if (donutDocPeriod.getSupplier().equals(supplierUser.getSupplier())) {
                    docPeriodBuilder.add("owned", true);
                } else {
                    docPeriodBuilder.add("owned", false);
                }
            } else {
                docPeriodBuilder.add("owned", false);
            }
        }
    }
}
