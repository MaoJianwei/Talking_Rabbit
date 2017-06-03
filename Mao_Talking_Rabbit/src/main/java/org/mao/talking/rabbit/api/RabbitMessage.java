package org.mao.talking.rabbit.api;


import java.awt.Color;

/**
 * Created by mao on 17-5-30.
 */
public class RabbitMessage {

    private Color backgroundColor;
    private Color wordColor;
    private String message;

    public static RabbitMessage getRabbitMessage(Color backgroundColor) {
        return getRabbitMessage(backgroundColor, null, null);
    }

    public static RabbitMessage getRabbitMessage(Color backgroundColor, Color wordColor, String message) {
        return new RabbitMessage(backgroundColor, wordColor, message);
    }

    private RabbitMessage(Color backgroundColor, Color wordColor, String message) {
        this.backgroundColor = backgroundColor;
        this.wordColor = wordColor;
        this.message = message;
    }



    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getWordColor() {
        return wordColor;
    }

    public String getMessage() {
        return message;
    }
}































