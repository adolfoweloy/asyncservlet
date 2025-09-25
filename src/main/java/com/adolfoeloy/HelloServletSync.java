package com.adolfoeloy;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value="/hello-sync", name="helloSyncServlet")
public class HelloServletSync extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("Processing in thread: " + Thread.currentThread().getName());


        var timeoutParameter = req.getParameter("timeout");
        var timeout = 4000L;
        if (timeoutParameter != null) {
            timeout = Long.parseLong(timeoutParameter);
        }

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        res.getWriter().println("Hello, World!");
    }
}
