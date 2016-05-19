package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dto.AuthResult;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.html").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String passMd5 = request.getParameter("password");
        AuthResult authResult = DaoFacade.checkUser(login, passMd5);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (authResult.isAuthSuccess()) {
            JsonObject jsonObject = objectBuilder.add("redirect", "main").build();
            HttpSession session = request.getSession();
            session.setAttribute("user", authResult.getUser());
            sendJson(response, jsonObject);
        } else if (authResult.isNoSuchLogin()) {
            JsonObject jsonObject = objectBuilder.add("loginErrorText", "логин указан неверно").add("passwordErrorText", "").build();
            sendJson(response, jsonObject);
        } else if (authResult.isNoSuchPassword()) {
            JsonObject jsonObject = objectBuilder.add("loginErrorText", "").add("passwordErrorText", "пароль указан неверно").build();
            sendJson(response, jsonObject);
        } else if (authResult.isSystemError()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ошибка сервера при авторизации");
        }
    }

    private void sendJson(HttpServletResponse response, JsonObject jsonObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }
}
