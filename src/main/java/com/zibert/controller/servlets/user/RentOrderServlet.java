package com.zibert.controller.servlets.user;

import com.zibert.DAO.exceptions.BusyCarException;
import com.zibert.DAO.exceptions.DBException;
import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DateCheckException;
import com.zibert.DAO.entity.Car;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.entity.OrderReceipt;
import com.zibert.DAO.entity.User;
import com.zibert.service.DatesManagement;
import com.zibert.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

@WebServlet("/rent_order")
public class RentOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String address = null;

        // get parameters
        int carId = Integer.parseInt(req.getParameter("id"));
        String passport = req.getParameter("passport");
        if (passport.equals("")) {
            passport = (String) req.getAttribute("passport");
        }
        int driver = 0;
        String driverCheckbox = req.getParameter("driver");
        if ("1".equals(driverCheckbox)) {
            driver = 1;
        }

        String rentBeg = req.getParameter("rent-start");
        String rentFin = req.getParameter("rent-end");

        DatesManagement datesManagement = new DatesManagement();
        DateValidation dateValidation = new DateValidation();

        Date rentStart = null;
        Date rentEnd = null;

        try {
            rentStart = datesManagement.parseDate(rentBeg);
            rentEnd = datesManagement.parseDate(rentFin);
            if (!(dateValidation.checkIfSecondDateIsBigger(rentBeg, rentFin))) {
                req.getSession().setAttribute("dateInputError", "Rent end date should be bigger than rent start date");
                req.getSession().setAttribute("id", carId);
                req.getSession().setAttribute("passport", passport);
                req.getSession().setAttribute("driver", driver);
                req.getSession().setAttribute("rent-start", rentBeg);
                req.getSession().setAttribute("rent-end", rentFin);
                resp.sendRedirect("rent_order_redirect");
                return;
            }
        } catch (DateCheckException | ParseException e) {
            req.setAttribute("error", "Something went wrong, please, try again");
            req.setAttribute("back", "choose_car");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        // getting user id
        User user = (User) req.getSession().getAttribute("user");
        int userId = user.getId();

        // preparing order for insert into DB
        Order order = new Order();
        order.setRentStart(rentStart);
        order.setRentEnd(rentEnd);
        order.setPassport(passport);
        order.setDriver(driver);
        order.setUserId(userId);
        order.setCarId(carId);
        order.setCancelComments(null);
        order.setStatus("Order received");

        int orderId;

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertOrder(order);
            orderId = order.getId();
            address = "rent_order_redirect";
        } catch (BusyCarException e) {
            req.getSession().setAttribute("busy", e.getMessage());
            req.getSession().setAttribute("id", carId);
            req.getSession().setAttribute("passport", passport);
            req.getSession().setAttribute("driver", driver);
            req.getSession().setAttribute("rent-start", rentBeg);
            req.getSession().setAttribute("rent-end", rentFin);
            resp.sendRedirect("rent_order_redirect");
            return;
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "rent?id=" + carId + "&rent-start=" + rentBeg + "&rent-end=" + rentFin + "&passport=" + passport + "&driver=" + driver);
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        // finding the number of days between rent start and end dates
        int days = new DatesManagement().countDaysBetweenTwoDates(rentBeg, rentFin);

        // calc of the cost of rent for the given period
        Car car = new Car();
        try {
            car = dbManager.getCarById(carId);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "choose_car");
            address = "error.jsp";
        }

        int carPricePerDay = car.getPrice();

        int cost = carPricePerDay * days;

        // preparing order receipt for insert into DB
        OrderReceipt orderReceipt = new OrderReceipt();
        orderReceipt.setCost(cost);
        orderReceipt.setPaymentStatus(0);
        orderReceipt.setPaymentDate(null);
        orderReceipt.setOrder_id(orderId);
        dbManager.insertOrderReceipt(orderReceipt);

        Order fullOrder = new Order();
        try {
            fullOrder = dbManager.getOrderById(orderId);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "choose_car");
            address = "error.jsp";
        }

        // placing attributes for jsp
        req.getSession().setAttribute("days", days);
        req.getSession().setAttribute("order", fullOrder);

        resp.sendRedirect(address);

    }
}
