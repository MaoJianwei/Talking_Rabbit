package org.mao.talking.rabbit.api;

/**
 * Created by mao on 17-5-30.
 */
public interface RabbitUI {

    void initUI();

    void startUpdateUI();

    boolean isRunning();

    void stopUpdateUI();

    void destroyUI();
}
