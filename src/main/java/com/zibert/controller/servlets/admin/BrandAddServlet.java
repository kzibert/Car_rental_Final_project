package com.zibert.controller.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.entity.User;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/brand_add")
public class BrandAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "brand_list";

        String name = req.getParameter("brand");

        Brand brand = new Brand();
        brand.setName(name);

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertBrand(brand);
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "brand_list");
            address = "error.jsp";
        }

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
