package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.SupplierUser;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.dao.userDao.WarehouseSupervisor;
import ru.logistica.tms.dao.userDao.WarehouseUser;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/getTableData")
public class GetTableDataServlet extends AppHttpServlet {
    /*
    var exampleData = [
                {docPeriodId: 10, periodBegin: 630, periodEnd: 690, state: "CLOSED",   owned: true},
                {docPeriodId: 11, periodBegin: 690, periodEnd: 720, state: "OCCUPIED", owned: false, supplierName: "someSupplier"},
                {docPeriodId: 12, periodBegin: 720, periodEnd: 780, state: "OCCUPIED", owned: true,  supplierName: "someSupplier2"},
                {docPeriodId: 13, periodBegin: 840, periodEnd: 870, state: "OCCUPIED", owned: true,  supplierName: "someSupplier2"}
            ];
            */

    private Map<User, DocDateSelectorData> sessionDataHolder;

    @Override
    public void init() throws ServletException {
        sessionDataHolder  = (Map<User, DocDateSelectorData>) getServletContext().getAttribute("sessionDataHolder");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUser(req);
        String receivedData = req.getParameter("docDateSelection");
        DocDateSelectorData docDateSelectorData;
        try {
            docDateSelectorData = new DocDateSelectorData(receivedData);
            sessionDataHolder.put(user, docDateSelectorData);
        } catch (ValidateDataException e) {
            throw new ServletException(e.getMessage(), e);
        }

        Integer windowSize = Integer.parseInt(getServletContext().getInitParameter("windowSize"));

        long timeStampBegin = docDateSelectorData.utcDate;
        // TODO
        long timeStampEnd = timeStampBegin + windowSize * 60 * 1000;
        List<DocPeriod> allPeriods;
        try {
            allPeriods = DaoFacade.getAllPeriodsForDoc(docDateSelectorData.docId, new Date(timeStampBegin), new Date(timeStampEnd));
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }

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
                docPeriodBuilder.add("supplierName", donutDocPeriod.getSupplierUser().getSupplier().getInn());
                docPeriodBuilder.add("occupiedStatus", getOccupiedStatus(donutDocPeriod.getOrders()).name());
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
            UserRoles userRoleId = warehouseUser.getUserRole().getUserRoleId();
            if (userRoleId == UserRoles.WH_BOSS) {
                if (!period.getDoc().getWarehouse().equals(warehouseUser.getWarehouse())) {
                    throw new ServletException("not accessed");
                }
                docPeriodBuilder.add("owned", true);

            } else if(userRoleId == UserRoles.WH_DISPATCHER || userRoleId == UserRoles.WH_SECURITY_OFFICER) {
                if (isDonutDocPeriod) {
                    docPeriodBuilder.add("owned", true);
                } else {
                    docPeriodBuilder.add("owned", false);
                }
            } else {
                throw new ServletException("bad role");
            }
        }
        else if (user instanceof WarehouseSupervisor) {
            WarehouseSupervisor warehouseSupervisor = (WarehouseSupervisor) user;
            UserRoles userRoleId = warehouseSupervisor.getUserRole().getUserRoleId();
            if (userRoleId == UserRoles.WH_SUPERVISOR) {
                docPeriodBuilder.add("owned", true);
            } else {
                throw new ServletException("bad role");
            }
        }
        else if (user instanceof SupplierUser) {
            if (isDonutDocPeriod) {
                SupplierUser supplierUser = (SupplierUser) user;
                DonutDocPeriod donutDocPeriod = (DonutDocPeriod) period;
                if (donutDocPeriod.getSupplierUser().equals(supplierUser)) {
                    docPeriodBuilder.add("owned", true);
                } else {
                    docPeriodBuilder.add("owned", false);
                }
            } else {
                docPeriodBuilder.add("owned", false);
            }
        }
    }

    private OccupiedStatus getOccupiedStatus(Set<Order> orders) {
        Objects.requireNonNull(orders);

        if (orders.size() == 0)
            return OccupiedStatus.IN_PROCESS;

        int deliveredCount = 0;
        int arrivedCount = 0;
        for (Order order : orders) {
            OrderStatuses orderStatus = order.getOrderStatus();
            if (orderStatus == OrderStatuses.ERROR) {
                return OccupiedStatus.ERROR;
            }
            if (orderStatus == OrderStatuses.DELIVERED) {
                deliveredCount++;
            }
            else if (orderStatus == OrderStatuses.ARRIVED) {
                arrivedCount++;
            }
        }
        int size = orders.size();
        if (deliveredCount == size)
            return OccupiedStatus.DELIVERED;
        else if (arrivedCount == size) {
            return OccupiedStatus.ARRIVED;
        } else {
            return OccupiedStatus.IN_PROCESS;
        }
    }
    // BINDING tablePlugin2.js setData()
    enum OccupiedStatus {ERROR, DELIVERED, IN_PROCESS, ARRIVED}
}
