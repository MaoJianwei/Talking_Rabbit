package org.mao.talking.rabbit.impl;

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
public final class MaoRabbitManager {

    private static final MaoRabbitManager rabbit = new MaoRabbitManager();


    private RabbitUI monitorUi;
    private RabbitServer webServer;
    private Queue<RabbitMessage> messageQueue;

    private long lastTime;


    public static void main (String [] args) {
        rabbit.init();
        rabbit.rabbitRun();


        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //break;
            }
        }


        // TODO -
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        rabbit.rabbitRest();
//        rabbit.destroy();
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
        lastTime = System.currentTimeMillis();
        webServer = RestfulServer.getRabbitServer();
        System.out.println(System.currentTimeMillis()-lastTime);

        lastTime = System.currentTimeMillis();
        webServer.initInterface();
        System.out.println(System.currentTimeMillis()-lastTime);

        RabbitResource.setMessageQueue(messageQueue);
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

        // start Web server and verify that it is working well
        lastTime = System.currentTimeMillis();
        webServer.startInterface();
        System.out.println(System.currentTimeMillis()-lastTime);

        lastTime = System.currentTimeMillis();
        varifyServerRunning();
        System.out.println(System.currentTimeMillis()-lastTime);
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
        lastTime = System.currentTimeMillis();
        webServer.stopInterface();
        System.out.println(System.currentTimeMillis()-lastTime);

        lastTime = System.currentTimeMillis();
        varifyServerStopped();
        System.out.println(System.currentTimeMillis()-lastTime);


        // UI manager stop to listen to / pull from MQ
        monitorUi.stopUpdateUI();
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
        lastTime = System.currentTimeMillis();
        webServer.destroyInterface();
        webServer = null;
        System.out.println(System.currentTimeMillis()-lastTime);


        // clear and release MQ
        messageQueue.clear();
        messageQueue = null;

        // release UI resource
        // release UI manager
        monitorUi.destroyUI();
    }





    private void varifyServerRunning() {

        for(int i = 0; !webServer.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException, quit confirmation!");
                break;
            }
        }
    }

    private void varifyServerStopped() {

        for(int i = 0; webServer.isRunning() && i < 6; i++){

            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException, quit confirmation!");
                break;
            }
        }
    }
}
