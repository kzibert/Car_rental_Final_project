package com.zibert.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DBException;
import com.zibert.DAO.exceptions.DateCheckException;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.entity.Car;
import com.zibert.validation.DateValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Servlet for showing all available cars
 * Role: user
 */

@WebServlet("/choose_car")
public class AvailableCarsServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(AvailableCarsServlet.class);

    private static final int shift = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "choose_car.jsp";

        req.getSession().removeAttribute("order");
        req.getSession().removeAttribute("dateInputError");

        String q_class = req.getParameter("quality_class");
        String brand = req.getParameter("brand");

        String paramPage = req.getParameter("page");
        String paramPageSize = req.getParameter("pageSize");

        int page = 1;
        int pageSize = 5;

        if (paramPage != null && paramPageSize != null) {
            page = Integer.parseInt(paramPage);
            pageSize = Integer.parseInt(paramPageSize);
        }

        int size;

        DBManager dbManager = DBManager.getInstance();

        List<Brand> brands = new ArrayList<>();
        List<String> q_cars = new ArrayList<>();
        try {
            brands = dbManager.getAllBrands();
            q_cars = dbManager.getAllCarsQualityClasses();
        } catch (DBException e) {
            log.error(e.getMessage());
        }

        // get dates parameters from jsp
        String rentBeg = req.getParameter("rent-start");
        String rentFin = req.getParameter("rent-end");
        String sort = req.getParameter("sort"); // if not null than sort

        String date1 = "1970-01-01";
        String date2 = "1970-01-02";
        if (!(rentBeg == null || rentFin == null || "".equals(rentBeg) || "".equals(rentFin))) {
            date1 = rentBeg;
            date2 = rentFin;
        }

        List<Car> cars;
        try {
            if ("price".equals(sort)) {
                cars = dbManager.getAllCarsSortedByPrice(date1, date2, pageSize * (page - 1), pageSize);
                size = dbManager.countWorkableCarsForDates(date1, date2);
            } else if ("model".equals(sort)) {
                cars = dbManager.getAllCarsSortedByModel(date1, date2, pageSize * (page - 1), pageSize);
                size = dbManager.countWorkableCarsForDates(date1, date2);
            } else if ("brand".equals(sort)) {
                int brand_id = 0;
                if (req.getParameter("brand") != null) {
                    brand_id = Integer.parseInt(req.getParameter("brand"));
                }
                cars = dbManager.getAllCarsWithBrand(brand_id, date1, date2, pageSize * (page - 1), pageSize);
                size = dbManager.countAllCarsWithBrand(brand_id, date1, date2);
            } else if ("class".equals(sort)) {
                String quality_class = req.getParameter("quality_class");
                cars = dbManager.getAllCarsWithClass(quality_class, date1, date2, pageSize * (page - 1), pageSize);
                size = dbManager.countAllCarsWithClass(quality_class, date1, date2);
            } else if (rentBeg != null && rentFin != null) {
                cars = dbManager.getWorkableCarsForDates(rentBeg, rentFin, pageSize * (page - 1), pageSize);
                size = dbManager.countWorkableCarsForDates(date1, date2);
            } else {
                cars = dbManager.getWorkableCarsForDates("1970-01-01", "1970-01-02", pageSize * (page - 1), pageSize);
                size = dbManager.countWorkableCarsForDates("1970-01-01", "1970-01-02");
            }
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_main.jsp");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        // input dates validation
        DateValidation dateValidation = new DateValidation();
        try {
            if (!(dateValidation.checkIfSecondDateIsBigger(date1, date2))) {
                req.setAttribute("dateInputError", "Rent end date should be bigger than rent start date");
            } else {
                req.removeAttribute("dateInputError");
            }
        } catch (DateCheckException e) {
            log.error(e.getMessage());
        }

        int minPagePossible = page - shift < 1 ? 1 : page - shift;

        int pageCount = (int) Math.ceil((float) size / pageSize);

        int maxPagePossible = page + shift > pageCount ? pageCount : page + shift;

        req.setAttribute("pageCount", pageCount);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("minPossiblePage", minPagePossible);
        req.setAttribute("maxPossiblePage", maxPagePossible);

        req.setAttribute("sort", sort);

        req.setAttribute("brands", brands);
        req.setAttribute("q_cars", q_cars);
        req.setAttribute("cars", cars);
        req.setAttribute("rentBeg", rentBeg);
        req.setAttribute("rentFin", rentFin);

        req.setAttribute("quality_class", q_class);
        req.setAttribute("brand", brand);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
