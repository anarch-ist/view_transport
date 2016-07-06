package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.orderDao.Order;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet("/selectDonut")
public class SelectDonutDocPeriod extends AppHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
        DonutDocPeriod donutDocPeriod;
        try {
            donutDocPeriod = DaoFacade.getDonutWithRequests(donutDocPeriodId);
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }
        JsonObject donutDocPeriodAsJsonObject = getAsJsonObject(donutDocPeriod);
        sendJson(resp, donutDocPeriodAsJsonObject);
    }

    /**
     * @return data in FORMAT LIKE: <br>
     * {
     *  driver:"someDriver",
     *  licensePlate: "hbguegrf",
     *  palletsQty: 4,
     *  driverPhoneNumber: "+7906548976",
     *  commentForDonut:"someComment",
     *  orders:[
     *    {
     *      orderId: 1,
     *      orderNumber: "orewnoer123",
     *      finalDestinationWarehouseId: 1,
     *      boxQty:4,
     *      commentForStatus: "order status comment",
     *      orderStatusId: "STATUS",
     *      invoiceNumber: invNuumber,
     *      goodsCost: 12000.03,
     *      orderPalletsQty: 4
     *    }
     *   ]
     * }
     *
     * @param donutDocPeriod
     *
     */
    private JsonObject getAsJsonObject(DonutDocPeriod donutDocPeriod) {
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder
                .add("driver", donutDocPeriod.getDriver())
                .add("licensePlate", donutDocPeriod.getLicensePlate())
                .add("palletsQty", donutDocPeriod.getPalletsQty())
                .add("driverPhoneNumber", donutDocPeriod.getDriverPhoneNumber())
                .add("commentForDonut", donutDocPeriod.getCommentForDonut())
                .add("supplierInn", donutDocPeriod.getSupplierUser().getSupplier().getInn());

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        Set<Order> orders = donutDocPeriod.getOrders();
        for (Order order : orders) {
            jsonArrayBuilder.add(
                    Json.createObjectBuilder()
                            .add("orderId", order.getOrderId())
                            .add("orderNumber", order.getOrderNumber())
                            .add("finalDestinationWarehouseId", order.getFinalDestinationWarehouse().getWarehouseId())
                            .add("boxQty", order.getBoxQty())
                            .add("commentForStatus", order.getCommentForStatus())
                            .add("orderStatusId", order.getOrderStatus().name())
                            .add("invoiceNumber", order.getInvoiceNumber())
                            .add("goodsCost", order.getGoodsCost())
                            .add("orderPalletsQty", order.getOrderPalletsQty())
            );
        }
        resultBuilder.add("orders", jsonArrayBuilder);
        return resultBuilder.build();
    }
}
