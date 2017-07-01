package org.mao.talking.rabbit.impl;

import org.mao.talking.rabbit.api.MaoRabbitService;
import org.mao.talking.rabbit.api.RabbitMessage;
import org.mao.talking.rabbit.api.RabbitServer;
import org.mao.talking.rabbit.api.RabbitUI;
import org.mao.talking.rabbit.restful.RabbitResource;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Main.
 *
 * Created by mao on 17-5-12.
 */
public final class MaoRabbitManager implements MaoRabbitService {

    private static final MaoRabbitManager rabbit = new MaoRabbitManager();

    private static final Object exitSignal = new Object();


    private RabbitUI monitorUi;
    private RabbitServer webServer;
    private Queue<RabbitMessage> messageQueue;


    public static void main (String [] args) {
        rabbit.init();
        rabbit.rabbitRun();

        try {
            synchronized (exitSignal) {
                exitSignal.wait();
            }

            //wait for Server response finish.
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rabbit.rabbitRest();
        rabbit.destroy();
    }

    public void shutdownGracefully() {
        synchronized (exitSignal) {
            exitSignal.notify();
        }
    }

    /**
     * Prepare all resources.
     *
     * Use in the front of main().
     */
    public void init() {

        //  --- Architecture ---
        //  Web API -> Message Queue(MQ) ->  UI & Sound notification for you :)


        // init and clear Message Queue(MQ)
        messageQueue =  new ConcurrentLinkedQueue<RabbitMessage>();

        // init UI manager
        // init UI resource
        // UI - show Standby view
        monitorUi = RabbitAwtUI.getRabbitUI();
        monitorUi.initUI(messageQueue);

        // init Web server instance
        webServer = RestfulServer.getRabbitServer();

        webServer.initInterface();

        RabbitResource.setMessageQueue(messageQueue);
        RabbitResource.setRabbitService(this);
    }

    /**
     * Start working.
     *
     * Our dear rabbit runs so fast :)
     */
    public void rabbitRun() {

        //  --- Architecture ---
        //  Web API -> Message Queue ->  UI & Sound notification for you :)


        // UI manager starts to listen to / pull from MQ
        monitorUi.startUpdateUI();
        verifyMonitorRunning();

        // start Web server and verify that it is working well
        webServer.startInterface();

        verifyServerRunning();
    }

    /**
     * Stop working.
     *
     * Let our rabbit have a rest :)
     */
    public void rabbitRest() {

        //  --- Architecture ---
        //  Web API -> Message Queue ->  UI & Sound notification for you :)


        // stop Web server and release network resource
        webServer.stopInterface();

        verifyServerStopped();


        // UI manager stop to listen to / pull from MQ
        monitorUi.stopUpdateUI();
        verifyMonitorStopped();
    }

    /**
     * Release all resources.
     *
     * Use in the end of main().
     */
    public void destroy() {

        //  --- Architecture ---
        //  Web API -> Message Queue ->  UI & Sound notification for you :)



        // Call rabbitRest() first!

        // release Web server instance
        webServer.destroyInterface();
        webServer = null;


        // clear and release MQ
        messageQueue.clear();
        messageQueue = null;

        // release UI resource
        // release UI manager
        monitorUi.destroyUI();
    }




    private void verifyMonitorRunning() {

        for(int i = 0; !monitorUi.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("monitor start InterruptedException, quit confirmation!");
                break;
            }
        }
    }

    private void verifyMonitorStopped() {

        for(int i = 0; monitorUi.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("monitor stop InterruptedException, quit confirmation!");
                break;
            }
        }
    }

    private void verifyServerRunning() {

        for(int i = 0; !webServer.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("server start InterruptedException, quit confirmation!");
                break;
            }
        }
    }

    private void verifyServerStopped() {

        for(int i = 0; webServer.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("server stop InterruptedException, quit confirmation!");
                break;
            }
        }
    }
}
