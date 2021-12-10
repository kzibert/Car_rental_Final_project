package com.zibert.filters;

import com.zibert.DAO.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class AccessFilter implements Filter {

    List<String> loginUrls = new ArrayList<>();
    List<String> userUrls = new ArrayList<>();
    List<String> managerUrls = new ArrayList<>();
    List<String> adminUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        //pages for not logged users
        loginUrls.add("/auth");
        loginUrls.add("/auth.jsp");
        loginUrls.add("/login_page");
        loginUrls.add("/registration.jsp");
        loginUrls.add("/register");
        loginUrls.add("/index.html");
        loginUrls.add("/logout");
        loginUrls.add("/no_user_found.jsp");

        //user pages
        userUrls.add("/choose_car");
        userUrls.add("/damage_payment");
        userUrls.add("/order_payment");
        userUrls.add("/rent_order_redirect");
        userUrls.add("/rent_order");
        userUrls.add("/rent");
        userUrls.add("/show_damage_receipt");
        userUrls.add("/show_order_receipt");
        userUrls.add("/user_orders");
        userUrls.add("/choose_car.jsp");
        userUrls.add("/damage_receipt.jsp");
        userUrls.add("/order_receipt.jsp");
        userUrls.add("/paid_damage.jsp");
        userUrls.add("/paid_order.jsp");
        userUrls.add("/rent_order.jsp");
        userUrls.add("/user_main.jsp");
        userUrls.add("/user_orders.jsp");

        // manager pages
        managerUrls.add("/cancelled_orders");
        managerUrls.add("/car_back");
        managerUrls.add("/closed_orders");
        managerUrls.add("/manager_orders");
        managerUrls.add("/approve_order");
        managerUrls.add("/cancel_order");
        managerUrls.add("/rented_cars");
        managerUrls.add("/cancelled_orders.jsp");
        managerUrls.add("/closed_orders.jsp");
        managerUrls.add("/logged_manager.jsp");
        managerUrls.add("/manager_orders.jsp");
        managerUrls.add("/rented_cars.jsp");

        // admin pages
        adminUrls.add("/brand_add");
        adminUrls.add("/deleteBrand");
        adminUrls.add("/brand_list");
        adminUrls.add("/car_add");
        adminUrls.add("/car_delete");
        adminUrls.add("/car_edit");
        adminUrls.add("/car_manage");
        adminUrls.add("/car_update");
        adminUrls.add("/register_manager");
        adminUrls.add("/user_manage");
        adminUrls.add("/user_status_update");
        adminUrls.add("/brand_management.jsp");
        adminUrls.add("/car_edit.jsp");
        adminUrls.add("/car_management.jsp");
        adminUrls.add("/logged_admin.jsp");
        adminUrls.add("/manager_registered.jsp");
        adminUrls.add("/register_manager.jsp");
        adminUrls.add("/user_manage.jsp");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String servPath = req.getServletPath();

        User user = (User) req.getSession().getAttribute("user");

        int role = 0;
        if (req.getSession().getAttribute("role") != null) {
            role = (int) req.getSession().getAttribute("role");
        }

        String address = servPath;

        if (user == null && !(loginUrls.contains(servPath))) {
            address = "no_access";
        }
        if (role == 1) {
            if (userUrls.contains(servPath) || managerUrls.contains(servPath)) {
                address = "no_access";
            }
        }
        if (role == 2) {
            if (userUrls.contains(servPath) || adminUrls.contains(servPath)) {
                address = "no_access";
            }
        }
        if (role == 3) {
            if (adminUrls.contains(servPath) || managerUrls.contains(servPath)) {
                address = "no_access";
            }
        }
        req.getRequestDispatcher(address).forward(req, resp);
    }
}
