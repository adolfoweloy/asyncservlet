# AsyncServlet

This project is a very small example of how to use asynchronous servlets in Java. 
It demonstrates how to handle long-running tasks without blocking the servlet container's threads.
This project was created as part of a blog post on [Async responses with Servlet](https://adolfoeloy.com/java/async/reactive/2025/10/01/async-responses.en.html).

Here is what this project includes:
- HelloServlet: A simple servlet that handles GET requests asynchronously.
- HelloServletComet: A servlet that simulates a long-running task.
- HelloServletSync: A synchronous version of the HelloServlet for comparison.
- MultipartMixedServlet: A servlet that generates multipart/mixed responses.

## Prerequisites

- Java 21
- Maven

## Building the Project

To build the project, navigate to the project directory and run:

```bash
mvn clean package
```

## Running the Project

You can run the project using the following command:

```bash
java -jar target/asyncservlet-1.0-SNAPSHOT-shaded.jar
```
