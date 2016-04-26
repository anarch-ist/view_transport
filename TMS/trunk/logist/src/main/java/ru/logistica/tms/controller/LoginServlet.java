package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;
import ru.logistica.tms.dao.usersDao.AbstractUser;
import ru.logistica.tms.dao.usersDao.AbstractUserDaoImpl;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.utils.ConnectionManager;
import ru.logistica.tms.dao.utils.SQLConnection;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        super.init();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            SQLConnection sqlConnection = new SQLConnection();
            ConnectionManager.setConnection(sqlConnection.getConnection());

            ConstantsDao constantsDao = new ConstantsDaoImpl();
            ConstantCollections.setUserRoles(constantsDao.getUserRoles());


            JsonReader reader = Json.createReader(request.getReader());
            JsonObject authData = (JsonObject)reader.read();
            JsonValue login = authData.get("login");
            if (!login.getValueType().equals(JsonValue.ValueType.STRING) )
                throw new RuntimeException("bad value type");

            JsonValue passwordMd5 = authData.get("password");
            if (!passwordMd5.getValueType().equals(JsonValue.ValueType.STRING) )
                throw new RuntimeException("bad value type");


            GenericUserDao<AbstractUser> abstractUserDao = new AbstractUserDaoImpl();
            AbstractUser abstractUser = abstractUserDao.getByLogin(login.toString());
            System.out.println(abstractUser);
            UsersManager


            if (abstractUser != null) {
                response.sendRedirect("success.jsp");
            } else {
                // отвечать по JSON поля: loginErrorText, passwordErrorText
                //JsonObjectBuilder
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                // Assuming your json object is **jsonObject**, perform the following, it will return your json object
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                JsonObject jsonObject = objectBuilder.add("loginErrorText", "").add("passwordErrorText", "").build();

                out.print(jsonObject);
                out.flush();

                //response.sendRedirect("login.html");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }



//        LoginService loginService = new LoginService();
//        boolean authenticated = false;
//        try {
//            authenticated = loginService.authenticate(userLogin, userPassword);
//        } catch (DaoException e) {
//            logger.error(e);
//        }
//
//        if (authenticated) {
//            response.sendRedirect("success.jsp");
//            return;
//        } else {
//            response.sendRedirect("login.jsp");
//            return;
//        }
    }
}
