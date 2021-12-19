package com.zibert.servlets.manager;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for approving new orders
 * Role: manager
 */

@WebServlet("/approve_order")
public class OrderApprovalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "manager_orders";

        int order_id = Integer.parseInt(req.getParameter("order_id"));

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.approveOrderById(order_id);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "manager_orders");
            address = "error.jsp";
        }

        resp.sendRedirect(address);
    }
}
