package org.mao.talking.rabbit.impl;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.mao.talking.rabbit.api.RabbitWebServer;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mao on 17-5-12.
 */
public class RestfulServer implements RabbitWebServer {

    // Base URI the Grizzly HTTP server will listen on
    // example: "http://localhost:8080/myapp/"
    public static final String BASE_URI = "http://0.0.0.0:10110/";
    public static final String RESOURCE_PACKAGE = "org.mao.talking.rabbit.restful";

    private volatile static RestfulServer restServer;


    private HttpServer httpServer;


    private RestfulServer() {
        ;
    }

    @Override
    public RabbitWebServer getRabbitWebServer() {
        if (restServer == null) {
            synchronized (RestfulServer.class) {
                if (restServer == null) {
                    restServer = new RestfulServer();
                }
            }
        }

        return restServer;
    }

    @Override
    public void initWebInterface() {

        // create a resource config that scans for JAX-RS resources and providers
        final ResourceConfig rc = new ResourceConfig().packages(RESOURCE_PACKAGE);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        try {
            httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
        } catch (ProcessingException e) {
            // TODO - robustness
            e.printStackTrace();
        }
    }

    @Override
    public void startWebInterface() {

        // TODO - auto retry - against httpServer's STOPPING state

        if (httpServer.isStarted()) {
            return;
        }

        try {
            httpServer.start();
        } catch (IOException e) {
            // TODO - robustness
            e.printStackTrace();
        }
    }

    @Override
    public void stopWebInterface() {

        if (!httpServer.isStarted()) {
            return;
        }

        try {
            httpServer.shutdown().get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO - robustness
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO - robustness
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO - robustness
            e.printStackTrace();
        }

        httpServer.shutdownNow();
    }

    @Override
    public void destroyWebInterface() {

        stopWebInterface();

        httpServer = null;
    }
}
