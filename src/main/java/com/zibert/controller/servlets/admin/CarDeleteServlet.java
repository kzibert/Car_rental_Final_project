package com.zibert.controller.servlets.admin;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/car_delete")
public class CarDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "car_manage";

        int id = Integer.parseInt(req.getParameter("id"));

        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.deleteCar(id);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "car_manage");
            address = "error.jsp";
        }

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
