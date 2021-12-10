package com.zibert.controller.servlets.admin;

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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/car_add")
public class CarAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_manage";

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


        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertCar(car);
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "car_manage");
            address = "error.jsp";
        }

        resp.sendRedirect(address);
    }
}
