package org.mao.talking.rabbit.api;


import java.awt.Color;

/**
 * Created by mao on 17-5-30.
 */
public class RabbitMessage {

    public static final String CUSTOM_COLOR_STR_REGEX = "([0-9,a-f]{6})";

    public static final String COLOR_STR_RED = "red";
    public static final String COLOR_STR_GREEN = "green";
    public static final String COLOR_STR_YELLOW = "yellow";
    public static final String COLOR_STR_STANDBY = "standby";

    private String backgroundColor;
    private String wordColor;
    private String message;


    public static RabbitMessage getRabbitMessage(String backgroundColor) {
        return getRabbitMessage(backgroundColor, null, null);
    }

    public static RabbitMessage getRabbitMessage(String backgroundColor, String wordColor, String message) {

        // calculate first, reduce GC

        backgroundColor = backgroundColor != null ? calculateColor(backgroundColor) : backgroundColor;

        wordColor = wordColor != null ? calculateColor(wordColor) : wordColor;


        return checkValid(backgroundColor, wordColor, message)
                ? new RabbitMessage(backgroundColor, wordColor, message)
                : null;
    }

    private RabbitMessage(String backgroundColor, String wordColor, String message) {
        this.backgroundColor = backgroundColor;
        this.wordColor = wordColor;
        this.message = message;
    }



    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getWordColor() {
        return wordColor;
    }

    public String getMessage() {
        return message;
    }



    private static boolean checkValid(String backgroundColor, String wordColor, String message) {

        if(!checkColor(backgroundColor)) {
            return false;
        }

        if(message == null && wordColor == null) {
            return true;
        }

        if(message!=null && wordColor !=null){

            return checkColor(wordColor);

        } else {

            return false;
        }
    }



    private static boolean checkColor(String trimLowerCase) {

        if(trimLowerCase.equals(COLOR_STR_RED) ||
                trimLowerCase.equals(COLOR_STR_GREEN) ||
                trimLowerCase.equals(COLOR_STR_YELLOW) ||
                trimLowerCase.equals(COLOR_STR_STANDBY) ||
                isValidCustomColor(trimLowerCase)) {

            return true;
        }

        return false;
    }

    private static String calculateColor(String rawColor) {
        return rawColor.trim().toLowerCase();
    }

    // Custom colors should be "abcdef", "123abc", "987654", etc.
    private static boolean isValidCustomColor(String trimLowerCase) {
        if(trimLowerCase.length() == 6 && trimLowerCase.matches(CUSTOM_COLOR_STR_REGEX)) {
            return true;
        }
        return false;
    }
}































