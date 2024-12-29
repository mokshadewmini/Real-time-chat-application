package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibenateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Hello");

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
//        responseJsonObject.addProperty("message", "Error");

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        Part image = request.getPart("image");

        if (fname.isEmpty()) {
//             First name is required.
            responseJsonObject.addProperty("message", "First name is required.");

        } else if (fname.length() > 50) {
//            First name must be at least 50 characters lowast.
            responseJsonObject.addProperty("message", "First name must be at least 50 characters lowast.");
        } else if (lname.length() > 50) {
            //            Last name must be at least 50 characters lowast.
            responseJsonObject.addProperty("message", "Last name must be at least 50 characters lowast.");
        } else if (lname.isEmpty()) {
            //             Last name is required.
            responseJsonObject.addProperty("message", "Last name is required.");
        } else if (mobile.isEmpty()) {
            //            Mobile Number is required.
            responseJsonObject.addProperty("message", "Mobile number is required.");
        } else if (!Validation.isMobileNumberValid(mobile)) {
//            mobile number not validate please check this number!
            responseJsonObject.addProperty("message", " Mobile number not Valid Please Check this Number!");
        } else if (password.isEmpty()) {
            //            password is required.
            responseJsonObject.addProperty("message", " password is required.");
        } else if (!Validation.isPasswordValid(password)) {
            System.out.println(password);
//            Password not validate please check this number!
            responseJsonObject.addProperty("message", "password with at least one lowercase letter, uppercase letter digit, special character, and a minimum of 8 characters.");

        } else if (image == null){
            responseJsonObject.addProperty("message", "Please select profile image.");
        }else{
            Session session = HibenateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("mobile", mobile));

            if (!criteria1.list().isEmpty()) {
                //mobile used
                responseJsonObject.addProperty("message", "Mobile number allready register!");

            } else {

                User user = new User();

                user.setFname(fname);
                user.setLname(lname);
                user.setMobile(mobile);
                user.setPassword(password);
                user.setDate(new Date());

//                status 2 is offline
                User_Status user_Status = (User_Status) session.get(User_Status.class, 2);
                user.setUser_Status(user_Status);

                session.save(user);
                session.beginTransaction().commit();

                //user Found
                    //Image selected
                    
                    //Image selected

                    String serverPath = request.getServletContext().getRealPath("");
                    String newApplicationPath = serverPath.replace("build" + File.separator + "web", "web");
                    File aveterImagePath = new File(newApplicationPath + File.separator + "profile_images");
                    aveterImagePath.mkdir();

                    System.out.println(aveterImagePath);

                    File file1 = new File(aveterImagePath, "" + mobile + ".png");
                    InputStream inputStream1 = image.getInputStream();
                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    responseJsonObject.addProperty("success", true);

                    responseJsonObject.addProperty("message", "Registation Complete!");

                   
               
            }

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }

}
