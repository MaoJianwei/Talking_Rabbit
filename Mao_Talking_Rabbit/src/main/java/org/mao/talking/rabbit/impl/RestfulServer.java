package org.mao.talking.rabbit.impl;

import org.mao.talking.rabbit.api.RabbitWebServer;

/**
 * Created by mao on 17-5-12.
 */
public class RestfulServer implements RabbitWebServer {

    private static RestfulServer restServer; //int

    private RestfulServer() {
        ;
    }

    @Override
    public RabbitWebServer getRabbitWebServer() {
        if(restServer == null) {
            synchronized (RestfulServer.class) {
                if (restServer == null) {
                    restServer = new RestfulServer();
                }
            }
        }

        return restServer;
    }

    @Override
    public void startWebInterface() {

    }

    @Override
    public void stopWebInterface() {

    }
}
