package com.adolfoeloy;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value="/hello", name="helloServlet", asyncSupported = true)
public class HelloServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        System.out.println("Processing in thread: " + Thread.currentThread().getName());

        AsyncContext asyncContext = req.startAsync();

        asyncContext.start(() -> {

            System.out.println("Processing async in thread: " + Thread.currentThread().getName());

            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            try {
                response.getWriter().println("Hello, World!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            asyncContext.complete();
        });

    }
}
