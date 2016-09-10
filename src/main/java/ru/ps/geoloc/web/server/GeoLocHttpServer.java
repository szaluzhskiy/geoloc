package ru.ps.geoloc.web.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

public class GeoLocHttpServer {
    private static HttpServer server;

    public static void start(int port) throws IOException {
        server = new HttpServer();
        NetworkListener listener = new NetworkListener("grizzly2", "localhost", port);
        server.addListener(listener);
        WebappContext ctx = new WebappContext("ctx","/");
        /*final ServletRegistration reg = ctx.addServlet("spring", new SpringServlet());
        reg.addMapping("*//*");*/
        ctx.addContextInitParameter("contextConfigLocation", "classpath:root-applicationContext.xml");
        ctx.addContextInitParameter("log4jConfigLocation","classpath:log4j.properties");
        ctx.addListener("org.springframework.web.context.ContextLoaderListener");
        ctx.addListener("org.springframework.web.context.request.RequestContextListener");
        final ServletRegistration regGeoloc = ctx.addServlet("geoloc-servlet", new DispatcherServlet());
        regGeoloc.addMapping("/geoloc/*");
        ctx.deploy(server);

        server.start();
    }

    public static void stop() {
        server.shutdown();
    }
}
