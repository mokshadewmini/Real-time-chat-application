package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;

import entity.Chat_Status;
import entity.User;
import java.io.File;
import java.io.IOException;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "OpenChat", urlPatterns = {"/OpenChat"})
public class OpenChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibenateUtil.getSessionFactory().openSession();

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);

        String meId = request.getParameter("meId");
        String friendId = request.getParameter("friendId");

        // get user in db
        User me = (User) session.get(User.class, Integer.parseInt(meId));
        // get friend in db
        User friend = (User) session.get(User.class, Integer.parseInt(friendId));

        // get conversation between them
        Criteria criteria1 = session.createCriteria(Chat.class);
        criteria1.add(
                Restrictions.or(
                        Restrictions.and(
                                Restrictions.eq("form_user", me),
                                Restrictions.eq("to_user", friend)
                        ),
                        Restrictions.and(
                                Restrictions.eq("form_user", friend),
                                Restrictions.eq("to_user", me)
                        )
                )
        );

        // sort coversation between them
        criteria1.addOrder(Order.asc("date_time"));

        // get chat history
        List<Chat> chatList = criteria1.list();

        // get chat status in db
        Chat_Status chatSeen = (Chat_Status) session.get(Chat_Status.class, 1);

        // create Json Array
        JsonArray chatArray = new JsonArray();

        // create time formatter
        SimpleDateFormat timeFomatter = new SimpleDateFormat("hh:mm a");

        // separate own messages and friend messages
        for (Chat chat : chatList) {

            //System.out.println("chat id : " + chat.getId());
            // one by one chat item
            JsonObject chatItem = new JsonObject();

            if (chat.getForm_user().getId() == me.getId()) {
                // only I send messages
                chatItem.addProperty("is_my_chat", true);
                chatItem.addProperty("chat_status", chat.getChat_status().getName());  // seen, unseen

                // System.out.println("I send message : " + chat.getMessage() + " to : " + chat.getToUser().getFirstName());
            } else {
                // only friend send messages to me
                chatItem.addProperty("is_my_chat", false);

                // change chat status unseen to seen ( 1=seen, 2=unseen )
                if (chat.getChat_status().getId() == 2) {
                    chat.setChat_status(chatSeen);
                    // update
                    session.update(chat);
                }
            }

            // common own and friend
            chatItem.addProperty("chat_id", chat.getId());
            chatItem.addProperty("chat_message", chat.getMessage());
            chatItem.addProperty("chat_time", timeFomatter.format(chat.getDate_time()));

            // new added code start : here
            // check I sended images for my friend
            // get application path
            String appPath = getServletContext().getRealPath("");
            String savePath = appPath.replace("build\\web", "web");

            File file = new File( savePath + File.separator + "profile_images" + File.separator + friend.getMobile() + ".png");

            if (file.exists()) {
                //System.out.println("Image found after chat : " + chat.getId());
                chatItem.addProperty("image_found", true);
                chatItem.addProperty("image_id", chat.getId());
            } else {
                chatItem.addProperty("image_found", false);
            }

            // new added code end : here
            
            
            // chat item add one-by-one chat array
            chatArray.add(chatItem);

        }

        // commit updates
        session.beginTransaction().commit();
        session.close();

        responseJson.addProperty("success", true);
        // add chat array responseJson
        responseJson.add("chatArray", chatArray);

       // System.out.println(gson.toJson(responseJson));
        
        // send response 
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJson));

    }

}
