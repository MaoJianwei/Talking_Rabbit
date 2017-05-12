package org.mao.talking.rabbit.api;

/**
 * Created by mao on 17-5-12.
 */
public interface RabbitWebServer {

    /**
     * Singleton Pattern.
     *
     * @return
     */
    RabbitWebServer getRabbitWebServer();

    /**
     * Start web server to receive external events.
     */
    void startWebInterface();

    /**
     * Stop web server.
     */
    void stopWebInterface();
}
