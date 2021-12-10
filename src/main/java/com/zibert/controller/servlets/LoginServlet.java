package com.zibert.controller.servlets;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/auth")
public class LoginServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        DBManager dbManager = DBManager.getInstance();
        User user = dbManager.getUser(email);

        String address = "no_user_found.jsp";

        if (user != null && user.getPassword().equals(password)) {
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("role", user.getRoleId());
            if (user.getStatus() == 1) {
                address = "blocked_user.jsp";
                log.info("Blocked user access attempt. ID: " + user.getId());
            } else if (user.getRoleId() == 1) {
                address = "logged_admin.jsp";
                log.info("Admin logged in. ID: " + user.getId());
            } else if (user.getRoleId() == 2) {
                address = "logged_manager.jsp";
                log.info("Manager logged in. ID: " + user.getId());
            } else if (user.getRoleId() == 3) {
                address = "user_main.jsp";
                log.info("User logged in. ID: " + user.getId());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(formatter);
        req.getSession().setAttribute("today", today);

        resp.sendRedirect(address);
    }
}

