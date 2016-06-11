package ru.logistica.tms.controller;


import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserRole;
import ru.logistica.tms.dao.userDao.UserRoles;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // список всех складов(пары ключ-имя)
        // список всех доков для всех складов(id склада, id дока, имя дока)
        JsonObjectBuilder sendDataBuilder = Json.createObjectBuilder();
        Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();
        JsonArrayBuilder warehouseArrayBuilder = Json.createArrayBuilder();
        for (Warehouse warehouse : allWarehousesWithDocs) {
            JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouse);
            warehouseArrayBuilder.add(warehouseBuilder);
        }
        sendDataBuilder.add("warehouses", warehouseArrayBuilder);

        // список всех статусов для заявки
        JsonObjectBuilder orderStatusesBuilder = Json.createObjectBuilder();
        orderStatusesBuilder.add(OrderStatuses.CREATED.name(), "СОЗДАН");
        orderStatusesBuilder.add(OrderStatuses.CANCELLED_BY_SUPPLIER_USER.name(), "УДАЛЕН ПОСТАВЩИКОМ");
        orderStatusesBuilder.add(OrderStatuses.CANCELLED_BY_WAREHOUSE_USER.name(), "УДАЛЕН НАЧАЛЬНИКОМ СКЛАДА");
        orderStatusesBuilder.add(OrderStatuses.DELIVERED.name(), "ДОСТАВЛЕН");
        orderStatusesBuilder.add(OrderStatuses.ERROR.name(), "ОШИБКА");

        request.setAttribute("docDateSelectorDataObject", sendDataBuilder.build().toString());
        request.setAttribute("orderStatuses", orderStatusesBuilder.build().toString());

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


}
