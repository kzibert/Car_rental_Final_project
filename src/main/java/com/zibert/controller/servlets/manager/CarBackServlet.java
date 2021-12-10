package com.zibert.controller.servlets.manager;

import com.zibert.DAO.exceptions.DBException;
import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.DamageReceipt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/car_back")
public class CarBackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "rented_cars";

        int orderId = Integer.parseInt(req.getParameter("order_id"));
        String cost = req.getParameter("damage_cost");
        DBManager dbManager = DBManager.getInstance();
        if (!cost.equals("")) {
            int damageCost = Integer.parseInt(cost);
            DamageReceipt damageReceipt = new DamageReceipt();
            damageReceipt.setDamageCost(damageCost);
            damageReceipt.setDamagePaymentStatus(0);
            damageReceipt.setDamagePaymentDate(null);
            damageReceipt.setOrder_id(orderId);
            try {
                dbManager.insertDamageReceiptAndUpdateCarStatusAsDamaged(damageReceipt, orderId);
                dbManager.updateOrderStatusAsClosed(orderId);
            } catch (DBException ex) {
                req.setAttribute("error", ex.getMessage());
                req.setAttribute("back", "rented_cars");
                address = "error.jsp";
            }
        }

        resp.sendRedirect(address);
    }
}
