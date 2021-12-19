package com.zibert.servlets.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for proceeding to filling order details
 * Role: user
 */

@WebServlet("/rent")
public class RentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "rent_order.jsp";

        // get dates parameters from jsp
        String carId = req.getParameter("id");
        String passport = req.getParameter("passport");
        int driver = 1;
        if (req.getParameter("driver") == null) {
            driver = 0;
        }
        String rentBeg = req.getParameter("rent-start");
        String rentFin = req.getParameter("rent-end");

        if (carId == null) {
            carId = String.valueOf(req.getAttribute("id"));
        }
        if (passport == null) {
            passport = (String) req.getAttribute("passport");
        }
        if (req.getAttribute("driver") != null) {
            driver = (int) req.getAttribute("driver");
        }
        if (rentBeg == null && rentFin == null) {
            rentBeg = (String) req.getAttribute("rent-start");
            rentFin = (String) req.getAttribute("rent-end");
        }

        req.setAttribute("id", Integer.parseInt(carId));
        req.setAttribute("driver", driver);
        req.setAttribute("passport", passport);
        req.setAttribute("rentBeg", rentBeg);
        req.setAttribute("rentFin", rentFin);

        req.getRequestDispatcher(address).forward(req, resp);

    }
}
