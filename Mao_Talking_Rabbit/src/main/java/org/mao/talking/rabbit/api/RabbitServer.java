package org.mao.talking.rabbit.api;

/**
 * Server interface. Server can be Restful, socket, RPC, etc.
 *
 * Created by mao on 17-5-12.
 */
public interface RabbitServer {

    /**
     * Singleton Pattern.
     *
     * @return
     */
    //RabbitServer getRabbitServer();

    void initInterface();

    /**
     * Start server to receive external events.
     */
    void startInterface();

    /**
     * Monitor status of server.
     *
     * @return true, if the server is still running. false, otherwise
     */
    boolean isRunning();

    /**
     * Stop server.
     */
    void stopInterface();

    void destroyInterface();
}
