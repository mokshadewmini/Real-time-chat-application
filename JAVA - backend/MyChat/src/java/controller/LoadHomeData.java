package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibenateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
public class LoadHomeData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject responseJson = new JsonObject();

        responseJson.addProperty("success", false);
        responseJson.addProperty("message", "Unable to process your request");

        try {

            String user_id = request.getParameter("id");

            Session session = HibenateUtil.getSessionFactory().openSession();

            User user = (User) session.get(User.class, Integer.parseInt(user_id));
            //user (online) 1 status Update

            User_Status user_Status = (User_Status) session.get(User_Status.class, 1);

            user.setUser_Status(user_Status);
            session.update(user);

            //get oTHER uSER
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.ne("id", user.getId()));

            List<User> userList = criteria1.list();
            //remove password

            //get Other user one by one
            JsonArray jsonChatItemArray = new JsonArray();
            for (User OtherUser : userList) {

                //get Last convercation
                Criteria criteria2 = session.createCriteria(Chat.class);
                criteria2.add(Restrictions.or(
                        Restrictions.and(
                                Restrictions.eq("form_user", user),
                                Restrictions.eq("to_user", OtherUser)
                        ),
                        Restrictions.and(
                                Restrictions.eq("form_user", OtherUser),
                                Restrictions.eq("to_user", user)
                        )
                ));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                //create chat item to send font-end data
                JsonObject chatItem = new JsonObject();
                chatItem.addProperty("other_user_id", OtherUser.getId());
                chatItem.addProperty("other_user_mobile", OtherUser.getMobile());

                chatItem.addProperty("other_user_name", OtherUser.getFname() + " " + OtherUser.getLname());
                chatItem.addProperty("other_user_status", OtherUser.getUser_Status().getId());

                //chaeck aveter image
                String appPath = getServletContext().getRealPath("");
                String savePath = appPath.replace("build\\web", "web");
                String otherUserAvaterImagePath = savePath + File.separator + "profile_images" + File.separator + OtherUser.getMobile() + ".png";
                File file = new File(otherUserAvaterImagePath);

                if (file.exists()) {
                    //aveter image found
                    chatItem.addProperty("avaterImageFound", true);
                } else {
                    //aveter image not found
                    chatItem.addProperty("avaterImageFound", false);

                }
                //get chat List
                List<Chat> chatList = criteria2.list();
                SimpleDateFormat dataeFromates = new SimpleDateFormat("yyy,MM,dd hh:ss a");

                if (criteria2.list().isEmpty()) {
//                    no chat
                    chatItem.addProperty("message", "Start New Conversation.");
                    chatItem.addProperty("dateTime", dataeFromates.format(user.getDate()));

                    chatItem.addProperty("chat_status_id", 1);
                } else {
                    //found chat
                    chatItem.addProperty("message", chatList.get(0).getMessage());
                    chatItem.addProperty("dateTime", dataeFromates.format(chatList.get(0).getDate_time()));
                    chatItem.addProperty("chat_status_id", chatList.get(0).getChat_status().getId());
                }

                Criteria chatStatusCriteria = session.createCriteria(Chat_Status.class);
                chatStatusCriteria.add(Restrictions.eq("name", "unseen"));

                Chat_Status chat_status = (Chat_Status) chatStatusCriteria.uniqueResult();

                Criteria criteria3 = session.createCriteria(Chat.class);
                criteria3.add(Restrictions.and(
                        Restrictions.eq("form_user", OtherUser),
                        Restrictions.eq("to_user", user),
                        Restrictions.eq("chat_status", chat_status)
                ));

                criteria3.setProjection(Projections.rowCount());

                Long count = (Long) criteria3.uniqueResult(); // Use Long to match the expected return type

                chatItem.addProperty("count", count != null ? count.intValue() : 0); // Handle null case

                jsonChatItemArray.add(chatItem);

            }

            //user Chat Lists
            //user Send
            responseJson.addProperty("success", true);
            responseJson.addProperty("message", "Success");

            responseJson.add("jsonChatItemArray", gson.toJsonTree(jsonChatItemArray));
            session.beginTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJson));

    }

}
