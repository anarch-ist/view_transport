package ru.logistica.tms.controller;


import ru.logistica.tms.controller.ajax.AppHttpServlet;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dto.DocDateSelectorData;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet("/main")
public class MainServlet extends AppHttpServlet {

    private Map<User, DocDateSelectorData> sessionDataHolder;

    @Override
    public void init() throws ServletException {
        sessionDataHolder  = (Map<User, DocDateSelectorData>) getServletContext().getAttribute("sessionDataHolder");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObjectBuilder warehousesWithDocs = Json.createObjectBuilder();
        Set<Warehouse> allWarehousesWithDocs;
        try {
            allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();
        } catch (DaoScriptException e) {
            throw new ServletException(e.getMessage(), e);
        }
        JsonArrayBuilder warehouseArrayBuilder = Json.createArrayBuilder();
        for (Warehouse warehouse : allWarehousesWithDocs) {
            JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouse);
            warehouseArrayBuilder.add(warehouseBuilder);
        }
        warehousesWithDocs.add("warehouses", warehouseArrayBuilder);

        // request statuses for roles
        User user = getUser(request);
        UserRoles userRoleId = user.getUserRole().getUserRoleId();
        JsonArray orderStatusesForRole;
        if (userRoleId.equals(UserRoles.WH_DISPATCHER)) {
            orderStatusesForRole = createOrderStatuses(false, true, true, true, false, false);
        } else if (userRoleId.equals(UserRoles.SUPPLIER_MANAGER)) {
            orderStatusesForRole = createOrderStatuses(true, false, false, false, false, false);
        } else if (userRoleId.equals(UserRoles.WH_BOSS)) {
            orderStatusesForRole = createOrderStatuses(false, false, false, false, false, false);
        } else if (userRoleId.equals(UserRoles.WH_SECURITY_OFFICER)) {
            orderStatusesForRole = createOrderStatuses(false, true, false, false, false, false);
        } else
            throw new ServletException("no such role");

        String userRoleRusName = "";
        if (userRoleId == UserRoles.SUPPLIER_MANAGER)
            userRoleRusName = "ПОСТАВЩИК";
        else if (userRoleId == UserRoles.WH_DISPATCHER) {
            userRoleRusName = "ДИСПЕТЧЕР СКЛАДА";
        } else if (userRoleId == UserRoles.WH_BOSS) {
            userRoleRusName = "НАЧАЛЬНИК СКЛАДА";
        } else if (userRoleId == UserRoles.WH_SECURITY_OFFICER) {
            userRoleRusName = "ОХРАННИК";
        }

        request.setAttribute("userRoleRusName", userRoleRusName);
        request.setAttribute("warehousesWithDocs", warehousesWithDocs.build().toString());
        request.setAttribute("orderStatuses", orderStatusesForRole.toString());
        request.setAttribute("lastDocDateSelection", sessionDataHolder.get(user));

        request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
    }

    private JsonObjectBuilder createWarehouseBuilder(Warehouse warehouse) {
        JsonObjectBuilder warehouseBuilder = Json.createObjectBuilder();
        warehouseBuilder.add("warehouseId", warehouse.getWarehouseId());
        warehouseBuilder.add("warehouseName", warehouse.getWarehouseName());

        RusTimeZoneAbbr timeZoneAbbr = warehouse.getRusTimeZoneAbbr();
        warehouseBuilder.add("rusTimeZoneAbbr", timeZoneAbbr.name());
        warehouseBuilder.add("timeOffset", AppContextCache.timeZoneAbbrIntegerMap.get(timeZoneAbbr));

        Set<Doc> docs = warehouse.getDocs();
        JsonArrayBuilder docArrayBuilder = Json.createArrayBuilder();
        for (Doc doc : docs) {
            JsonObjectBuilder docBuilder = Json.createObjectBuilder();
            docBuilder.add("docId", doc.getDocId());
            docBuilder.add("docName", doc.getDocName());
            docArrayBuilder.add(docBuilder);
        }
        warehouseBuilder.add("docs", docArrayBuilder);
        return warehouseBuilder;
    }

    private JsonObjectBuilder createStatusBuilder(String statusName, String statusRusName, boolean isUpdatable) {
        JsonObjectBuilder statusBuilder = Json.createObjectBuilder();
        statusBuilder.add("statusName", statusName);
        statusBuilder.add("statusRusName", statusRusName);
        statusBuilder.add("isUpdatable", isUpdatable);
        return  statusBuilder;
    }

    private JsonArray createOrderStatuses(
            boolean isCreatedUpdatable,
            boolean isArrivedUpdatable,
            boolean isDeliveredUpdatable,
            boolean isErrorUpdatable,
            boolean isCancelledBySupplierUserUpdatable,
            boolean isCancelledByWarehouseUserUpdatable
    ) {
        JsonArrayBuilder orderStatusesBuilder = Json.createArrayBuilder();
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CREATED.name(),                     "СОЗДАН",                    isCreatedUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.ARRIVED.name(),                     "ПРИБЫТИЕ НА ТЕРРИТОРИЮ",    isArrivedUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.DELIVERED.name(),                   "ДОСТАВЛЕН",                 isDeliveredUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.ERROR.name(),                       "ОШИБКА",                    isErrorUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CANCELLED_BY_SUPPLIER_USER.name(),  "УДАЛЕН ПОСТАВЩИКОМ",        isCancelledBySupplierUserUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CANCELLED_BY_WAREHOUSE_USER.name(), "УДАЛЕН НАЧАЛЬНИКОМ СКЛАДА", isCancelledByWarehouseUserUpdatable));
        return orderStatusesBuilder.build();
    }

}
