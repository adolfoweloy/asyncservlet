package com.adolfoeloy;

import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@WebServlet(value="/hello-comet", name="helloCometServlet", asyncSupported = true)
public class HelloServletComet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream"); // Use SSE for streaming
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        var ctx = req.startAsync();
        ctx.setTimeout(20 * 1_000);

        var writer = resp.getWriter();
        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                writer.println("data: " + System.currentTimeMillis());
                writer.println(); // Blank line for SSE event separation
                writer.flush();
            } catch (Exception e) {
                ctx.complete();
                scheduler.shutdown();
            }
        }, 0, 2, TimeUnit.SECONDS);

        ctx.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent event) {
                scheduler.shutdown();
            }
            @Override public void onTimeout(AsyncEvent event) {
                scheduler.shutdown();
                event.getAsyncContext().complete();
            }
            @Override public void onError(AsyncEvent event) {
                scheduler.shutdown();
                event.getAsyncContext().complete();
            }
            @Override public void onStartAsync(AsyncEvent event) {}
        });
    }

}
