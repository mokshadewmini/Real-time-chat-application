package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibenateUtil;
import org.hibernate.Session;


@WebServlet(name = "SendChat", urlPatterns = {"/SendChat"})
public class SendChat extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibenateUtil.getSessionFactory().openSession();

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);

        JsonObject requestJson = gson.fromJson(request.getReader(), JsonObject.class);

        int senderId = requestJson.get("senderId").getAsInt();
        int receiverId = requestJson.get("receiverId").getAsInt();
        String message = requestJson.get("message").getAsString();

       
        User fromUser = (User) session.get(User.class, senderId);

        // get to user
        User toUser = (User) session.get(User.class, receiverId);

        // get unseen status (unseen=2)
        Chat_Status unseenStatus = (Chat_Status) session.get(Chat_Status.class, 2);

        // create new chat
        Chat chat = new Chat();
        chat.setChat_status(unseenStatus);
        chat.setDate_time(new Date());
        chat.setForm_user(fromUser);
        chat.setTo_user(toUser);
        chat.setMessage(message);
        // save new chat
        session.save(chat);
        // commit
        session.beginTransaction().commit();

        responseJson.addProperty("success", true);

        // send response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJson));

    }

}
