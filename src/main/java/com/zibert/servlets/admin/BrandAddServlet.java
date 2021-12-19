package com.zibert.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for adding new brands
 * Role: admin
 */

@WebServlet("/brand_add")
public class BrandAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "Brand hasn't been added");
        req.setAttribute("back", "brand_list");
        req.getRequestDispatcher("error.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "brand_list";

        String name = req.getParameter("brand");

        Brand brand = new Brand();
        brand.setName(name);

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.insertBrand(brand);
        } catch (DBException ex) {
            address = "brand_add";
        }
        resp.sendRedirect(address);
    }
}
