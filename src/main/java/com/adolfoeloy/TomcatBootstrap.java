package com.adolfoeloy;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URL;

public class TomcatBootstrap {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(System.getProperty("PORT", "8080")));

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context ctx = tomcat.addWebapp("", docBase.getAbsolutePath());

        // Finding where are the classes: target/classes
        URL loc = TomcatBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
        File codeSource = new File(loc.toURI());

        WebResourceRoot resources = new StandardRoot(ctx);
        if (codeSource.isDirectory()) {
            // when running from IDE
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    codeSource.getAbsolutePath(), "/"));
        } else {
            // when running the fat-jar
            resources.addPreResources(new JarResourceSet(resources, "/WEB-INF/classes",
                    codeSource.getAbsolutePath(), "/"));
        }
        ctx.setResources(resources);

        tomcat.start();
        System.out.println("UP http://localhost:" + tomcat.getConnector().getPort() + "/hello");
        tomcat.getServer().await();
    }

}