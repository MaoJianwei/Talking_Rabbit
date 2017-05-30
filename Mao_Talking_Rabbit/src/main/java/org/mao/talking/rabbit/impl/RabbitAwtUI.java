package org.mao.talking.rabbit.impl;

import org.mao.talking.rabbit.api.RabbitUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Created by mao on 17-5-30.
 */
public class RabbitAwtUI implements RabbitUI {

    private static final Color COLOR_RED = new Color(255, 0, 0);
    private static final Color COLOR_YELLOW = new Color(255, 255, 0);
    private static final Color COLOR_GREEN = new Color(0, 255, 0);
    private static final Color COLOR_BLACK = Color.BLACK;
    private static final Color COLOR_WHITE = Color.WHITE;




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


    private Frame background;
    private Label message;


    @Override
    public void initUI() {

        initWords();

        initBackground();

        showStandBy();

        background.setVisible(true);
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

    private void initWords() {
        message = new Label("Talking Rabbit :) Standby");
        message.setAlignment(Label.CENTER);
        message.setFont(new Font(null, 0, 18));
    }

    private void showStandBy() {
        updateUI(COLOR_WHITE, COLOR_BLACK, "Talking Rabbit :) Standby");
    }

    private void updateUI(Color backgroundColor, Color msgColor, String msg) {

        background.removeAll();

        background.setBackground(backgroundColor);

        message.setText(msg);
        message.setForeground(msgColor);

        background.add(message);
    }

    @Override
    public void startUpdateUI() {

    }

    @Override
    public boolean isRunning() {
        //TODO
        return false;
    }

    @Override
    public void stopUpdateUI() {

    }

    @Override
    public void destroyUI() {

    }
}
