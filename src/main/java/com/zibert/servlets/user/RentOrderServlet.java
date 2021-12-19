package com.zibert.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Car;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.entity.OrderReceipt;
import com.zibert.DAO.entity.User;
import com.zibert.DAO.exceptions.BusyCarException;
import com.zibert.DAO.exceptions.DBException;
import com.zibert.DAO.exceptions.DateCheckException;
import com.zibert.service.DatesManagement;
import com.zibert.validation.DateValidation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * Servlet for renting car for certain dates
 * Role: user
 */

@WebServlet("/rent_order")
public class RentOrderServlet extends HttpServlet {

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
            rentStart = new SimpleDateFormat("yyyy-MM-dd").parse(rentBeg);
            rentEnd = new SimpleDateFormat("yyyy-MM-dd").parse(rentFin);
            if (!(dateValidation.checkIfSecondDateIsBigger(rentBeg, rentFin))) {
                req.getSession().setAttribute("dateInputError", "Rent end date should be bigger than rent start date");
                req.getSession().setAttribute("id", carId);
                req.getSession().setAttribute("passport", passport);
                req.getSession().setAttribute("driver", driver);
                req.getSession().setAttribute("rent-start", rentBeg);
                req.getSession().setAttribute("rent-end", rentFin);
                resp.sendRedirect("rent_order");
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
            address = "rent_order";
        } catch (BusyCarException e) {
            req.getSession().setAttribute("busy", e.getMessage());
            req.getSession().setAttribute("id", carId);
            req.getSession().setAttribute("passport", passport);
            req.getSession().setAttribute("driver", driver);
            req.getSession().setAttribute("rent-start", rentBeg);
            req.getSession().setAttribute("rent-end", rentFin);
            resp.sendRedirect("rent_order");
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
