package com.zibert.controller.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.exceptions.DBException;
import com.zibert.service.DatesManagement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/show_damage_receipt")
public class ShowDamageReceiptServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "damage_receipt.jsp";

        int orderId = Integer.parseInt(req.getParameter("order_id"));
        DBManager dbManager = DBManager.getInstance();
        Order order = new Order();
        try {
            order = dbManager.getOrderById(orderId);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_orders");
            address = "error.jsp";
        }

        req.setAttribute("order", order);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
