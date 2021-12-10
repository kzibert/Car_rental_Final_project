package com.zibert.controller.servlets.manager;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/manager_orders")
public class ManagerOrdersServlet extends HttpServlet {

    private static final int shift = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "manager_orders.jsp";

        String paramPage = req.getParameter("page");
        String paramPageSize = req.getParameter("pageSize");

        int page = 1;
        int pageSize = 5;

        if (paramPage != null && paramPageSize != null) {
            page = Integer.parseInt(paramPage);
            pageSize = Integer.parseInt(paramPageSize);
        }

        DBManager dbManager = DBManager.getInstance();

        int size = dbManager.getReceivedOrdersSize();

        int minPagePossible = page - shift < 1 ? 1 : page - shift;

        int pageCount = (int) Math.ceil((float) size / pageSize);

        int maxPagePossible = page + shift > pageCount ? pageCount : page + shift;

        List<Order> orders = new ArrayList<>();
        try {
            orders = dbManager.getAllReceivedOrders(pageSize * (page - 1), pageSize);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "logged_manager.jsp");
            address = "error.jsp";
        }

        req.setAttribute("pageCount", pageCount);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("minPossiblePage", minPagePossible);
        req.setAttribute("maxPossiblePage", maxPagePossible);

        req.setAttribute("orders", orders);

        req.getRequestDispatcher(address).forward(req, resp);
    }
}
