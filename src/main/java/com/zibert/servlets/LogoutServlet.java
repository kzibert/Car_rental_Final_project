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
 * Servlet for logging out from the system
 * Role: none
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        User user = (User) req.getSession().getAttribute("user");

        req.getSession().invalidate();

        resp.sendRedirect("auth");

        if (user != null) {
            log.info("User logged out. ID: " + user.getId());
        }
    }
}
