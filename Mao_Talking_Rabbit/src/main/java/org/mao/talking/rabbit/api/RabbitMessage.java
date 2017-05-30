package org.mao.talking.rabbit.api;

/**
 * Created by mao on 17-5-30.
 */
public class RabbitMessage {

    private String backgroundColor;
    private String wordColor;
    private String message;

    public static RabbitMessage getRabbitMessage(String backgroundColor) {
        return getRabbitMessage(backgroundColor, null, null);
    }

    public static RabbitMessage getRabbitMessage(String backgroundColor, String wordColor, String message) {
        return new RabbitMessage(backgroundColor, wordColor, message);
    }

    private RabbitMessage(String backgroundColor, String wordColor, String message) {
        this.backgroundColor = backgroundColor;
        this.wordColor = wordColor;
        this.message = message;
    }
}































