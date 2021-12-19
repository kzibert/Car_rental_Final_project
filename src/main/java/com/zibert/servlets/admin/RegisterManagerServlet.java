package com.zibert.servlets.admin;

import com.zibert.DAO.exceptions.DBException;
import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for new managers registration
 * Role: admin
 */

@WebServlet("/register_manager")
public class RegisterManagerServlet extends HttpServlet {

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
        user.setRoleId(2); // sets role as Manager by default during registration
        user.setStatus(0);
        user.setBlockDescr(null);

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertUser(user);
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "register_manager.jsp");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect("manager_registered.jsp");
    }
}
