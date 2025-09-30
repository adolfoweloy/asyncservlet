package com.adolfoeloy;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value="/hello", name="helloServlet", asyncSupported = true)
public class HelloServlet extends HttpServlet {
    private final static String CUSTOM_THREAD_NAME = "custom-thread";

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        System.out.println("Handling the request in thread: " + Thread.currentThread().getName());

        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(10_000);

        new Thread(
                new BackgroundTask(asyncContext),
                CUSTOM_THREAD_NAME
        ).start();
    }

    private record BackgroundTask(AsyncContext asyncContext) implements Runnable {

        @Override
            public void run() {
                try {
                    System.out.println("Processing async in thread: "
                            + Thread.currentThread().getName());
                    Thread.sleep(4_000);
                    var res = (HttpServletResponse) asyncContext.getResponse();
                    res.setContentType("application/json");
                    writeResponse("Hello, World", 200);
                } catch (InterruptedException e) {
                    writeResponse("db error", 500);
                } finally {
                    asyncContext.complete();
                }
            }

            private void writeResponse(String message, int status) {
                var res = (HttpServletResponse) asyncContext.getResponse();
                try {
                    res.setStatus(status);
                    res.getWriter().println(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
