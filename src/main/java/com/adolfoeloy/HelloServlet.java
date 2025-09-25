package com.adolfoeloy;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.*;

@WebServlet(value="/hello", name="helloServlet", asyncSupported = true)
public class HelloServlet extends HttpServlet {

    private final ExecutorService threadPoolExecutor =
            new ThreadPoolExecutor(32, 32, 0, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(200), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {

        System.out.println("Processing in thread: " + Thread.currentThread().getName());

        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(10_000);

        CompletableFuture
            .supplyAsync(() -> {
                System.out.println("Starting async in thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return new Result("someValue");
            }, threadPoolExecutor)
            .whenComplete((result, err) -> {
                try {
                    if (err != null) {
                        res.setStatus(500);
                        res.getWriter().println("db error");
                    } else {
                        res.setContentType("application/json");
                        res.getWriter().println("Hello, World " + result.name); }
                } catch (IOException ignored) {
                } finally {
                    asyncContext.complete();
                }
            });

    }

    record Result(String name) {
    }

}
