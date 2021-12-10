package com.zibert.controller.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Car;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rent_order_redirect")
public class RentOrderRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // getting attributes from session
        int carId = 0;
        if (req.getSession().getAttribute("id") != null) {
            carId = (int) req.getSession().getAttribute("id");
        }
        String passport = null;
        if (req.getSession().getAttribute("passport") != null) {
            passport = (String) req.getSession().getAttribute("passport");
        }
        int driver = 0;
        if (req.getSession().getAttribute("driver") != null) {
            driver = (int) req.getSession().getAttribute("driver");
        }
        String rentBeg = null;
        if (req.getSession().getAttribute("rent-start") != null) {
            rentBeg = (String) req.getSession().getAttribute("rent-start");
        }
        String rentFin = null;
        if (req.getSession().getAttribute("rent-end") != null) {
            rentFin = (String) req.getSession().getAttribute("rent-end");
        }
        String dataInputError = null;
        if (req.getSession().getAttribute("dateInputError") != null) {
            dataInputError = (String) req.getSession().getAttribute("dateInputError");
        }
        String busy = null;
        if (req.getSession().getAttribute("busy") != null) {
            busy = (String) req.getSession().getAttribute("busy");
        }
        int days = 0;
        if (req.getSession().getAttribute("days") != null) {
            days = (int) req.getSession().getAttribute("days");
        }
        Order order = new Order();
        if (req.getSession().getAttribute("order") != null) {
            order = (Order) req.getSession().getAttribute("order");
        }

        String address = "rent";
        if (order.getId() != 0) {
            address = "order_receipt.jsp";
        }

        //deleting attributes from session
        req.getSession().removeAttribute("id");
        req.getSession().removeAttribute("passport");
        req.getSession().removeAttribute("driver");
        req.getSession().removeAttribute("rent-start");
        req.getSession().removeAttribute("rent-end");
        req.getSession().removeAttribute("dateInputError");
        req.getSession().removeAttribute("busy");
        req.getSession().removeAttribute("days");
        req.getSession().removeAttribute("order");

        //setting attributes for jsp
        req.setAttribute("id", carId);
        req.setAttribute("passport", passport);
        req.setAttribute("driver", driver);
        req.setAttribute("rent-start", rentBeg);
        req.setAttribute("rent-end", rentFin);
        req.setAttribute("dateInputError", dataInputError);
        req.setAttribute("busy", busy);
        req.setAttribute("days", days);
        req.setAttribute("order", order);
        req.setAttribute("back", "choose_car");

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
