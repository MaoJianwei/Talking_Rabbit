package org.mao.talking.rabbit.restful;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mao.talking.rabbit.api.AbstractWebResource;
import org.mao.talking.rabbit.api.RabbitMessage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Queue;

/**
 * Hello world!
 *
 */
@Path("/")
public class RabbitResource extends AbstractWebResource {


    private static final String CONTENT_TYPE_JSON = "application/json";

    // Custom colors should be "abcdef", "123abc", "987654", etc.
    private static final String CUSTOM_COLOR_REGEX = "([0-9,a-f]{6})";
    private static final String COLOR_RED = "red";
    private static final String COLOR_GREEN = "green";
    private static final String COLOR_YELLOW = "yellow";
    private static final String COLOR_STANDBY = "standby";

    private static final String JSON_MSG_COLOR_RESP_CODE = "errcode";
    private static final String JSON_MSG_COLOR_RESP_MSG = "errmsg";

    private static final String JSON_MSG_COLOR_BACKGROUND_COLOR = "backgroundColor";
    private static final String JSON_MSG_COLOR_WORD_COLOR = "wordColor";
    private static final String JSON_MSG_COLOR_MESSAGE = "message";

    private static final String RESPONSE_OK = "ok";
    private static final String RESPONSE_ERROR_COLOR = "Color fields error";
    private static final String RESPONSE_ERROR_JSON = "JSON key or value error";


    private static Queue messageQueue;

    public static void setMessageQueue(Queue queue) {
        messageQueue = queue;
    }

    @GET
    @Path("/color/{color}")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    public Response colorEvent(@PathParam("color") String color) {

        if(!checkColorInput(color)){
            return ok(buildResult(1, RESPONSE_ERROR_COLOR));
        }

        color = calculateColor(color);

        messageQueue.offer(RabbitMessage.getRabbitMessage(color));

        return ok(buildResult(0, RESPONSE_OK));
    }

    @POST
    @Path("/message")
    @Produces(CONTENT_TYPE_JSON) // necessary !!!
    @Consumes(CONTENT_TYPE_JSON) // necessary !!!
    public Response messageColorEvent(ObjectNode rawMsgColor) {

        if(!checkMessageColorInput(rawMsgColor)) {
            return ok(buildResult(2, RESPONSE_ERROR_JSON));
        }

        String backColor = calculateColor(rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR).asText());
        String wordColor = calculateColor(rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR).asText());
        String message = rawMsgColor.get(JSON_MSG_COLOR_MESSAGE).asText();

        messageQueue.offer(RabbitMessage.getRabbitMessage(backColor, wordColor, message));


        return ok(buildResult(0, RESPONSE_OK));
    }

    @GET
    @Path("clear")
    @Produces(CONTENT_TYPE_JSON)
    public Response clearEvent() {

        messageQueue.offer(RabbitMessage.getRabbitMessage(COLOR_STANDBY));

        return ok(buildResult(0, RESPONSE_OK));
    }

    private ObjectNode buildResult(int code, String message) {
        return buildResult(code, message, null);
    }

    private ObjectNode buildResult(int code, String message, Map<String, String> customFields) {
        ObjectNode data = getMapper().createObjectNode()
                .put(JSON_MSG_COLOR_RESP_CODE, code)
                .put(JSON_MSG_COLOR_RESP_MSG, message);

        if(customFields != null) {
            customFields.forEach((k, v) -> data.put(k, v));
        }

        return data;
    }

    private String calculateColor(String rawColor) {
        return rawColor.trim().toLowerCase();
    }

    private Boolean checkColorInput(String rawColor) {
        String trimLowerCase = rawColor.trim().toLowerCase();
        if(trimLowerCase.equals(COLOR_RED) ||
                trimLowerCase.equals(COLOR_GREEN) ||
                trimLowerCase.equals(COLOR_YELLOW) ||
                trimLowerCase.equals(COLOR_STANDBY) ||
                isCustomColor(trimLowerCase)) {
            return true;
        }

        return false;
    }

    // Custom colors should be "abcdef", "123abc", "987654", etc.
    private boolean isCustomColor(String trimLowerCase) {
        if(trimLowerCase.length() == 6 && trimLowerCase.matches(CUSTOM_COLOR_REGEX)) {
            return true;
        }
        return false;
    }

    private boolean checkMessageColorInput(ObjectNode rawMsgColor) {

        if(rawMsgColor.size() != 3 ||
                rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR) == null ||
                rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR) == null ||
                rawMsgColor.get(JSON_MSG_COLOR_MESSAGE) == null) {

            return false;
        }

        if(!checkColorInput(rawMsgColor.get(JSON_MSG_COLOR_BACKGROUND_COLOR).asText()) ||
                !checkColorInput(rawMsgColor.get(JSON_MSG_COLOR_WORD_COLOR).asText())) {
            return false;
        }

        return true;
    }
}































