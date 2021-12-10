package com.zibert.controller.servlets.manager;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cancel_order")
public class OrderCancelServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "manager_orders";

        req.setCharacterEncoding("UTF-8");

        int order_id = Integer.parseInt(req.getParameter("order_id"));
        String comment = req.getParameter("comment");
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.cancelOrderById(comment, order_id);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "cancelled_orders");
            address = "error.jsp";
        }

        resp.sendRedirect(address);
    }
}
