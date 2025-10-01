package com.adolfoeloy;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(value = "/multipart-mixed", name = "multipartMixedServlet")
public class MultipartMixedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String boundary = "----MyBoundary" + System.currentTimeMillis();
        resp.setContentType("multipart/mixed; boundary=" + boundary);
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        // First part: plain text
        writer.println("--" + boundary);
        writer.println("Content-Type: text/plain; charset=UTF-8");
        writer.println();
        writer.println("This is the first part (plain text).");

        // Second part: JSON
        writer.println("--" + boundary);
        writer.println("Content-Type: application/json; charset=UTF-8");
        writer.println();
        writer.println("{\"message\": \"This is the second part (JSON)\"}");

        // End boundary
        writer.println("--" + boundary + "--");
        writer.flush();
    }
}