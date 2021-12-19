package com.zibert.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.User;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for showing all users
 * Role: admin
 */

@WebServlet("/user_manage")
public class UserManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "user_manage.jsp";

        req.setCharacterEncoding("UTF-8");

        List<User> users = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        try {
            users = dbManager.getAllUsers();
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "logged_admin.jsp");
            address = "error.jsp";
        }

        req.setAttribute("users", users);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
