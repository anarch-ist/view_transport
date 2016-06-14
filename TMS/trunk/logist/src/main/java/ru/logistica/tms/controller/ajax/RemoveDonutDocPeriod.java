package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.util.EmailUtils;
import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteDonut")
public class RemoveDonutDocPeriod extends AjaxHttpServlet {
    private String host;
    private String port;
    private String fromAddress;
    private String pass;

    public void init() {
        // reads SMTP server setting from web.xml file
        ServletContext context = getServletContext();
        host = context.getInitParameter("smtpHost");
        port = context.getInitParameter("smtpPort");
        fromAddress = context.getInitParameter("smtpUserAddress");
        pass = context.getInitParameter("smtpPass");
    }
    // testsmpt@bk.ru
    // testtest12345
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject dataForDelete = JsonUtils.parseStringAsObject(req.getParameter("dataForDelete"));
        long donutDocPeriodId = dataForDelete.getJsonNumber("donutDocPeriodId").longValueExact();
        if (dataForDelete.containsKey("emailContent")) {

            String exampleToEmail = "ask_sergey@inbox.ru"; // should be supplier email or supplierUser email??
            // reads form fields
            String toAddress = exampleToEmail;
            String subject = "Удаление маршрутного листа";
            String content = dataForDelete.getString("emailContent");
            System.out.println("BEFORE SEND");
            try {
                EmailUtils.sendEmailSSL(host, port, fromAddress, pass, toAddress, subject, content);
                System.out.println("AFTER SEND");
                //resultMessage = "The e-mail was sent successfully";
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e);
            } finally {
//                request.setAttribute("Message", resultMessage);
//                getServletContext().getRequestDispatcher("/Result.jsp").forward(
//                        request, response);
            }
        }
        //DaoFacade.deleteDonutWithRequests(donutDocPeriodId);
        req.getRequestDispatcher("/getTableData").forward(req, resp);
    }

    public void sendEmail(String fromEmail, String username, String password, String toEmail, String subject, String message) {

    }
}
