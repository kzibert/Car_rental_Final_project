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
 * Servlet for editing car's details
 * Role: admin
 */

@WebServlet("/car_edit")
public class CarEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_edit.jsp";

        int id = Integer.parseInt(req.getParameter("id"));

        List<Brand> brands = new ArrayList<>();
        Car car = new Car();
        try {
            DBManager dbManager = DBManager.getInstance();
            brands = dbManager.getAllBrands();
            car = dbManager.getCarById(id);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "car_manage");
            address = "error.jsp";
        }

        req.setAttribute("brands", brands);
        req.setAttribute("car", car);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
