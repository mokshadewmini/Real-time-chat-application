package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibenateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Gson gson = new Gson();
        Session session = HibenateUtil.getSessionFactory().openSession();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        responseJsonObject.addProperty("message", "");

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        String mobile = requestJsonObject.get("mobile").getAsString();
        String password = requestJsonObject.get("password").getAsString();

        if (mobile.isEmpty()) {
            responseJsonObject.addProperty("message", "Please enter mobile number!");

        } else {

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("mobile", mobile));

            if (!criteria1.list().isEmpty()) {
                User user = (User) criteria1.list().get(0);

                if (!password.equals(user.getPassword())) {
//               password worng not match this password
                    responseJsonObject.addProperty("message", "Password does not match please check password!");

                } else {
//                Match password
                    user = (User) criteria1.uniqueResult();

                    responseJsonObject.addProperty("success", true);

                    responseJsonObject.addProperty("message", "Sign In Success!");
                    responseJsonObject.add("user", gson.toJsonTree(user));

                }
                session.close();

            }else{
                 responseJsonObject.addProperty("message", "User Account not Found");
            }

        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }

}
