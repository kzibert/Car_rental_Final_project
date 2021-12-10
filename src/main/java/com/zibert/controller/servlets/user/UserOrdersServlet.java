package com.zibert.controller.servlets.user;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.Car;
import com.zibert.DAO.entity.Order;
import com.zibert.DAO.entity.OrderReceipt;
import com.zibert.DAO.entity.User;
import com.zibert.DAO.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user_orders")
public class UserOrdersServlet extends HttpServlet {

    private static final int shift = 0;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String address = "user_orders.jsp";

        User user = (User) req.getSession().getAttribute("user");
        int userId = user.getId();

        String paramPage = req.getParameter("page");
        String paramPageSize = req.getParameter("pageSize");

        int page = 1;
        int pageSize = 5;

        if (paramPage != null && paramPageSize != null) {
            page = Integer.parseInt(paramPage);
            pageSize = Integer.parseInt(paramPageSize);
        }

        DBManager dbManager = DBManager.getInstance();

        int size = dbManager.getUserOrdersSize(userId);

        int minPagePossible = page - shift < 1 ? 1 : page - shift;

        int pageCount = (int) Math.ceil((float) size / pageSize);

        int maxPagePossible = page + shift > pageCount ? pageCount : page + shift;

        List<Order> orders = new ArrayList<>();
        try {
            orders = dbManager.getAllOrdersByUserId(userId, pageSize * (page - 1), pageSize);
        } catch (DBException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("back", "user_main.jsp");
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
