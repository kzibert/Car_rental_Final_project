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
 * Servlet for deleting car
 * Role: admin
 */

@WebServlet("/car_delete")
public class CarDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "The car hasn't been deleted");
        req.setAttribute("back", "car_manage");
        req.getRequestDispatcher("error.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_manage";

        int id = Integer.parseInt(req.getParameter("id"));

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.deleteCar(id);
        } catch (DBException e) {
            address = "car_delete";
        }

        resp.sendRedirect(address);
    }
}
