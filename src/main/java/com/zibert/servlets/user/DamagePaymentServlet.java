package com.zibert.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for paying damage receipt
 * Role: user
 */

@WebServlet("/damage_payment")
public class DamagePaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "paid_damage.jsp";

        int orderId = Integer.parseInt(req.getParameter("order_id"));

        DBManager dbManager = DBManager.getInstance();
        Order order;
        try {
            order = dbManager.getOrderById(orderId);
            dbManager.updateDamageReceiptStatusAsPaidToday(order);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_orders");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(address);
    }
}
