package com.zibert.controller.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.entity.User;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user_status_update")
public class UserUpdateStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "user_manage";

        List<User> users = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        try {
            users = dbManager.getAllUsers();
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "logged_admin.jsp");
            address = "error.jsp";
        }

        int id = Integer.parseInt(req.getParameter("id"));
        int status = Integer.parseInt(req.getParameter("status"));

        User user = new User();
        user.setId(id);
        user.setStatus(status);

        try {
            dbManager.updateUserStatus(user, id);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_manage");
            address = "error.jsp";
        }

        req.setAttribute("users", users);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
