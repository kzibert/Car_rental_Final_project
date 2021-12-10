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

@WebServlet("/cancelled_orders")
public class CancelledOrdersServlet extends HttpServlet {

    private static final int shift = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "cancelled_orders.jsp";

        String paramPage = req.getParameter("page");
        String paramPageSize = req.getParameter("pageSize");

        int page = 1;
        int pageSize = 5;

        if (paramPage != null && paramPageSize != null) {
            page = Integer.parseInt(paramPage);
            pageSize = Integer.parseInt(paramPageSize);
        }

        DBManager dbManager = DBManager.getInstance();

        int size = dbManager.getCancelledOrdersSize();

        int minPagePossible = page - shift < 1 ? 1 : page - shift;

        int pageCount = (int) Math.ceil((float) size / pageSize);

        int maxPagePossible = page + shift > pageCount ? pageCount : page + shift;

        List<Order> cancelledOrders = new ArrayList<>();
        try {
            cancelledOrders = dbManager.getAllCancelledOrders(pageSize * (page - 1), pageSize);
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

        req.setAttribute("cancelledOrders", cancelledOrders);

        req.getRequestDispatcher(address).forward(req, resp);

    }
}
