package com.zibert.servlets;

import com.zibert.DAO.exceptions.DBException;
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

/**
 * Servlet for a new user registration
 * Role: none
 */

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if ("success".equals(req.getParameter("registration"))) {
            req.setAttribute("registration", "success");
        }
        req.getRequestDispatcher("auth").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setRoleId(3); // sets role as USER by default during registration
        user.setStatus(0);
        user.setBlockDescr(null);

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertUser(user);
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "registration.jsp");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect("register?registration=success");

        log.info("New user was registered. User ID: " + user.getId());
    }
}
