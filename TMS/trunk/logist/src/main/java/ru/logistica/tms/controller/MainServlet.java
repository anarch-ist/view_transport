package ru.logistica.tms.controller;


import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
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

        // достать сессию, оттуда достать роль пользователя.
        // извлечь все данные для соответсвующей роли пользователя
        // записать данные в dto объекты и передать их в контексте запроса
        //
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        UserRole userRole = user.getUserRole();
        UserRoles userRoleId = userRole.getUserRoleId();

        // список всех складов(пары ключ-имя)
        // список всех доков для всех складов(id склада, id дока, имя дока)


        JsonObjectBuilder sendDataBuilder = Json.createObjectBuilder();
        if (userRoleId == UserRoles.SUPPLIER_MANAGER) {
            Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();


            JsonArrayBuilder warehouseArrayBuilder = Json.createArrayBuilder();
            for (Warehouse warehouse : allWarehousesWithDocs) {
                JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouse);
                warehouseArrayBuilder.add(warehouseBuilder);
            }

            JsonObjectBuilder timeZoneBuilder = Json.createObjectBuilder();
            for (Map.Entry<RusTimeZoneAbbr, Double> rusTimeZoneAbbrEntry : AppContextCache.timeZoneAbbrIntegerMap.entrySet()) {
                timeZoneBuilder.add(rusTimeZoneAbbrEntry.getKey().name(), rusTimeZoneAbbrEntry.getValue());
            }
            sendDataBuilder.add("warehouses", warehouseArrayBuilder);
            sendDataBuilder.add("timeZones", timeZoneBuilder);


        } else if (userRoleId == UserRoles.WH_BOSS || userRoleId == UserRoles.WH_DISPATCHER) {
            Warehouse warehouse = DaoFacade.getWarehouseWithDocsForUser(user.getUserId());
            warehouse.getRusTimeZoneAbbr();
            Double timeZoneOffset = AppContextCache.timeZoneAbbrIntegerMap.get(warehouse.getRusTimeZoneAbbr());
            JsonObjectBuilder warehouseBuilder = createWarehouseBuilder(warehouse);
            sendDataBuilder = Json.createObjectBuilder();
            sendDataBuilder
                    .add("warehouse", warehouseBuilder)
                    .add("timeZoneOffset", timeZoneOffset);
        }
        request.setAttribute("docDateSelectorDataObject", sendDataBuilder.build().toString());

        request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
    }

    private JsonObjectBuilder createWarehouseBuilder(Warehouse warehouse) {
        JsonObjectBuilder warehouseBuilder = Json.createObjectBuilder();
        warehouseBuilder.add("warehouseId", warehouse.getWarehouseId());
        warehouseBuilder.add("warehouseName", warehouse.getWarehouseName());
        warehouseBuilder.add("rusTimeZoneAbbr", warehouse.getRusTimeZoneAbbr().name());

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
