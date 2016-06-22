package ru.logistica.tms.controller.ajax;

import ru.logistica.tms.dao.userDao.User;

import javax.json.JsonObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AjaxHttpServlet extends HttpServlet {

    protected User getUser(HttpServletRequest req) {
        return (User)req.getSession(false).getAttribute("user");
    }

    protected void sendJson(HttpServletResponse response, JsonObject jsonObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }
}
