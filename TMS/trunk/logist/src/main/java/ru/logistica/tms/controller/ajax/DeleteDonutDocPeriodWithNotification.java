package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.DaoScriptException;
import ru.logistica.tms.util.EmailUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteDonutWithNotification")
public class DeleteDonutDocPeriodWithNotification extends HttpServlet{
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long donutDocPeriodId = Long.parseLong(req.getParameter("donutDocPeriodId"));
        String emailContent = req.getParameter("emailContent");

        String supplierUserEmail = null;
        try {
            supplierUserEmail = DaoFacade.getSupplierEmailByDonutDocPeriodId(donutDocPeriodId);
        } catch (DaoScriptException e) {
            throw new ServletException(e);
        }

        String subject = "Удаление маршрутного листа";
        String message = "Сообщение отправлено";
        try {
            EmailUtils.sendEmailSSL(host, port, fromAddress, pass, supplierUserEmail, subject, emailContent);
            getServletContext().getRequestDispatcher("/deleteDonut").forward(req, resp);
        } catch (Exception e) {
            message = e.getMessage();
            throw new ServletException(e);
        } finally {
            req.setAttribute("message", message);
        }

    }
}
