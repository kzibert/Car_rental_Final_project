package com.zibert.controller.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.entity.Car;
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
import java.util.List;

@WebServlet("/show_order_receipt")
public class ShowOrderReceiptServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "order_receipt.jsp";

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

        Date rentStart = order.getRentStart();
        Date rentEnd = order.getRentEnd();
        DatesManagement datesManagement = new DatesManagement();
        int days = 0;
        if (rentStart != null && rentEnd != null) {
            days = datesManagement.countDaysBetweenTwoDates(rentStart, rentEnd);
        } else {
            req.setAttribute("error", "Something went wrong. Please, try again");
            req.setAttribute("back", "user_orders");
            address = "error.jsp";
        }

        req.setAttribute("order", order);
        req.setAttribute("days", days);
        req.setAttribute("back", "user_orders");

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
