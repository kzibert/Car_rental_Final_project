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
 * Servlet for paying order receipt
 * Role: user
 */

@WebServlet("/order_payment")
public class OrderPaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "paid_order.jsp";

        int orderId = Integer.parseInt(req.getParameter("order_id"));

        DBManager dbManager = DBManager.getInstance();
        Order order;
        try {
            order = dbManager.getOrderById(orderId);
            dbManager.updateOrderReceiptStatusAsPaidToday(order);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_orders");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        req.getSession().removeAttribute("orderReceipt");
        req.getSession().removeAttribute("car");
        req.getSession().removeAttribute("days");
        req.getSession().removeAttribute("order");

        resp.sendRedirect(address);
    }
}
