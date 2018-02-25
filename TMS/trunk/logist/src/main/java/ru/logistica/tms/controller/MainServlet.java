package ru.logistica.tms.controller;


import ru.logistica.tms.controller.ajax.AppHttpServlet;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.SupplierUser;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.dao.userDao.WarehouseUser;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.util.RusNames;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

        User user = getUser(request);
        UserRoles userRoleId = user.getUserRole().getUserRoleId();
        JsonObject docDateSelectorDataForRole;
        JsonArray orderStatusesForRole;
        String userRoleRusName;


        JsonArrayBuilder warehouseArrayBuilder = Json.createArrayBuilder();
        if (user instanceof WarehouseUser) {
            WarehouseUser warehouseUser = (WarehouseUser) user;
            Warehouse warehouseWithDocs;
            try {
                warehouseWithDocs = DaoFacade.getWarehouseWithDocs(warehouseUser.getWarehouse().getWarehouseId());
            } catch (DaoScriptException e) {
                throw new ServletException(e.getMessage(), e);
            }
            JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouseWithDocs);
            warehouseArrayBuilder.add(warehouseBuilder);
        } else {
            Set<Warehouse> allWarehousesWithDocs;
            try {
                allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();
            } catch (DaoScriptException e) {
                throw new ServletException(e.getMessage(), e);
            }
            for (Warehouse warehouse : allWarehousesWithDocs) {
                JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouse);
                warehouseArrayBuilder.add(warehouseBuilder);
            }
        }
        JsonObjectBuilder warehousesWithDocs = Json.createObjectBuilder();
        warehousesWithDocs.add("warehouses", warehouseArrayBuilder);
        docDateSelectorDataForRole = warehousesWithDocs.build();


        Map<UserRoles, String> userRolesConverter = RusNames.getUserRolesConverter();
        if (userRoleId == UserRoles.WH_DISPATCHER) {
            orderStatusesForRole = createOrderStatuses(false, true, true, true, false, false);
            userRoleRusName = userRolesConverter.get(UserRoles.WH_DISPATCHER);
        } else if (userRoleId == UserRoles.SUPPLIER_MANAGER) {
            orderStatusesForRole = createOrderStatuses(true, false, false, false, false, false);
            userRoleRusName = userRolesConverter.get(UserRoles.SUPPLIER_MANAGER);
        } else if (userRoleId == UserRoles.WH_BOSS) {
            orderStatusesForRole = createOrderStatuses(false, false, false, false, false, false);
            userRoleRusName = userRolesConverter.get(UserRoles.WH_BOSS);
        } else if (userRoleId == UserRoles.WH_SECURITY_OFFICER) {
            orderStatusesForRole = createOrderStatuses(false, true, false, false, false, false);
            userRoleRusName = userRolesConverter.get(UserRoles.WH_SECURITY_OFFICER);
        } else if (userRoleId == UserRoles.WH_SUPERVISOR) {
            orderStatusesForRole = createOrderStatuses(false, false, false, false, false, false);
            userRoleRusName = userRolesConverter.get(UserRoles.WH_SUPERVISOR);
        } else
            throw new ServletException("no such role");


        if (userRoleId == UserRoles.WH_DISPATCHER || userRoleId == UserRoles.WH_BOSS || userRoleId == UserRoles.WH_SUPERVISOR
                || userRoleId == UserRoles.SUPPLIER_MANAGER) {
            JsonObject warehousesForDonutCrudPlugin;
            try {
                Set<Warehouse> allWarehouses = DaoFacade.getAllWarehouses();
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                for (Warehouse warehouse : allWarehouses) {
                    jsonObjectBuilder.add(String.valueOf(warehouse.getWarehouseId()), warehouse.getWarehouseName());
                }
                warehousesForDonutCrudPlugin = jsonObjectBuilder.build();
            } catch (DaoScriptException e) {
                throw new ServletException(e.getMessage(), e);
            }
            request.setAttribute("warehousesForDonutCrudPlugin", warehousesForDonutCrudPlugin.toString());
        }
        if (user instanceof SupplierUser) {
            request.setAttribute("maxCells", (Object)((SupplierUser)user).getSupplier().getMaxCells());
        } else {
            request.setAttribute("maxCells", (Object)48);
        }


        request.setAttribute("userRoleRusName", userRoleRusName);
        request.setAttribute("docDateSelectorDataForRole", docDateSelectorDataForRole.toString());
        request.setAttribute("orderStatusesForRole", orderStatusesForRole.toString());
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
        Map<OrderStatuses, String> orderStatusesConverter = RusNames.getOrderStatusesConverter();
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CREATED.name(),                     orderStatusesConverter.get(OrderStatuses.CREATED),                     isCreatedUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.ARRIVED.name(),                     orderStatusesConverter.get(OrderStatuses.ARRIVED),                     isArrivedUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.DELIVERED.name(),                   orderStatusesConverter.get(OrderStatuses.DELIVERED),                   isDeliveredUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.ERROR.name(),                       orderStatusesConverter.get(OrderStatuses.ERROR),                       isErrorUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CANCELLED_BY_SUPPLIER_USER.name(),  orderStatusesConverter.get(OrderStatuses.CANCELLED_BY_SUPPLIER_USER),  isCancelledBySupplierUserUpdatable));
        orderStatusesBuilder.add(createStatusBuilder(OrderStatuses.CANCELLED_BY_WAREHOUSE_USER.name(), orderStatusesConverter.get(OrderStatuses.CANCELLED_BY_WAREHOUSE_USER), isCancelledByWarehouseUserUpdatable));
        return orderStatusesBuilder.build();
    }

}
