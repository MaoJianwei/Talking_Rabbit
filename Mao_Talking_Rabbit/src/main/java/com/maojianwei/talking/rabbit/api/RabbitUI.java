package com.maojianwei.talking.rabbit.api;

import java.util.Queue;

/**
 * Created by mao on 17-5-30.
 */
public interface RabbitUI {

    void initUI(Queue msgQueue);

    void startUpdateUI();

    boolean isRunning();

    void stopUpdateUI();

    void destroyUI();
}
