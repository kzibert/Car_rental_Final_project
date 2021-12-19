package com.zibert.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for deleting brands
 * Role: admin
 */

@WebServlet("/deleteBrand")
public class BrandDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "Brand hasn't been deleted");
        req.setAttribute("back", "brand_list");
        req.getRequestDispatcher("error.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "brand_list";

        int id = Integer.parseInt(req.getParameter("id"));

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.deleteBrand(id);
        } catch (DBException ex) {
            address = "deleteBrand";
        }
        resp.sendRedirect(address);
    }
}
