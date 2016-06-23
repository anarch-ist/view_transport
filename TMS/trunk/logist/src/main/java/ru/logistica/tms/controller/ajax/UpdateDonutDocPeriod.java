package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.dto.DonutUpdateData;
import ru.logistica.tms.dto.ValidateDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateDonut")
public class UpdateDonutDocPeriod extends AppHttpServlet {
    /*var exampleData = {
                period:{periodBegin:123499000000, periodEnd: 11223234500000},
                driver:"someDriver",
                licensePlate: "hbguegrf",
                palletsQty: 4,
                driverPhoneNumber: "+7906548976",
                commentForDonut:"someComment",
                orders:[
                    {
                        orderId: 1,  // number or null
                        orderNumber: "orewnoer123",
                        finalDestinationWarehouseId: 1,
                        boxQty:4,
                        commentForStatus: "order status comment",
                        orderStatusId: "STATUS"
                    }
                ]
            };
    * */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DonutUpdateData donutUpdateData = new DonutUpdateData(req.getParameter("updatedDonut"));
            DaoFacade.updateDonutWithRequests(getUser(req).getUserId(), donutUpdateData);
        } catch (ValidateDataException | DaoScriptException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }
}
