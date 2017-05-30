package org.mao.talking.rabbit.impl;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.mao.talking.rabbit.api.RabbitServer;
import org.mao.talking.rabbit.restful.RabbitWebApplication;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mao on 17-5-12.
 */
public class RestfulServer implements RabbitServer {

    // Base URI the Grizzly HTTP server will listen on
    // example: "http://localhost:8080/myapp/"
    public static final String BASE_URI = "http://0.0.0.0:10110/";

    private volatile static RestfulServer restServer;


    private HttpServer httpServer;


    private RestfulServer() {
        ;
    }


    public static RabbitServer getRabbitServer() {
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
    public void <RabbitMessage>initInterface() {

        // ResourceConfig: a resource config that scans for JAX-RS resources and providers
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        try {
            httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new RabbitWebApplication(), false);
        } catch (ProcessingException e) {
            // TODO - robustness
            e.printStackTrace();
        }
    }

    @Override
    public void startInterface() {

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
    public boolean isRunning(){
        return httpServer.isStarted();
    }

    @Override
    public void stopInterface() {

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
    public void destroyInterface() {

        stopInterface();

        httpServer = null;
    }
}
