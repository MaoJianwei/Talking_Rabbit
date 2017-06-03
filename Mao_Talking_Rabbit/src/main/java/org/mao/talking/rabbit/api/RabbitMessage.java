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
        return new RabbitMessage(backgroundColor, wordColor, message);
    }
    private RabbitMessage(String backgroundColor, String wordColor, String message) {
        this.backgroundColor = calculateColor(backgroundColor);
        this.wordColor = calculateColor(wordColor);
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

    public boolean checkValid() {

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



    public static boolean checkColor(String rawColor) {
        String trimLowerCase = calculateColor(rawColor);

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































