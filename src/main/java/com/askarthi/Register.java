package com.askarthi;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class Register extends HttpServlet {

    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/newuser";
    public static final String JDBC_USER = "askarthikey";
    public static final String JDBC_PASS = "ask";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String age = req.getParameter("age");
        String phone = req.getParameter("phone");
        String rollno = req.getParameter("rollno");
        String section = req.getParameter("section");
        String password = req.getParameter("password");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
            String sql = "INSERT INTO users (username, fullname, email, age, phone, rollno, section, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, fullname);
                ps.setString(3, email);
                ps.setInt(4, Integer.parseInt(age));
                ps.setString(5, phone);
                ps.setString(6, rollno);
                ps.setString(7, section);
                ps.setString(8, password);
                ps.executeUpdate();
            }
            // Redirect to login page on successful registration
            resp.sendRedirect("index2.html");
        } catch (SQLException e) {
            resp.getWriter().println("Registration failed: " + e.getMessage());
        }
    }
}