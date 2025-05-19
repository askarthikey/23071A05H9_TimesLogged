package com.askarthi;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class TimesLog extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/newuser";
    private static final String JDBC_USER = "askarthikey";
    private static final String JDBC_PASS = "ask";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleLogin(req, resp);
    }
    

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Handle cookies for count
                        int count = 1;
                        Cookie[] cookies = req.getCookies();
                        if (cookies != null) {
                            for (Cookie c : cookies) {
                                if (c.getName().equals(username + "_count")) {
                                    try {
                                        count = Integer.parseInt(c.getValue()) + 1;
                                    } catch (NumberFormatException e) {
                                        count = 1;
                                    }
                                }
                            }
                        }
                        Cookie userCookie = new Cookie("username", username);
                        Cookie countCookie = new Cookie(username + "_count", String.valueOf(count));
                        userCookie.setMaxAge(60 * 60 * 24 * 7); // 1 week
                        countCookie.setMaxAge(60 * 60 * 24 * 7);
                        resp.addCookie(userCookie);
                        resp.addCookie(countCookie);

                        resp.getWriter().println("Welcome, " + username + "!\n You have logged in " + count + " times.");
                    } else {
                        resp.getWriter().println("Login failed. <a href='index2.html'>Try again</a>");
                    }
                }
            }
        } catch (SQLException e) {
            resp.getWriter().println("Login error: " + e.getMessage());
        }
    }
}