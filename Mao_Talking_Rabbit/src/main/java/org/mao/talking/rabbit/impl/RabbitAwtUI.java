package org.mao.talking.rabbit.impl;

import org.mao.talking.rabbit.api.RabbitMessage;
import org.mao.talking.rabbit.api.RabbitUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import static java.lang.Integer.valueOf;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_GREEN;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_RED;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_STANDBY;
import static org.mao.talking.rabbit.api.RabbitMessage.COLOR_STR_YELLOW;


/**
 * Created by mao on 17-5-30.
 */
public class RabbitAwtUI implements RabbitUI {

    private static final Color COLOR_RED = new Color(255, 0, 0);
    private static final Color COLOR_YELLOW = new Color(255, 255, 0);
    private static final Color COLOR_GREEN = new Color(0, 255, 0);
    private static final Color COLOR_BLACK = Color.BLACK;
    private static final Color COLOR_WHITE = Color.WHITE;



    private static Queue<RabbitMessage> messageQueue;

    private EventToUi getToShow;

    private Frame background;
    private Label message;



    private volatile static RabbitUI awtUi;

    private RabbitAwtUI() {
        ;
    }

    public static RabbitUI getRabbitUI() {
        if (awtUi == null) {
            synchronized (RabbitAwtUI.class) {
                if (awtUi == null) {
                    awtUi = new RabbitAwtUI();
                }
            }
        }

        return awtUi;
    }

//    private static void setMessageQueue(Queue queue) {
//        messageQueue = queue;
//    }



    @Override
    public void initUI(Queue msgQueue) {

        messageQueue = msgQueue;

        initWords();

        initBackground();

        showStandBy();

        background.setVisible(true);
    }

    private void initWords() {
        message = new Label("Talking Rabbit :) Standby");
        message.setAlignment(Label.CENTER);
        message.setFont(new Font(null, 0, 18));
    }

    private void initBackground() {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        background = new Frame("Talking Rabbit :)");
        background.setUndecorated(true);
        background.setBounds(0,0,dimension.width,dimension.height);
        background.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        background.add(message);

    }

    private void showStandBy() {
        updateUI(COLOR_WHITE, COLOR_BLACK, "Talking Rabbit :) Standby");
    }



    @Override
    public void startUpdateUI() {
        getToShow = new EventToUi();
        getToShow.start();
    }

    @Override
    public boolean isRunning() {
        return getToShow.isAlive();
    }

    @Override
    public void stopUpdateUI() {

        getToShow.needFinish();

        try {

            getToShow.join(3000);

        } catch (InterruptedException e) {

            System.out.println("wait UI MQ out of time, interrupt it...");

            getToShow.interrupt();
        }

        getToShow = null;
    }

    @Override
    public void destroyUI() {
        background.removeAll();
        background = null;
        message = null;
    }

    private void updateUI(Color backgroundColor, Color msgColor, String msg) {

        background.removeAll();

        background.setBackground(backgroundColor);

        if(msgColor != null && msg != null) {
            message.setText(msg);
            message.setForeground(msgColor);

            background.add(message);
        }
    }





    private class EventToUi extends Thread {

        private static final String UI_MQ_NAME = "MessageQueueToUi";

        private boolean goingWork = true;

        public EventToUi() {
            super(UI_MQ_NAME);
        }

        public void needFinish() {
            goingWork = false;
        }


        @Override
        public void run() {

            while(goingWork) {

                RabbitMessage msg = messageQueue.poll();

                if(msg == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(UI_MQ_NAME + " interrupt sleep, it exits...");
                        break;
                    }
                    continue;
                }

                if(msg.getBackgroundColor() == COLOR_STR_STANDBY) {

                    showStandBy();

                } else {

                    updateUI(getColor(msg.getBackgroundColor()), getColor(msg.getWordColor()), msg.getMessage());

                }
            }
        }

        private Color getColor(String colorStr){

            switch(colorStr) {
                case COLOR_STR_RED:
                    return COLOR_RED;

                case COLOR_STR_YELLOW:
                    return COLOR_YELLOW;

                case COLOR_STR_GREEN:
                    return COLOR_GREEN;

                case COLOR_STR_STANDBY:
                    /* Bypass this outside */
                    return null;
                default:
                    return calculateColor(colorStr);
            }

        }

        /**
         * Get new Color defined by RGB.
         *
         * @param customColor Custom colors should be "abcdef", "123abc", "987654", etc.
         * @return
         */
        private Color calculateColor(String customColor) {

            String R = customColor.substring(0,2);
            String G = customColor.substring(2,4);
            String B = customColor.substring(4,6);

            return new Color(valueOf(R, 16), valueOf(G, 16), valueOf(B, 16));
        }
    }
}
