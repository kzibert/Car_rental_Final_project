package com.zibert.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.entity.Car;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for updating car's details
 * Role: admin
 */

@WebServlet("/car_update")
public class CarUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_manage";

        String updateCarError = req.getParameter("updateCarError");
        if ("yes".equals(updateCarError)) {
            req.setAttribute("error", "Car status hasn't been updated");
            req.setAttribute("back", "car_manage");
            address = "error.jsp";
            req.getRequestDispatcher(address).forward(req, resp);
            return;
        }

        List<Brand> brands = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        try {
            brands = dbManager.getAllBrands();
        } catch (DBException e) {
            req.setAttribute("error", "Brand list is not available at the moment");
            req.setAttribute("back", "car_manage");
            address = "error.jsp";
        }

        req.setAttribute("brands", brands);

        req.getRequestDispatcher(address).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_update";

        DBManager dbManager = DBManager.getInstance();

        int id = Integer.parseInt(req.getParameter("id"));
        int brandiD = Integer.parseInt(req.getParameter("brand"));
        String model = req.getParameter("model");
        String qualityClass = req.getParameter("quality_class");
        int price = Integer.parseInt(req.getParameter("price"));
        String status = req.getParameter("status");

        Car car = new Car();
        car.setBrandId(brandiD);
        car.setModel(model);
        car.setQualityClass(qualityClass);
        car.setPrice(price);
        car.setCarStatus(status);

        try {
            dbManager.updateCar(car, id);
        } catch (DBException e) {
            address = "car_update?updateCarError=yes";
        }

        resp.sendRedirect(address);
    }
}
