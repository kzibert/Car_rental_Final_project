package com.zibert.controller.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Brand;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/brand_list")
public class BrandListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "brand_management.jsp";

        List<Brand> brands = new ArrayList<>();

        try {
            brands = DBManager.getInstance().getAllBrands();
        } catch (DBException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("back", "logged_admin.jsp");
            address = "error.jsp";
        }

        req.setAttribute("brands", brands);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
