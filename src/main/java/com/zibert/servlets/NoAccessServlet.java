package com.zibert.servlets;

import com.zibert.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for forming info page when access is not granted
 * Role: all
 */

@WebServlet("/no_access")
public class NoAccessServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(NoAccessServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        req.setAttribute("user", user);

        req.getRequestDispatcher("no_access.jsp").forward(req, resp);

        log.info("User tried to access not allowed area. User ID: " + user.getId());

    }
}
