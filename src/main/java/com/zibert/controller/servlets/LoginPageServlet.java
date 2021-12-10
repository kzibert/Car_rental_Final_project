package com.zibert.controller.servlets;

import com.zibert.DAO.DBManager;
import com.zibert.DAO.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/login_page")
public class LoginPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        req.getSession().invalidate();

        req.getRequestDispatcher("auth.jsp").forward(req, resp);
    }
}

