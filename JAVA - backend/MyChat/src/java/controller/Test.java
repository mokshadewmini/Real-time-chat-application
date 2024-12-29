/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.User_Status;
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


@WebServlet(name = "Test", urlPatterns = {"/Test"})
public class Test extends HttpServlet {

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         
         try {
            Session session = HibenateUtil.getSessionFactory().openSession();
           User_Status chat_status = new User_Status();
            chat_status.setName("Seen");
            
            session.save(chat_status);
            session.beginTransaction().commit();
            System.out.println("done");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
          
    }

   
}

