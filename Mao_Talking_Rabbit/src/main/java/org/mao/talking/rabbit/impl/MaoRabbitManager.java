package org.mao.talking.rabbit.impl;

import org.mao.talking.rabbit.api.RabbitWebServer;

/**
 * Main.
 *
 * Created by mao on 17-5-12.
 */
public final class MaoRabbitManager {

    private static final MaoRabbitManager rabbit = new MaoRabbitManager();


    private RabbitWebServer webServer;


    public static void main (String [] args) {
        rabbit.init();
        rabbit.rabbitRun();

        // TODO -
        
        rabbit.rabbitRest();
        rabbit.destroy();
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

        // init UI manager
        // init UI resource
        // UI - show Standby view

        // init Web server instance
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

        // start Web server and verify that it is working well
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

        // UI manager stop to listen to / pull from MQ
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

        // clear and release MQ

        // release UI resource
        // release UI manager
    }
}
